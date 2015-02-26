package de.i3mainz.springframework.swe.sos;

import java.io.IOException;
import java.util.List;

import de.i3mainz.springframework.swe.n52.sos.exceptions.GetFOIServiceException;
import de.i3mainz.springframework.swe.n52.sos.exceptions.GetObservationException;
import de.i3mainz.springframework.swe.n52.sos.model.FeatureOfInterest;
import de.i3mainz.springframework.swe.n52.sos.model.Observation;
import de.i3mainz.springframework.swe.n52.sos.model.Sensor;

public interface SOSService {

    String getSosVersion();

    boolean isSOSAvailable();

    boolean isSOSTransactional();

    String insertSensor(Sensor sensor);

    String insertObservation(Sensor sensor, FeatureOfInterest foi,
            Observation observation) throws IOException;

    String getFeatureOfInterest(String foiID) throws GetFOIServiceException;

    String getObservation(String offeringId, List<String> sensors,
            List<String> observedProperties, String srsName)
            throws GetObservationException;

}
