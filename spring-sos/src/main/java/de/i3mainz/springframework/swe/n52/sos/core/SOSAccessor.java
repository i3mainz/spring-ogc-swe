package de.i3mainz.springframework.swe.n52.sos.core;

import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.i3mainz.springframework.ogc.OWSConnectionParameter;
import de.i3mainz.springframework.ogc.core.OWSAccessor;
import de.i3mainz.springframework.swe.n52.sos.SOSConnectionParameter;
import de.i3mainz.springframework.swe.n52.sos.service.SOSService;
import de.i3mainz.springframework.swe.n52.sos.service.versions.v100.SOSServiceV100Impl;
import de.i3mainz.springframework.swe.n52.sos.service.versions.v200.SOSServiceV200Impl;

public abstract class SOSAccessor extends OWSAccessor {

    private static final Logger LOG = LoggerFactory
            .getLogger(SOSTemplate.class);
    protected static final String VERSION200 = "2.0.0";
    protected static final String VERSION100 = "1.0.0";
    private SOSService service;

    /**
     * @return the log
     */
    protected static final Logger getLog() {
        return LOG;
    }

    @Override
    public void setConnectionParameter(
            OWSConnectionParameter owsConnectionParameter) {
        super.setConnectionParameter(owsConnectionParameter);
        if (VERSION100.equals(getConnectionParameter().getVersion())) {
            this.service = new SOSServiceV100Impl(
                    (SOSConnectionParameter) getConnectionParameter());
        } else if (VERSION200.equals(getConnectionParameter().getVersion())) {
            this.service = new SOSServiceV200Impl(
                    (SOSConnectionParameter) getConnectionParameter());
        } else {
            throw new NotImplementedException("Version "
                    + getConnectionParameter().getVersion()
                    + " not supported by SOS-Services");
        }
    }

    /**
     * @return the service
     */
    protected final SOSService getService() {
        return service;
    }

    public void afterPropertiesSet() throws Exception {
    }

}