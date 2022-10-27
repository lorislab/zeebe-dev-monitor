package org.lorislab.zeebe.dev.monitor;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.time.OffsetDateTime;

@RegisterForReflection
public record ErrorData(long position, long errorEventPosition, Long processInstanceKey, String exceptionMessage,
                        String stacktrace, OffsetDateTime timestamp) {
}
