package de.i3mainz.springframework.xd.swe.sos;

import javax.validation.constraints.Pattern;

import org.springframework.xd.module.options.spi.ModuleOption;

/**
 * Mixin for parameter for a connection to an Sensor Observation Service.
 * 
 * @author Nikolai Bock
 *
 */
public class SOSConnectionMixin {

    /** URL to the service */
    private String url;
    /** Version of the SOS */
    private String serviceversion;
    /** Binding which will be used */
    private String binding;

    /**
     * @param url
     *            the url to set
     */
    @ModuleOption("URL of the SOS-Service")
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @param serviceversion
     *            the serviceversion to set
     */
    @ModuleOption("Version of the SOS-Service")
    public void setServiceversion(String serviceversion) {
        this.serviceversion = serviceversion;
    }

    /**
     * @param binding
     *            the binding to set
     */
    @ModuleOption("Binding for SOS-Connection (SOAP,POX,KVP)")
    public void setBinding(String binding) {
        this.binding = binding;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @return the serviceversion
     */
    public String getServiceversion() {
        return serviceversion;
    }

    /**
     * @return the binding
     */
    @Pattern(regexp = "(SOAP|POX|KVP)")
    public String getBinding() {
        return binding;
    }

}
