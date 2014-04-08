package de.i3mainz.springframework.swe.n52.sos.core;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.ServiceDescriptor;

import de.i3mainz.springframework.swe.n52.sos.model.FeatureOfInterest;
import de.i3mainz.springframework.swe.n52.sos.model.Observation;
import de.i3mainz.springframework.swe.n52.sos.model.Sensor;

public interface SensorObservationServiceOperations {

	public abstract ServiceDescriptor getCapabilities();

	public abstract boolean isSOSAvailable();

	public abstract boolean isSOSTransactional();

	public abstract <T> String registerSensor(T o, Collection<T> mapper);

	public abstract String registerSensor(Sensor sensor);

//	public abstract String registerSensor(RegisterSensor rs);
//
//	public abstract String insertObservation(InsertObservation io)
//			throws IOException;

	public abstract String insertObservation(String sensorId, FeatureOfInterest foi, Observation observation) throws IOException;

	public OperationResult getFeatureOfInterest(String foiID) throws OXFException, ExceptionReport;
	public OperationResult getObservation(String offeringID, List<String>observedProperties) throws OXFException, ExceptionReport;
}