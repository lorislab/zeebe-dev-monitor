package org.lorislab.zeebe.dev.monitor.mapper;

import org.lorislab.zeebe.dev.monitor.dto.CalledProcessInstanceDTO;
import org.lorislab.zeebe.dev.monitor.dto.InstanceDetailDTO;
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
    InstanceDetailDTO detail(Instance instance, String parentBpmnProcessId, long parentProcessDefinitionKey);

    @Named("isRunning")
    default boolean isRunning(LocalDateTime end) {
        return end == null;
    }



    default List<CalledProcessInstanceDTO> processes(Stream<Instance> items, Map<Long, String> elementIdsForKeys) {
        if (items == null) {
            return null;
        }
        return items.map(x -> process(x, elementIdsForKeys.getOrDefault(x.parentElementInstanceKey, "")))
                .toList();
    }
    CalledProcessInstanceDTO process(Instance instance, String elementId);

}
