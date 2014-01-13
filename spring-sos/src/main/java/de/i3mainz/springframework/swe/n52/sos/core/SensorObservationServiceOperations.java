package de.i3mainz.springframework.swe.n52.sos.core;

import java.io.IOException;
import java.util.Collection;

import org.n52.oxf.ows.ServiceDescriptor;
import org.n52.sos.importer.feeder.model.requests.InsertObservation;
import org.n52.sos.importer.feeder.model.requests.RegisterSensor;

import de.i3mainz.springframework.swe.n52.sos.model.FeatureOfInterest;
import de.i3mainz.springframework.swe.n52.sos.model.Observation;
import de.i3mainz.springframework.swe.n52.sos.model.Sensor;

public interface SensorObservationServiceOperations {

	public abstract ServiceDescriptor getCapabilities();

	public abstract boolean isSOSAvailable();

	public abstract boolean isSOSTransactional();

	public abstract <T> String registerSensor(T o, Collection<T> mapper);

	public abstract String registerSensor(Sensor sensor);

	public abstract String registerSensor(RegisterSensor rs);

	public abstract String insertObservation(InsertObservation io)
			throws IOException;

	public abstract String insertObservation(String sensorId, FeatureOfInterest foi, Observation observation);

}