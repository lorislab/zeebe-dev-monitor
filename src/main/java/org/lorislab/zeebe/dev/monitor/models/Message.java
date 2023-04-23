package org.lorislab.zeebe.dev.monitor.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "MESSAGE")
public class Message extends PanacheEntityBase {
    @Id
    @Column(name = "P_KEY")
    public long key;

    @Column(name = "NAME")
    public String name;

    @Column(name = "CORRELATION_KEY")
    public String correlationKey;

    @Column(name = "MESSAGE_ID")
    public String messageId;

    @Column(name = "VARIABLES")
    public String variables;

    @Column(name = "STATE")
    @Enumerated(EnumType.STRING)
    public State state;

    @Column(name = "TIMESTAMP")
    public LocalDateTime timestamp;

    public enum State {
        PUBLISH, PUBLISHED, EXPIRE, EXPIRED;
    }
}
