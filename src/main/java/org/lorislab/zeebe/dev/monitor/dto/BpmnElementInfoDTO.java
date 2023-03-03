package org.lorislab.zeebe.dev.monitor.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record BpmnElementInfoDTO(String elementId, String info) {
}
