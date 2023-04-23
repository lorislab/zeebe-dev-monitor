package org.lorislab.zeebe.dev.monitor.rs;

import io.camunda.zeebe.client.ZeebeClient;
import io.quarkus.panache.common.Parameters;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.lorislab.zeebe.dev.monitor.mapper.JobMapper;
import org.lorislab.zeebe.dev.monitor.mapper.UserTaskMapper;
import org.lorislab.zeebe.dev.monitor.models.Job;
import org.lorislab.zeebe.dev.monitor.models.UserTask;

import java.util.List;
import java.util.Map;

@Path("/api/task")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class UserTaskController {

    @Inject
    ZeebeClient client;

    @Inject
    UserTaskMapper userTaskMapper;

    @GET
    public Response getAll() {
        List<UserTask> userTasks = UserTask.find("ORDER BY timestamp DESC").list();
        return Response.ok(userTaskMapper.userTasks(userTasks)).build();
    }

    @PUT
    @Path("{key}/complete")
    public Response completeJob(@PathParam("key") long key, Map<String, Object> variables) {
        client.newCompleteCommand(key).variables(variables).send().join();
        return Response.ok().build();
    }

    @PUT
    @Path("{key}/fail")
    public Response failJob(@PathParam("key") long key, UserTaskFailDTO dto) {
        client.newFailCommand(key).retries(dto.retries).errorMessage(dto.errorMessage).send().join();
        return Response.ok().build();
    }

    @PUT
    @Path("{key}/throw-error")
    public Response throwError(@PathParam("key") long key, UserTaskThrowErrorDTO dto) {
        client.newThrowErrorCommand(key).errorCode(dto.errorCode).errorMessage(dto.errorMessage).send().join();
        return Response.ok().build();
    }

    @RegisterForReflection
    public record UserTaskFailDTO(String errorMessage, int retries) {};

    @RegisterForReflection
    public record UserTaskThrowErrorDTO(String errorCode, String errorMessage) {};
}
