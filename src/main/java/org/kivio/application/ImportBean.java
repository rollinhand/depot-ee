package org.kivio.application;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.kivio.entities.Portfolio;
import org.kivio.event.Import;
import org.kivio.event.UpdateEvent;
import org.kivio.helper.PortfolioBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
public class ImportBean {
    private static Logger log = LoggerFactory.getLogger(ImportBean.class);

    @Inject
    @Import
    private Event<UpdateEvent> updateEvents;

    @Inject
    private PortfolioBuilder portfolioBuilder;

    @Inject
    private PortfolioDao portfolioDao;

    public List<Portfolio> createPortfolio(InputStream is) throws IOException {
        return createPortfolio(new InputStreamReader(is));
    }

    public List<Portfolio> createPortfolio(Reader reader) throws IOException {
        List<Portfolio> portfolioList = new ArrayList<>();

        try {
            Iterable<CSVRecord> records = CSVFormat.newFormat(';').withQuote('"').parse(reader);
            Date navDate = null;
            int recordCount = 0;

            for (CSVRecord record : records) {
                if (recordCount == 0) {
                    log.debug("extracting nav date");
                    navDate = portfolioBuilder.getNavDate(record.get(0));
                } else if (recordCount >= 6) {
                    log.debug("extracting portfolio");
                    Portfolio p = portfolioBuilder.build(record, navDate);
                    if (p != null) {
                        portfolioList.add(p);
                        //instrumentDao.save(p.getInstrument());
                        portfolioDao.save(p);
                    }
                }
                recordCount++;
            }
            log.info("{} records extracted", recordCount);
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            reader.close();
            int size = portfolioList.size();
            UpdateEvent updateEvent = new UpdateEvent(size + " datasets imported");
            updateEvents.fire(updateEvent);
        }

        return portfolioList;
    }
}
