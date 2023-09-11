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
package lyzzcw.work.speedkill.goods.interfaces.controller;

import lombok.RequiredArgsConstructor;

import lyzzcw.work.component.common.http.entity.Result;
import lyzzcw.work.speedkill.goods.application.service.GoodsService;
import lyzzcw.work.speedkill.goods.domain.convert.GoodsConverter;
import lyzzcw.work.speedkill.goods.domain.dto.GoodsDTO;
import lyzzcw.work.speedkill.goods.domain.vo.GoodsVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lzy
 * @version 1.0.0
 * @description 商品接口
 */
@RestController
@RequestMapping(value = "/goods")
@RequiredArgsConstructor
public class GoodsController {


    final GoodsService goodsService;

    /**
     * 保存秒杀商品
     */
    @RequestMapping(value = "/saveGoods", method = {RequestMethod.GET,RequestMethod.POST})
    public Result<String> saveActivityDTO(@RequestBody GoodsDTO GoodsDTO){
        goodsService.saveGoods(GoodsDTO);
        return Result.ok();
    }

    /**
     * 获取商品详情
     */
    @RequestMapping(value = "/getGoodsId", method = {RequestMethod.GET,RequestMethod.POST})
    public Result<GoodsVO> getGoodsId(@RequestParam Long id){
        return Result.ok(GoodsConverter.INSTANCE.goods2GoodsVO(goodsService.getGoodsId(id)));
    }

    /**
     * 获取商品列表
     */
    @RequestMapping(value = "/getGoodsByActivityId", method = {RequestMethod.GET,RequestMethod.POST})
    public Result<List<GoodsVO>> getGoodsByActivityId(@RequestParam Long activityId){
        return Result.ok(GoodsConverter.INSTANCE.
                goods2GoodsVOList(goodsService.getGoodsByActivityId(activityId)));
    }

    /**
     * 更新商品状态
     */
    @RequestMapping(value = "/updateStatus", method = {RequestMethod.GET,RequestMethod.POST})
    public Result<String> updateStatus(@RequestParam Integer status,@RequestParam Long id){
        goodsService.updateStatus(status, id);
        return Result.ok();
    }

}
