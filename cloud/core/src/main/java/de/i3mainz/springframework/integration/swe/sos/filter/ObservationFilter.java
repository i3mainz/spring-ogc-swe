package de.i3mainz.springframework.integration.swe.sos.filter;

import org.springframework.integration.annotation.Filter;
import org.springframework.messaging.handler.annotation.Payload;

import de.i3mainz.springframework.swe.n52.sos.model.Observation;

/**
 * This class provides a filter causing the payload is an {@link Observation}.
 * 
 * @author Nikolai Bock
 *
 */
public class ObservationFilter {

    /**
     * Check whether type of payload is {@link Observation} or not.
     * 
     * @param payload payload of the message
     * @return if payload is {@link Observation}
     */
    @Filter
    public boolean filter(@Payload Object payload) {
        return payload instanceof Observation;
    }

}
