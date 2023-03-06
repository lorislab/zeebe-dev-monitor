package org.lorislab.zeebe.dev.monitor.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.lorislab.zeebe.dev.monitor.models.Message;

import java.time.OffsetDateTime;

@RegisterForReflection
public record MessageDTO(long key, String name, String correlationKey, String messageId, Message.State state,
                          OffsetDateTime timestamp) {}