package org.lorislab.zeebe.dev.monitor.mapper;

import org.lorislab.zeebe.dev.monitor.dto.MessageDTO;
import org.lorislab.zeebe.dev.monitor.models.Message;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(uses = OffsetDateTimeMapper.class)
public interface MessageMapper {

    List<MessageDTO> messages(List<Message> message);

    MessageDTO message(Message message);

}
