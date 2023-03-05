package org.lorislab.zeebe.dev.monitor.rs;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.PublishMessageResponse;
import io.quarkus.panache.common.Parameters;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.lorislab.zeebe.dev.monitor.dto.PublishMessageDTO;
import org.lorislab.zeebe.dev.monitor.mapper.MessageMapper;
import org.lorislab.zeebe.dev.monitor.models.Job;
import org.lorislab.zeebe.dev.monitor.models.Message;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.Duration;
import java.util.List;
import java.util.Map;


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
