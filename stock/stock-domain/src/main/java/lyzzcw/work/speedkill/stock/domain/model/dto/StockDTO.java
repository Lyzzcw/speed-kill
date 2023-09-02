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

import java.io.Serializable;

/**
 * @author lzy
 * @version 1.0.0
 * @description 库存DTO
 */
public class StockDTO implements Serializable {
    private static final long serialVersionUID = 6707252274621460974L;
    //库存总量
    private Integer totalStock;
    //可用库存量
    private Integer availableStock;

    public StockDTO() {
    }

    public StockDTO(Integer totalStock, Integer availableStock) {
        this.totalStock = totalStock;
        this.availableStock = availableStock;
    }

    public Integer getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(Integer totalStock) {
        this.totalStock = totalStock;
    }

    public Integer getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(Integer availableStock) {
        this.availableStock = availableStock;
    }
}
