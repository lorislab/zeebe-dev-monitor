package org.lorislab.zeebe.dev.monitor.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
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

    @Column(name = "PAYLOAD")
    public byte[] payload;

    @Column(name = "STATE")
    @Enumerated(EnumType.STRING)
    public State state;

    @Column(name = "TIMESTAMP")
    public LocalDateTime timestamp;

    public enum State {
        PUBLISH, PUBLISHED, EXPIRE, EXPIRED;
    }
}
