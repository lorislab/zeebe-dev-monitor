package org.lorislab.zeebe.dev.monitor.mapper;

import org.lorislab.zeebe.dev.monitor.dto.IncidentState;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.time.LocalDateTime;

@Mapper
public interface IncidentStateMapper {

    @Named("state")
    default IncidentState state(LocalDateTime resolved) {
        if (resolved != null) {
            return IncidentState.RESOLVED;
        }
        return IncidentState.CREATED;
    }
}
