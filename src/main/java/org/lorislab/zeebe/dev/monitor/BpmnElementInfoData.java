package org.lorislab.zeebe.dev.monitor;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record BpmnElementInfoData(String elementId, String info) {
}
