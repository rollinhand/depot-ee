package org.kivio.application;

import org.kivio.entities.LogMessage;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class LogBean {
    @Inject
    private LogDao logDao;

    public void warn(final String message) {
        buildMessage(LogMessage.Level.WARN, message);
    }

    public void error(final String message) {
        buildMessage(LogMessage.Level.ERROR, message);
    }

    public void info(final String message) {
        buildMessage(LogMessage.Level.INFO, message);
    }

    private void buildMessage(final LogMessage.Level lvl, final String message) {
        logDao.save(new LogMessage(lvl, message));
    }
}
