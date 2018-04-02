package org.kivio.security;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.omnifaces.util.Faces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;

@Named
@RequestScoped
public class SecurityBean {
    private final Logger log = LoggerFactory.getLogger(SecurityBean.class);
    private static final String LOGIN_URL = "login.xhtml";
    private static final String DASHBOARD = "dashboard.xhtml";

    private String username;
    private String password;

    @Inject
    private Subject subject;

    public void doLogin() {
        UsernamePasswordToken token = new UsernamePasswordToken(getUsername(), getPassword());

        try {
            log.info("Logge Benutzer ein");
            subject.login(token);
            SavedRequest savedRequest = WebUtils.getAndClearSavedRequest(Faces.getRequest());
            Faces.redirect(savedRequest != null ? savedRequest.getRequestUrl() : DASHBOARD);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void doLogout() throws IOException {
        log.info("Logge aktuellen Benutzer '{}' aus", subject.getPrincipal());
        subject.logout();
        Faces.invalidateSession();  // cleanup user related session state
        Faces.redirect(LOGIN_URL);
    }

    public boolean getAuthenticated() {
        return subject.isAuthenticated();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
