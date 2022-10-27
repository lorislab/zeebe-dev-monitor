package org.lorislab.zeebe.dev.monitor;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.lorislab.zeebe.dev.monitor.models.Job;

import java.time.OffsetDateTime;

@RegisterForReflection
public record JobData(long key, String jobType, Job.State state, String worker, int retries, String elementId,
                      long elementInstanceKey, long processInstanceKey, OffsetDateTime timestamp, boolean isActivatable) {}

