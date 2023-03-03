package org.lorislab.zeebe.dev.monitor.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.lorislab.zeebe.dev.monitor.models.Instance;

@RegisterForReflection
public record CalledProcessInstanceDTO(long key, String elementId, long elementInstanceKey, String bpmnProcessId, long processDefinitionKey,
                                        Instance.State state) {}