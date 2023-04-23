package org.lorislab.zeebe.dev.monitor.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Column;
import org.lorislab.zeebe.dev.monitor.models.Signal;
import org.lorislab.zeebe.dev.monitor.models.SignalSubscription;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@RegisterForReflection
public record SignalSubscriptionDTO(long key, String name,
                                    OffsetDateTime timestamp, SignalSubscription.Status status,
                                    String bpmnProcessId, Long processDefinitionKey, String catchEventId,
                                    Long catchEventInstanceKey) {}
