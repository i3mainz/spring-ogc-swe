/**
 * 
 */
package org.springframework.cloud.stream.app.sos.sink;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author Nikolai Bock
 *
 */
@Data
@ConfigurationProperties(prefix = "sos.sensor.propertyinfo")
public class SensorPropertyInformationProperties {

    /**
     * Property name for insert observation in dynamic mode (SpEL)
     */
    private String propertyField;
    /**
     * UOM for insert observation in dynamic mode (SpEL)
     */
    private String uomField;
    /**
     * Properties information for register sensor static information
     */
    private String registerSensorPropertiesInformation = "[{\"name\":\"text\", \"uri\":\"urn:ogc:def:dataType:OGC:1.1:string\",\"mvt\": \"TEXT\", \"uom\":\"mg/m³\"}]";
    /**
     * Key for dynamic register sensor properties information
     */
    private String registerSensorPropertiesInformationKey = "REGSENSORPROPINFO";
    /**
     * Property information for insert observation in static mode
     */
    private String insertObservationSensorPropertyInformation = "{\"name\":\"text\", \"uri\":\"urn:ogc:def:dataType:OGC:1.1:string\",\"mvt\": \"TEXT\", \"uom\":\"mg/m³\"}";
}
