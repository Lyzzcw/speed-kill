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
package lyzzcw.work.speedkill.order.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import lyzzcw.work.component.common.http.exception.BaseException;
import lyzzcw.work.speedkill.order.domain.constant.HttpCode;
import lyzzcw.work.speedkill.order.domain.entity.Order;
import lyzzcw.work.speedkill.order.domain.repository.OrderRepository;
import lyzzcw.work.speedkill.order.infrastructure.mapper.OrderMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lzy
 * @version 1.0.0
 * @description 订单
 */
@Component
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    final OrderMapper orderMapper;

    @Override
    public int saveOrder(Order order) {
        if (order == null){
            throw new BaseException(HttpCode.PARAMS_INVALID.getMesaage());
        }
        return orderMapper.saveOrder(order);
    }

    @Override
    public List<Order> getOrderByUserId(Long userId) {
        return orderMapper.getOrderByUserId(userId);
    }

    @Override
    public List<Order> getOrderByActivityId(Long activityId) {
        return orderMapper.getOrderByActivityId(activityId);
    }
}
