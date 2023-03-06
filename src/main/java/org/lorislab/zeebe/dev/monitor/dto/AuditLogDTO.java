package org.lorislab.zeebe.dev.monitor.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.lorislab.zeebe.dev.monitor.models.ElementInstance;

import java.time.OffsetDateTime;

@RegisterForReflection
public record AuditLogDTO(long key, long flowScopeKey, String elementId, String elementName, String bpmnElementType,
                           String payload, ElementInstance.Intent state, OffsetDateTime timestamp) {}