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

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.component.common.IDUtils.SnowflakeIdWorker;
import lyzzcw.work.component.redis.cache.redis.RedisCache;
import lyzzcw.work.component.redis.cache.redis.lua.stock.RedisLuaService;
import lyzzcw.work.speedkill.dubbo.interfaces.goods.GoodsDubboService;
import lyzzcw.work.speedkill.goods.domain.entity.Goods;
import lyzzcw.work.speedkill.order.application.listener.message.TxMessage;
import lyzzcw.work.speedkill.order.application.listener.provider.TransactionOrderProvider;
import lyzzcw.work.speedkill.order.application.place.order.PlaceOrderService;
import lyzzcw.work.speedkill.order.domain.constant.Constant;
import lyzzcw.work.speedkill.order.domain.dto.OrderDTO;
import lyzzcw.work.speedkill.order.domain.entity.Order;
import lyzzcw.work.speedkill.order.domain.repository.OrderRepository;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * @author lzy
 * @version 1.0.0
 * @description lua防超卖 + rocketmq分布式事务实现
 */
@Service("transactionOrderService")
@ConditionalOnProperty(name = "place.order.type", havingValue = "lua&mq")
@RequiredArgsConstructor
@Slf4j
public class PlaceOrderServiceImpl implements PlaceOrderService {

    final RedisLuaService redisLuaService;
    final RedisCache redisCache;
    final OrderRepository orderRepository;
    final TransactionOrderProvider transactionOrderProvider;

    @SneakyThrows
    @Override
    public Order placeOrder(OrderDTO orderDTO, Goods goods) {
        //模拟用户风控

        //满足风控后开始扣减库存
        boolean decrementStock = false;
        boolean exception = false;
        this.checkGoods(orderDTO,goods);
        //扣减库存不成功，则库存不足
        String key = Constant.GOODS_STOCK_CACHE_KEY_PREFIX.concat(String.valueOf(goods.getId()));

        try {
            Long result = redisLuaService.decrementByLua(key, orderDTO.getQuantity());
            redisLuaService.checkResult(result);
            decrementStock = true; //设置库存扣除成功,catch中校验是否需要回滚
        }catch (Exception e) {
            //异常
            exception = true;
            //将内存中的库存增加回去
            if (decrementStock){
                redisLuaService.incrementByLua(key, orderDTO.getQuantity());
                log.info("transaction decrementStock");
            }
        }

        TxMessage txMessage = new TxMessage(SnowflakeIdWorker.generateId().toString(),exception,decrementStock,orderDTO,goods);
        transactionOrderProvider.send(JSON.toJSONString(txMessage));

        return null;
    }


    @Override
    public Long initGoodsStock(String goodsId,int stock){
        String key = Constant.GOODS_STOCK_CACHE_KEY_PREFIX.concat(goodsId);
        return redisLuaService.initByLua(key,stock);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrderInTransaction(TxMessage txMessage) {
        try{
            Boolean submitTransaction = redisCache.exist(Constant.LOCAL_TRANSACTION_ORDER_KEY_PREFIX.concat(txMessage.getTxNo()));
            if (BooleanUtil.isTrue(submitTransaction)){
                log.info("saveOrderInTransaction|已经执行过本地事务|{}", txMessage.getTxNo());
                return;
            }
            //构建订单
            Order order = this.buildOrder(txMessage.getOrderDto(), txMessage.getGoods());
            //保存订单
            orderRepository.saveOrder(order);
            //保存事务日志
            redisCache.setCacheObject(Constant.LOCAL_TRANSACTION_ORDER_KEY_PREFIX.concat(txMessage.getTxNo())
                                        ,txMessage,7,TimeUnit.DAYS);//这里设置的事务日志失效时间必须大于rocketmq回查的最大时限
        }catch (Exception e){
            log.error("saveOrderInTransaction|异常|{}", e.getMessage());
            redisCache.deleteObject(Constant.LOCAL_TRANSACTION_ORDER_KEY_PREFIX.concat(txMessage.getTxNo()));
            //扣减过缓存库存
            if (BooleanUtil.isFalse(txMessage.getDecrementStock())){
                String key = Constant.GOODS_STOCK_CACHE_KEY_PREFIX.concat(txMessage.getOrderDto().getGoodsId()+"");
                redisLuaService.incrementByLua(key, txMessage.getOrderDto().getQuantity());
                log.info("transaction decrementStock");
            }
            throw e;
        }
    }
}
