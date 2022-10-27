package org.lorislab.zeebe.dev.monitor.mapper;

import org.lorislab.zeebe.dev.monitor.MessageViewController;
import org.lorislab.zeebe.dev.monitor.models.Message;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(uses = OffsetDateTimeMapper.class)
public interface MessageMapper {

    List<MessageViewController.MessageData> items(List<Message> message);

    MessageViewController.MessageData item(Message message);

}
