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
package lyzzcw.work.speedkill.stock.domain.model.entity;


import lyzzcw.work.component.common.IDUtils.SnowflakeIdWorker;

import java.io.Serializable;

/**
 * @author lzy
 * @version 1.0.0
 * @description 商品库存分桶
 */
public class StockBucket implements Serializable {
    private static final long serialVersionUID = 6965796752002288513L;
    //数据主键id
    private Long id;
    //商品id
    private Long goodsId;
    //初始库存
    private Integer initialStock;
    //当前可用库存
    private Integer availableStock;
    //状态，0: 不可用; 1:可用
    private Integer status;
    //分桶编号
    private Integer serialNo;

    public StockBucket() {
    }

    public StockBucket(Long goodsId, Integer initialStock, Integer availableStock, Integer status, Integer serialNo) {
        this.id = SnowflakeIdWorker.generateId();
        this.goodsId = goodsId;
        this.initialStock = initialStock;
        this.availableStock = availableStock;
        this.status = status;
        this.serialNo = serialNo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getInitialStock() {
        return initialStock;
    }

    public void setInitialStock(Integer initialStock) {
        this.initialStock = initialStock;
    }

    public Integer getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(Integer availableStock) {
        this.availableStock = availableStock;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(Integer serialNo) {
        this.serialNo = serialNo;
    }
}
