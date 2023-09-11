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
package lyzzcw.work.speedkill.order.interfaces.controller;

import lombok.RequiredArgsConstructor;

import lyzzcw.work.component.common.http.entity.Result;
import lyzzcw.work.speedkill.order.application.place.order.PlaceOrderService;
import lyzzcw.work.speedkill.order.application.service.OrderService;
import lyzzcw.work.speedkill.order.domain.convert.OrderConverter;
import lyzzcw.work.speedkill.order.domain.dto.OrderDTO;
import lyzzcw.work.speedkill.order.domain.entity.Order;
import lyzzcw.work.speedkill.order.domain.vo.OrderVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lzy
 * @version 1.0.0
 * @description 订单
 */

@RestController
@RequestMapping(value = "/order")
@RequiredArgsConstructor
public class OrderController {

    final OrderService orderService;
    final PlaceOrderService placeOrderService;

    /**
     * 保存秒杀订单
     */
    @RequestMapping(value = "/saveOrder", method = {RequestMethod.GET,RequestMethod.POST})
    public Result<OrderVO> saveOrder(@RequestBody OrderDTO orderDTO){
        Order order = orderService.saveOrder(orderDTO);
        return Result.ok(OrderConverter.INSTANCE.order2OrderVO(order));
    }
    /**
     * 获取用户维度的订单列表
     */
    @RequestMapping(value = "/getOrderByUserId", method = {RequestMethod.GET,RequestMethod.POST})
    public Result<List<OrderVO>> getOrderByUserId(@RequestParam Long userId){
        List<Order> orderList = orderService.getOrderByUserId(userId);
        return Result.ok(OrderConverter.INSTANCE.order2OrderVOList(orderList));
    }

    /**
     * 获取活动维度的订单列表
     */
    @RequestMapping(value = "/getOrderByActivityId", method = {RequestMethod.GET,RequestMethod.POST})
    public Result<List<OrderVO>> getOrderByActivityId(@RequestParam Long activityId){
        List<Order> orderList = orderService.getOrderByActivityId(activityId);
        return Result.ok(OrderConverter.INSTANCE.order2OrderVOList(orderList));
    }

    /**
     * 初始化redis商品库存
     */
    @RequestMapping(value = "/init", method = {RequestMethod.GET,RequestMethod.POST})
    public Result<String> getGoodsId(@RequestParam String id,@RequestParam int stock){
        placeOrderService.initGoodsStock(id,stock);
        return Result.ok();
    }
}
