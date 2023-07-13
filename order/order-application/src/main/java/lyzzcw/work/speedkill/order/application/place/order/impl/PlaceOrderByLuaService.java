/**
 * Copyright 2022-9999 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package lyzzcw.work.speedkill.order.application.place.order.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.component.redis.cache.redis.RedisCache;
import lyzzcw.work.component.redis.cache.redis.lua.stock.RedisLuaService;
import lyzzcw.work.speedkill.dubbo.interfaces.goods.GoodsDubboService;
import lyzzcw.work.speedkill.goods.domain.entity.Goods;
import lyzzcw.work.speedkill.order.application.place.order.PlaceOrderService;
import lyzzcw.work.speedkill.order.domain.constant.Constant;
import lyzzcw.work.speedkill.order.domain.dto.OrderDTO;
import lyzzcw.work.speedkill.order.domain.entity.Order;
import lyzzcw.work.speedkill.order.domain.repository.OrderRepository;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * @author lzy
 * @version 1.0.0
 * @description 同步下单
 */
@Service
@ConditionalOnProperty(name = "place.order.type", havingValue = "lua")
@RequiredArgsConstructor
@Slf4j
public class PlaceOrderByLuaService implements PlaceOrderService {

    @DubboReference(version = "1.0.0")
    private GoodsDubboService goodsService;

    final RedisLuaService redisLuaService;
    final RedisCache redisCache;
    final OrderRepository orderRepository;

    @Override
    public Order placeOrder(OrderDTO orderDTO, Goods goods) {
        boolean decrementStock = false;
        this.checkGoods(orderDTO,goods);
        //扣减库存不成功，则库存不足
        String key = Constant.GOODS_STOCK_CACHE_KEY_PREFIX.concat(String.valueOf(goods.getId()));

        Long result = redisLuaService.decrementByLua(key, orderDTO.getQuantity());
        decrementStock = true; //设置库存扣除成功,catch中校验是否需要回滚
        redisLuaService.checkResult(result);

        try {
            Order order = this.buildOrder(orderDTO,goods);
            //插入 订单
            orderRepository.saveOrder(order);
            //更新库存(这里更新库存可以用异步，反正是用redis控制)
            goodsService.updateAvailableStock(order.getQuantity(),order.getGoodsId());
            return order;
        }catch (Exception e){
            //将内存中的库存增加回去
            if (decrementStock){
                redisLuaService.incrementByLua(key, orderDTO.getQuantity());
                log.info("transaction decrementStock");
            }
            throw e;
        }

    }


    @Override
    public Long initGoodsStock(String goodsId,int stock){
        String key = Constant.GOODS_STOCK_CACHE_KEY_PREFIX.concat(goodsId);
        return redisLuaService.initByLua(key,stock);
    }
}
