package org.lorislab.zeebe.dev.monitor.mapper;

import org.lorislab.zeebe.dev.monitor.dto.IncidentTableItemDTO;
import org.lorislab.zeebe.dev.monitor.models.Incident;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(uses = {OffsetDateTimeMapper.class, IncidentStateMapper.class})
public interface IncidentTableMapper {

    List<IncidentTableItemDTO> incidents(List<Incident> incidents);

    @Mapping(target = "state", source = "resolved", qualifiedByName = "state")
    IncidentTableItemDTO incident(Incident incident);
}
