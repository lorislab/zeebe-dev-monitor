package org.lorislab.zeebe.dev.monitor.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.lorislab.zeebe.dev.monitor.models.Job;

import java.time.OffsetDateTime;

@RegisterForReflection
public record JobDTO(long key, String jobType, Job.State state, String worker, int retries, String elementId,
                     long elementInstanceKey, long processInstanceKey, OffsetDateTime timestamp,
                     String errorCode, String errorMessage, String variables,
                     boolean isActivatable) {}

