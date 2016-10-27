/**
 * 
 */
package de.i3mainz.springframework.integration.ogc.sos.core;

import java.util.ArrayList;
import java.util.List;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;

import de.i3mainz.springframework.swe.n52.sos.exceptions.GetObservationException;
import de.i3mainz.springframework.swe.n52.sos.model.TemporalRequestFilter;

/**
 * @author Nikolai Bock
 *
 */
public class OGCSOSGetObservationExecutor extends OGCSOSExecutor {

    /*
     * (non-Javadoc)
     * 
     * @see de.i3mainz.springframework.integration.ogc.sos.core.OGCSOSExecutor#
     * executeOutboundOperation(org.springframework.messaging.Message)
     */
    @Override
    public Object executeOutboundOperation(Message<?> message) {
        List<String> observedProperties = new ArrayList<>();
        observedProperties.add("urn:ogc:def:dataType:OGC:1.1:string");
        try {
            return getRequest("Twitter_Sentiment", null, observedProperties, null, null);
        } catch (GetObservationException e) {
            throw new MessagingException(message, "Can't request SOS-Service. ", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.i3mainz.springframework.integration.ogc.sos.core.OGCSOSExecutor#poll(
     * org.springframework.messaging.Message)
     */
    @Override
    public Object poll(Message<?> requestMessage) {

        List<String> observedProperties = new ArrayList<>();
        observedProperties.add("urn:ogc:def:dataType:OGC:1.1:string");
        try {
            return getRequest("Twitter_Sentiment", null, observedProperties, null, null);
        } catch (GetObservationException e) {
            throw new MessagingException(requestMessage, "Can't request SOS-Service. ", e);
        }
    }

    private Object getRequest(String offering, List<String> sensors, List<String> observedProperties, String srsName,
            TemporalRequestFilter tempFilter) throws GetObservationException {

        return getSosTemplate().getObservation("Twitter_Sentiment", null, observedProperties, null, null);

    }

}
