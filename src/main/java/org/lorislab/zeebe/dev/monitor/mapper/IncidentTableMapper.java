package org.lorislab.zeebe.dev.monitor.mapper;

import org.lorislab.zeebe.dev.monitor.IncidentState;
import org.lorislab.zeebe.dev.monitor.IncidentViewController;
import org.lorislab.zeebe.dev.monitor.models.Incident;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(uses = {OffsetDateTimeMapper.class, IncidentStateMapper.class})
public interface IncidentTableMapper {

    List<IncidentViewController.IncidentTableData> items(List<Incident> incidents);

    @Mapping(target = "state", source = "resolved", qualifiedByName = "state")
    IncidentViewController.IncidentTableData item(Incident incident);


}
