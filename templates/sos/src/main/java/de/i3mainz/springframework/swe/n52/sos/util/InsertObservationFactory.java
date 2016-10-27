/**
 * 
 */
package de.i3mainz.springframework.swe.n52.sos.util;

import java.util.Date;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.integration.support.json.JsonObjectMapper;
import org.springframework.integration.support.json.JsonObjectMapperProvider;

import de.i3mainz.springframework.swe.n52.sos.model.FeatureOfInterest;
import de.i3mainz.springframework.swe.n52.sos.model.MeasuredValueType;
import de.i3mainz.springframework.swe.n52.sos.model.Observation;
import de.i3mainz.springframework.swe.n52.sos.model.ObservedProperty;
import de.i3mainz.springframework.swe.n52.sos.model.Sensor;
import de.i3mainz.springframework.swe.n52.sos.model.Timestamp;
import de.i3mainz.springframework.swe.n52.sos.model.UnitOfMeasurement;
import de.i3mainz.springframework.swe.n52.sos.model.requests.InsertObservation;

/**
 * @author Nikolai Bock
 *
 */
public class InsertObservationFactory {

    private static final Log LOG = LogFactory.getLog(InsertObservationFactory.class);

    private final JsonObjectMapper<?, ?> jsonObjectMapper = JsonObjectMapperProvider.newInstance();

    private Sensor sensor;
    private UnitOfMeasurement uom;
    private ObservedProperty obsProp;
    private MeasuredValueType mvType;

    public InsertObservationFactory() {
        // Provide empty constructor to allow the object creation without
        // parameter, calling the create function with all of them
    }

    public InsertObservationFactory(final Sensor sensor, final UnitOfMeasurement uom, final ObservedProperty obsProp,
            final MeasuredValueType mvType) {
        this.sensor = sensor;
        this.uom = uom;
        this.obsProp = obsProp;
        this.mvType = mvType;

    }

    public InsertObservationFactory(final Sensor sensor, String propertyInfo) {
        try {
            Map<String, Object> property = jsonObjectMapper.fromJson(propertyInfo, Map.class);
            this.sensor = sensor;
            this.uom = new UnitOfMeasurement(property.get("uom").toString(), property.get("uom").toString());
            this.obsProp = new ObservedProperty(property.get("name").toString(), property.get("uri").toString());
            this.mvType = MeasuredValueType.valueOf(property.get("mvt").toString());
        } catch (Exception e) {
            LOG.error("Parameter for InsertObservation could not been created! Could not parse JSON!", e);
        }

    }

    public InsertObservationFactory(final String sensorID, String propertyInfo) {
        this(new Sensor(null, sensorID, null), propertyInfo);
    }

    public InsertObservation create(final Sensor sensor, final UnitOfMeasurement uom, final ObservedProperty obsProp,
            final MeasuredValueType mvType, final FeatureOfInterest foi, final Date time, Object resultValue) {
        return new InsertObservation(sensor, foi, uom, obsProp, mvType, new Timestamp().setDate(time), resultValue);
    }

    public InsertObservation create(FeatureOfInterest foi, Date time, Object resultValue) {
        return this.create(this.sensor, this.uom, this.obsProp, this.mvType, foi, time, resultValue);
    }

    public InsertObservation create(FeatureOfInterest foi, Observation obs) {
        return this.create(foi, obs.getTime(), obs.getValue());
    }

}
