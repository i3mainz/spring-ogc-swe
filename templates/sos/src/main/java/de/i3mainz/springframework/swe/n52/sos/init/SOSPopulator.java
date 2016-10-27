package de.i3mainz.springframework.swe.n52.sos.init;

import de.i3mainz.springframework.swe.n52.sos.SOSConnectionParameter;

/**
 * This interface represents a populator of the SOS module.
 * 
 * @author Nikolai Bock
 *
 */
@FunctionalInterface
public interface SOSPopulator {

    /**
     * This method should implement the execution of a process.
     * 
     * @param serviceParam
     *            the connection parameter of the SOS
     */
    void populate(SOSConnectionParameter serviceParam);

}
