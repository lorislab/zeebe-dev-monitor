package org.lorislab.zeebe.dev.monitor.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record ActivateElementItemDTO(String id, String name) {};