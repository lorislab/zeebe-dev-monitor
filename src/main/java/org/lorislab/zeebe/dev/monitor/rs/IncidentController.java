package org.lorislab.zeebe.dev.monitor.rs;

import io.camunda.zeebe.client.ZeebeClient;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.lorislab.zeebe.dev.monitor.mapper.IncidentTableMapper;
import org.lorislab.zeebe.dev.monitor.models.Incident;
import org.lorislab.zeebe.dev.monitor.ws.NotificationService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/incident")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class IncidentController {

    @Inject
    ZeebeClient client;

    @Inject
    NotificationService notificationService;

    @Inject
    IncidentTableMapper mapper;
    @GET
    public Response getAll() {
        return Response.ok(mapper.incidents(Incident.find("resolved IS NULL ORDER BY created DESC").list())).build();
    }
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
