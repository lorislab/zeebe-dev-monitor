package org.lorislab.zeebe.dev.monitor.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.lorislab.zeebe.dev.monitor.models.ElementInstance;

import java.time.OffsetDateTime;

@RegisterForReflection
public record ElementInstanceDTO (
        String id,
        Long position,
        int partitionId,

        long key,

        ElementInstance.Intent intent,

        long processInstanceKey,

        String elementId,

        String bpmnElementType,

        long flowScopeKey,

         long processDefinitionKey,

        OffsetDateTime timestamp) { }
