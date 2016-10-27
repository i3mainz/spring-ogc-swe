/**
 * 
 */
package de.i3mainz.springframework.swe.n52.sos.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.integration.support.json.JsonObjectMapper;
import org.springframework.integration.support.json.JsonObjectMapperProvider;

import de.i3mainz.springframework.swe.n52.sos.model.MeasuredValueType;
import de.i3mainz.springframework.swe.n52.sos.model.ObservationSensorInformation;
import de.i3mainz.springframework.swe.n52.sos.model.ObservedProperty;
import de.i3mainz.springframework.swe.n52.sos.model.requests.RegisterSensor;

/**
 * @author Nikolai Bock
 *
 */
public class RegisterSensorFactory {

    private static final Log LOG = LogFactory.getLog(RegisterSensorFactory.class);

    private final JsonObjectMapper<?, ?> jsonObjectMapper = JsonObjectMapperProvider.newInstance();

    private ObservationSensorInformation io;
    private Collection<ObservedProperty> observedProperties;
    private Map<ObservedProperty, MeasuredValueType> measuredValueTypes;
    private Map<ObservedProperty, String> unitOfMeasurements;

    public RegisterSensorFactory(ObservationSensorInformation io, String propertiesInfos) {
        this.io = io;
        try {
            Collection<Map<String, Object>> properties = jsonObjectMapper.fromJson(propertiesInfos, Collection.class);

            this.observedProperties = new ArrayList<>();
            this.measuredValueTypes = new HashMap<>();
            this.unitOfMeasurements = new HashMap<>();

            for (Map<String, Object> propertyMap : properties) {
                ObservedProperty property = new ObservedProperty(propertyMap.get("name").toString(),
                        propertyMap.get("uri").toString());
                observedProperties.add(property);
                measuredValueTypes.put(property, MeasuredValueType.valueOf(propertyMap.get("mvt").toString()));
                unitOfMeasurements.put(property, propertyMap.get("uom").toString());
            }

        } catch (Exception e) {
            LOG.error("Could not create parameter! JSON parser not work!", e);
        }
    }

    public RegisterSensor create() {
        return new RegisterSensor(io, observedProperties, measuredValueTypes, unitOfMeasurements);
    }

}
