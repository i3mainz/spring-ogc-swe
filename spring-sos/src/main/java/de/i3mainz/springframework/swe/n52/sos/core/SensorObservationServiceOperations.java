package de.i3mainz.springframework.swe.n52.sos.core;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.n52.oxf.feature.OXFFeatureCollection;

import de.i3mainz.springframework.ogc.core.OWSServiceOperations;
import de.i3mainz.springframework.swe.n52.sos.exceptions.GetFOIServiceException;
import de.i3mainz.springframework.swe.n52.sos.exceptions.GetObservationException;
import de.i3mainz.springframework.swe.n52.sos.exceptions.RegisterSensorException;
import de.i3mainz.springframework.swe.n52.sos.model.FeatureOfInterest;
import de.i3mainz.springframework.swe.n52.sos.model.Observation;
import de.i3mainz.springframework.swe.n52.sos.model.Sensor;

public interface SensorObservationServiceOperations extends
        OWSServiceOperations {

    boolean isSOSAvailable();

    boolean isSOSTransactional();

    <T> String registerSensor(T o, Collection<T> mapper);

    String registerSensor(Sensor sensor) throws RegisterSensorException;

    String insertObservation(String sensorId, FeatureOfInterest foi,
            Observation observation) throws IOException;

    OXFFeatureCollection getFeatureOfInterest(String foiID)
            throws GetFOIServiceException;

    OXFFeatureCollection getObservation(String offeringID,
            List<String> sensors, List<String> observedProperties,
            String srsName) throws GetObservationException;
}