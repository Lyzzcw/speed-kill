package lyzzcw.work.speedkill.activity.infrastructure.mapper;


import lyzzcw.work.speedkill.activity.domain.entity.Activity;

import java.util.List;

public interface ActivityMapper {

    int deleteActivityById(Long id);

    int saveActivity(Activity record);

    Activity getActivityById(Long id);

    List<Activity> getActivityList(Activity activity);

    int updateActivityById(Activity record);

}