package org.lorislab.zeebe.dev.monitor.mapper;

import org.lorislab.zeebe.dev.monitor.InstanceViewController;
import org.lorislab.zeebe.dev.monitor.models.Instance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Mapper(uses = OffsetDateTimeMapper.class)
public interface InstanceMapper {

    @Mapping(target = "isRunning", source = "instance.end", qualifiedByName = "isRunning")
    InstanceViewController.InstanceDetailData item(Instance instance, String parentBpmnProcessId);

    @Named("isRunning")
    default boolean isRunning(LocalDateTime end) {
        return end == null;
    }

    default List<InstanceViewController.CalledProcessInstanceData> items(Stream<Instance> items, Map<Long, String> elementIdsForKeys) {
        if (items == null) {
            return null;
        }
        return items.map(x -> itemCallInstance(x, elementIdsForKeys.getOrDefault(x.parentElementInstanceKey, "")))
                .toList();
    }
    InstanceViewController.CalledProcessInstanceData itemCallInstance(Instance instance, String elementId);
}
