package org.lorislab.zeebe.dev.monitor.rs;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.SetVariablesResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api/instance")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class InstanceController {

    @Inject
    ZeebeClient client;

    @DELETE
    @Path("/{key}")
    public Response cancelProcessInstance(@PathParam("key") long key) {
        client.newCancelInstanceCommand(key).send().join();
        return Response.ok().build();
    }

    @PUT
    @Path("/{key}/variable")
    public Response setVariables(@PathParam("key") long key, @QueryParam("local") @DefaultValue("false") boolean local, String payload) {
        SetVariablesResponse event = client.newSetVariablesCommand(key).variables(payload)
                .local(local).send().join();
        return Response.ok(event.getKey()).build();
    }



}
