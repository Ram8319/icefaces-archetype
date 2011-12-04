package com.jacum.icefaces.webapp;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 * @author timur
 */
@ManagedBean(name = "logoutConfirmDialog")
@SessionScoped
public class LogoutConfirmDialog extends ConfirmDialog {

    @ManagedProperty(value = "#{authenticationService}")
    private AuthenticationService authenticationService;

    @Override
    public String confirm() {
        authenticationService.logout();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession httpSession = (HttpSession) facesContext.getExternalContext().getSession(false);
        try {
            httpSession.invalidate();
        } catch (org.icefaces.application.SessionExpiredException e) {
            // we expect SessionExpiredException on invalidate() call here, after all beans were destroyred
        }
        setVisible(false);
        return "logout";
    }

    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
}
