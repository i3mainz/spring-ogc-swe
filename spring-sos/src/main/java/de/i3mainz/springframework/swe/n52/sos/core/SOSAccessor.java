package de.i3mainz.springframework.swe.n52.sos.core;

import java.util.Iterator;

import org.apache.commons.lang.NotImplementedException;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.OWSException;
import org.n52.oxf.ows.OwsExceptionCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.i3mainz.springframework.ogc.OWSConnectionParameter;
import de.i3mainz.springframework.ogc.core.OWSAccessor;
import de.i3mainz.springframework.swe.n52.sos.SOSConnectionParameter;
import de.i3mainz.springframework.swe.n52.sos.model.Sensor;
import de.i3mainz.springframework.swe.n52.sos.service.SOSService;
import de.i3mainz.springframework.swe.n52.sos.service.versions.v100.SOSServiceV100Impl;
import de.i3mainz.springframework.swe.n52.sos.service.versions.v200.SOSServiceV200;
import de.i3mainz.springframework.swe.n52.sos.service.versions.v200.SOSServiceV200Impl;
import de.i3mainz.springframework.swe.n52.sos.util.Configuration;

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
                    + "not supported by SOS-Services");
        }
    }

    /**
     * @return the service
     */
    protected final SOSService getService() {
        return service;
    }

    protected String handleExceptionReportException(ExceptionReport e, Sensor rs) {
        // Handle already registered sensor case here (happens when the
        // sensor is registered but not listed in the capabilities):
        final Iterator<OWSException> iter = e.getExceptionsIterator();
        while (iter.hasNext()) {
            final OWSException owsEx = iter.next();
            if (owsEx.getExceptionCode().equals(
                    OwsExceptionCode.NoApplicableCode.name())
                    && owsEx.getExceptionTexts() != null
                    && owsEx.getExceptionTexts().length > 0) {
                return handleNotApplicableCode(owsEx, rs);
            } else if (owsEx.getExceptionCode().equals(
                    OwsExceptionCode.InvalidParameterValue.name())
                    && "offeringIdentifier".equals(owsEx.getLocator())
                    && owsEx.getExceptionTexts() != null
                    && owsEx.getExceptionTexts().length > 0) {
               return handleOfferingExists(owsEx, rs);
            }

        }
        LOG.error(String.format("Exception thrown: %s", e.getMessage()), e);
        return null;
    }

    private String handleNotApplicableCode(OWSException owsEx, Sensor rs) {
        for (final String string : owsEx.getExceptionTexts()) {
            if (string
                    .indexOf(Configuration.SOS_SENSOR_ALREADY_REGISTERED_MESSAGE_START) > -1
                    && string
                            .indexOf(Configuration.SOS_SENSOR_ALREADY_REGISTERED_MESSAGE_END) > -1) {
                return rs.getId();
            }
        }
        return null;
    }

    private String handleOfferingExists(OWSException owsEx, Sensor rs) {
        // handle offering already contained case here
        for (final String string : owsEx.getExceptionTexts()) {
            if (string
                    .indexOf(Configuration.SOS_200_OFFERING_ALREADY_REGISTERED_MESSAGE_START) > -1
                    && string
                            .indexOf(Configuration.SOS_200_OFFERING_ALREADY_REGISTERED_MESSAGE_END) > -1) {
                ((SOSServiceV200) getService()).getOfferings().put(rs.getId(),
                        rs.getOffering().getId());
                return rs.getId();
            }
        }
        return null;
    }

    public void afterPropertiesSet() throws Exception {
    }

}