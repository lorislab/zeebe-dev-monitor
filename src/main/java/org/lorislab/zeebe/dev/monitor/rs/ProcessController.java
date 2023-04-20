package org.lorislab.zeebe.dev.monitor.rs;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.protocol.record.value.BpmnElementType;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.lorislab.zeebe.dev.monitor.bpmn.BpmnModel;
import org.lorislab.zeebe.dev.monitor.dto.ElementInstanceStateDTO;
import org.lorislab.zeebe.dev.monitor.dto.ProcessDTO;
import org.lorislab.zeebe.dev.monitor.dto.ProcessInstanceDTO;
import org.lorislab.zeebe.dev.monitor.dto.ProcessTableItemDTO;
import org.lorislab.zeebe.dev.monitor.mapper.InstanceTableMapper;
import org.lorislab.zeebe.dev.monitor.mapper.MessageSubscriptionMapper;
import org.lorislab.zeebe.dev.monitor.mapper.ProcessMapper;
import org.lorislab.zeebe.dev.monitor.mapper.TimerMapper;
import org.lorislab.zeebe.dev.monitor.models.*;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/api/process")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class ProcessController {

    static final List<ElementInstance.Intent> ELEMENT_COMPLETED = List.of(ElementInstance.Intent.ELEMENT_COMPLETED, ElementInstance.Intent.ELEMENT_TERMINATED);

    static final List<String> EXCLUDE_ELEMENT_TYPES = List.of(BpmnElementType.PROCESS.name(), BpmnElementType.MULTI_INSTANCE_BODY.name());

    static final List<ElementInstance.Intent> ELEMENT_ENTERED = List.of(ElementInstance.Intent.ELEMENT_ACTIVATED);


    @Inject
    ZeebeClient client;

    @Inject
    ProcessMapper mapper;


    @Inject
    TimerMapper timerMapper;

    @Inject
    InstanceTableMapper instanceMapper;

    @Inject
    MessageSubscriptionMapper messageSubscriptionMapper;

    @GET
    public Response getAll() {
        List<ProcessTableItemDTO> tmp = mapper.processes(Definition.list("ORDER BY timestamp DESC"), Instance.countEndedInstances(), Instance.countActiveInstances());
        return Response.ok(tmp).build();
    }

    @GET
    @Path("{processDefinitionKey}")
    public Response processDefinition(@PathParam("processDefinitionKey") Long processDefinitionKey) {
        Definition def = Definition.findById(processDefinitionKey);
        BpmnXmlResource xml = BpmnXmlResource.findById(processDefinitionKey);

        long active = Instance.countActiveInstanceOfProcessDefinitionKey(def.key);
        long close = Instance.countEndedInstanceOfProcessDefinitionKey(def.key);

        List<ElementInstance.ElementInstanceStatistics> elementEntered = ElementInstance.findElementInstanceByKeyAndIntentIn(def.key, ELEMENT_ENTERED, EXCLUDE_ELEMENT_TYPES);

        Map<String, Long> elementCompleted = ElementInstance.findElementInstanceByKeyAndIntentIn(def.key, ELEMENT_COMPLETED, EXCLUDE_ELEMENT_TYPES)
                .stream().collect(Collectors.toMap(ElementInstance.ElementInstanceStatistics::elementId, ElementInstance.ElementInstanceStatistics::count));

        List<ElementInstanceStateDTO> elementsInstances = elementEntered.stream().map(x -> {
            var ci = elementCompleted.getOrDefault(x.elementId(), 0L);
            return new ElementInstanceStateDTO(x.elementId(), x.count() - ci, ci);
        }).toList();

        return Response.ok(new ProcessDTO(
                mapper.info(def, active, close),
                new String(xml.resource),
                instanceMapper.tableItems(Instance.findByProcessDefinitionKey(def.key)),
                timerMapper.timers(Timer.findByProcessDefinitionKeyAndProcessInstanceKeyIsNull(def.key)),
                messageSubscriptionMapper.messages(MessageSubscription.findByProcessDefinitionKeyAndProcessInstanceKeyIsNull(def.key)),
                new ProcessInstanceDTO(BpmnModel.loadBpmnElements(xml), elementsInstances)
        )).build();
    }

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

