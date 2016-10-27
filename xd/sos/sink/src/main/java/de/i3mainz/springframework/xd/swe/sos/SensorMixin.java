package de.i3mainz.springframework.xd.swe.sos;

import org.springframework.xd.module.options.spi.Mixin;
import org.springframework.xd.module.options.spi.ModuleOption;
import org.springframework.xd.module.options.spi.ModulePlaceholders;

/**
 * Mixin for sensor parameter
 * 
 * @author Nikolai Bock
 *
 */
@Mixin({ OfferingMixin.class })
public class SensorMixin{

    /** The SensorID */
    private String sensorID = ModulePlaceholders.XD_STREAM_NAME;
    private String sensorName = ModulePlaceholders.XD_STREAM_NAME;

    /**
     * @param sensorID
     *            the sensorID to set
     */
    @ModuleOption("ID for the Sensor. Parameter or Expression (expression only in dynamic mode)")
    public final void setSensorID(String sensorID) {
        this.sensorID = sensorID;
    }

    /**
     * @return the sensorID
     */
    public final String getSensorID() {
        return sensorID;
    }

    /**
     * @return the sensorName
     */
    public final String getSensorName() {
        return sensorName;
    }

    /**
     * @param sensorName
     *            the sensorName to set
     */
    @ModuleOption("Name of the sensor. Parameter or Expression (expression only in dynamic mode)")
    public final void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

}
