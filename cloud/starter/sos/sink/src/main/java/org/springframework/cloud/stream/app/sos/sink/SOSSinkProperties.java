/**
 * 
 */
package org.springframework.cloud.stream.app.sos.sink;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.expression.Expression;

import lombok.Data;

/**
 * @author Nikolai Bock
 *
 */
@ConfigurationProperties(prefix = "sos")
@Data
public class SOSSinkProperties {

    /**
     * The reference to an already created InsertObservationFactory (SpEL).
     */
    private Expression insertObservationFactory;
    /**
     * The reference to an already created Observation object (SpEL).
     */
    private Expression observation;
}
