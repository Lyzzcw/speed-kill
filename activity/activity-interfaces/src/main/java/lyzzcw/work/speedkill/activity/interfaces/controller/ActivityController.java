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
package lyzzcw.work.speedkill.activity.interfaces.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import lombok.RequiredArgsConstructor;
import lyzzcw.work.component.common.HttpUtils.entity.Result;
import lyzzcw.work.speedkill.activity.application.service.ActivityService;
import lyzzcw.work.speedkill.activity.domain.convert.ActivityConverter;
import lyzzcw.work.speedkill.activity.domain.dto.ActivityDTO;
import lyzzcw.work.speedkill.activity.domain.entity.Activity;
import lyzzcw.work.speedkill.activity.domain.vo.ActivityVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lzy
 * @version 1.0.0
 * @description activity Controller
 */
@RestController
@RequestMapping(value = "/activity")
@RequiredArgsConstructor
public class ActivityController {

    final ActivityService activityService;
    /**
     * insert activity
     */
    @RequestMapping(value = "/saveActivity", method = {RequestMethod.GET,RequestMethod.POST})
    public Result<String> saveActivityDTO(@RequestBody ActivityDTO ActivityDTO){

        activityService.saveActivity(ActivityDTO);

        return Result.ok();
    }

    /**
     * list activity
     */
    @RequestMapping(value = "/getActivityList", method = {RequestMethod.GET,RequestMethod.POST})
    @SentinelResource(value = "QUEUE-DATA-FLOW")
    public Result<List<ActivityVO>> getActivityList(@RequestBody ActivityDTO activityDTO){

        List<Activity> activityList = activityService.getActivityList(
                ActivityConverter.INSTANCE.activityDTO2Activity(activityDTO));

        List<ActivityVO> result = activityList.stream()
                .map(ActivityConverter.INSTANCE::activity2ActivityVO)
                .collect(Collectors.toList());

        return Result.ok(result);
    }

    /**
     * one activity
     */
    @RequestMapping(value = "/getActivityById", method = {RequestMethod.GET,RequestMethod.POST})
    public Result<ActivityVO> getActivityById(@RequestBody ActivityDTO activityDTO){

        Activity activity = activityService.getActivityById(activityDTO.getId());

        ActivityVO activityVO = ActivityConverter.INSTANCE.activity2ActivityVO(activity);

        return Result.ok(activityVO);
    }

    /**
     * update activity
     */
    @RequestMapping(value = "/updateActivity", method = {RequestMethod.GET,RequestMethod.POST})
    public Result<String> updateStatus(@RequestBody ActivityDTO activityDTO){


        activityService.updateActivityById(
                ActivityConverter.INSTANCE.activityDTO2Activity(activityDTO));

        return Result.ok();
    }

}
