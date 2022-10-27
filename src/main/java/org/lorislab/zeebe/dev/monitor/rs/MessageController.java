package org.lorislab.zeebe.dev.monitor.rs;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.PublishMessageResponse;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.Duration;
import java.util.Map;


@Path("/api/message")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class MessageController {

    @Inject
    ZeebeClient client;

    @POST
    public Response publishMessage(PublishMessageDTO dto) {
        PublishMessageResponse event = client.newPublishMessageCommand()
                .messageName(dto.name)
                .correlationKey(dto.correlationKey)
                .variables(dto.payload)
                .timeToLive(Duration.parse(dto.timeToLive))
                .send().join();
        return Response.ok(event.getMessageKey()).build();
    }

    @RegisterForReflection
    public record PublishMessageDTO(String name, String correlationKey, Map<String, Object> payload, String timeToLive) {};
}
