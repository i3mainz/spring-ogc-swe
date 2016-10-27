package de.i3mainz.springframework.swe.n52.sos.core;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import org.n52.oxf.feature.OXFFeature;
import org.n52.oxf.feature.OXFFeatureCollection;

import de.i3mainz.springframework.ogc.core.OWSServiceOperations;
import de.i3mainz.springframework.swe.n52.sos.exceptions.GetFOIServiceException;
import de.i3mainz.springframework.swe.n52.sos.exceptions.GetObservationException;
import de.i3mainz.springframework.swe.n52.sos.exceptions.InsertObservationException;
import de.i3mainz.springframework.swe.n52.sos.exceptions.RegisterSensorException;
import de.i3mainz.springframework.swe.n52.sos.model.FeatureOfInterest;
import de.i3mainz.springframework.swe.n52.sos.model.Observation;
import de.i3mainz.springframework.swe.n52.sos.model.TemporalRequestFilter;
import de.i3mainz.springframework.swe.n52.sos.model.requests.InsertObservation;
import de.i3mainz.springframework.swe.n52.sos.model.requests.RegisterSensor;

public interface SensorObservationServiceOperations extends OWSServiceOperations {

	boolean isSOSAvailable();

	boolean isSOSTransactional();

	<T> String registerSensor(T o, Collection<T> mapper);

	String registerSensor(RegisterSensor sensor) throws RegisterSensorException;

	String insertObservation(String sensorId, FeatureOfInterest foi, Observation observation)
			throws InsertObservationException;

	OXFFeatureCollection getFeatureOfInterest(String foiID) throws GetFOIServiceException;

	OXFFeatureCollection getObservation(String offeringID, List<String> sensors, List<String> observedProperties,
			String srsName, TemporalRequestFilter tempFilter) throws GetObservationException;

	<T> List<T> getObservation(String offeringID, List<String> sensors, List<String> observedProperties, String srsName,
			TemporalRequestFilter tempFilter, Function<OXFFeature, T> mapper) throws GetObservationException;

	String insertObservation(InsertObservation io) throws InsertObservationException;
}