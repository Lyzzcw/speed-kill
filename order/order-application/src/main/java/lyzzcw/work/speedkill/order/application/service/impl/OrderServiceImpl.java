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
package lyzzcw.work.speedkill.order.application.service.impl;

import lombok.RequiredArgsConstructor;
import lyzzcw.work.speedkill.dubbo.interfaces.goods.GoodsDubboService;
import lyzzcw.work.speedkill.goods.domain.entity.Goods;
import lyzzcw.work.speedkill.order.application.place.order.PlaceOrderService;
import lyzzcw.work.speedkill.order.application.service.OrderService;
import lyzzcw.work.speedkill.order.domain.constant.HttpCode;
import lyzzcw.work.speedkill.order.domain.dto.OrderDTO;
import lyzzcw.work.speedkill.order.domain.entity.Order;
import lyzzcw.work.speedkill.order.domain.repository.OrderRepository;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author lzy
 * @version 1.0.0
 * @description 订单业务
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    @DubboReference(version = "1.0.0")
    private GoodsDubboService goodsDubboService;

    final OrderRepository orderRepository;
    final PlaceOrderService placeOrderService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order saveOrder(OrderDTO orderDTO) {
        Assert.notNull(orderDTO, HttpCode.PARAMS_INVALID.getMesaage());

        //获取商品(good详情可以通过L2二级缓存获取，更新频率不高)
        Goods goods = goodsDubboService.getGoods(orderDTO.getGoodsId());
        //尝试下单
        Order order = placeOrderService.placeOrder(orderDTO,goods);

        return order;
    }

    @Override
    public List<Order> getOrderByUserId(Long userId) {
        return orderRepository.getOrderByUserId(userId);
    }

    @Override
    public List<Order> getOrderByActivityId(Long activityId) {
        return orderRepository.getOrderByActivityId(activityId);
    }
}
