package org.kivio.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "LOG")
@NamedQueries({
        @NamedQuery(name = "LogMessage.findAll", query = "SELECT l from LogMessage l")
})
public class LogMessage {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date timestamp;

    @Enumerated(value = EnumType.STRING)
    private Level level;

    @Basic
    private String message;

    public enum Level {
        ERROR, WARN, INFO;
    }

    public LogMessage() {

    }

    public LogMessage(Level lvl, String message) {
        this.timestamp = new Date();
        this.level = lvl;
        this.message = message;
    }
}
