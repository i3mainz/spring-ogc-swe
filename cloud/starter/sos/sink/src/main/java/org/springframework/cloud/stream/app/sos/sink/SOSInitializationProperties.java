package org.springframework.cloud.stream.app.sos.sink;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author Nikolai Bock
 *
 */
@Data
@ConfigurationProperties
public class SOSInitializationProperties {

    /**
     * Whether a sensor should be registered.
     */
    private boolean initSensor = false;
    /**
     * Create the in dynamic mode based on processed messages.
     */
    private boolean dynamicSensorCreation = false;

}
