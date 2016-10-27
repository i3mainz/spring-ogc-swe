package de.i3mainz.springframework.swe.n52.sos.model.requests;

import java.util.Collection;
import java.util.Map;

import de.i3mainz.springframework.swe.n52.sos.model.MeasuredValueType;
import de.i3mainz.springframework.swe.n52.sos.model.ObservationSensorInformation;
import de.i3mainz.springframework.swe.n52.sos.model.ObservedProperty;

public class RegisterSensor {

    private final ObservationSensorInformation io;
    private final Map<ObservedProperty, MeasuredValueType> measuredValueTypes;
    private final Collection<ObservedProperty> observedProperties;
    private final Map<ObservedProperty, String> unitOfMeasurements;

    public RegisterSensor(final ObservationSensorInformation io, final Collection<ObservedProperty> observedProperties,
            final Map<ObservedProperty, MeasuredValueType> measuredValueTypes,
            final Map<ObservedProperty, String> unitOfMeasurements) {
        this.io = io;
        this.observedProperties = observedProperties;
        this.measuredValueTypes = measuredValueTypes;
        this.unitOfMeasurements = unitOfMeasurements;
    }

    public String getAltitudeUnit() {
        return "m";
    }

    public double getAltitudeValue() {
        return io.getAltitudeValue();
    }

    public String getEpsgCode() {
        return io.getEpsgCode();
    }

    public String getFeatureOfInterestName() {
        return io.getFeatureOfInterestName();
    }

    public String getFeatureOfInterestURI() {
        return io.getFeatureOfInterestURI();
    }

    public String getLatitudeUnit() {
        return "degree";
    }

    public double getLatitudeValue() {
        return io.getLatitudeValue();
    }

    public String getLongitudeUnit() {
        return "degree";
    }

    public double getLongitudeValue() {
        return io.getLongitudeValue();
    }

    public MeasuredValueType getMeasuredValueType(final ObservedProperty observedProperty) {
        return measuredValueTypes.get(observedProperty);
    }

    public Collection<ObservedProperty> getObservedProperties() {
        return observedProperties;
    }

    public String getOfferingName() {
        return io.getOffering().getName();
    }

    public String getOfferingUri() {
        return io.getOffering().getUri();
    }

    public String getSensorName() {
        return io.getSensorName();
    }

    public String getSensorURI() {
        return io.getSensorURI();
    }

    public String getUnitOfMeasurementCode(final ObservedProperty observedProperty) {
        return unitOfMeasurements.get(observedProperty);
    }

}
