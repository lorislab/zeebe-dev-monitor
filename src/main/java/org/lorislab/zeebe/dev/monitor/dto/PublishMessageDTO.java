package org.lorislab.zeebe.dev.monitor.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.Map;

@RegisterForReflection
public record PublishMessageDTO(String name, String correlationKey, Map<String, Object> payload, String timeToLive) {};