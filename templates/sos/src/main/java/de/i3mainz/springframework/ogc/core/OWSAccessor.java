package de.i3mainz.springframework.ogc.core;

import org.springframework.beans.factory.InitializingBean;

import de.i3mainz.springframework.ogc.OWSConnectionParameter;

public abstract class OWSAccessor implements InitializingBean {

    private OWSConnectionParameter connectionParameter;

    public OWSConnectionParameter getConnectionParameter() {
        return connectionParameter;
    }

    public void setConnectionParameter(
            OWSConnectionParameter connectionParameter) {
        this.connectionParameter = connectionParameter;
    }
}