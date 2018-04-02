package org.kivio.presentation;

import org.kivio.application.PortfolioDao;
import org.kivio.c3faces.model.C3Category;
import org.kivio.c3faces.model.C3DataSet;
import org.kivio.c3faces.model.C3ViewDataSet;
import org.kivio.c3faces.script.property.Data;
import org.kivio.c3faces.style.C3Color;
import org.kivio.c3faces.style.C3Theme;
import org.kivio.entities.PortfolioTotal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
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
public class C3ChartBean implements Serializable {
    private final Logger log = LoggerFactory.getLogger(C3ChartBean.class);

    private final Data data = new Data();

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

    private List<PortfolioTotal> cachedPortfolio;

    private void init() {
        // initialize data for graphics
        data.getDataSets().add(
                new C3ViewDataSet("AuM",
                        new C3DataSet(getCurrentMarketValue()), C3Color.MIDNIGHT));

        data.getDataSets().add(
                new C3ViewDataSet("Return",
                        new C3DataSet(getReturnNominal()), C3Color.ORANGE));
    }

    public void update(List<PortfolioTotal> portfolioTotalList) {
        this.cachedPortfolio = portfolioTotalList;
        data.getDataSets().clear(); // alle zurücksetzen
        init();
    }

    private List<BigDecimal> getCurrentMarketValue() {
        return cachedPortfolio.stream()
                .sorted()
                .map(PortfolioTotal::getMarketValueAvg)
                .collect(Collectors.toList());
    }

    public C3Category getMonths() {
        List<String> months = cachedPortfolio.stream()
                .sorted()
                .map(p -> p.getYearMonth().format(formatter))
                .collect(Collectors.toList());

        return new C3Category(months);
    }

    private List<BigDecimal> getReturnNominal() {
        return cachedPortfolio.stream()
                .sorted()
                .map(PortfolioTotal::getReturnNominalAvg)
                .collect(Collectors.toList());
    }

    public Data getData() {
        if (data == null) {
            init();
        }
        return data;
    }
}
