package org.lorislab.zeebe.dev.monitor.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.lorislab.zeebe.dev.monitor.models.Signal;

import java.time.OffsetDateTime;

@RegisterForReflection
public record SignalDTO(long key, String name, String variables, OffsetDateTime timestamp, Signal.Status status) {}
