package org.lorislab.zeebe.dev.monitor.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.panache.common.Parameters;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "SIGNAL_SUBSCRIPTION", indexes = {
        @Index(name = "SIGNAL_PROCESS_DEF_KEY_INDEX", columnList = "PROCESS_DEFINITION_KEY"),
})
public class SignalSubscription extends PanacheEntityBase  {

    @Id
    @Column(name = "P_KEY")
    public long key;

    @Column(name = "NAME")
    public String name;

    @Column(name = "POSITION")
    public Long position;

    @Column(name = "PARTITION_ID")
    public int partitionId;

    @Column(name = "TIMESTAMP")
    public LocalDateTime timestamp;

    @Column(name = "BPMN_PROCESS_ID")
    public String bpmnProcessId;

    @Column(name = "PROCESS_DEFINITION_KEY")
    public Long processDefinitionKey;
    @Column(name = "CATCH_EVENT_ID")
    public String catchEventId;
    @Column(name = "CATCH_EVENT_INSTANCE_KEY")
    public Long catchEventInstanceKey;

    @Column(name = "STATUS")
    public Status status;

    public enum Status {
        CREATED, DELETED;
    }

    public static List<SignalSubscription> findByProcessDefinitionKeyAndProcessInstanceKeyIsNull(long processDefinitionKey) {
        return find("processDefinitionKey = :processDefinitionKey ORDER BY timestamp DESC",
                Parameters.with("processDefinitionKey", processDefinitionKey).map())
                .list();
    }
}
