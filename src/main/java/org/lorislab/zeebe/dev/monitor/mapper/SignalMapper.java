package org.lorislab.zeebe.dev.monitor.mapper;

import org.lorislab.zeebe.dev.monitor.dto.SignalDTO;
import org.lorislab.zeebe.dev.monitor.models.Signal;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(uses = OffsetDateTimeMapper.class)
public interface SignalMapper {

    List<SignalDTO> signals(List<Signal> signals);

    SignalDTO signal(Signal signal);

}
