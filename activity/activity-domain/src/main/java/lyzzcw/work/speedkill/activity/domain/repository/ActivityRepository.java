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
package lyzzcw.work.speedkill.activity.domain.repository;


import lyzzcw.work.speedkill.activity.domain.entity.Activity;

import java.util.List;

/**
 * @author lzy
 * @version 1.0.0
 * @description 活动
 */
public interface ActivityRepository {

    /**
     * 保存活动信息
     */
    int saveActivity(Activity activity);

    /**
     * 根据状态获取活动列表
     */
    List<Activity> getActivityList(Activity activity);

    /**
     * 根据id获取活动信息
     */
    Activity getActivityById(Long id);

    /**
     * 修改状态
     */
    int updateActivityById(Activity activity);
}
