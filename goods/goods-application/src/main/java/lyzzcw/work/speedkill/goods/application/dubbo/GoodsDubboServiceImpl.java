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
package lyzzcw.work.speedkill.goods.application.dubbo;

import lyzzcw.work.speedkill.dubbo.interfaces.goods.GoodsDubboService;
import lyzzcw.work.speedkill.goods.application.service.GoodsService;
import lyzzcw.work.speedkill.goods.domain.entity.Goods;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author lzy
 * @version 1.0.0
 * @description 商品Dubbo服务实现类
 */
@Component
@DubboService(version = "1.0.0")
public class GoodsDubboServiceImpl implements GoodsDubboService {
    @Autowired
    private GoodsService goodsService;

    @Override
    public Goods getGoods(Long id) {
        return goodsService.getGoodsId(id);
    }

    @Override
    public boolean updateDbAvailableStock(Integer count, Long id) {
        return goodsService.updateDbAvailableStock(count, id);
    }

    @Override
    public int updateAvailableStock(Integer count, Long id) {
        return goodsService.updateAvailableStock(count,id);
    }
}
