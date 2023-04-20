package org.lorislab.zeebe.dev.monitor.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "PROCESS_DEFINITION_RESOURCE")
public class BpmnXmlResource extends PanacheEntityBase {

    @Id
    @Column(name = "P_KEY")
    public long key;

    @Column(name = "NAME")
    public String name;
    @Column(name = "RESOURCE")
    public byte[] resource;
}
