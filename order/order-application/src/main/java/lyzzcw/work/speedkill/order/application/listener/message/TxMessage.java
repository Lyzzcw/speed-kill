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
import lyzzcw.work.speedkill.goods.domain.entity.Goods;
import lyzzcw.work.speedkill.order.domain.dto.OrderDTO;

/**
 * @author lzy
 * @version 1.0.0
 * @description 事务消息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TxMessage {
    //全局事务编号
    private String txNo;
    //是否抛出了异常
    private Boolean exception;
    //redis是否扣除库存
    private Boolean decrementStock;
    //下单详情
    private OrderDTO orderDto;
    //商品详情
    private Goods goods;
}
