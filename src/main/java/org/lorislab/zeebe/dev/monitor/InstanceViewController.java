package org.lorislab.zeebe.dev.monitor;

import io.camunda.zeebe.model.bpmn.instance.FlowElement;
import io.camunda.zeebe.protocol.record.value.BpmnElementType;
import io.quarkus.qute.RawString;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.lorislab.zeebe.dev.monitor.mapper.AuditLogMapper;
import org.lorislab.zeebe.dev.monitor.mapper.ErrorMapper;
import org.lorislab.zeebe.dev.monitor.mapper.IncidentMapper;
import org.lorislab.zeebe.dev.monitor.mapper.InstanceMapper;
import org.lorislab.zeebe.dev.monitor.mapper.InstanceTableMapper;
import org.lorislab.zeebe.dev.monitor.mapper.JobMapper;
import org.lorislab.zeebe.dev.monitor.mapper.MessageSubscriptionMapper;
import org.lorislab.zeebe.dev.monitor.mapper.OffsetDateTimeMapper;
import org.lorislab.zeebe.dev.monitor.mapper.TimerMapper;
import org.lorislab.zeebe.dev.monitor.mapper.VariableMapper;
import org.lorislab.zeebe.dev.monitor.models.BpmnXmlResource;
import org.lorislab.zeebe.dev.monitor.models.ElementInstance;
import org.lorislab.zeebe.dev.monitor.models.Incident;
import org.lorislab.zeebe.dev.monitor.models.Instance;
import org.lorislab.zeebe.dev.monitor.models.Job;
import org.lorislab.zeebe.dev.monitor.models.Error;
import org.lorislab.zeebe.dev.monitor.models.MessageSubscription;
import org.lorislab.zeebe.dev.monitor.models.Timer;
import org.lorislab.zeebe.dev.monitor.models.Variable;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("instance")
public class InstanceViewController {

    @Inject
    Template instances;

    @Inject
    Template instance;

    @Inject
    InstanceTableMapper instanceTableMapper;

    @Inject
    InstanceMapper instanceMapper;

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

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getInstances() {
        return instances.data(
                "instances", instanceTableMapper.items(Instance.findAll().list()),
                "count", Instance.count()
        );
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.TEXT_HTML)
    @Transactional(Transactional.TxType.NEVER)
    public TemplateInstance getInstance(@PathParam("id") Long id) {
        Instance item = Instance.findById(id);
        String parentBpmnProcessId = null;
        if (item.parentProcessInstanceKey > 0) {
            Instance tmp = Instance.findById(item.parentProcessInstanceKey);
            if (tmp != null) {
                parentBpmnProcessId = tmp.bpmnProcessId;
            }
        }

        // instance detail
        InstanceDetailData detail = instanceMapper.item(item, parentBpmnProcessId);


        final Map<Long, String> elementIdsForKeys = new HashMap<>();
        elementIdsForKeys.put(item.key, item.bpmnProcessId);

        long eventsCount = ElementInstance.count("processInstanceKey", item.key);
        List<ElementInstance> events = ElementInstance.find("processInstanceKey", item.key).list();

        Set<String> completedActivities = new HashSet<>();
        Set<String> completedItems = new HashSet<>();
        List<String> takenSequenceFlows = new ArrayList<>();
        Map<String, Long> completedElementsById = new HashMap<>();
        Map<String, Long> enteredElementsById = new HashMap<>();

        Set<Long> completedElementInstances = new HashSet<>();

        events.forEach(e -> {
            elementIdsForKeys.put(e.key, e.elementId);
            if (e.intent == ElementInstance.Intent.ELEMENT_COMPLETED) {
                if (!BpmnElementType.PROCESS.getElementTypeName().get().equals(e.bpmnElementType)) {
                    completedItems.add(e.elementId);
                }
            }
            if (e.intent == ElementInstance.Intent.ELEMENT_COMPLETED || e.intent == ElementInstance.Intent.ELEMENT_TERMINATED) {
                completedElementInstances.add(e.key);
                if (!BpmnElementType.PROCESS.getElementTypeName().get().equals(e.bpmnElementType)) {
                    completedActivities.add(e.elementId);
                }
                if (!BpmnElementType.MULTI_INSTANCE_BODY.name().equals(e.bpmnElementType)) {
                    completedElementsById.compute(e.elementId, InstanceViewController::count);
                }
            }
            if (e.intent == ElementInstance.Intent.SEQUENCE_FLOW_TAKEN) {
                takenSequenceFlows.add(e.elementId);
            }
            if (e.intent == ElementInstance.Intent.ELEMENT_ACTIVATED) {
                if (!BpmnElementType.MULTI_INSTANCE_BODY.name().equals(e.bpmnElementType)) {
                    enteredElementsById.compute(e.elementId, InstanceViewController::count);
                }
            }
        });

        final List<ElementInstance> terminateActiveActivities = new ArrayList<>();
        final List<ElementInstance> ancestorActivities = new ArrayList<>();
        final List<String> activeActivitiesTmp =
                events.stream()
                        .filter(e -> !BpmnElementType.PROCESS.getElementTypeName().get().equals(e.bpmnElementType))
                        .filter(e -> e.intent == ElementInstance.Intent.ELEMENT_ACTIVATED)
                        .peek(ancestorActivities::add)
                        .filter(e -> !completedActivities.contains(e.elementId))
                        .peek(terminateActiveActivities::add)
                        .map(e -> e.elementId)
                        .toList();

        final List<ElementInstanceStateData> elementStates =
                enteredElementsById.entrySet().stream()
                        .map(e -> {
                            long completedInstances = completedElementsById.getOrDefault(e.getKey(), 0L);
                            return new ElementInstanceStateData(e.getKey(), e.getValue() - completedInstances, completedInstances);
                        }).toList();

        List<String> activeActivities = new ArrayList<>(activeActivitiesTmp);
        List<String> incidentActivities = null;

        // incidents
        long incidentCount = Incident.count("processInstanceKey", item.key);
        List<IncidentData> incidents = null;
        if (incidentCount > 0) {
            List<Incident> tmp = Incident.find("processInstanceKey", item.key).list();
            incidents = incidentMapper.items(tmp, elementIdsForKeys);

            incidentActivities = incidents.stream()
                    .filter(x -> !x.isResolved)
                    .map(x -> elementIdsForKeys.get(x.elementInstanceKey))
                    .distinct().toList();

            activeActivities.removeAll(incidentActivities);
        }

        // Activity scope
        List<ActiveScopeData> activeScopes = null;
        if (detail.isRunning) {
            activeScopes = events.stream()
                .filter(e -> e.intent == ElementInstance.Intent.ELEMENT_ACTIVATED)
                .map(e -> e.key)
                .filter(e -> !completedElementInstances.contains(e))
                .map(key -> new ActiveScopeData(key, elementIdsForKeys.get(key))).toList();
        }

        // definition XML
        BpmnXmlResource xml = BpmnXmlResource.findById(item.processDefinitionKey);
        RawString resource = new RawString(new String(xml.resource));

        // jobs
        long jobCount = Job.count("processInstanceKey", item.key);
        List<JobData> jobs = null;
        if (jobCount > 0) {
            jobs = jobMapper.items(Job.find("processInstanceKey", item.key).list(), elementIdsForKeys);
        }

        // variables
        long variableCount = Variable.count("processInstanceKey", item.key);
        List<VariableData> variables = null;
        if (variableCount > 0) {
            variables = variableMapper.items(Variable.find("processInstanceKey", item.key).stream(), elementIdsForKeys);
        }

        // errors
        long errorCount =  Error.count("processInstanceKey", item.key);
        List<ErrorData> errors = null;
        if (errorCount > 0) {
            errors = errorMapper.items(Error.find("processInstanceKey", item.key).list());
        }

        // timers
        long timerCount = Timer.count("processInstanceKey", item.key);
        List<TimerData> timers = null;
        if (timerCount > 0) {
            timers = timerMapper.items(Timer.find("processInstanceKey", item.key).list());
        }

        // messageSubscriptions
        long messageSubscriptionCount = MessageSubscription.count("processInstanceKey", item.key);
        List<MessageSubscriptionData> messageSubscriptions = null;
        if (messageSubscriptionCount > 0) {
            messageSubscriptions = messageSubscriptionMapper.items(MessageSubscription.find("processInstanceKey", item.key).stream(), elementIdsForKeys);
        }

        // audit log
        final List<ActivateElement> activateActivities = new ArrayList<>();
        final var bpmn = BpmnModel.loadModel(xml);
        final Map<String, String> flowElements = new HashMap<>();
        bpmn.getModelElementsByType(FlowElement.class).forEach(e -> {

            String name = Optional.ofNullable(e.getName()).orElse("");
            flowElements.put(e.getId(), name);

            if (!MODIFY_UNSUPPORTED_ELEMENT_TYPES.contains(Optional.of(e.getElementType().getTypeName()))) {
                activateActivities.add(new ActivateElement(e.getId(), name));
            }
        });
        List<AuditLogData> auditLogEntries = auditLogMapper.items(events, flowElements);

        // bpmnElementInfos
        List<BpmnElementInfoData> bpmnElementInfos = BpmnModel.loadBpmnElementInfos(bpmn);

        // call process instances
        long callProcessInstancesCount = Instance.count("parentProcessInstanceKey", item.key);
        List<CalledProcessInstanceData> callProcessInstances = null;
        if (callProcessInstancesCount > 0) {
            callProcessInstances = instanceMapper.items(Instance.find("parentProcessInstanceKey", item.key).stream(), elementIdsForKeys);
        }

        return instance.data("instance", new InstanceWrapper(
                detail, resource, elementStates, activeScopes, activeActivities, takenSequenceFlows, auditLogEntries,
                eventsCount,
                incidentActivities, callProcessInstances, callProcessInstancesCount, incidents, incidentCount, jobs,
                jobCount, messageSubscriptions,
                messageSubscriptionCount, timers, timerCount, errors, errorCount, variables, variableCount,
                bpmnElementInfos, completedItems, terminateActiveActivities, activateActivities, ancestorActivities));
    }


    @RegisterForReflection
    public record ActivateElement(String id, String name) {};
    @RegisterForReflection
    public record InstanceWrapper(InstanceDetailData detail, RawString resource, List<ElementInstanceStateData> elementInstances,
                                  List<ActiveScopeData> activeScopes, List<String> activeActivities, List<String> takenSequenceFlows,
                                  List<AuditLogData> auditLogEntries, long auditLogEntriesCount, List<String> incidentActivities,
                                  List<CalledProcessInstanceData> callProcessInstances,  long callProcessInstancesCount,
                                  List<IncidentData> incidents, long incidentsCount, List<JobData> jobs, long jobsCount,
                                  List<MessageSubscriptionData> messageSubscriptions, long messageSubscriptionsCount,
                                  List<TimerData> timers, long timersCount, List<ErrorData> errors, long errorsCount,
                                  List<VariableData> variables, long variablesCount, List<BpmnElementInfoData> bpmnElementInfos,
                                  Set<String> completedActivities, List<ElementInstance> terminateActiveActivities, List<ActivateElement> activateActivities,
                                  List<ElementInstance> ancestorActivities) {}
    @RegisterForReflection
    public record VariableId(long scopeKey, String name) {}

    static Long count(String key, Long value) {
        if (value == null) {
            return 1L;
        }
        return value + 1;
    }

    @RegisterForReflection
    public record VariableData(String name, String value, long scopeKey, String elementId, OffsetDateTime timestamp,
                               List<VariableValueData> values) {}

    @RegisterForReflection
    public record VariableValueData(String value, OffsetDateTime timestamp) {}

    @RegisterForReflection
    public record IncidentData(long key, String bpmnProcessId, String elementId, long elementInstanceKey, long jobKey,
                               String payload,
                               Incident.ErrorType errorType, String errorMessage, IncidentState state,
                               OffsetDateTime created, OffsetDateTime resolved, boolean isResolved) {}

    @RegisterForReflection
    public record ActiveScopeData(long scopeKey, String scopeName) {}

    @RegisterForReflection
    public record AuditLogData(long key, long flowScopeKey, String elementId, String elementName, String bpmnElementType,
                                String payload, ElementInstance.Intent state, OffsetDateTime timestamp) {}

    @RegisterForReflection
    public record CalledProcessInstanceData(long key, String elementId, long elementInstanceKey, String bpmnProcessId,
                                            Instance.State state) {}

    @RegisterForReflection
    public record InstanceDetailData(long key, String bpmnProcessId, long processDefinitionKey,
                                     Instance.State state, OffsetDateTime start, OffsetDateTime end,
                                     int partitionId, int version, boolean isRunning, Long parentProcessInstanceKey,
                                     String parentBpmnProcessId) {}

    private static final Set<Optional<String>> MODIFY_UNSUPPORTED_ELEMENT_TYPES =
            Set.of(BpmnElementType.UNSPECIFIED.getElementTypeName(), BpmnElementType.START_EVENT.getElementTypeName(),
                    BpmnElementType.SEQUENCE_FLOW.getElementTypeName(), BpmnElementType.BOUNDARY_EVENT.getElementTypeName());

}
