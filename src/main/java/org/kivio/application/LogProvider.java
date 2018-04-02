package org.kivio.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 * Produces a SLF4J logger by injection.
 *
 * @author Björn Berg, rollin.hand@gmx.de
 * @version 1.0
 */
@ApplicationScoped
public class LogProvider {
    @Produces
    public Logger createLogger(InjectionPoint ip) {
        return LoggerFactory.getLogger(ip.getBean().getBeanClass().getName());
    }
}
