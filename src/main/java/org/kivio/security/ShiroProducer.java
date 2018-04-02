package org.kivio.security;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

/**
 * Producer for some of the important security management components
 * of Apache Shiro framework. With this producer Shiro components can
 * be injected by CDI context.
 *
 * @author Björn Berg, rollin.hand@gmx.de
 * @version 1.0
 * @since 2018-03-18
 */
@Singleton
public class ShiroProducer {
    @Produces
    public SecurityManager createSecurityManager() {
        // by default this uses an INI filter configuration
        return SecurityUtils.getSecurityManager();
    }

    @Produces
    public Subject createSubject() {
        return SecurityUtils.getSubject();
    }

    @Produces
    public Session createSession() {
        return SecurityUtils.getSubject().getSession();
    }
}
