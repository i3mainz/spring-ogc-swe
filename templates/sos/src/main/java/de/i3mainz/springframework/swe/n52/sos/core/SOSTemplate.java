package de.i3mainz.springframework.swe.n52.sos.core;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang.NotImplementedException;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.feature.FeatureStore;
import org.n52.oxf.feature.OXFFeature;
import org.n52.oxf.feature.OXFFeatureCollection;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.ServiceDescriptor;
import org.n52.oxf.sos.feature.SOSObservationStore;
import org.slf4j.Logger;

import de.i3mainz.springframework.swe.n52.sos.exceptions.GetFOIServiceException;
import de.i3mainz.springframework.swe.n52.sos.exceptions.GetObservationException;
import de.i3mainz.springframework.swe.n52.sos.exceptions.InsertObservationException;
import de.i3mainz.springframework.swe.n52.sos.exceptions.RegisterSensorException;
import de.i3mainz.springframework.swe.n52.sos.model.FeatureOfInterest;
import de.i3mainz.springframework.swe.n52.sos.model.Observation;
import de.i3mainz.springframework.swe.n52.sos.model.TemporalRequestFilter;
import de.i3mainz.springframework.swe.n52.sos.model.requests.InsertObservation;
import de.i3mainz.springframework.swe.n52.sos.model.requests.RegisterSensor;
import de.i3mainz.springframework.swe.n52.sos.service.versions.v100.SOSServiceV100;
import de.i3mainz.springframework.swe.n52.sos.service.versions.v200.SOSServiceV200;

public class SOSTemplate extends SOSAccessor implements SensorObservationServiceOperations {

	private static final Logger LOG = getLog();

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.i3mainz.springframework.ows.n52.sos.core.
	 * SensorObservationServiceOperations#getCapabilities()
	 */
	@Override
	public ServiceDescriptor getCapabilities() {
		return getService().getCapabilities();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.i3mainz.springframework.ows.n52.sos.core.
	 * SensorObservationServiceOperations#isSOSAvailable()
	 */
	@Override
	public boolean isSOSAvailable() {
		return getService().getCapabilities() != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.i3mainz.springframework.ows.n52.sos.core.
	 * SensorObservationServiceOperations#isSOSTransactional()
	 */
	@Override
	public boolean isSOSTransactional() {
		LOG.trace("isTransactional()");
		if (getService().getCapabilities() == null) {
			LOG.error(
					String.format("Service descriptor not available for SOS '%s'", getConnectionParameter().getUrl()));
			return false;
		}
		return getService().isTransactional();
	}

	@Override
	public <T> String registerSensor(T sensorDescription, Collection<T> mapper) {
		throw new NotImplementedException("Derzeit nicht implementiert");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.i3mainz.springframework.ows.n52.sos.core.
	 * SensorObservationServiceOperations
	 */
	@Override
	public String registerSensor(final RegisterSensor rs) throws RegisterSensorException {
		try {
			if (VERSION100.equals(getConnectionParameter().getVersion())) {
				return ((SOSServiceV100) getService()).registerSensor(rs);
			} else if (VERSION200.equals(getConnectionParameter().getVersion())) {
				return ((SOSServiceV200) getService()).insertSensor(rs);
			}
		} catch (final ExceptionReport e) {
			RegisterSensorException regSenEx = new RegisterSensorException(e);
			String sID = regSenEx.checkReport(rs, getService());
			if (sID == null) {
				LOG.error(String.format("Exception thrown: %s", e.getMessage()), e);
			}
			return sID;
		}
		return null;
	}

	@Override
	public String insertObservation(String sensorId, FeatureOfInterest foi, Observation observation)
			throws InsertObservationException {
		try {
			return getService().insertObservation(sensorId, foi, observation);
		} catch (Exception e) {
			throw new InsertObservationException(e);
		}
	}

	@Override
	public String insertObservation(final InsertObservation io) throws InsertObservationException {
		try {
			return getService().insertObservation(io);
		} catch (ExceptionReport e) {
			InsertObservationException inObservEx = new InsertObservationException(e);
			String oID = inObservEx.checkReport();
			if (oID == null) {
				LOG.error(String.format("Exception thrown: %s", e.getMessage()), e);
			}
			return oID;
		}
	}

	@Override
	public OXFFeatureCollection getFeatureOfInterest(String foiID) throws GetFOIServiceException {
		try {
			FeatureStore store = new FeatureStore(getService().getFeatureOfInterest(foiID));
			return store.unmarshalFeatures();
		} catch (OXFException e) {
			throw new GetFOIServiceException(e);
		}
	}

	@Override
	public OXFFeatureCollection getObservation(String offering, List<String> sensors, List<String> observedProperties,
			String srsName, TemporalRequestFilter tempFilter) throws GetObservationException {
		LOG.debug("SOS-URL: " + getConnectionParameter().getUrl());
		OperationResult result = getService().getObservation(offering, sensors, observedProperties, srsName,
				tempFilter);
		LOG.debug("Sended request: \n" + result.getSendedRequest());

		try {
			SOSObservationStore store = new SOSObservationStore(result);
			return store.unmarshalFeatures();
		} catch (OXFException e) {
			throw new GetObservationException(e);
		}
	}

	@Override
	public <T> List<T> getObservation(String offeringID, List<String> sensors, List<String> observedProperties,
			String srsName, TemporalRequestFilter tempFilter, Function<OXFFeature, T> mapper)
			throws GetObservationException {
		return getObservation(offeringID, sensors, observedProperties, srsName, tempFilter).toList().stream()
				.map(mapper).collect(Collectors.toList());
	}
}