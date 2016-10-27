/**
 * 
 */
package de.i3mainz.springframework.xd.swe.sos;

import org.springframework.xd.module.options.spi.ModuleOption;

/**
 * @author Nikolai Bock
 *
 */
public class SensorPropertyInformationMixin {

    private String registerSensorPropertiesInformation = "[{\"name\":\"text\", \"uri\":\"urn:ogc:def:dataType:OGC:1.1:string\",\"mvt\": \"TEXT\", \"uom\":\"mg/m³\"}]";
    private String registerSensorPropertiesInformationKey = "REGSENSORPROPINFO";
    private String insertObservationSensorPropertyInformation = "{\"name\":\"text\", \"uri\":\"urn:ogc:def:dataType:OGC:1.1:string\",\"mvt\": \"TEXT\", \"uom\":\"mg/m³\"}";

    private String propertyField;
    private String uomField;

    /**
     * @return the registerSensorPropertiesInformation
     */
    public final String getRegisterSensorPropertiesInformation() {
        return registerSensorPropertiesInformation;
    }

    /**
     * @param registerSensorPropertiesInformation
     *            the registerSensorPropertiesInformation to set
     */
    @ModuleOption("Properties information for register sensor static information")
    public final void setRegisterSensorPropertiesInformation(String registerSensorPropertiesInformation) {
        this.registerSensorPropertiesInformation = registerSensorPropertiesInformation;
    }

    /**
     * @return the registerSensorPropertiesInformationKey
     */
    public final String getRegisterSensorPropertiesInformationKey() {
        return registerSensorPropertiesInformationKey;
    }

    /**
     * @param registerSensorPropertiesInformationKey
     *            the registerSensorPropertiesInformationKey to set
     */
    @ModuleOption("Key for dynamic register sensor properties information")
    public final void setRegisterSensorPropertiesInformationKey(String registerSensorPropertiesInformationKey) {
        this.registerSensorPropertiesInformationKey = registerSensorPropertiesInformationKey;
    }

    /**
     * @return the insertObservationSensorPropertyInformation
     */
    public final String getInsertObservationSensorPropertyInformation() {
        return insertObservationSensorPropertyInformation;
    }

    /**
     * @param insertObservationSensorPropertyInformation
     *            the insertObservationSensorPropertyInformation to set
     */
    @ModuleOption("Property information for insert observation in static mode")
    public final void setInsertObservationSensorPropertyInformation(String insertObservationSensorPropertyInformation) {
        this.insertObservationSensorPropertyInformation = insertObservationSensorPropertyInformation;
    }

    /**
     * @return the propertyField
     */
    public final String getPropertyField() {
        return propertyField;
    }

    /**
     * @param propertyField
     *            the propertyField to set
     */
    @ModuleOption("Property name expression for insert observation in dynamic mode")
    public final void setPropertyField(String propertyField) {
        this.propertyField = propertyField;
    }

    /**
     * @return the uomField
     */
    public final String getUomField() {
        return uomField;
    }

    /**
     * @param uomField
     *            the uomField to set
     */
    @ModuleOption("UOM expression for insert observation in dynamic mode")
    public final void setUomField(String uomField) {
        this.uomField = uomField;
    }

}
