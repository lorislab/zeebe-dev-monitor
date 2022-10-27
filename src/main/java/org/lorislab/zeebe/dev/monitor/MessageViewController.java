package org.lorislab.zeebe.dev.monitor;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.lorislab.zeebe.dev.monitor.mapper.MessageMapper;
import org.lorislab.zeebe.dev.monitor.models.Message;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.OffsetDateTime;

@Path("message")
public class MessageViewController {

    @Inject
    Template messages;

    @Inject
    MessageMapper messageMapper;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getMessages() {
        return messages.data(
                "messages", messageMapper.items(Message.findAll().list()),
                "count", Message.count()
        );
    }

    @RegisterForReflection
    public record MessageData(long key, String name, String correlationKey, String messageId, Message.State state,
                              OffsetDateTime timestamp) {}
}
