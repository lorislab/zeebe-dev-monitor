package org.lorislab.zeebe.dev.monitor.rs;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.command.ModifyProcessInstanceCommandStep1;
import io.camunda.zeebe.client.api.response.SetVariablesResponse;
import io.camunda.zeebe.model.bpmn.instance.FlowElement;
import io.camunda.zeebe.protocol.record.value.BpmnElementType;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.lorislab.zeebe.dev.monitor.bpmn.BpmnModel;
import org.lorislab.zeebe.dev.monitor.dto.*;
import org.lorislab.zeebe.dev.monitor.mapper.*;
import org.lorislab.zeebe.dev.monitor.models.*;
import org.lorislab.zeebe.dev.monitor.models.Error;
import org.lorislab.zeebe.dev.monitor.models.Timer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Path("/api/instance")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class InstanceController {

    @Inject
    ZeebeClient client;

    @Inject
    InstanceMapper instanceMapper;

    @Inject
    InstanceTableMapper tableMapper;

    @Inject
    MessageSubscriptionMapper messageSubscriptionMapper;

    @Inject
    TimerMapper timerMapper;

    @Inject
    JobMapper jobMapper;

    @Inject
    ErrorMapper errorMapper;

    @Inject
    IncidentMapper incidentMapper;

    @Inject
    AuditLogMapper auditLogMapper;

    @Inject
    VariableMapper variableMapper;

    @Inject
    ElementInstanceMapper elementInstanceMapper;

    @GET
    public Response getAll() {
        return Response.ok(tableMapper.tableItems(Instance.list("ORDER BY start DESC"))).build();
    }

    @GET
    @Path("{key}")
    public Response loadByKey(@PathParam("key") long key) {
        Instance item = Instance.findById(key);
        String parentBpmnProcessId = null;
        Long parentProcessDefinitionKey = -1L;
        if (item.parentProcessInstanceKey > 0) {
            Instance parent = Instance.findById(item.parentProcessInstanceKey);
            if (parent != null) {
                parentProcessDefinitionKey = parent.processDefinitionKey;
                parentBpmnProcessId = parent.bpmnProcessId;

            }
        }

        // instance detail
        InstanceDetailDTO detail = instanceMapper.detail(item, parentBpmnProcessId, parentProcessDefinitionKey);



        final Map<Long, String> elementIdsForKeys = new HashMap<>();
        elementIdsForKeys.put(item.key, item.bpmnProcessId);

        List<ElementInstance> events = ElementInstance.find("processInstanceKey = ?1 ORDER BY timestamp DESC", item.key).list();

        Set<String> completedActivities = new HashSet<>();
        Set<String> completedItems = new HashSet<>();
        List<String> takenSequenceFlows = new ArrayList<>();
        Map<String, Long> completedElementsById = new HashMap<>();
        Map<String, Long> enteredElementsById = new HashMap<>();

        Set<Long> completedElementInstances = new HashSet<>();

        events.forEach(e -> {
            elementIdsForKeys.put(e.key, e.elementId);
            if (e.intent == ElementInstance.Intent.ELEMENT_COMPLETED) {
                if (!BpmnElementType.PROCESS.name().equals(e.bpmnElementType)) {
                    completedItems.add(e.elementId);
                }
            }
            if (e.intent == ElementInstance.Intent.ELEMENT_COMPLETED || e.intent == ElementInstance.Intent.ELEMENT_TERMINATED) {
                completedElementInstances.add(e.key);
                if (!BpmnElementType.PROCESS.name().equals(e.bpmnElementType)) {
                    completedActivities.add(e.elementId);
                }
                if (!BpmnElementType.MULTI_INSTANCE_BODY.name().equals(e.bpmnElementType)) {
                    completedElementsById.compute(e.elementId, InstanceController::count);
                }
            }
            if (e.intent == ElementInstance.Intent.SEQUENCE_FLOW_TAKEN) {
                takenSequenceFlows.add(e.elementId);
            }
            if (e.intent == ElementInstance.Intent.ELEMENT_ACTIVATED) {
                if (!BpmnElementType.MULTI_INSTANCE_BODY.name().equals(e.bpmnElementType) &&
                        !BpmnElementType.PROCESS.name().equals(e.bpmnElementType)) {
                    enteredElementsById.compute(e.elementId, InstanceController::count);
                }
            }
        });

        final List<ElementInstanceDTO> terminateActiveActivities = new ArrayList<>();
        final List<ElementInstanceDTO> ancestorActivities = new ArrayList<>();
        final List<String> activeActivitiesTmp =
                events.stream()
                        .filter(e -> !BpmnElementType.PROCESS.name().equals(e.bpmnElementType))
                        .filter(e -> e.intent == ElementInstance.Intent.ELEMENT_ACTIVATED)
                        .peek(e -> ancestorActivities.add(elementInstanceMapper.item(e)))
                        .filter(e -> !completedActivities.contains(e.elementId))
                        .peek(e -> terminateActiveActivities.add(elementInstanceMapper.item(e)))
                        .map(e -> e.elementId)
                        .toList();

        final List<ElementInstanceStateDTO> elementStates =
                enteredElementsById.entrySet().stream()
                        .map(e -> {
                            long completedInstances = completedElementsById.getOrDefault(e.getKey(), 0L);
                            return new ElementInstanceStateDTO(e.getKey(), e.getValue() - completedInstances, completedInstances);
                        }).toList();

        List<String> activeActivities = new ArrayList<>(activeActivitiesTmp);
        List<String> incidentActivities = null;

        // incidents
        List<Incident> tmp = Incident.find("processInstanceKey = ?1 ORDER BY created DESC", item.key).list();
        List<InstanceIncidentDTO> incidents = incidentMapper.incidents(tmp, elementIdsForKeys);

        incidentActivities = incidents.stream()
                .filter(x -> !x.isResolved())
                .map(x -> elementIdsForKeys.get(x.elementInstanceKey()))
                .distinct().toList();

        activeActivities.removeAll(incidentActivities);


        // Activity scope
        List<ActiveScopeDTO> activeScopes = null;
        if (detail.isRunning()) {
            activeScopes = events.stream()
                    .filter(e -> e.intent == ElementInstance.Intent.ELEMENT_ACTIVATED)
                    .map(e -> e.key)
                    .filter(e -> !completedElementInstances.contains(e))
                    .map(k -> new ActiveScopeDTO(k, elementIdsForKeys.get(k))).toList();
        }

        // definition XML
        BpmnXmlResource xml = BpmnXmlResource.findById(item.processDefinitionKey);

        // jobs
        List<JobDTO> jobs = jobMapper.jobs(Job.find("processInstanceKey = ?1 ORDER BY timestamp DESC", item.key).list(), elementIdsForKeys);

        // variables
        List<VariableDTO> variables = variableMapper.variables(Variable.find("processInstanceKey = ?1 ORDER BY timestamp DESC", item.key).stream(), elementIdsForKeys);

        // errors
        List<ErrorDTO> errors = errorMapper.errors(Error.find("processInstanceKey = ?1 ORDER BY timestamp DESC", item.key).list());

        // timers
        List<TimerDTO> timers = timerMapper.timers(Timer.find("processInstanceKey = ?1 ORDER BY timestamp DESC", item.key).list());

        // messageSubscriptions
        List<MessageSubscriptionDTO> messageSubscriptions = messageSubscriptionMapper.messages2(MessageSubscription.find("processInstanceKey = ?1 ORDER BY timestamp DESC", item.key).stream(), elementIdsForKeys);

        // audit log
        final List<ActivateElementItemDTO> activateActivities = new ArrayList<>();
        final var bpmn = BpmnModel.loadModel(xml);
        final Map<String, String> flowElements = new HashMap<>();
        bpmn.getModelElementsByType(FlowElement.class).forEach(e -> {

            String name = Optional.ofNullable(e.getName()).orElse("");
            flowElements.put(e.getId(), name);

            BpmnElementType type = BpmnElementType.bpmnElementTypeFor(e.getElementType().getTypeName());
            if (type != null && !MODIFY_UNSUPPORTED_ELEMENT_TYPES.contains(type.name())) {
                activateActivities.add(new ActivateElementItemDTO(e.getId(), name));
            }
        });
//        Collections.reverse(events);
        List<AuditLogDTO> auditLogEntries = auditLogMapper.logs(events, flowElements);

        // bpmnElementInfos
        List<BpmnElementInfoDTO> bpmnElementInfos = BpmnModel.loadBpmnElements(bpmn);

        // call process instances
        List<CalledProcessInstanceDTO> callProcessInstances = instanceMapper.processes(Instance.find("parentProcessInstanceKey = ?1 ORDER BY start DESC", item.key).stream(), elementIdsForKeys);

        InstanceDTO result = new InstanceDTO(
                detail, new String(xml.resource), elementStates, activeScopes, activeActivities, takenSequenceFlows, auditLogEntries,
                incidentActivities, callProcessInstances, incidents, jobs,
                messageSubscriptions, timers, errors, variables,
                bpmnElementInfos, completedItems, terminateActiveActivities, activateActivities, ancestorActivities);

        return Response.ok(result).build();
    }

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

    static Long count(String key, Long value) {
        if (value == null) {
            return 1L;
        }
        return value + 1;
    }

    private static final Set<String> MODIFY_UNSUPPORTED_ELEMENT_TYPES =
            Set.of(BpmnElementType.UNSPECIFIED.name(), BpmnElementType.START_EVENT.name(),
                    BpmnElementType.SEQUENCE_FLOW.name(), BpmnElementType.BOUNDARY_EVENT.name());

    @RegisterForReflection
    public record ActivateElementDTO(String id, Long ancestor, Map<String, Object> vars) {};
    @RegisterForReflection
    public record ProcessInstanceModifyDTO(List<ActivateElementDTO> activates, List<Long> terminates) {};
}
