package org.lorislab.zeebe.dev.monitor.mapper;

import org.lorislab.zeebe.dev.monitor.dto.SignalSubscriptionDTO;
import org.lorislab.zeebe.dev.monitor.models.SignalSubscription;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(uses = OffsetDateTimeMapper.class)
public interface SignalSubscriptionMapper {

    List<SignalSubscriptionDTO> signals(List<SignalSubscription> signals);

    SignalSubscriptionDTO signal(SignalSubscription signal);
}
