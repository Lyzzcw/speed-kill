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
package lyzzcw.work.speedkill.stock.application.service;


import lyzzcw.work.speedkill.stock.domain.model.dto.StockBucketDTO;

/**
 * @author lzy
 * @version 1.0.0
 * @description 库存编排服务
 */
public interface StockBucketArrangementService {

    /**
     * 编码分桶库存
     * @param goodsId 商品id
     * @param stock 库存数量
     * @param bucketsQuantity 分桶数量
     * @param assignmentMode 编排模式
     */
    void arrangeStockBuckets(Long goodsId, Integer stock, Integer bucketsQuantity, Integer assignmentMode);

    /**
     * 通过商品id获取库存分桶信息
     */
    StockBucketDTO getStockBucketDTO(Long goodsId, Long version);
}
