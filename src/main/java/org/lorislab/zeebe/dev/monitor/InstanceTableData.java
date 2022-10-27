package org.lorislab.zeebe.dev.monitor;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.lorislab.zeebe.dev.monitor.models.Instance;

import java.time.OffsetDateTime;

@RegisterForReflection
public record InstanceTableData(long key, String bpmnProcessId, long processDefinitionKey,
                                Instance.State state, OffsetDateTime start, OffsetDateTime end) {
}
