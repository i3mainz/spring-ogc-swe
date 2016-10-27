package de.i3mainz.springframework.swe.n52.sos.model.requests;

import de.i3mainz.springframework.swe.n52.sos.model.FeatureOfInterest;
import de.i3mainz.springframework.swe.n52.sos.model.MeasuredValueType;
import de.i3mainz.springframework.swe.n52.sos.model.ObservationSensorInformationImpl;
import de.i3mainz.springframework.swe.n52.sos.model.ObservedProperty;
import de.i3mainz.springframework.swe.n52.sos.model.Sensor;
import de.i3mainz.springframework.swe.n52.sos.model.Timestamp;
import de.i3mainz.springframework.swe.n52.sos.model.UnitOfMeasurement;

public class InsertObservation extends ObservationSensorInformationImpl {

    private Object resultValue;
    private Timestamp timeStamp;
    private ObservedProperty observedProperty;
    private UnitOfMeasurement unitOfMeasurement;
    private MeasuredValueType measuredValueType;

    private InsertObservation(Sensor sensor, FeatureOfInterest foi) {
        super(sensor, foi);
    }

    public InsertObservation(final Sensor sensor, final FeatureOfInterest foi, final UnitOfMeasurement uom,
            final ObservedProperty obsProp, final MeasuredValueType mvType, Timestamp time, Object resultValue) {
        this(sensor, foi);
        this.unitOfMeasurement = uom;
        this.timeStamp = time;
        this.resultValue = resultValue;
        this.observedProperty = obsProp;
        this.measuredValueType = mvType;
    }

    public String getObservedPropertyURI() {
        return observedProperty.getUri();
    }

    public String getUnitOfMeasurementCode() {
        return unitOfMeasurement.getCode();
    }

    public Object getResultValue() {
        return resultValue;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public ObservedProperty getObservedProperty() {
        return observedProperty;
    }

    protected UnitOfMeasurement getUnitOfMeasurment() {
        return unitOfMeasurement;
    }

    public MeasuredValueType getMeasuredValueType() {
        return measuredValueType;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("InsertObservation [resultValue=")
                .append(resultValue)
                .append(", timeStamp=")
                .append(timeStamp)
                .append(", sensor=")
                .append(getSensor())
                .append(", observedProperty=")
                .append(observedProperty)
                .append(", featureOfInterest=")
                .append(getFeatureOfInterest())
                .append(", unitOfMeasurement=")
                .append(unitOfMeasurement)
                .append(", offering=")
                .append(getOffering())
                .append(", measuredValueType=")
                .append(measuredValueType)
                .append("]")
                .toString();
    }

}
