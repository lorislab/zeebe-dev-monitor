package org.lorislab.zeebe.dev.monitor.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.panache.common.Parameters;
import io.quarkus.runtime.annotations.RegisterForReflection;

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
@Table(name = "ELEMENT_INSTANCE", indexes = {
        @Index(name = "ELEMENT_INSTANCE_PROCESS_INSTANCE_KEY", columnList = "PROCESS_INSTANCE_KEY"),
})
public class ElementInstance extends PanacheEntityBase  {

    @Id
    @Column(name = "ID")
    public String id;

    @Column(name = "POSITION")
    public Long position;

    @Column(name = "PARTITION_ID")
    public int partitionId;

    @Column(name = "ELEMENT_KEY")
    public long key;

    @Column(name = "INTENT")
    @Enumerated(EnumType.STRING)
    public Intent intent;

    @Column(name = "PROCESS_INSTANCE_KEY")
    public long processInstanceKey;

    @Column(name = "ELEMENT_ID")
    public String elementId;

    @Column(name = "BPMN_ELEMENT_TYPE")
    public String bpmnElementType;

    @Column(name = "FLOW_SCOPE_KEY")
    public long flowScopeKey;

    @Column(name = "PROCESS_DEFINITION_KEY")
    public long processDefinitionKey;

    @Column(name = "TIMESTAMP")
    public LocalDateTime timestamp;


    @Override
    public String toString() {
        return "ElementInstance{" +
                "id='" + id + '\'' +
                ", position=" + position +
                ", partitionId=" + partitionId +
                ", key=" + key +
                ", intent=" + intent +
                ", processInstanceKey=" + processInstanceKey +
                ", elementId='" + elementId + '\'' +
                ", bpmnElementType='" + bpmnElementType + '\'' +
                ", flowScopeKey=" + flowScopeKey +
                ", processDefinitionKey=" + processDefinitionKey +
                ", timestamp=" + timestamp +
                '}';
    }

    public static List<ElementInstanceStatistics> findElementInstanceByKeyAndIntentIn(long processDefinitionKey, List<Intent> intents, List<String> excludeElementTypes) {
        return find("""
            SELECT e.elementId AS elementId, COUNT(*) AS count FROM ElementInstance e
            WHERE e.processDefinitionKey = :processDefinitionKey and e.intent in (:intents) and e.bpmnElementType not in (:excludeElementTypes)
            GROUP BY e.elementId
        """, Parameters.with("processDefinitionKey", processDefinitionKey)
                        .and("intents", intents)
                        .and("excludeElementTypes", excludeElementTypes)
                        .map()
        ).project(ElementInstanceStatistics.class).list();
    }

    @RegisterForReflection
    public record ElementInstanceStatistics(String elementId, long count) {}

    public enum Intent {
        CANCEL, SEQUENCE_FLOW_TAKEN, ELEMENT_ACTIVATING, ELEMENT_ACTIVATED, ELEMENT_COMPLETING, ELEMENT_COMPLETED,
        ELEMENT_TERMINATING, ELEMENT_TERMINATED, ACTIVATE_ELEMENT, COMPLETE_ELEMENT, TERMINATE_ELEMENT;
    }

}
