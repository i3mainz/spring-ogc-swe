package de.i3mainz.springframework.swe.sos;

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
			Observation observation);

}
