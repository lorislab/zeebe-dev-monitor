package org.lorislab.zeebe.dev.monitor.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.panache.common.Parameters;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "TIMER", indexes = {
        @Index(name = "TIMER_PROCESS_INSTANCE_KEY_INDEX", columnList = "PROCESS_INSTANCE_KEY"),
})
public class Timer extends PanacheEntityBase {

    @Id
    @Column(name = "P_KEY")
    public long key;

    @Column(name = "PROCESS_DEFINITION_KEY")
    public long processDefinitionKey;

    @Column(name = "PROCESS_INSTANCE_KEY")
    public Long processInstanceKey;

    @Column(name = "ELEMENT_INSTANCE_KEY")
    public Long elementInstanceKey;

    @Column(name = "TARGET_ELEMENT_ID")
    public String targetElementId;

    @Column(name = "DUE_DATE")
    public LocalDateTime dueDate;

    @Column(name = "REPETITIONS")
    public int repetitions;

    @Column(name = "STATE")
    @Enumerated(EnumType.STRING)
    public State state;

    @Column(name = "TIMESTAMP")
    public LocalDateTime timestamp;

    public enum State {
        CREATED, TRIGGER, TRIGGERED, CANCEL,  CANCELED;
    }
    public static List<Timer> findByProcessDefinitionKeyAndProcessInstanceKeyIsNull(long processDefinitionKey) {
        return find("processDefinitionKey = :processDefinitionKey AND processInstanceKey IS NULL",
                Parameters.with("processDefinitionKey", processDefinitionKey).map())
                .list();
    }
}
