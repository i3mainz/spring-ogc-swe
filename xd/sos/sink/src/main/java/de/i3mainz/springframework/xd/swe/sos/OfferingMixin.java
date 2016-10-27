package de.i3mainz.springframework.xd.swe.sos;

import org.springframework.xd.module.options.spi.ModuleOption;

/**
 * Mixin for Offering parameter.
 * 
 * @author Nikolai Bock
 *
 */
public class OfferingMixin {

    /** ID of the offering */
    private String offeringID;
    /** Name of the offering */
    private String offeringName;

    /**
     * @param offeringID
     *            the offeringID to set
     */
    @ModuleOption("ID for Sensor-Offering")
    public void setOfferingID(String offeringID) {
        this.offeringID = offeringID;
    }

    /**
     * @param offeringName
     *            the offeringName to set
     */
    @ModuleOption("Name for Sensor-Offering")
    public void setOfferingName(String offeringName) {
        this.offeringName = offeringName;
    }

    /**
     * @return the offeringID
     */
    public String getOfferingID() {
        return offeringID;
    }

    /**
     * @return the offeringName
     */
    public String getOfferingName() {
        return offeringName;
    }

}
