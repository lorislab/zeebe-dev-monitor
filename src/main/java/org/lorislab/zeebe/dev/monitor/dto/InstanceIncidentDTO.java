package org.lorislab.zeebe.dev.monitor.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.lorislab.zeebe.dev.monitor.models.Incident;

import java.time.OffsetDateTime;


@RegisterForReflection
public record InstanceIncidentDTO(long key, String bpmnProcessId, String elementId, long elementInstanceKey, long jobKey,
                           String payload,
                           Incident.ErrorType errorType, String errorMessage, IncidentState state,
                           OffsetDateTime created, OffsetDateTime resolved, boolean isResolved) {}