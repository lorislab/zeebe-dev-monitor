package org.lorislab.zeebe.dev.monitor.mapper;


import org.lorislab.zeebe.dev.monitor.dto.VariableDTO;
import org.lorislab.zeebe.dev.monitor.dto.VariableIdDTO;
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


    default List<VariableDTO> variables(Stream<Variable> items, Map<Long, String> elementIdsForKeys) {
        if (items == null) {
            return null;
        }
        return variable(items.collect(Collectors.groupingBy(e -> new VariableIdDTO(e.scopeKey, e.name))), elementIdsForKeys);


    }

    default List<VariableDTO> variable(Map<VariableIdDTO, List<Variable>> map, Map<Long, String> elementIdsForKeys) {
        if (map == null) {
            return null;
        }

        List<VariableDTO> result = new ArrayList<>();
        map.forEach((id, variables) -> {
            Variable last = variables.get(0);
            result.add(var(id.name(), id.scopeKey(), last.value, last.timestamp, variables, elementIdsForKeys.get(id.scopeKey())));
        });
        return result;
    }

    @Mapping(target = "values", source = "variables")
    VariableDTO var(String name, long scopeKey, String value, LocalDateTime timestamp, List<Variable> variables, String elementId);

}
