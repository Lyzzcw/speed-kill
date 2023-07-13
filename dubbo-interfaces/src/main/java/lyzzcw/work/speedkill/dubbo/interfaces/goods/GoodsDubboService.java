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
package lyzzcw.work.speedkill.dubbo.interfaces.goods;


import lyzzcw.work.speedkill.goods.domain.entity.Goods;

/**
 * @author lzy
 * @version 1.0.0
 * @description 商品Dubbo服务接口
 */
public interface GoodsDubboService {

    /**
     * 根据id和版本号获取商品详情
     */
    Goods getGoods(Long id);

    /**
     * 尝试扣减数据库库存
     */
    boolean updateDbAvailableStock(Integer count, Long id);

    /**
     * 扣减库存
     */
    int updateAvailableStock(Integer count, Long id);
}
