package com.jacum.icefaces.webapp.jsfutil;

import org.icefaces.application.SessionExpiredException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewExpiredException;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.Iterator;


/**
 * @author timur
 */
public class LoggingExceptionHandler extends ExceptionHandlerWrapper {

    private ExceptionHandler wrapped;

    public LoggingExceptionHandler(ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return this.wrapped;
    }

    private final static Logger log = LoggerFactory.getLogger(LoggingExceptionHandler.class);

    @Override
    public void handle() throws FacesException {

        final Iterable<ExceptionQueuedEvent> unhandledExceptionQueuedEvents = getUnhandledExceptionQueuedEvents();
        try {
            for (Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents().iterator(); i.hasNext();) {

                ExceptionQueuedEvent event = i.next();
                ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
                Throwable t = context.getException();

                if (t instanceof ViewExpiredException) {
                    ViewExpiredException vee = (ViewExpiredException) t;
                    FacesContext fc = FacesContext.getCurrentInstance();
//                    Map<String, Object> requestMap = fc.getExternalContext().getRequestMap();
                    NavigationHandler nav =
                            fc.getApplication().getNavigationHandler();
                    try {
                        // Push some useful stuff to the request scope for
                        // use in the page
//                    requestMap.put("currentViewId", vee.getViewId());
                        if (fc.getViewRoot() == null) {
                            UIViewRoot view = fc.getApplication().getViewHandler().createView(fc, vee.getViewId());
                            fc.setViewRoot(view);
                        }
                        log.info("View expired, redirecting to home page transparently");
                        FacesContext.getCurrentInstance().getExternalContext().redirect("/faces/secure/home.xhtml");
                        fc.renderResponse();
                    } catch (IOException e) {
                        log.error("Error while redirecting", e);

                    } finally {
                        i.remove();
                    }
                } else if (t instanceof SessionExpiredException) {
                    log.info("Session has expired");
                    i.remove();

                } else {

                    log.error("JSF exception", t);

                    getWrapped().handle();
                }
            }

        } catch (ConcurrentModificationException e) {
            // stupid ICEfaces throws it somehow
        }

    }

}
