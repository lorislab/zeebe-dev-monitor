package org.lorislab.zeebe.dev.monitor.mapper;

import org.lorislab.zeebe.dev.monitor.dto.UserTaskDTO;
import org.lorislab.zeebe.dev.monitor.models.UserTask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = OffsetDateTimeMapper.class)
public abstract class UserTaskMapper {

    public abstract List<UserTaskDTO> userTasks(List<UserTask> userTasks);

    @Mapping(target = "isActivatable", source ="userTask", qualifiedByName = "isActivatable")
    public abstract UserTaskDTO userTask(UserTask userTask);

    @Named("isActivatable")
    boolean isActivatable(UserTask userTask) {
        return userTask.retries > 0 &&
                (userTask.status == UserTask.Status.CREATED || userTask.status == UserTask.Status.FAILED ||
                        userTask.status == UserTask.Status.TIMED_OUT || userTask.status == UserTask.Status.RETRIES_UPDATED);
    }

}
