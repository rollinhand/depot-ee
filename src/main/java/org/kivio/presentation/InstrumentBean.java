package org.kivio.presentation;

import org.kivio.application.InstrumentDao;
import org.kivio.entities.Instrument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ManagedBean;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
@Named
public class InstrumentBean {
    @Inject
    private Logger log;

    @Inject
    private InstrumentDao instrumentDao;

    public List<String> getAllIsins() {
        return instrumentDao.findAll()
                .stream().map(Instrument::getIsin)
                .collect(Collectors.toList());
    }

    public List<Instrument> getAllInstruments() {
        log.info("loading instruments...");
        List<Instrument> list = instrumentDao.findAll();
        log.info("has {} instruments", list.size());
        return list;
    }
}
