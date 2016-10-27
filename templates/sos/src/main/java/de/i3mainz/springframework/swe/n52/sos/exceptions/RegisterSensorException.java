/**
 * 
 */
package de.i3mainz.springframework.swe.n52.sos.exceptions;

import java.util.Iterator;

import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.OWSException;
import org.n52.oxf.ows.OwsExceptionCode;

import de.i3mainz.springframework.swe.n52.sos.model.requests.RegisterSensor;
import de.i3mainz.springframework.swe.n52.sos.service.SOSService;
import de.i3mainz.springframework.swe.n52.sos.service.versions.v200.SOSServiceV200;
import de.i3mainz.springframework.swe.n52.sos.util.Configuration;

/**
 * @author Nikolai Bock
 *
 */
public class RegisterSensorException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param cause
     *            why the exception will be thrown
     */
    public RegisterSensorException(Throwable cause) {
        super.initCause(cause);
    }

    public String checkReport(RegisterSensor rs, SOSService service) throws RegisterSensorException {
        if (getCause() instanceof ExceptionReport) {
            String sID = handleExceptionReportException((ExceptionReport) getCause(), rs, service);
            if (sID != null) {
                return sID;
            }
        }
        throw this;
    }

    private String handleExceptionReportException(ExceptionReport e, RegisterSensor rs, SOSService service) {
        // Handle already registered sensor case here (happens when the
        // sensor is registered but not listed in the capabilities):
        String sID = null;
        final Iterator<OWSException> iter = e.getExceptionsIterator();
        while (iter.hasNext()) {
            final OWSException owsEx = iter.next();
            if (owsEx.getExceptionCode().equals(OwsExceptionCode.NoApplicableCode.name()) && hasTextElements(owsEx)) {
                sID = handleNotApplicableCode(owsEx, rs);
            } else if (owsEx.getExceptionCode().equals(OwsExceptionCode.InvalidParameterValue.name())
                    && "offeringIdentifier".equals(owsEx.getLocator()) && hasTextElements(owsEx)) {
                sID = handleOfferingExists(owsEx, rs, service);
            }
            if (sID != null) {
                return sID;
            }
        }
        return sID;
    }

    private static boolean hasTextElements(OWSException owsEx) {
        return owsEx.getExceptionTexts() != null && owsEx.getExceptionTexts().length > 0;
    }

    private static String handleNotApplicableCode(OWSException owsEx, RegisterSensor rs) {
        for (final String string : owsEx.getExceptionTexts()) {
            if (string.indexOf(Configuration.SOS_SENSOR_ALREADY_REGISTERED_MESSAGE_START) > -1
                    && string.indexOf(Configuration.SOS_SENSOR_ALREADY_REGISTERED_MESSAGE_END) > -1) {
                return rs.getSensorURI();
            }
        }
        return null;
    }

    private static String handleOfferingExists(OWSException owsEx, RegisterSensor rs, SOSService service) {
        // handle offering already contained case here
        for (final String string : owsEx.getExceptionTexts()) {
            if (string.indexOf(Configuration.SOS_200_OFFERING_ALREADY_REGISTERED_MESSAGE_START) > -1
                    && string.indexOf(Configuration.SOS_200_OFFERING_ALREADY_REGISTERED_MESSAGE_END) > -1) {
                ((SOSServiceV200) service).getOfferings().put(rs.getSensorName(), rs.getOfferingName());
                return rs.getSensorURI();
            }
        }
        return null;
    }
}
