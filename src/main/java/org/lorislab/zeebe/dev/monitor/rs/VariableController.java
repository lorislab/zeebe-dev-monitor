package org.lorislab.zeebe.dev.monitor.rs;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.SetVariablesResponse;
import org.lorislab.zeebe.dev.monitor.ws.NotificationService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("/api/variables")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class VariableController {

    @Inject
    ZeebeClient client;

    @PUT
    @Path("/{key}")
    public Response setVariables(@PathParam("key") long key, @QueryParam("local") @DefaultValue("false") boolean local, Map<String, Object> variables) {
        SetVariablesResponse event = client.newSetVariablesCommand(key).variables(variables)
                .local(local).send().join();
        return Response.ok(event.getKey()).build();
    }
}
