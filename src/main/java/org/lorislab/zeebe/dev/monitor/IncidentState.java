package org.lorislab.zeebe.dev.monitor;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public enum IncidentState {
    CREATED, RESOLVED;
}
