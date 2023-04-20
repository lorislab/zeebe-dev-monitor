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
@Table(name = "JOB", indexes = {
        @Index(name = "JOB_PROCESS_INSTANCE_KEY_INDEX", columnList = "PROCESS_INSTANCE_KEY"),
})
public class Job extends PanacheEntityBase {
    @Id
    @Column(name = "P_KEY")
    public long key;

    @Column(name = "PROCESS_INSTANCE_KEY")
    public long processInstanceKey;

    @Column(name = "ELEMENT_INSTANCE_KEY")
    public long elementInstanceKey;

    @Column(name = "JOB_TYPE")
    public String jobType;

    @Column(name = "WORKER")
    public String worker;

    @Column(name = "STATE")
    @Enumerated(EnumType.STRING)
    public State state;

    @Column(name = "RETRIES")
    public int retries;

    @Column(name = "TIMESTAMP")
    public LocalDateTime timestamp;

    public enum State {
        CREATED, COMPLETE, COMPLETED, TIME_OUT, TIMED_OUT, FAIL, FAILED, UPDATE_RETRIES,
        RETRIES_UPDATED, CANCEL, CANCELED, THROW_ERROR, ERROR_THROWN, RECUR_AFTER_BACKOFF, RECURRED_AFTER_BACKOFF;
    }
}
