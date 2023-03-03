package org.lorislab.zeebe.dev.monitor.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;
import java.util.Set;

@RegisterForReflection
public record InstanceDTO(InstanceDetailDTO detail, String xml, List<ElementInstanceStateDTO> elementInstances,
                              List<ActiveScopeDTO> activeScopes, List<String> activeActivities, List<String> takenSequenceFlows,
                              List<AuditLogDTO> auditLogEntries, List<String> incidentActivities,
                              List<CalledProcessInstanceDTO> callProcessInstances,
                              List<InstanceIncidentDTO> incidents, List<JobDTO> jobs,
                              List<MessageSubscriptionDTO> messageSubscriptions,
                              List<TimerDTO> timers, List<ErrorDTO> errors,
                              List<VariableDTO> variables, List<BpmnElementInfoDTO> bpmnElementInfos,
                              Set<String> completedActivities, List<ElementInstanceDTO> terminateActiveActivities, List<ActivateElementItemDTO> activateActivities,
                              List<ElementInstanceDTO> ancestorActivities) {}
