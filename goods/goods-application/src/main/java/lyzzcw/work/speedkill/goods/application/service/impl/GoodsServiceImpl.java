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
package lyzzcw.work.speedkill.goods.application.service.impl;

import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.component.common.http.exception.BaseException;
import lyzzcw.work.component.common.id.SnowflakeIdWorker;
import lyzzcw.work.component.redis.cache.business.L2CacheService;
import lyzzcw.work.component.redis.cache.data.CacheData;
import lyzzcw.work.component.redis.cache.distribute.DistributeCacheService;

import lyzzcw.work.speedkill.activity.domain.entity.Activity;
import lyzzcw.work.speedkill.dubbo.interfaces.activity.ActivityDubboService;
import lyzzcw.work.speedkill.goods.application.listener.provider.UpdateCacheProvider;
import lyzzcw.work.speedkill.goods.application.service.GoodsService;
import lyzzcw.work.speedkill.goods.domain.constant.Constant;
import lyzzcw.work.speedkill.goods.domain.constant.HttpCode;
import lyzzcw.work.speedkill.goods.domain.convert.GoodsConverter;
import lyzzcw.work.speedkill.goods.domain.dto.GoodsDTO;
import lyzzcw.work.speedkill.goods.domain.enums.GoodsStatus;
import lyzzcw.work.speedkill.goods.domain.repository.GoodsRepository;
import lyzzcw.work.speedkill.goods.domain.entity.Goods;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author lzy
 * @version 1.0.0
 * @description 商品服务
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GoodsServiceImpl implements GoodsService {

    @DubboReference(version = "1.0.0")
    private  ActivityDubboService activityDubboService;


    final GoodsRepository goodsRepository;
    final L2CacheService L2CacheService;
    final DistributeCacheService distributeCacheService;
    final UpdateCacheProvider updateCacheProvider;

    @Override
    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public int saveGoods(GoodsDTO GoodsDTO) {
        if (GoodsDTO == null){
            throw new BaseException(HttpCode.PARAMS_INVALID.getMesaage());
        }
        Activity Activity = activityDubboService.getActivity(GoodsDTO.getActivityId());
        if (Activity == null){
            throw new BaseException(HttpCode.ACTIVITY_NOT_EXISTS.getMesaage());
        }
        Goods Goods = GoodsConverter.INSTANCE.goodsDTO2Goods(GoodsDTO);
        Goods.setId(SnowflakeIdWorker.generateId());
        Goods.setStatus(GoodsStatus.PUBLISHED.getCode());
        updateCacheProvider.send(Goods.getId(),Goods, Constant.GOODS_DETAIL_CACHE_KEY_PREFIX);
        return goodsRepository.saveGoods(Goods);
    }

    @Override
    public Goods getGoodsId(Long id) {

        CacheData cacheData = L2CacheService.getCacheData(
                id,
                goodsRepository::getGoodsId,
                Constant.GOODS_DETAIL_CACHE_KEY_PREFIX,null);


        Goods goods = distributeCacheService.getResult(cacheData.getData(),Goods.class);

        log.info("goods ----> :{}",goods);

        return goodsRepository.getGoodsId(id);
    }


    @Override
    public List<Goods> getGoodsByActivityId(Long activityId) {

        PageHelper.startPage(1,10);

        CacheData cacheData = L2CacheService.getCacheDataList(
                activityId,
                goodsRepository::getGoodsByActivityId,
                Constant.GOODS_CACHE_KEY_PREFIX,null);

        List<Goods> goods =
                JSONUtil.toList(JSONUtil.parseArray(JSONUtil.toJsonStr(cacheData.getData())), Goods.class);

        log.info("goods list ----> :{}",goods);

        return goods;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateStatus(Integer status, Long id) {
        return goodsRepository.updateStatus(status, id);
    }

    @Override
    public int updateAvailableStock(Integer count, Long id) {
        return goodsRepository.updateAvailableStock(count, id);
    }

    @Override
    public boolean updateDbAvailableStock(Integer count, Long id) {
        return goodsRepository.updateDbAvailableStock(count, id);
    }

    @Override
    public Integer getAvailableStockById(Long id) {
        return goodsRepository.getAvailableStockById(id);
    }
}
