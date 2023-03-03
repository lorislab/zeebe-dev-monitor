package org.lorislab.zeebe.dev.monitor.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record ElementInstanceStateDTO(String elementId, long activeInstances, long endedInstances) {
}
