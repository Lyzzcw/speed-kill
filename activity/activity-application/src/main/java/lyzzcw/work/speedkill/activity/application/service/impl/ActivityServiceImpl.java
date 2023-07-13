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
package lyzzcw.work.speedkill.activity.application.service.impl;

import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.component.common.HttpUtils.exception.BaseException;
import lyzzcw.work.component.common.IDUtils.SnowflakeIdWorker;
import lyzzcw.work.component.redis.cache.business.L2CacheService;
import lyzzcw.work.component.redis.cache.data.CacheData;
import lyzzcw.work.speedkill.activity.application.service.ActivityService;
import lyzzcw.work.speedkill.activity.domain.constant.Constant;
import lyzzcw.work.speedkill.activity.domain.constant.HttpCode;
import lyzzcw.work.speedkill.activity.domain.convert.ActivityConverter;
import lyzzcw.work.speedkill.activity.domain.dto.ActivityDTO;
import lyzzcw.work.speedkill.activity.domain.entity.Activity;
import lyzzcw.work.speedkill.activity.domain.enums.ActivityStatus;
import lyzzcw.work.speedkill.activity.domain.repository.ActivityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author lzy
 * @version 1.0.0
 * @description 秒杀活动
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityServiceImpl implements ActivityService {

    final ActivityRepository activityRepository;
    final L2CacheService L2CacheService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveActivity(ActivityDTO activityDTO) {
        if (activityDTO == null){
            throw new BaseException(HttpCode.PARAMS_INVALID.getMesaage());
        }
        Activity Activity = ActivityConverter.INSTANCE.activityDTO2Activity(activityDTO);
        Activity.setId(SnowflakeIdWorker.generateId());
        Activity.setStatus(ActivityStatus.PUBLISHED.getCode());
        return activityRepository.saveActivity(Activity);
    }

    @Override
    public List<Activity> getActivityList(Activity activity) {

//        PageHelper.startPage(1,10);

        CacheData cacheData = L2CacheService.getCacheDataList(
                activity,
                activityRepository::getActivityList,
                Constant.ACTIVITY_CACHE_KEY_PREFIX,null);


        List<Activity> activityList =
                JSONUtil.toList(JSONUtil.parseArray(JSONUtil.toJsonStr(cacheData.getData())), Activity.class);

        log.info("activityList:{}",activityList);
        return activityList;
    }

    @Override
    public Activity getActivityById(Long id) {
        return activityRepository.getActivityById(id);
    }

    @Override
    public int updateActivityById(Activity activity) {
        return activityRepository.updateActivityById(activity);
    }



}
