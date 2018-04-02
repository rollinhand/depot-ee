package org.kivio.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Represents a single instrument (security, bond, equity).
 *
 * @author Björn Berg, rollin.hand@gmx.de
 */
@Getter
@Setter
@Entity
@Table(name = "INSTRUMENT")
@NamedQueries({
        @NamedQuery(name = "Instrument.findAll", query = "SELECT i FROM Instrument i")
})
public class Instrument {
    @Id
    private String isin;
    private String name;

    public Instrument() {

    }

    public Instrument(String isin, String name) {
        this.isin = isin;
        this.name = name;
    }
}
