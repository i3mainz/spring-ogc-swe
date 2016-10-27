/**
 * 
 */
package de.i3mainz.springframework.boot.autoconfigure.ogc.swe.sos;

import org.n52.oxf.sos.adapter.ISOSRequestBuilder.Binding;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author Nikolai Bock
 *
 */
@ConfigurationProperties(prefix = "sos.connection")
@Data
public class SOSConnectionProperties {

    /**
     * URL of the SOS
     */
    private String url;
    /**
     * Version of the SOS
     */
    private String version;
    /**
     * Binding which will be used to request
     */
    private Binding binding;

}
