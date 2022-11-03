package org.lorislab.zeebe.dev.monitor.rs;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
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

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response deployProcess(@MultipartForm MultipartFormDataInput input) throws Exception {
        Map<String, List<InputPart>> map = input.getFormDataMap();
        List<InputPart> listFilename = map.get("filename");
        if (listFilename == null || listFilename.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing filename in request").build();
        }
        List<InputPart> listFile = map.get("file");
        if (listFile == null || listFile.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing file in request").build();
        }

        client.newDeployResourceCommand()
                .addResourceStringUtf8(listFile.get(0).getBodyAsString(), listFilename.get(0).getBodyAsString())
                .send().join();
        return Response.ok().build();
    }
}

