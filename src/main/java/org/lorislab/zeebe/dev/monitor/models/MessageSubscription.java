package org.lorislab.zeebe.dev.monitor.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.panache.common.Parameters;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "MESSAGE_SUBSCRIPTION", indexes = {
        @Index(name = "MESSAGE_SUBSCRIPTION_PROCESS_INSTANCE_KEY_INDEX", columnList = "PROCESS_INSTANCE_KEY"),
})
public class MessageSubscription extends PanacheEntityBase  {

    @Id
    @Column(name = "ID")
    public String id;

    @Column(name = "MESSAGE_NAME")
    public String messageName;

    @Column(name = "CORRELATION_KEY")
    public String correlationKey;

    @Column(name = "PROCESS_INSTANCE_KEY")
    public Long processInstanceKey;

    @Column(name = "ELEMENT_INSTANCE_KEY")
    public Long elementInstanceKey;

    @Column(name = "PROCESS_DEFINITION_KEY")
    public Long processDefinitionKey;

    @Column(name = "STATE")
    @Enumerated(EnumType.STRING)
    public State state;

    @Column(name = "TIMESTAMP")
    public LocalDateTime timestamp;

    @Column(name = "VARIABLES")
    public String variables;

    @Column(name = "ELEMENT_ID")
    public String elementId;

    public enum State {
        CREATING, CREATE, CREATED, CORRELATING, CORRELATE, CORRELATED, REJECT,
        REJECTED, DELETE, DELETED, DELETING;
    }

    public static List<MessageSubscription> findByProcessDefinitionKeyAndProcessInstanceKeyIsNull(long processDefinitionKey) {
        return find("processDefinitionKey = :processDefinitionKey AND processInstanceKey IS NULL ORDER BY timestamp DESC",
                Parameters.with("processDefinitionKey", processDefinitionKey).map())
                .list();
    }

    public static Optional<MessageSubscription> findByElementInstanceKeyAndMessageName(long elementInstanceKey, String messageName) {
        return find("elementInstanceKey = :elementInstanceKey AND messageName = :messageName",
                Parameters.with("elementInstanceKey", elementInstanceKey).and( "messageName", messageName).map())
                .firstResultOptional();
    }

    public static Optional<MessageSubscription> findByProcessDefinitionKeyAndMessageName(long processDefinitionKey, String messageName) {
        return find("processDefinitionKey = :processDefinitionKey AND messageName = :messageName",
                Parameters.with("processDefinitionKey", processDefinitionKey).and( "messageName", messageName).map())
                .firstResultOptional();
    }


}
