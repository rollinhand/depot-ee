package org.kivio.helper;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.kivio.entities.Instrument;
import org.kivio.entities.Portfolio;
import org.kivio.entities.PortfolioId;
import org.slf4j.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Dependent
public class PortfolioBuilder {
    @Inject
    private Logger log;

    public Portfolio build(CSVRecord record, Date navDate) {
        Portfolio p = null;

        if (record != null && StringUtils.isNotEmpty(record.get(0))) {
            p = new Portfolio();
            p.setPortfolioId(buildPortfolioId(record, navDate));
            p.setInstrument(buildInstrument(record));
            p.setNominal(toBigDecimal(record.get(2)));
            p.setCurrency(record.get(5));
            p.setBuyingRate(toBigDecimal(record.get(4)));           // Einstandskurs
            p.setBuyingMarketValue(toBigDecimal(record.get(6)));    // Einstandswert
            p.setCurrentRate(toBigDecimal(record.get(8)));          // Bewertungskurs
            p.setCurrentMarketValue(toBigDecimal(record.get(12)));  // Kurswert
        }

        return p;
    }

    private PortfolioId buildPortfolioId(CSVRecord record, Date navDate) {
        return new PortfolioId(navDate, record.get(0)); // 0 = ISIN
    }

    private Instrument buildInstrument(CSVRecord record) {
        return new Instrument(record.get(0), record.get(1));
    }

    protected BigDecimal toBigDecimal(String numeric) {
        try {
            numeric = numeric.replace(".", "");
            return new BigDecimal(numeric.replace(",", "."));
        } catch (NumberFormatException | NullPointerException e) {
            log.warn("Value '{}' cannot be transformed", numeric);
        }
        return null;
    }

    public Date getNavDate(String line) {
        LocalDate localDate = DateHelper.fromValuation(line);
        return Date.from(
                localDate.atStartOfDay(ZoneId.systemDefault())
                        .toInstant());
    }
}
