package org.lorislab.zeebe.dev.monitor.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.time.OffsetDateTime;

@RegisterForReflection
public record VariableValueDTO(String value, OffsetDateTime timestamp) {}