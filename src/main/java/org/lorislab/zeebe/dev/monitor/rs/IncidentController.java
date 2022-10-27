package org.lorislab.zeebe.dev.monitor.rs;

import io.camunda.zeebe.client.ZeebeClient;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.lorislab.zeebe.dev.monitor.ws.NotificationService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api/incident")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class IncidentController {

    @Inject
    ZeebeClient client;

    @Inject
    NotificationService notificationService;

    @PUT
    @Path("/{key}")
    public Response resolveIncident(@PathParam("key") long key, ResolveIncidentDTO dto) {
        if (dto.jobKey != null && dto.jobKey > 0) {
            client.newUpdateRetriesCommand(dto.jobKey).retries(dto.retries).send()
                    .exceptionally(e -> {
                        notificationService.sendEvent(new NotificationService.ClusterEvent(e.getMessage(), NotificationService.ClusterEventType.ERROR));
                        return null;
                    })
                    .toCompletableFuture().join();
        }
        client.newResolveIncidentCommand(key).send().join();
        return Response.ok().build();
    }

    @RegisterForReflection
    public record ResolveIncidentDTO(Long jobKey, int retries) {};

}
