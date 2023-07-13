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
package lyzzcw.work.speedkill.activity.application.dubbo;

import lyzzcw.work.speedkill.activity.application.service.ActivityService;
import lyzzcw.work.speedkill.activity.domain.entity.Activity;
import lyzzcw.work.speedkill.dubbo.interfaces.activity.ActivityDubboService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author lzy
 * @version 1.0.0
 * @description Dubbo服务
 */
@Component
@DubboService(version = "1.0.0")
public class ActivityDubboServiceImpl implements ActivityDubboService {
    @Autowired
    private ActivityService activityService;

    @Override
    public Activity getActivity(Long id) {
        return activityService.getActivityById(id);
    }
}
