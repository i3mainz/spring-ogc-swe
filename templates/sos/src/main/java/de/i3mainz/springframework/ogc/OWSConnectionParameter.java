package de.i3mainz.springframework.ogc;

import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public abstract class OWSConnectionParameter {
    
    
    private static final Logger LOG = LoggerFactory
            .getLogger(OWSConnectionParameter.class);

    private String url;
    private String service;
    private String version;

    public final String getUrl() {
        return url;
    }

    @Required
    public final void setUrl(String url) {
        this.url = url;
    }

    public final String getService() {
        return service;
    }

    public final void setService(String service) {
        this.service = service;
    }

    public final String getVersion() {
        return version;
    }

    @Required
    public final void setVersion(String version) {
        this.version = version;
    }

    public final URL getCapabilitiesURL() {
        try {
            return new URL(getUrl() + "?" + "service=" + getService() + "&"
                    + "version=" + getVersion() + "&" + "request="
                    + "GetCapabilities");
        } catch (MalformedURLException e) {
            LOG.error("GetCapabilities-URL not well formed", e);
        }
        return null;
    }

}