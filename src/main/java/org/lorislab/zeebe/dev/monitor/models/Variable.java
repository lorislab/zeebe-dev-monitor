package org.lorislab.zeebe.dev.monitor.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "VARIABLE", indexes = {
        @Index(name = "VARIABLE_PROCESS_INSTANCE_KEY_INDEX", columnList = "PROCESS_INSTANCE_KEY"),
})
public class Variable extends PanacheEntityBase {
    @Id
    @Column(name = "ID")
    public String id;

    @Column(name = "POSITION")
    public Long position;

    @Column(name = "PARTITION_ID")
    public int partitionId;

    @Column(name = "P_NAME")
    public String name;

    @Column(name = "P_VALUE")
    public String value;

    @Column(name = "PROCESS_INSTANCE_KEY")
    public long processInstanceKey;

    @Column(name = "SCOPE_KEY")
    public long scopeKey;

    @Column(name = "STATE")
    @Enumerated(EnumType.STRING)
    public State state;

    @Column(name = "TIMESTAMP")
    public LocalDateTime timestamp;

    public enum State {
        CREATED, UPDATED;
    }
}
