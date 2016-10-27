/**
 * 
 */
package de.i3mainz.springframework.swe.n52.sos.exceptions;

import java.util.Arrays;
import java.util.Iterator;

import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.OWSException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.i3mainz.springframework.swe.n52.sos.util.Configuration;

/**
 * @author Nikolai Bock
 *
 */
public class InsertObservationException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(InsertObservationException.class);

    /**
     * @param cause
     *            why the exception is thrown
     */
    public InsertObservationException(Throwable cause) {
        super.initCause(cause);
    }

    public String checkReport() throws InsertObservationException {
        if (getCause() instanceof ExceptionReport) {
            String oID = handleExceptionReportException((ExceptionReport) getCause());
            if (oID != null) {
                return oID;
            }
        }

        throw this;
    }

    private static String handleExceptionReportException(ExceptionReport e) {
        final Iterator<OWSException> iter = e.getExceptionsIterator();
        StringBuffer buf = new StringBuffer();
        while (iter.hasNext()) {
            final OWSException owsEx = iter.next();
            // check for observation already contained exception
            // TODO update to latest 52nSOS 4.0x message
            if (isObservationAlreadyContained(owsEx)) {
                return Configuration.SOS_OBSERVATION_ALREADY_CONTAINED;
            }
            buf = buf.append(String.format("ExceptionCode: '%s' because of '%s'%n", owsEx.getExceptionCode(),
                    Arrays.toString(owsEx.getExceptionTexts())));
        }
        // TODO improve logging here:
        // add logOwsEceptionReport static util method to OxF or
        // some OER report logger which has unit tests
        LOG.error(String.format("Exception thrown: %s%n%s", e.getMessage(), buf.toString()));
        LOG.debug(e.getMessage(), e);
        return null;
    }
    
    private static boolean isObservationAlreadyContained(final OWSException owsEx) {
        return owsEx.getExceptionCode().equals(Configuration.SOS_EXCEPTION_CODE_NO_APPLICABLE_CODE) &&
                owsEx.getExceptionTexts().length > 0 &&
                (owsEx.getExceptionTexts()[0].indexOf(Configuration.SOS_EXCEPTION_OBSERVATION_DUPLICATE_CONSTRAINT) > -1
                        ||
                        owsEx.getExceptionTexts()[0].indexOf(Configuration.SOS_EXCEPTION_OBSERVATION_ALREADY_CONTAINED) > -1
                        ||
                        owsEx.getExceptionTexts()[0].indexOf(Configuration.SOS_200_DUPLICATE_OBSERVATION_CONSTRAINT) > -1);
    }

}
