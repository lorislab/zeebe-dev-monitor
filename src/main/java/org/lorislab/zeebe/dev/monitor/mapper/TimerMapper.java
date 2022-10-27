package org.lorislab.zeebe.dev.monitor.mapper;

import org.lorislab.zeebe.dev.monitor.TimerData;
import org.lorislab.zeebe.dev.monitor.models.Timer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(uses = OffsetDateTimeMapper.class)
public interface TimerMapper {

    List<TimerData> items(List<Timer> timers);

    TimerData item(Timer timer);
}
