package org.lorislab.zeebe.dev.monitor.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "ERROR", indexes = {
        @Index(name = "ERROR_PROCESS_INSTANCE_KEY_INDEX", columnList = "PROCESS_INSTANCE_KEY"),
})
public class Error extends PanacheEntityBase {

    @Id
    @Column(name = "POSITION")
    public long position;

    @Column(name = "ERROR_EVENT_POSITION")
    public long errorEventPosition;

    @Column(name = "PROCESS_INSTANCE_KEY")
    public long processInstanceKey;

    @Column(name = "EXCEPTION_MESSAGE")
    public String exceptionMessage;

    @Column(name = "STACKTRACE")
    public String stacktrace;

    @Column(name = "TIMESTAMP")
    public LocalDateTime timestamp;

}
