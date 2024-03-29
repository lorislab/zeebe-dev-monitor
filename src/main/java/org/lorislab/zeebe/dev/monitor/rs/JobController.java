package org.lorislab.zeebe.dev.monitor.rs;

import io.camunda.zeebe.client.ZeebeClient;
import io.quarkus.panache.common.Parameters;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.lorislab.zeebe.dev.monitor.mapper.JobMapper;
import org.lorislab.zeebe.dev.monitor.models.Job;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Path("/api/job")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class JobController {

    @Inject
    ZeebeClient client;

    @Inject
    JobMapper jobMapper;

    @GET
    public Response getAll() {
        List<Job> jobs = Job.find("ORDER BY timestamp DESC").list();
        return Response.ok(jobMapper.jobs(jobs)).build();
    }

    @PUT
    @Path("{key}/complete")
    public Response completeJob(@PathParam("key") long key, Map<String, Object> variables) {
        client.newCompleteCommand(key).variables(variables).send().join();
        return Response.ok().build();
    }

    @PUT
    @Path("{key}/fail")
    public Response failJob(@PathParam("key") long key, JobFailDTO dto) {
        client.newFailCommand(key).retries(dto.retries).errorMessage(dto.errorMessage).send().join();
        return Response.ok().build();
    }

    @PUT
    @Path("{key}/throw-error")
    public Response throwError(@PathParam("key") long key, JobThrowErrorDTO dto) {
        client.newThrowErrorCommand(key).errorCode(dto.errorCode).errorMessage(dto.errorMessage).send().join();
        return Response.ok().build();
    }

    @PUT
    @Path("{key}/retries")
    public Response retries(@PathParam("key") long key, JobRetriesDTO dto) {
        client.newUpdateRetriesCommand(key).retries(dto.retries).send().join();
        return Response.ok().build();
    }

    @RegisterForReflection
    public record JobRetriesDTO(int retries) {};

    @RegisterForReflection
    public record JobFailDTO(String errorMessage, int retries) {};

    @RegisterForReflection
    public record JobThrowErrorDTO(String errorCode, String errorMessage) {};
}
