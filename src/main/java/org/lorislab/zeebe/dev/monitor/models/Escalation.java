package org.lorislab.zeebe.dev.monitor.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ESCALATION", indexes = {
        @Index(name = "ESC_PROCESS_INSTANCE_KEY_INDEX", columnList = "PROCESS_INSTANCE_KEY"),
})
public class Escalation extends PanacheEntityBase  {

    @Id
    @Column(name = "P_KEY")
    public long key;

    @Column(name = "POSITION")
    public Long position;

    @Column(name = "PARTITION_ID")
    public int partitionId;

    @Column(name = "TIMESTAMP")
    public LocalDateTime timestamp;

    @Column(name = "PROCESS_INSTANCE_KEY")
    public long processInstanceKey;
    @Column(name = "CATCH_ELEMENT_ID")
    public String catchElementId;
    @Column(name = "ESCALATION_CODE")
    public String escalationCode;
    @Column(name = "THROW_ELEMENT_ID")
    public String throwElementId;
}
