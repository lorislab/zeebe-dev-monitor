package org.lorislab.zeebe.dev.monitor.mapper;

import org.lorislab.zeebe.dev.monitor.dto.TimerDTO;
import org.lorislab.zeebe.dev.monitor.models.Timer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(uses = OffsetDateTimeMapper.class)
public interface TimerMapper {

    List<TimerDTO> timers(List<Timer> timers);

    TimerDTO timer(Timer timers);

}
