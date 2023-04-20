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
@Table(name = "INCIDENT", indexes = {
        @Index(name = "INCIDENT_PROCESS_INSTANCE_KEY_INDEX", columnList = "PROCESS_INSTANCE_KEY"),
})
public class Incident extends PanacheEntityBase {
    @Id
    @Column(name = "P_KEY")
    public long key;

    @Column(name = "BPMN_PROCESS_ID")
    public String bpmnProcessId;

    @Column(name = "PROCESS_DEFINITION_KEY")
    public long processDefinitionKey;

    @Column(name = "PROCESS_INSTANCE_KEY")
    public long processInstanceKey;

    @Column(name = "ELEMENT_INSTANCE_KEY")
    public long elementInstanceKey;

    @Column(name = "JOB_KEY")
    public long jobKey;

    @Column(name = "ERROR_TYPE")
    @Enumerated(EnumType.STRING)
    public ErrorType errorType;

    @Column(name = "ERROR_MSG")
    public String errorMessage;

    @Column(name = "CREATED")
    public LocalDateTime created;

    @Column(name = "RESOLVED")
    public LocalDateTime resolved;


    public enum ErrorType {
        UNKNOWN, IO_MAPPING_ERROR, JOB_NO_RETRIES, CONDITION_ERROR, EXTRACT_VALUE_ERROR, CALLED_ELEMENT_ERROR,
        UNHANDLED_ERROR_EVENT, MESSAGE_SIZE_EXCEEDED, CALLED_DECISION_ERROR, DECISION_EVALUATION_ERROR,
    }

}
