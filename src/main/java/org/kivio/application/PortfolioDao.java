package org.kivio.application;

import org.kivio.entities.Portfolio;
import org.kivio.entities.PortfolioTotal;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Stateless
public class PortfolioDao implements Serializable {
    @Inject
    private Logger log;

    @PersistenceContext(unitName = "depot")
    private EntityManager em;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void save(Portfolio portfolio) {
        em.merge(portfolio);    // sonst exception mit instrument
        em.flush();
    }

    public List<Portfolio> findAll() {
        return em.createNamedQuery("Portfolio.findAll", Portfolio.class)
                .getResultList();
    }

    public List<Portfolio> findByNavDate(final Date navDate) {
        return em.createNamedQuery("Portfolio.byNavDate", Portfolio.class)
                .setParameter("navDate", navDate)
                .getResultList();
    }

    public List<Portfolio> findByIsin(final String isin) {
        return em.createNamedQuery("Portfolio.byIsin", Portfolio.class)
                .setParameter("isin", isin)
                .getResultList();
    }

    public List<Portfolio> findByIsin(final Date navDate, final List<String> isin) {
        return em.createNamedQuery("Portfolio.byIsinAndDate", Portfolio.class)
                .setParameter("isin", isin)
                .setParameter("navDate", navDate)
                .getResultList();
    }

    /**
     * A list of NAV dates in format yyyy-MM-dd.
     * @return list of NAv dates.
     */
    public List<Date> findNavDates() {
        return em.createNamedQuery("Portfolio.navDates").getResultList();
    }

    public Date findLatestNavDate() {
        try {
            return (Date) em.createNamedQuery("Portfolio.latestNavDate")
                    .getSingleResult();
        } catch (EntityExistsException e) {
            log.warn("no entries in portfolio");
        }

        return null;
    }

    public PortfolioTotal findTotal(Date navDate) {
        return em.createNamedQuery("Portfolio.totalByNavDate", PortfolioTotal.class)
                .setParameter(1, navDate)
                .getSingleResult();
    }

    public List<PortfolioTotal> findTotal() {
        return em.createNamedQuery("Portfolio.total", PortfolioTotal.class)
                .getResultList();
    }
}
