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
package lyzzcw.work.speedkill.order.application.listener.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lyzzcw.work.speedkill.order.domain.dto.OrderDTO;

/**
 * @author lzy
 * @version 1.0.0
 * @description 错误消息，扣减库存失败，由商品微服务发送给订单微服务
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage {
    //全局事务编号
    private Long txNo;
    //是否扣减了缓存库存
    private Boolean exception;
    //下单详情
    private OrderDTO orderDTO;
}
