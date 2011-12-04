package com.jacum.icefaces.webapp;

import javax.faces.event.ActionEvent;
import java.io.Serializable;

/**
 * @author timur
 */
public abstract class ConfirmDialog  implements Serializable {

    private boolean visible;

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public abstract String confirm();

    public void cancel(ActionEvent event) {
        setVisible(false);
    }

    public void show() {
        setVisible(true);
    }

}