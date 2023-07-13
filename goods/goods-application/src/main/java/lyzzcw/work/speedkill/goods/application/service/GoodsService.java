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
package lyzzcw.work.speedkill.goods.application.service;


import lyzzcw.work.speedkill.goods.domain.dto.GoodsDTO;
import lyzzcw.work.speedkill.goods.domain.entity.Goods;

import java.util.List;

/**
 * @author lzy
 * @version 1.0.0
 * @description 商品
 */
public interface GoodsService {

    /**
     * 保存商品信息
     */
    int saveGoods(GoodsDTO GoodsDTO);

    /**
     * 根据id获取商品详细信息
     */
    Goods getGoodsId(Long id);

    /**
     * 根据活动id获取商品列表
     */
    List<Goods> getGoodsByActivityId(Long activityId);

    /**
     * 修改商品状态
     */
    int updateStatus(Integer status, Long id);

    /**
     * 扣减库存
     */
    int updateAvailableStock(Integer count, Long id);

    /**
     * db尝试扣减库存
     */
    boolean updateDbAvailableStock(Integer count, Long id);
    /**
     * 获取当前可用库存
     */
    Integer getAvailableStockById(Long id);
}
