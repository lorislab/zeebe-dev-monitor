package org.lorislab.zeebe.dev.monitor;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.lorislab.zeebe.dev.monitor.models.Timer;

import java.time.OffsetDateTime;

@RegisterForReflection
public record TimerData(String targetElementId, Long elementInstanceKey, OffsetDateTime dueDate, String repetitions,
                        Timer.State state, OffsetDateTime timestamp) {}
