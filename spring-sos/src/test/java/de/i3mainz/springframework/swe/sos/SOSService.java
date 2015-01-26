package de.i3mainz.springframework.swe.sos;

import java.io.IOException;
import java.util.List;

import org.n52.oxf.OXFException;
import org.n52.oxf.ows.ExceptionReport;

import de.i3mainz.springframework.swe.n52.sos.model.FeatureOfInterest;
import de.i3mainz.springframework.swe.n52.sos.model.Observation;
import de.i3mainz.springframework.swe.n52.sos.model.Sensor;

public interface SOSService {

    String getSosVersion();

    // int getObservation();
    // void insertSensor();

    // String registerSensor(String id, String offeringId, String offeringName);

    boolean isSOSAvailable();

    boolean isSOSTransactional();

    String insertSensor(Sensor sensor);

    String insertObservation(Sensor sensor, FeatureOfInterest foi,
            Observation observation) throws IOException;

    String getFeatureOfInterest(String foiID) throws OXFException,
            ExceptionReport;
    
    String getObservation(String offeringId, List<String> sensors,
            List<String> observedProperties, String srsName) throws OXFException,
            ExceptionReport;

}
