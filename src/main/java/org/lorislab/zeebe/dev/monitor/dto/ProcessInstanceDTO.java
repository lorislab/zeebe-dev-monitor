package org.lorislab.zeebe.dev.monitor.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;

@RegisterForReflection
public record ProcessInstanceDTO(List<BpmnElementInfoDTO> bpmnElementInfos, List<ElementInstanceStateDTO> elementInstances) {}