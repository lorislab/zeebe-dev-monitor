package org.lorislab.zeebe.dev.monitor.mapper;

import org.lorislab.zeebe.dev.monitor.dto.MessageSubscriptionDTO;
import org.lorislab.zeebe.dev.monitor.models.MessageSubscription;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Mapper(uses = OffsetDateTimeMapper.class)
public abstract class MessageSubscriptionMapper {


    public abstract List<MessageSubscriptionDTO> messages(List<MessageSubscription> items);

    @Mapping(target = "open", source = "state", qualifiedByName = "stateToOpen")
    abstract MessageSubscriptionDTO message(MessageSubscription item);

    @Named("stateToOpen")
    Boolean stateToOpen(MessageSubscription.State state) {
        return state == MessageSubscription.State.CREATED;
    }
}
