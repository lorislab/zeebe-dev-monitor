package org.lorislab.zeebe.dev.monitor.mapper;


import org.lorislab.zeebe.dev.monitor.InstanceViewController;
import org.lorislab.zeebe.dev.monitor.models.Variable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mapper(uses = OffsetDateTimeMapper.class)
public interface VariableMapper {

    default List<InstanceViewController.VariableData> items(Stream<Variable> items, Map<Long, String> elementIdsForKeys) {
        if (items == null) {
            return null;
        }
        return map(items.collect(Collectors.groupingBy(e -> new InstanceViewController.VariableId(e.scopeKey, e.name))), elementIdsForKeys);


    }

    default List<InstanceViewController.VariableData> map(Map<InstanceViewController.VariableId, List<Variable>> map, Map<Long, String> elementIdsForKeys) {
        if (map == null) {
            return null;
        }

        List<InstanceViewController.VariableData> result = new ArrayList<>();
        map.forEach((id, variables) -> {
            Variable last = variables.get(variables.size() - 1);
            result.add(item(id.name(), id.scopeKey(), last.value, last.timestamp, variables, elementIdsForKeys.get(id.scopeKey())));
        });
        return result;
    }

    @Mapping(target = "values", source = "variables")
    InstanceViewController.VariableData item(String name, long scopeKey, String value, LocalDateTime timestamp, List<Variable> variables, String elementId);


    default List<InstanceViewController.VariableValueData> values(List<Variable> values) {
        if (values == null) {
            return null;
        }
        return values.stream().map(this::value).toList();
    }

    InstanceViewController.VariableValueData value(Variable variable);

}
