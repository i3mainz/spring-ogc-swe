package de.i3mainz.springframework.swe.sos;

import java.util.List;

import de.i3mainz.springframework.swe.n52.sos.exceptions.GetFOIServiceException;
import de.i3mainz.springframework.swe.n52.sos.exceptions.GetObservationException;
import de.i3mainz.springframework.swe.n52.sos.exceptions.InsertObservationException;
import de.i3mainz.springframework.swe.n52.sos.exceptions.RegisterSensorException;
import de.i3mainz.springframework.swe.n52.sos.model.FeatureOfInterest;
import de.i3mainz.springframework.swe.n52.sos.model.Observation;
import de.i3mainz.springframework.swe.n52.sos.model.Sensor;
import de.i3mainz.springframework.swe.n52.sos.model.TemporalRequestFilter;
import de.i3mainz.springframework.swe.n52.sos.model.requests.InsertObservation;
import de.i3mainz.springframework.swe.n52.sos.model.requests.RegisterSensor;

public interface SOSService {

    String getSosVersion();

    boolean isSOSAvailable();

    boolean isSOSTransactional();

    String insertSensor(RegisterSensor sensor) throws RegisterSensorException;

    String insertObservation(Sensor sensor, FeatureOfInterest foi,
            Observation observation) throws InsertObservationException;

    String getFeatureOfInterest(String foiID) throws GetFOIServiceException;

    String getObservation(String offeringId, List<String> sensors,
            List<String> observedProperties, String srsName, TemporalRequestFilter tempFilter)
            throws GetObservationException;

	String insertObservation(InsertObservation create) throws InsertObservationException;

}
