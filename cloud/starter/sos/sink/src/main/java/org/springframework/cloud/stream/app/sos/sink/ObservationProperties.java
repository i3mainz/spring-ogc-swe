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
@ConfigurationProperties(prefix = "sos.observation")
public class ObservationProperties {
    /**
     * Where the spatial information is located in the message (SpEL)
     */
    private String spatialField;
    /**
     * Where the temporal information is located in the message (SpEL)
     */
    private String timeField;
    /**
     * Where the value is stored in the message (SpEL)
     */
    private String value;
}
