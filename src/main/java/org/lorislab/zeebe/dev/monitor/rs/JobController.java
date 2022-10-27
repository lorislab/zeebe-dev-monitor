package org.lorislab.zeebe.dev.monitor.rs;

import io.camunda.zeebe.client.ZeebeClient;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("/api/job")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class JobController {

    @Inject
    ZeebeClient client;

    @PUT
    @Path("{key}/complete")
    public Response completeJob(@PathParam("key") long key, Map<String, Object> variables) {
        client.newCompleteCommand(key).variables(variables).send().join();
        return Response.ok().build();
    }

    @PUT
    @Path("{key}/fail")
    public Response failJob(@PathParam("key") long key, FailDTO dto) {
        client.newFailCommand(key).retries(dto.retries).errorMessage(dto.errorMessage).send().join();
        return Response.ok().build();
    }

    @PUT
    @Path("{key}/throw-error")
    public Response throwError(@PathParam("key") long key, ThrowErrorDTO dto) {
        client.newThrowErrorCommand(key).errorCode(dto.errorCode).errorMessage(dto.errorMessage).send().join();
        return Response.ok().build();
    }

    @RegisterForReflection
    public record FailDTO(String errorMessage, int retries) {};

    @RegisterForReflection
    public record ThrowErrorDTO(String errorCode, String errorMessage) {};
}
