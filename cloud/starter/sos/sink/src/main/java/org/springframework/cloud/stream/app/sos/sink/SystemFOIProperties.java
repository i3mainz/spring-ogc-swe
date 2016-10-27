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
@ConfigurationProperties(prefix = "sos.system.foi")
public class SystemFOIProperties {
    /**
     * URI/ID of the System FOI (SpEL).
     */
    private String uri;
    /**
     * Name of the System FOI (SpEL).
     */
    private String name;
    /**
     * Position of the System FOI (SpEL).
     */
    private String position;
    /**
     * SRID of the System FOI.
     */
    private int srid;
}
