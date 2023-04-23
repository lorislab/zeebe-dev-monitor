package org.lorislab.zeebe.dev.monitor.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import java.time.OffsetDateTime;

@RegisterForReflection
public record EscalationDTO(long key, long processInstanceKey, String catchElementId,
                            OffsetDateTime timestamp, String escalationCode, String throwElementId) {}
