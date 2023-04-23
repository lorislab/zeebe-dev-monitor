package org.lorislab.zeebe.dev.monitor.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "USER_TASK", indexes = {
        @Index(name = "USER_TASK_PROCESS_INSTANCE_KEY_INDEX", columnList = "PROCESS_INSTANCE_KEY"),
})
public class UserTask extends PanacheEntityBase {

    @Id
    @Column(name = "P_KEY")
    public long key;

    @Column(name = "PROCESS_INSTANCE_KEY")
    public long processInstanceKey;

    @Column(name = "ELEMENT_INSTANCE_KEY")
    public long elementInstanceKey;

    @Column(name = "ELEMENT_ID")
    public String elementId;

    @Column(name = "JOB_TYPE")
    public String jobType;

    @Column(name = "WORKER")
    public String worker;

    @Column(name = "STATE")
    @Enumerated(EnumType.STRING)
    public Status status;

    @Column(name = "RETRIES")
    public int retries;

    @Column(name = "TIMESTAMP")
    public LocalDateTime timestamp;

    @Column(name = "USERS")
    public String users;

    @Column(name = "GROUPS")
    public String groups;

    @Column(name = "ASSIGNEE")
    public String assignee;

    @Column(name = "FOLLOW_UP_DATE")
    public LocalDateTime followUpDate;

    @Column(name = "DUE_DATE")
    public LocalDateTime dueDate;

    @Column(name = "ERROR_CODE")
    public String errorCode;

    @Column(name = "ERROR_MESSAGE")
    public String errorMessage;

    public enum Status {
        CREATED, COMPLETE, COMPLETED, TIME_OUT, TIMED_OUT, FAIL, FAILED, UPDATE_RETRIES,
        RETRIES_UPDATED, CANCEL, CANCELED, THROW_ERROR, ERROR_THROWN, RECUR_AFTER_BACKOFF, RECURRED_AFTER_BACKOFF;
    }
}
