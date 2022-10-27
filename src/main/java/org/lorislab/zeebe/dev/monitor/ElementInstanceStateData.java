package org.lorislab.zeebe.dev.monitor;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record ElementInstanceStateData(String elementId, long activeInstances, long endedInstances) {
}
