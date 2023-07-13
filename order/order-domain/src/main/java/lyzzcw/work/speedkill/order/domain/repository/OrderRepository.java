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
package lyzzcw.work.speedkill.order.domain.repository;

import lyzzcw.work.speedkill.order.domain.entity.Order;

import java.util.List;

/**
 * @author lzy
 * @version 1.0.0
 * @description 订单
 */
public interface OrderRepository {

    /**
     * 保存订单
     */
    int saveOrder(Order order);

    /**
     * 根据用户id获取订单列表
     */
    List<Order> getOrderByUserId(Long userId);

    /**
     * 根据活动id获取订单列表
     */
    List<Order> getOrderByActivityId(Long activityId);
}
