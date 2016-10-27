/**
 * 
 */
package de.i3mainz.springframework.swe.n52.sos.model;

/**
 * @author Nikolai Bock
 *
 */
public interface ObservationSensorInformation {

    public String getSensorName();

    public String getSensorURI();

    public String getFeatureOfInterestName();

    public String getFeatureOfInterestURI();

    public Offering getOffering();

    String getEpsgCode();

    double getLatitudeValue();

    double getLongitudeValue();

    double getAltitudeValue();

    boolean isSetAltitudeValue();

}