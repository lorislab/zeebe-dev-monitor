package org.lorislab.zeebe.dev.monitor.rs;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("/api/process")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class ProcessController {

    @Inject
    ZeebeClient client;

    @POST
    @Path("{processDefinitionKey}")
    public Response createProcessInstance(@PathParam("processDefinitionKey") Long processDefinitionKey, Map<String, Object> data) {
        ProcessInstanceEvent event = client.newCreateInstanceCommand()
                .processDefinitionKey(processDefinitionKey).variables(data)
                .send().join();

        return Response.ok(event.getProcessInstanceKey()).build();
    }

}

