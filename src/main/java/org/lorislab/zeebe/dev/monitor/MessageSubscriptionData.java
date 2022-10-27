package org.lorislab.zeebe.dev.monitor;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.lorislab.zeebe.dev.monitor.models.MessageSubscription;

import java.time.OffsetDateTime;

@RegisterForReflection
public record MessageSubscriptionData(String id, String messageName, String correlationKey,
        String targetFlowNodeId, Long elementInstanceKey, Long processInstanceKey, MessageSubscription.State state,
        OffsetDateTime timestamp, boolean open) {}

