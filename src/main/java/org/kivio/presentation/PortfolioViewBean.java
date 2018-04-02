package org.kivio.presentation;

import org.kivio.application.PortfolioDao;
import org.kivio.entities.Portfolio;
import org.kivio.entities.PortfolioTotal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class PortfolioViewBean implements Serializable {
    private static Logger log = LoggerFactory.getLogger(PortfolioViewBean.class);
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private Date selectedNavDate;

    private Portfolio selectedRow;

    @Inject
    private PortfolioDao portfolioDao;

    @PostConstruct
    private void init () {
        selectedNavDate = getLatestNavDate();
    }

    public synchronized List<String> getAllNavDates() {
        return portfolioDao.findNavDates().stream()
                .map(d -> sdf.format(d))
                .collect(Collectors.toList());
    }

    private synchronized Date getLatestNavDate() {
        return portfolioDao.findLatestNavDate();
    }

    public List<Portfolio> getValues() {
        if (selectedNavDate == null) {
            selectedNavDate = getLatestNavDate();
        }

        return portfolioDao.findByNavDate(selectedNavDate);
    }

    public PortfolioTotal getTotal() {
        return portfolioDao.findTotal(selectedNavDate);
    }

    public Date getSelectedNavDate() {
        return selectedNavDate;
    }

    public String getSelectedNavDateAsString() {
        Date selection = getSelectedNavDate();

        if (selection != null) {
            return sdf.format(selection);
        }

        return null;
    }

    public void setSelectedNavDate(String selectedNavDate) {
        try {
            this.selectedNavDate = sdf.parse(selectedNavDate);
        } catch (ParseException e) {
            log.warn("parsing date failed: {}", e.getMessage());
        }
    }

    public Portfolio getSelectedRow() {
        return selectedRow;
    }

}
