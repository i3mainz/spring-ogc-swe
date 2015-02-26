package de.i3mainz.springframework.swe.n52.sos.core;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.n52.oxf.OXFException;
import org.n52.oxf.feature.OXFFeatureCollection;
import org.n52.oxf.ows.ExceptionReport;

import de.i3mainz.springframework.ogc.core.OWSServiceOperations;
import de.i3mainz.springframework.swe.n52.sos.model.FeatureOfInterest;
import de.i3mainz.springframework.swe.n52.sos.model.Observation;
import de.i3mainz.springframework.swe.n52.sos.model.Sensor;

public interface SensorObservationServiceOperations extends
        OWSServiceOperations {

    boolean isSOSAvailable();

    boolean isSOSTransactional();

    <T> String registerSensor(T o, Collection<T> mapper);

    String registerSensor(Sensor sensor);

    String insertObservation(String sensorId, FeatureOfInterest foi,
            Observation observation) throws IOException;

    OXFFeatureCollection getFeatureOfInterest(String foiID)
            throws OXFException, ExceptionReport;

    OXFFeatureCollection getObservation(String offeringID,
            List<String> sensors, List<String> observedProperties,
            String srsName) throws OXFException, ExceptionReport;
}