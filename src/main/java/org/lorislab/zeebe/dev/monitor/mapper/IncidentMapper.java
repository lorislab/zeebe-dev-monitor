package org.lorislab.zeebe.dev.monitor.mapper;

import org.lorislab.zeebe.dev.monitor.dto.InstanceIncidentDTO;
import org.lorislab.zeebe.dev.monitor.models.Incident;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper(uses = {OffsetDateTimeMapper.class, IncidentStateMapper.class})
public interface IncidentMapper {

    default List<InstanceIncidentDTO> incidents(List<Incident> items, Map<Long, String> elementIdsForKeys) {
        if (items == null) {
            return null;
        }
        return items.stream()
                .map(x -> incident(x, elementIdsForKeys.getOrDefault(x.elementInstanceKey, "")))
                .toList();
    }

    @Mapping(target = "state", source = "item.resolved", qualifiedByName = "state")
    @Mapping(target = "isResolved", source = "item.resolved", qualifiedByName = "isResolved")
    InstanceIncidentDTO incident(Incident item, String elementId);

    @Named("isResolved")
    default boolean isResolved(LocalDateTime resolved) {
        return resolved != null;
    }
}
