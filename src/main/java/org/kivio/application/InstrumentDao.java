package org.kivio.application;

import org.kivio.entities.Instrument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.NEVER)
public class InstrumentDao {
    static final Logger log = LoggerFactory.getLogger(InstrumentDao.class);

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
