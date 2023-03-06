package org.lorislab.zeebe.dev.monitor.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.time.OffsetDateTime;

@RegisterForReflection
public record ErrorDTO(long position, long errorEventPosition, Long processInstanceKey, String exceptionMessage,
                       String stacktrace, OffsetDateTime timestamp) {
}
