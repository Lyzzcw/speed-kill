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
package lyzzcw.work.speedkill.order.application.place.order;

import lyzzcw.work.component.common.HttpUtils.exception.BaseException;
import lyzzcw.work.component.common.IDUtils.SnowflakeIdWorker;
import lyzzcw.work.speedkill.goods.domain.entity.Goods;
import lyzzcw.work.speedkill.goods.domain.enums.GoodsStatus;
import lyzzcw.work.speedkill.order.domain.constant.HttpCode;
import lyzzcw.work.speedkill.order.domain.convert.OrderConverter;
import lyzzcw.work.speedkill.order.domain.dto.OrderDTO;
import lyzzcw.work.speedkill.order.domain.entity.Order;
import lyzzcw.work.speedkill.order.domain.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * @author lzy
 * @version 1.0.0
 * @description 下单接口
 */
public interface PlaceOrderService {
    /**
     * 下单操作
     */
    Order placeOrder(OrderDTO orderDTO, Goods good);

    /**
     * 构建订单
     */
    default Order buildOrder(OrderDTO orderDTO, Goods goods){
        Order order ;
        order = OrderConverter.INSTANCE.orderDTO2Order(orderDTO);
        order.setId(SnowflakeIdWorker.generateId());
        order.setGoodsName(goods.getGoodsName());
        order.setActivityPrice(goods.getActivityPrice());
        BigDecimal orderPrice = goods.getActivityPrice().multiply(BigDecimal.valueOf(order.getQuantity()));
        order.setOrderPrice(orderPrice);
        order.setStatus(OrderStatus.CREATED.getCode());
        order.setCreateTime(LocalDateTime.now());
        return order;
    }

    /**
     * 检测商品信息
     */
    default void checkGoods(OrderDTO orderDTO, Goods goods){
        //商品不存在
        Optional.ofNullable(goods).
                orElseThrow(()->new BaseException(HttpCode.GOODS_NOT_EXISTS.getMesaage()));

        //商品未上线
        if (Objects.equals(goods.getStatus(), GoodsStatus.PUBLISHED.getCode())){
            throw new BaseException(HttpCode.GOODS_PUBLISH.getMesaage());
        }
        //商品已下架
        if (Objects.equals(goods.getStatus(), GoodsStatus.OFFLINE.getCode())){
            throw new BaseException(HttpCode.GOODS_OFFLINE.getMesaage());
        }
        //触发限购
        if (goods.getLimitNum() < orderDTO.getQuantity()){
            throw new BaseException(HttpCode.BEYOND_LIMIT_NUM.getMesaage());
        }
        // 库存不足
        if (goods.getAvailableStock() == null || goods.getAvailableStock() <= 0 || orderDTO.getQuantity() > goods.getAvailableStock()){
            throw new BaseException(HttpCode.STOCK_LT_ZERO.getMesaage());
        }
    }

    Long initGoodsStock(String goods,int stock);
}
