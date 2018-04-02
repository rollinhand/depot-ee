package org.kivio.application;

import org.kivio.entities.Instrument;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.NEVER)
public class InstrumentDao {
    @Inject
    private Logger log;

    @PersistenceContext(unitName = "depot")
    private EntityManager em;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void save (Instrument instrument) {
        log.info("EntityManager: {}", em);
        em.merge(instrument);
    }

    public Instrument find(final String isin) {
        return em.find(Instrument.class, isin);
    }

    public List<Instrument> findAll() {
        return em.createNamedQuery("Instrument.findAll", Instrument.class)
                .getResultList();
    }
}
