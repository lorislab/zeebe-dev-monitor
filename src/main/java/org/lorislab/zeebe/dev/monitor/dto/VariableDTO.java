package org.lorislab.zeebe.dev.monitor.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.time.OffsetDateTime;
import java.util.List;

@RegisterForReflection
public record VariableDTO(String name, String value, long scopeKey, String elementId, OffsetDateTime timestamp,
                           List<VariableValueDTO> values) {}