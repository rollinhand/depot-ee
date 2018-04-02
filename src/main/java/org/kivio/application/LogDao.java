package org.kivio.application;

import org.kivio.entities.LogMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.NEVER)
public class LogDao {
    private static Logger log = LoggerFactory.getLogger(LogDao.class);

    @PersistenceContext(unitName = "depot")
    private EntityManager em;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void save(final LogMessage logMessage) {
        try {
            log.debug("writing log '{}' (id: {})", logMessage.getMessage(), logMessage.getId());
            em.persist(logMessage);
        } catch (EntityExistsException e) {
            log.warn("log message with id {} exists. overwrite.", logMessage.getId());
            em.merge(logMessage);
        }
    }

    public List<LogMessage> findAll() {
        return em.createNamedQuery("LogMessage.findAll", LogMessage.class).getResultList();
    }
}
