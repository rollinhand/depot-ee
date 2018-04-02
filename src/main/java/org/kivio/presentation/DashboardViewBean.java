package org.kivio.presentation;

import org.kivio.application.PortfolioDao;
import org.kivio.c3faces.model.C3Category;
import org.kivio.c3faces.model.C3DataSet;
import org.kivio.c3faces.model.C3ViewDataSet;
import org.kivio.c3faces.script.property.Data;
import org.kivio.c3faces.style.C3Theme;
import org.kivio.entities.PortfolioTotal;
import org.kivio.event.Import;
import org.kivio.event.UpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class DashboardViewBean implements Serializable {
    final static Logger log = LoggerFactory.getLogger(DashboardViewBean.class);

    @Inject
    private PortfolioDao portfolioDao;

    @Inject
    private C3ChartBean c3ChartBean;

    private List<PortfolioTotal> cachedPortfolio;

    @PostConstruct
    protected void initialize() {
        cachedPortfolio = portfolioDao.findTotal();
        c3ChartBean.update(cachedPortfolio);
    }

    public List<PortfolioTotal> getTotalPortfolio() {
        // TODO: hier kann später gefiltert werden
        return cachedPortfolio;
    }

    public Data getData() {
        return c3ChartBean.getData();
    }

    public C3Category getMonths() {
        return c3ChartBean.getMonths();
    }

    /**
     * This method listens on updates on database.
     * @param event
     */
    public void listenOnImport(@Observes(during = TransactionPhase.BEFORE_COMPLETION) @Import UpdateEvent event) {
        log.info("New update received: {}", event.getMessage());
        initialize();
    }
}
