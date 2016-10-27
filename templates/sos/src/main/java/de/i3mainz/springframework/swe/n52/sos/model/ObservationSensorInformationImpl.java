/**
 * 
 */
package de.i3mainz.springframework.swe.n52.sos.model;

/**
 * @author Nikolai Bock
 *
 */
public class ObservationSensorInformationImpl implements ObservationSensorInformation {

    private final Sensor sensor;
    private final FeatureOfInterest featureOfInterest;

    public ObservationSensorInformationImpl(final Sensor sensor, final FeatureOfInterest foi) {
        this.featureOfInterest = foi;
        this.sensor = sensor;
    }

    @Override
    public String getSensorName() {
        return sensor.getName();
    }

    @Override
    public String getSensorURI() {
        return sensor.getUri();
    }

    @Override
    public String getFeatureOfInterestName() {
        return featureOfInterest.getName();
    }

    @Override
    public String getFeatureOfInterestURI() {
        return featureOfInterest.getUri();
    }

    @Override
    public Offering getOffering() {
      return sensor.getOffering();
    }
    
    @Override
    public String getEpsgCode() {
        return Integer.toString(featureOfInterest.getPosition().getSRID());
    }
    @Override
    public double getLatitudeValue() {
        return featureOfInterest.getPosition().getX();
    }
    @Override
    public double getLongitudeValue() {
        return featureOfInterest.getPosition().getY();
    }
    @Override
    public double getAltitudeValue() {
        return featureOfInterest.getPosition().getCoordinate().z;
    }
    @Override
    public boolean isSetAltitudeValue() {
        return featureOfInterest != null && featureOfInterest.getPosition().getDimension() == 3;
    }

    protected FeatureOfInterest getFeatureOfInterest() {
        return featureOfInterest;
    }

    protected Sensor getSensor() {
        return sensor;
    }

}
