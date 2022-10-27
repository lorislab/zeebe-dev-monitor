package org.lorislab.zeebe.dev.monitor.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "PROCESS_DEFINITION")
public class Definition extends PanacheEntityBase {
    @Id
    @Column(name = "P_KEY")
    public long key;

    @Column(name = "BPMN_PROCESS_ID")
    public String bpmnProcessId;

    @Column(name = "VERSION")
    public int version;

    @Column(name = "TIMESTAMP")
    public LocalDateTime timestamp;

}
