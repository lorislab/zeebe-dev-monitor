package org.lorislab.zeebe.dev.monitor.rs;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.PublishMessageResponse;
import org.lorislab.zeebe.dev.monitor.dto.PublishMessageDTO;
import org.lorislab.zeebe.dev.monitor.mapper.MessageMapper;
import org.lorislab.zeebe.dev.monitor.models.Message;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.Duration;


@Path("/api/message")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class MessageController {

    @Inject
    ZeebeClient client;

    @Inject
    MessageMapper mapper;

    @GET
    public Response getAll() {
        return Response.ok(mapper.messages(Message.list("ORDER BY timestamp DESC"))).build();
    }

    @POST
    public Response publishMessage(PublishMessageDTO dto) {
        PublishMessageResponse event = client.newPublishMessageCommand()
                .messageName(dto.name())
                .correlationKey(dto.correlationKey())
                .variables(dto.payload())
                .timeToLive(Duration.parse(dto.timeToLive()))
                .send().join();
        return Response.ok(event.getMessageKey()).build();
    }


}
