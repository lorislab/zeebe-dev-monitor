package org.lorislab.zeebe.dev.monitor.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.lorislab.zeebe.dev.monitor.models.UserTask;

import java.time.OffsetDateTime;

@RegisterForReflection
public record UserTaskDTO(long key, String jobType, UserTask.Status status, String worker, int retries,
                          String elementId,
                          long elementInstanceKey, long processInstanceKey, OffsetDateTime timestamp,
                          boolean isActivatable,
                          String errorCode, String errorMessage, String groups, String users, String assignee,
                          String variables,
                          OffsetDateTime dueDate, OffsetDateTime followUpDate)  {}

