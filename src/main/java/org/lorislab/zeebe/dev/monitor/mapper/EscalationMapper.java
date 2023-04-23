package org.lorislab.zeebe.dev.monitor.mapper;

import org.lorislab.zeebe.dev.monitor.dto.EscalationDTO;
import org.lorislab.zeebe.dev.monitor.models.Escalation;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(uses = OffsetDateTimeMapper.class)
public interface EscalationMapper {

    List<EscalationDTO> escalations(List<Escalation> signals);

    EscalationDTO escalation(Escalation signal);

}
