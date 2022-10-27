package org.lorislab.zeebe.dev.monitor.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.Table;
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
