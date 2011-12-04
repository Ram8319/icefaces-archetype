package com.jacum.icefaces.webapp;


import org.icefaces.application.PushRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

@ManagedBean(name = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;

    private final static Logger log = LoggerFactory.getLogger(LoginBean.class);

    @ManagedProperty(value = "#{authenticationService}")
    private transient AuthenticationService authenticationService;

    public String login() {

        PushRenderer.addCurrentSession("all");

        boolean success = authenticationService.login(username, password);

        if (success) {
            log.info("User " + this.username + " has logged in");
            return "success";
        } else {
            WebUtils.addFacesMessage(FacesMessage.SEVERITY_ERROR, "authentication.failed");
        }
        return "failure";
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


    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

}
