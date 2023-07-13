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
package lyzzcw.work.speedkill.order.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author lzy
 * @version 1.0
 * Date: 2023/7/5 9:46
 * Description: No Description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {
    private static final long serialVersionUID = -2900817329676795585L;
    //订单id
    private Long id;
    //用户id
    private Long userId;
    //商品id
    private Long goodsId;
    //商品名称
    private String goodsName;
    //秒杀活动价格
    private BigDecimal activityPrice;
    //购买数量
    private Integer quantity;
    //订单总金额
    private BigDecimal orderPrice;
    //活动id
    private Long activityId;
    //订单状态 1：已创建 2：已支付 0：已取消； -1：已删除
    private Integer status;
    //创建时间
    private LocalDateTime createTime;
}
