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
@ConfigurationProperties(prefix = "sos.sensor")
public class SensorProperties {
    /**
     * Sensor-ID. (SpEL in dynamic mode)
     */
    private String uri;
    /**
     * Name of the sensor (SpEL in dynamic mode)
     */
    private String name;
}
