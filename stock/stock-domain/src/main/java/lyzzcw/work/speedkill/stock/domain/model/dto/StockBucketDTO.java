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
package lyzzcw.work.speedkill.stock.domain.model.dto;



import lyzzcw.work.speedkill.stock.domain.model.entity.StockBucket;

import java.util.List;

/**
 * @author lzy
 * @version 1.0.0
 * @description 库存DTO
 */
public class StockBucketDTO extends StockDTO {
    private static final long serialVersionUID = 2704697441525819036L;
    //分桶数量
    private Integer bucketsQuantity;
    //库存分桶信息
    private List<StockBucket> buckets;

    public StockBucketDTO() {
    }

    public StockBucketDTO(Integer totalStock, Integer availableStock, List<StockBucket> buckets) {
        super(totalStock, availableStock);
        this.buckets = buckets;
        this.bucketsQuantity = buckets.size();
    }

    public List<StockBucket> getBuckets() {
        return buckets;
    }

    public void setBuckets(List<StockBucket> buckets) {
        this.buckets = buckets;
    }

    public Integer getBucketsQuantity() {
        return bucketsQuantity;
    }

    public void setBucketsQuantity(Integer bucketsQuantity) {
        this.bucketsQuantity = bucketsQuantity;
    }
}
