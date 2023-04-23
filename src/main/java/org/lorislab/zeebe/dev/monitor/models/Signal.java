package org.lorislab.zeebe.dev.monitor.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "SIGNAL")
public class Signal extends PanacheEntityBase  {

    @Id
    @Column(name = "P_KEY")
    public long key;

    @Column(name = "NAME")
    public String name;

    @Column(name = "VARIABLES")
    public String variables;

    @Column(name = "POSITION")
    public Long position;

    @Column(name = "PARTITION_ID")
    public int partitionId;

    @Column(name = "TIMESTAMP")
    public LocalDateTime timestamp;

    @Column(name = "STATUS")
    public Status status;

    public enum Status {
        BROADCAST, BROADCASTED;
    }

}
