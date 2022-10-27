package org.lorislab.zeebe.dev.monitor.mapper;

import org.lorislab.zeebe.dev.monitor.InstanceViewController;
import org.lorislab.zeebe.dev.monitor.models.ElementInstance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Map;

@Mapper(uses = OffsetDateTimeMapper.class)
public interface AuditLogMapper {

    default List<InstanceViewController.AuditLogData> items(List<ElementInstance> items, Map<String, String> flowElements) {
        if (items == null) {
            return null;
        }
        return items.stream()
                .map(x -> item(x, flowElements.getOrDefault(x.elementId, "")))
                .toList();
    }

    @Mapping(target = "state", source = "item.intent")
    InstanceViewController.AuditLogData item(ElementInstance item, String elementName);
}
