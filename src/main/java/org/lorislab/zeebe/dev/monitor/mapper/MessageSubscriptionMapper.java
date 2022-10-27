package org.lorislab.zeebe.dev.monitor.mapper;

import org.lorislab.zeebe.dev.monitor.MessageSubscriptionData;
import org.lorislab.zeebe.dev.monitor.models.MessageSubscription;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Mapper(uses = OffsetDateTimeMapper.class)
public abstract class MessageSubscriptionMapper {

    public abstract List<MessageSubscriptionData> items(List<MessageSubscription> items);

    @Mapping(target = "open", source = "state", qualifiedByName = "stateToOpen")
    abstract MessageSubscriptionData item(MessageSubscription item);

    @Mapping(target = "open", source = "item.state", qualifiedByName = "stateToOpen")
    abstract MessageSubscriptionData item(MessageSubscription item, String targetFlowNodeId);

    public List<MessageSubscriptionData> items(Stream<MessageSubscription> items, Map<Long, String> elementIdsForKeys) {
        if (items == null) {
            return null;
        }
        return items.map(x -> item(x, elementIdsForKeys.getOrDefault(x.elementInstanceKey, "")))
                .toList();
    }

    @Named("stateToOpen")
    Boolean stateToOpen(MessageSubscription.State state) {
        return state == MessageSubscription.State.CREATED;
    }
}
