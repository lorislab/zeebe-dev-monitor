package org.lorislab.zeebe.dev.monitor.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@Table(name = "PROCESS_INSTANCE")
@NamedQueries({
        @NamedQuery(name = "Instance.countEndedInstanceOfProcessDefinitionKey", query = "SELECT count(*) FROM Instance p WHERE p.processDefinitionKey = :p1 AND p.end IS NULL"),
        @NamedQuery(name = "Instance.countActiveInstanceOfProcessDefinitionKey", query = "SELECT count(*) FROM Instance p WHERE p.processDefinitionKey = :p1 AND p.end IS NOT NULL")
})
public class Instance extends PanacheEntityBase {

    @Id
    @Column(name = "P_KEY")
    public long key;

    @Column(name = "PARTITION_ID")
    public int partitionId;

    @Column(name = "PROCESS_DEFINITION_KEY")
    public long processDefinitionKey;

    @Column(name = "BPMN_PROCESS_ID")
    public String bpmnProcessId;

    @Column(name = "VERSION")
    public int version;

    @Column(name = "STATE")
    @Enumerated(EnumType.STRING)
    public State state;

    @Column(name = "PI_START")
    public LocalDateTime start;

    @Column(name = "PI_END")
    public LocalDateTime end;

    @Column(name = "PARENT_PROCESS_INSTANCE_KEY")
    public Long parentProcessInstanceKey;

    @Column(name = "PARENT_ELEMENT_INSTANCE_KEY")
    public Long parentElementInstanceKey;


    public enum State {
        ACTIVE, COMPLETED, TERMINATED;
    }

    public static Map<Long, Long> countEndedInstances() {
        PanacheQuery<StatusCount> query = find("SELECT p.processDefinitionKey as processDefinitionKey, count(p.processDefinitionKey) as count FROM Instance p WHERE p.end IS NULL GROUP BY p.processDefinitionKey")
                .project(StatusCount.class);
        return query.list().stream().collect(Collectors.toMap(StatusCount::processDefinitionKey, StatusCount::count));
    }

    public static Map<Long, Long> countActiveInstances() {
        PanacheQuery<StatusCount> query = find("SELECT p.processDefinitionKey as processDefinitionKey, count(p.processDefinitionKey) as count FROM Instance p WHERE p.end IS NOT NULL GROUP BY p.processDefinitionKey")
                .project(StatusCount.class);
        return query.list().stream().collect(Collectors.toMap(StatusCount::processDefinitionKey, StatusCount::count));
    }

    public static long countActiveInstanceOfProcessDefinitionKey(long processDefinitionKey) {
        return count("#Instance.countActiveInstanceOfProcessDefinitionKey", Parameters.with("p1", processDefinitionKey).map());
    }

    public static long countEndedInstanceOfProcessDefinitionKey(long processDefinitionKey) {
        return count("#Instance.countEndedInstanceOfProcessDefinitionKey", Parameters.with("p1", processDefinitionKey).map());
    }

    public static long countOfProcessDefinitionKey(long processDefinitionKey) {
        return count("processDefinitionKey",  processDefinitionKey);
    }

    public static List<Instance> findByProcessDefinitionKey(long processDefinitionKey) {
        return find("processDefinitionKey = ?1 ORDER BY start DESC",  processDefinitionKey).list();
    }

    @RegisterForReflection
    public record StatusCount(Long processDefinitionKey, Long count) { }
}
