package org.lorislab.zeebe.dev.monitor.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.time.OffsetDateTime;


@RegisterForReflection
public record ProcessTableItemDTO(long key, String bpmnProcessId, int version, OffsetDateTime timestamp,
                             long countRunning, long countEnded) {}

