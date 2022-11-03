package org.lorislab.zeebe.dev.monitor.rs;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.command.ModifyProcessInstanceCommandStep1;
import io.camunda.zeebe.client.api.response.ModifyProcessInstanceResponse;
import io.camunda.zeebe.client.api.response.SetVariablesResponse;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.Map;

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
    public Response setVariables(@PathParam("key") long key, @QueryParam("local") @DefaultValue("false") boolean local, Map<String, Object> variables) {
        SetVariablesResponse event = client.newSetVariablesCommand(key).variables(variables)
                .local(local).send().join();
        return Response.ok(event.getKey()).build();
    }

    @PUT
    @Path("/{key}/modify")
    public Response modify(@PathParam("key") long key, ProcessInstanceModifyDTO dto) {
        if (dto == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        ModifyProcessInstanceCommandStep1 req = client.newModifyProcessInstanceCommand(key);
        ModifyProcessInstanceCommandStep1.ModifyProcessInstanceCommandStep2 result = null;

        if (dto.terminates != null) {
            for (Long e : dto.terminates) {
                result = req.terminateElement(e);
                req = result.and();
            }
        }

        if (dto.activates != null) {
            for (ActivateElementDTO a : dto.activates) {

                ModifyProcessInstanceCommandStep1.ModifyProcessInstanceCommandStep3 tmp;
                if (a.ancestor != -1) {
                    tmp = req.activateElement(a.id, a.ancestor);
                } else {
                    tmp = req.activateElement(a.id);
                }
                if (a.vars != null && !a.vars.isEmpty()) {
                    tmp.withVariables(a.vars);
                }
                result = tmp;
                req = tmp.and();
            }
        }

        if (result == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        result.send().join();
        return Response.ok().build();
    }


    @RegisterForReflection
    public record ActivateElementDTO(String id, Long ancestor, Map<String, Object> vars) {};
    @RegisterForReflection
    public record ProcessInstanceModifyDTO(List<ActivateElementDTO> activates, List<Long> terminates) {};
}
