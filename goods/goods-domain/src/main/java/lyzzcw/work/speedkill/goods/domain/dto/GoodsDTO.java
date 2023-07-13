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
package lyzzcw.work.speedkill.goods.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

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
public class GoodsDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    //数据id
    private Long id;
    //商品名称
    private String goodsName;
    //秒杀活动id
    private Long activityId;
    //活动开始时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private LocalDateTime startTime;
    //活动结束时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private LocalDateTime endTime;
    //商品原价
    private BigDecimal originalPrice;
    //秒杀活动价格
    private BigDecimal activityPrice;
    //初始库存
    private Integer initialStock;
    //限购个数
    private Integer limitNum;
    //当前可用库存
    private Integer availableStock;
    //描述
    private String description;
    //图片
    private String imgUrl;
    //秒杀状态 0：已发布； 1：上线； 2：下线
    private Integer status;

}
