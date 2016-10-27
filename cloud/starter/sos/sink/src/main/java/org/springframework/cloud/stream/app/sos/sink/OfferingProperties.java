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
@ConfigurationProperties(prefix = "sos.offering")
public class OfferingProperties {
    /**
     * ID for sensor offering
     */
    private String uri;
    /**
     * Name for sensor offering
     */
    private String name;
}
