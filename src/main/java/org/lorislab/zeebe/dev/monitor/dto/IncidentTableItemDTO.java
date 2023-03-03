package org.lorislab.zeebe.dev.monitor.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.lorislab.zeebe.dev.monitor.models.Incident;

import java.time.OffsetDateTime;

@RegisterForReflection
public record IncidentTableItemDTO(long key, String bpmnProcessId, long processInstanceKey, long processDefinitionKey,
                                Incident.ErrorType errorType, String errorMessage, IncidentState state,
                                OffsetDateTime created, OffsetDateTime resolved) {}