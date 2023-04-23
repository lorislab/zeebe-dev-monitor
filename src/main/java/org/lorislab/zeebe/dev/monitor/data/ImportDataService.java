package org.lorislab.zeebe.dev.monitor.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.camunda.zeebe.protocol.Protocol;
import io.camunda.zeebe.protocol.record.Record;
import io.camunda.zeebe.protocol.record.intent.*;
import io.camunda.zeebe.protocol.record.value.*;
import io.camunda.zeebe.protocol.record.value.deployment.DeploymentResource;
import io.camunda.zeebe.protocol.record.value.deployment.ProcessMetadataValue;
import org.lorislab.zeebe.dev.monitor.models.*;
import org.lorislab.zeebe.dev.monitor.models.Error;
import org.lorislab.zeebe.dev.monitor.ws.NotificationService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
@Transactional
public class ImportDataService {


    @Inject
    ObjectMapper mapper;

    @Inject
    NotificationService instanceNotificationService;

    public void importProcessInstance(final Record<ProcessInstanceRecordValue> record) {
        if (record.getValue().getProcessInstanceKey() == record.getKey()) {
            processInstance(record);
        }
        elementInstance(record);
    }

    private void processInstance(final Record<ProcessInstanceRecordValue> record) {
        ProcessInstanceRecordValue value = record.getValue();
        Instance entity = Instance.findById(value.getProcessInstanceKey());
        if (entity == null) {
            entity = new Instance();
            entity.partitionId = record.getPartitionId();
            entity.key = value.getProcessInstanceKey();
            entity.bpmnProcessId = value.getBpmnProcessId();
            entity.version = value.getVersion();
            entity.processDefinitionKey = value.getProcessDefinitionKey();
            entity.parentProcessInstanceKey = value.getParentProcessInstanceKey();
            entity.parentElementInstanceKey = value.getParentElementInstanceKey();
        }

        ProcessInstanceIntent intent = (ProcessInstanceIntent) record.getIntent();
        switch (intent) {
            case ELEMENT_ACTIVATED -> {
                entity.state = Instance.State.ACTIVE;
                entity.start = localDateTime(record.getTimestamp());
                entity.persistAndFlush();
                instanceNotificationService.sendEvent(
                        new NotificationService.InstanceEvent(value.getProcessInstanceKey(), value.getProcessDefinitionKey(), NotificationService.ProcessInstanceEventType.CREATED)
                );
            }
            case ELEMENT_COMPLETED -> {
                entity.state = Instance.State.COMPLETED;
                entity.end = localDateTime(record.getTimestamp());
                entity.persistAndFlush();
                instanceNotificationService.sendEvent(
                        new NotificationService.InstanceEvent(value.getProcessInstanceKey(), value.getProcessDefinitionKey(), NotificationService.ProcessInstanceEventType.REMOVED)
                );
            }
            case ELEMENT_TERMINATED -> {
                entity.state = Instance.State.TERMINATED;
                entity.end = localDateTime(record.getTimestamp());
                entity.persistAndFlush();
                instanceNotificationService.sendEvent(
                        new NotificationService.InstanceEvent(value.getProcessInstanceKey(), value.getProcessDefinitionKey(), NotificationService.ProcessInstanceEventType.REMOVED)
                );
            }
        }
    }

    private void elementInstance(final Record<ProcessInstanceRecordValue> record) {
        String id = generateId(record);
        ElementInstance entity = ElementInstance.findById(id);
        if(entity == null) {
            ProcessInstanceRecordValue value = record.getValue();


            entity = new ElementInstance();
            entity.id = id;
            entity.partitionId = record.getPartitionId();
            entity.position = record.getPosition();
            entity.key = record.getKey();
            entity.intent = ElementInstance.Intent.valueOf(record.getIntent().name());
            entity.timestamp = localDateTime(record.getTimestamp());
            entity.processInstanceKey = value.getProcessInstanceKey();
            entity.elementId = value.getElementId();
            entity.flowScopeKey = value.getFlowScopeKey();
            entity.processDefinitionKey = value.getProcessDefinitionKey();
            entity.bpmnElementType = value.getBpmnElementType().name();
            entity.persistAndFlush();

            instanceNotificationService.sendEvent(
                    new NotificationService.InstanceEvent(value.getProcessInstanceKey(), value.getProcessDefinitionKey(), NotificationService.ProcessInstanceEventType.UPDATED)
            );
        }
    }

    public void importWorkflowProcess(final Record<DeploymentRecordValue> record) {
        if (record.getPartitionId() != Protocol.DEPLOYMENT_PARTITION) {
            return;
        }
        DeploymentRecordValue value = record.getValue();
        if (value.getProcessesMetadata() == null || value.getProcessesMetadata().isEmpty()) {
            return;
        }
        Map<String, byte[]> resources = value.getResources()
                .stream().collect(Collectors.toMap(DeploymentResource::getResourceName, DeploymentResource::getResource));
        value.getProcessesMetadata().forEach(x -> importWorkflowProcess(x, localDateTime(record.getTimestamp()), resources.get(x.getResourceName())));

        instanceNotificationService.sendEvent(new NotificationService.ProcessEvent(NotificationService.ProcessEventType.DEPLOYED));
    }

    private void importWorkflowProcess(final ProcessMetadataValue value, LocalDateTime timestamp, byte[] resource) {
        Definition tmp = Definition.findById(value.getProcessDefinitionKey());
        if (tmp != null) {
            //TODO: check version and duplicates
            return;
        }
        Definition entity = new Definition();
        entity.key = value.getProcessDefinitionKey();
        entity.bpmnProcessId = value.getBpmnProcessId();
        entity.version = value.getVersion();
        entity.timestamp = timestamp;
        entity.persistAndFlush();

        BpmnXmlResource dr = new BpmnXmlResource();
        dr.name = value.getResourceName();
        dr.key = entity.key;
        dr.resource = resource;
        dr.persistAndFlush();
    }

    public void importTimer(final Record<TimerRecordValue> record) {
        Timer timer = Timer.findById(record.getKey());
        if (timer == null) {
            TimerRecordValue value = record.getValue();
            timer = new Timer();
            timer.key = record.getKey();
            timer.processDefinitionKey = value.getProcessDefinitionKey();
            timer.targetElementId = value.getTargetElementId();
            timer.dueDate = localDateTime(value.getDueDate());
            timer.repetitions = value.getRepetitions();
            if (value.getProcessInstanceKey() > 0) {
                timer.processInstanceKey = value.getProcessInstanceKey();
                timer.elementInstanceKey = value.getElementInstanceKey();
            }
        }
        TimerIntent intent = (TimerIntent) record.getIntent();
        timer.state = Timer.State.valueOf(intent.name());
        timer.timestamp = localDateTime(record.getTimestamp());
        timer.persistAndFlush();
    }

    public void importMessageSubscription(final Record<ProcessMessageSubscriptionRecordValue> record) {
        ProcessMessageSubscriptionRecordValue value = record.getValue();
        MessageSubscription ms = MessageSubscription.findByElementInstanceKeyAndMessageName(value.getElementInstanceKey(), value.getMessageName())
                .orElseGet(() -> {
                    MessageSubscription tmp = new MessageSubscription();
                    tmp.id = UUID.randomUUID().toString();
                    tmp.elementInstanceKey = value.getElementInstanceKey();
                    tmp.elementId = value.getElementId();
                    tmp.messageName = value.getMessageName();
                    tmp.correlationKey = value.getCorrelationKey();
                    tmp.processInstanceKey = value.getProcessInstanceKey();
                    tmp.variables = toJsonString(value.getVariables());
                    return tmp;
                });

        ProcessMessageSubscriptionIntent intent = (ProcessMessageSubscriptionIntent) record.getIntent();
        ms.state = MessageSubscription.State.valueOf(intent.name());
        ms.timestamp = localDateTime(record.getTimestamp());
        ms.persistAndFlush();
    }

    public void importMessageStartEventSubscription(final Record<MessageStartEventSubscriptionRecordValue> record) {
        MessageStartEventSubscriptionRecordValue value = record.getValue();
        MessageSubscription ms = MessageSubscription.findByProcessDefinitionKeyAndMessageName(value.getProcessDefinitionKey(), value.getMessageName())
                .orElseGet(() -> {
                    MessageSubscription tmp = new MessageSubscription();
                    tmp.id = UUID.randomUUID().toString();
                    tmp.messageName = value.getMessageName();
                    tmp.processDefinitionKey = value.getProcessDefinitionKey();
                    tmp.elementId = value.getStartEventId();
                    tmp.variables = toJsonString(value.getVariables());
                    return tmp;
                });

        MessageStartEventSubscriptionIntent intent = (MessageStartEventSubscriptionIntent) record.getIntent();
        ms.state = MessageSubscription.State.valueOf(intent.name());
        ms.timestamp = localDateTime(record.getTimestamp());
        ms.persistAndFlush();
    }

    public void importError(final Record<ErrorRecordValue> record) {
        Error e = Error.findById(record.getPosition());
        if (e != null) {
            return;
        }
        ErrorRecordValue value = record.getValue();

        e = new Error();
        e.position = record.getPosition();
        e.errorEventPosition = value.getErrorEventPosition();
        e.processInstanceKey = value.getProcessInstanceKey();
        e.exceptionMessage = value.getExceptionMessage();
        e.stacktrace = value.getStacktrace();
        e.persistAndFlush();
    }

    public void importIncident(final Record<IncidentRecordValue> record) {
        Incident incident = Incident.findById(record.getKey());
        if (incident == null) {
            IncidentRecordValue value = record.getValue();
            incident = new Incident();
            incident.key = record.getKey();
            incident.bpmnProcessId = value.getBpmnProcessId();
            incident.processDefinitionKey = value.getProcessDefinitionKey();
            incident.processInstanceKey = value.getProcessInstanceKey();
            incident.elementInstanceKey = value.getElementInstanceKey();
            incident.jobKey = value.getJobKey();
            incident.errorType = Incident.ErrorType.valueOf(value.getErrorType().name());
            incident.errorMessage = value.getErrorMessage();
        }

        IncidentIntent intent = (IncidentIntent) record.getIntent();
        if (intent == IncidentIntent.CREATED) {
            incident.created = localDateTime(record.getTimestamp());
        }
        if (intent == IncidentIntent.RESOLVED) {
            incident.resolved = localDateTime(record.getTimestamp());
        }
        incident.persistAndFlush();
    }

    public void importJob(final Record<JobRecordValue> record) {
        JobRecordValue value = record.getValue();
        if (UserTaskHeaders.JOB_TYPE.equals(value.getType())) {
            importUserTask(record);
            return;
        }
        Job job = Job.findById(record.getKey());
        if (job == null) {
            job = new Job();
            job.key = record.getKey();
            job.processInstanceKey = value.getProcessInstanceKey();
            job.elementInstanceKey = value.getElementInstanceKey();
            job.jobType = value.getType();
            job.elementId = value.getElementId();
        }
        JobIntent intent = (JobIntent) record.getIntent();
        job.state = Job.State.valueOf(intent.name());
        job.timestamp = localDateTime(record.getTimestamp());
        job.worker = value.getWorker();
        job.retries = value.getRetries();
        job.errorCode = value.getErrorCode();
        job.errorMessage = value.getErrorMessage();
        job.variables = toJsonString(value.getVariables());
        job.persistAndFlush();
    }

    public void importUserTask(final Record<JobRecordValue> record) {
        UserTask task = UserTask.findById(record.getKey());
        JobRecordValue value = record.getValue();
        if (task == null) {
            task = new UserTask();
            task.key = record.getKey();
            task.processInstanceKey = value.getProcessInstanceKey();
            task.elementInstanceKey = value.getElementInstanceKey();
            task.jobType = value.getType();
            task.elementId = value.getElementId();
        }
        JobIntent intent = (JobIntent) record.getIntent();
        task.status = UserTask.Status.valueOf(intent.name());
        task.timestamp = localDateTime(record.getTimestamp());
        task.worker = value.getWorker();
        task.retries = value.getRetries();
        task.errorCode = value.getErrorCode();
        task.errorMessage = value.getErrorMessage();
        Map<String, String> headers = value.getCustomHeaders();
        task.users =  headers.get(UserTaskHeaders.CANDIDATE_USERS);
        task.groups =  headers.get(UserTaskHeaders.CANDIDATE_GROUPS);
        task.assignee =  headers.get(UserTaskHeaders.ASSIGNEE);
        task.dueDate = fromString(headers.get(UserTaskHeaders.DUE_DATE));
        task.followUpDate = fromString(headers.get(UserTaskHeaders.FOLLOW_UP_DATE));
        task.variables = toJsonString(value.getVariables());
        task.persistAndFlush();
    }



    public void importMessage(final Record<MessageRecordValue> record) {
        Message m = Message.findById(record.getKey());
        if (m == null) {
            MessageRecordValue value = record.getValue();
            m = new Message();
            m.key = record.getKey();
            m.name = value.getName();
            m.correlationKey = value.getCorrelationKey();
            m.messageId = value.getMessageId();
            m.variables = toJsonString(value.getVariables());
        }
        // check if message already published
        if (Message.State.PUBLISHED == m.state) {
            return;
        }
        MessageIntent intent = (MessageIntent) record.getIntent();
        m.state = Message.State.valueOf(intent.name());
        m.timestamp = localDateTime(record.getTimestamp());
        m.persistAndFlush();
    }

    public void importVariable(final Record<VariableRecordValue> record) {
        String id = generateId(record);
        Variable variable = Variable.findById(id);
        if (variable == null) {
            variable = new Variable();
            variable.id = id;
            variable.position = record.getPosition();
            variable.partitionId = record.getPartitionId();
            variable.timestamp = localDateTime(record.getTimestamp());
            VariableRecordValue value = record.getValue();
            variable.processInstanceKey = value.getProcessInstanceKey();
            variable.name = value.getName();
            variable.value = value.getValue();
            variable.scopeKey = value.getScopeKey();
            VariableIntent intent = (VariableIntent) record.getIntent();
            variable.state = Variable.State.valueOf(intent.name());
            variable.persistAndFlush();

            instanceNotificationService.sendEvent(
                    new NotificationService.InstanceEvent(value.getProcessInstanceKey(), value.getProcessDefinitionKey(), NotificationService.ProcessInstanceEventType.UPDATED)
            );
        }
    }

    public void importSignal(final Record<SignalRecordValue> record) {
        Signal signal = Signal.findById(record.getKey());
        if (signal == null) {
            signal = new Signal();
            signal.key = record.getKey();
            signal.position = record.getPosition();
            signal.partitionId = record.getPartitionId();
            signal.timestamp = localDateTime(record.getTimestamp());
            SignalRecordValue value = record.getValue();
            signal.name = value.getSignalName();
            signal.variables = toJsonString(value.getVariables());
            SignalIntent intent = (SignalIntent) record.getIntent();
            signal.status = Signal.Status.valueOf(intent.name());
            signal.persistAndFlush();
        }
    }

    public void importSignalSubscription(final Record<SignalSubscriptionRecordValue> record) {
        SignalSubscription signal = SignalSubscription.findById(record.getKey());
        if (signal == null) {
            signal = new SignalSubscription();
            signal.key = record.getKey();
            signal.position = record.getPosition();
            signal.partitionId = record.getPartitionId();
            signal.timestamp = localDateTime(record.getTimestamp());
            SignalSubscriptionRecordValue value = record.getValue();

            signal.bpmnProcessId = value.getBpmnProcessId();
            signal.name = value.getSignalName();
            signal.processDefinitionKey = value.getProcessDefinitionKey();
            signal.catchEventId = value.getCatchEventId();
            signal.catchEventInstanceKey = value.getCatchEventInstanceKey();

            SignalSubscriptionIntent intent = (SignalSubscriptionIntent) record.getIntent();
            signal.status = SignalSubscription.Status.valueOf(intent.name());

            signal.persistAndFlush();
        }
    }

    public void importEscalation(final Record<EscalationRecordValue> record) {
        Escalation escalation = Escalation.findById(record.getKey());
        if (escalation == null) {
            escalation = new Escalation();
            escalation.key = record.getKey();
            escalation.position = record.getPosition();
            escalation.partitionId = record.getPartitionId();
            escalation.timestamp = localDateTime(record.getTimestamp());
            EscalationRecordValue value = record.getValue();
            escalation.processInstanceKey = value.getProcessInstanceKey();
            escalation.catchElementId = value.getCatchElementId();
            escalation.escalationCode = value.getEscalationCode();
            escalation.throwElementId = value.getThrowElementId();
            escalation.persistAndFlush();
        }
    }

    private String toJsonString(Map<String, Object> data) {
        if (data == null) {
            return null;
        }
        try {
            return mapper.writeValueAsString(data);
        } catch (Exception ex) {
            throw new RuntimeException("Error convert to json string", ex);
        }
    }

    private static LocalDateTime localDateTime(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneOffset.systemDefault());
    }

    private static LocalDateTime fromString(String data) {
        if (data == null || data.isEmpty()) {
            return null;
        }
        OffsetDateTime odt = OffsetDateTime.parse(data);
        return LocalDateTime.ofInstant(odt.toInstant(), ZoneOffset.systemDefault());
    }

    private static String generateId(final Record<?> record) {
        return record.getPartitionId() + "-" + record.getPosition();
    }
}
