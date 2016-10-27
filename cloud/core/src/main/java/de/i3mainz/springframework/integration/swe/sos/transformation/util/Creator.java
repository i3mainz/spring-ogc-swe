package de.i3mainz.springframework.integration.swe.sos.transformation.util;

import org.springframework.messaging.Message;

/**
 * Interfaces which should be implemented providing a creator.
 * 
 * @author Nikolai Bock
 *
 */
@FunctionalInterface
public interface Creator {
    /**
     * Create the object
     * 
     * @param message
     *            the full message
     * @return the resulted object
     */
    Object create(Message<?> message);
}
