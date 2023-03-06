package org.lorislab.zeebe.dev.monitor.mapper;

import org.lorislab.zeebe.dev.monitor.dto.AuditLogDTO;
import org.lorislab.zeebe.dev.monitor.models.ElementInstance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Map;

@Mapper(uses = OffsetDateTimeMapper.class)
public interface AuditLogMapper {


    default List<AuditLogDTO> logs(List<ElementInstance> items, Map<String, String> flowElements) {
        if (items == null) {
            return null;
        }
        return items.stream()
                .map(x -> log(x, flowElements.getOrDefault(x.elementId, "")))
                .toList();
    }

    @Mapping(target = "state", source = "item.intent")
    AuditLogDTO log(ElementInstance item, String elementName);

}
