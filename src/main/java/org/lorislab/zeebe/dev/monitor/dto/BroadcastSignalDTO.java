package org.lorislab.zeebe.dev.monitor.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.Map;

@RegisterForReflection
public record BroadcastSignalDTO(String name, Map<String, Object> variables) {};