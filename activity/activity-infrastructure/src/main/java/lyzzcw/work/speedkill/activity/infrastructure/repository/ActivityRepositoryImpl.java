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
package lyzzcw.work.speedkill.activity.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import lyzzcw.work.component.common.HttpUtils.exception.BaseException;
import lyzzcw.work.speedkill.activity.domain.constant.HttpCode;
import lyzzcw.work.speedkill.activity.domain.entity.Activity;
import lyzzcw.work.speedkill.activity.domain.repository.ActivityRepository;
import lyzzcw.work.speedkill.activity.infrastructure.mapper.ActivityMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lzy
 * @version 1.0.0
 * @description 活动
 */
@Component
@RequiredArgsConstructor
public class ActivityRepositoryImpl implements ActivityRepository {

    final ActivityMapper activityMapper;

    @Override
    public int saveActivity(Activity activity) {
        if (activity == null){
            throw new BaseException(HttpCode.PARAMS_INVALID.getMesaage());
        }
        return activityMapper.saveActivity(activity);
    }

    @Override
    public List<Activity> getActivityList(Activity activity) {
        return activityMapper.getActivityList(activity);
    }


    @Override
    public Activity getActivityById(Long id) {
        return activityMapper.getActivityById(id);
    }

    @Override
    public int updateActivityById(Activity activity) {
        return activityMapper.updateActivityById(activity);
    }

}
