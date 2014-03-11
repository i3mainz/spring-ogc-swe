package de.i3mainz.springframework.swe.n52.sos.core;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import net.opengis.sos.x10.RegisterSensorResponseDocument;
import net.opengis.swes.x20.InsertSensorResponseDocument;

import org.apache.xmlbeans.XmlException;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.OWSException;
import org.n52.oxf.ows.OwsExceptionCode;
import org.n52.oxf.ows.ServiceDescriptor;
import org.n52.oxf.ows.capabilities.OperationsMetadata;
import org.n52.oxf.sos.adapter.ISOSRequestBuilder;
import org.n52.oxf.sos.adapter.SOSAdapter;
import org.n52.oxf.sos.request.v100.RegisterSensorParameters;
import org.n52.oxf.sos.request.v200.InsertSensorParameters;

import de.i3mainz.springframework.swe.n52.sos.model.FeatureOfInterest;
import de.i3mainz.springframework.swe.n52.sos.model.Observation;
import de.i3mainz.springframework.swe.n52.sos.model.Sensor;
import de.i3mainz.springframework.swe.n52.sos.util.Http_connect;
import de.i3mainz.springframework.swe.n52.sos.util.Sos_observation;
import de.i3mainz.springframework.swe.n52.sos.util.Sos_sensor;
import de.i3mainz.springframework.swe.n52.sos.util.Sos_xml_doc;

public class SOSTemplate extends SOSAccessor implements
		SensorObservationServiceOperations {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.i3mainz.springframework.ows.n52.sos.core.
	 * SensorObservationServiceOperations#getCapabilities()
	 */
	@Override
	public ServiceDescriptor getCapabilities() {
		return getSosWrapper().getServiceDescriptor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.i3mainz.springframework.ows.n52.sos.core.
	 * SensorObservationServiceOperations#isSOSAvailable()
	 */
	@Override
	public boolean isSOSAvailable() {
		return getSosWrapper().getServiceDescriptor() != null;
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
		if (getSosWrapper().getServiceDescriptor() == null) {
			LOG.error(String.format(
					"Service descriptor not available for SOS '%s'",
					getConnectionParameter().getUrl()));
			return false;
		}
		final OperationsMetadata opMeta = getSosWrapper()
				.getServiceDescriptor().getOperationsMetadata();
		LOG.debug(String.format("OperationsMetadata found: %s", opMeta));
		// check for RegisterSensor and InsertObservationOperation
		// TODO implement version specific
		if ((opMeta.getOperationByName(SOSAdapter.REGISTER_SENSOR) != null || opMeta
				.getOperationByName(SOSAdapter.INSERT_SENSOR) != null)
				&& opMeta.getOperationByName(SOSAdapter.INSERT_OBSERVATION) != null) {
			LOG.debug(String.format(
					"Found all required operations: (%s|%s), %s",
					SOSAdapter.REGISTER_SENSOR, SOSAdapter.INSERT_SENSOR,
					SOSAdapter.INSERT_OBSERVATION));
			return true;
		}
		return false;
	}

	@Override
	public <T> String registerSensor(T sensorDescription, Collection<T> mapper) {
		// TODO Not implemented yet
		return null;
	}

	@Override
	public String registerSensor(Sensor sensor) {
		String responserequest = "";
		String test_xml = "";
		Sos_sensor sensor_reg = new Sos_sensor();

		sensor_reg.set_sensor(sensor.getId(), sensor.getOffering().getId(),
				sensor.getOffering().getName());
		test_xml = Sos_xml_doc.register_sensor(sensor_reg);
		responserequest = Http_connect.excutePost(getConnectionParameter()
				.getUrl(), Sos_xml_doc.register_sensor(sensor_reg));

		// responserequest = test_xml;
		/*
		 * Antwort parsen!!! <?xml version="1.0" encoding="UTF-8"?>
		 * <sos:RegisterSensorResponse
		 * xmlns:sos="http://www.opengis.net/sos/1.0"
		 * xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		 * xsi:schemaLocation=
		 * "http://www.opengis.net/sos/1.0 http://schemas.opengis.net/sos/1.0.0/sosAll.xsd"
		 * > <sos:AssignedSensorId>Twitter_Sens</sos:AssignedSensorId>
		 * </sos:RegisterSensorResponse>
		 */

		return responserequest;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.i3mainz.springframework.ows.n52.sos.core.
	 * SensorObservationServiceOperations
	 */
	public String registerSensor_1(final Sensor rs) {
		try {
			if (getConnectionParameter().getVersion().equals("1.0.0")) {
				LOG.trace("registerSensor()");
				final RegisterSensorParameters regSensorParameter = createRegisterSensorParametersFromRS(rs);
				final OperationResult opResult = getSosWrapper()
						.doRegisterSensor(regSensorParameter);
				final RegisterSensorResponseDocument response = RegisterSensorResponseDocument.Factory
						.parse(opResult.getIncomingResultAsStream());
				LOG.debug("RegisterSensorResponse parsed");
				return response.getRegisterSensorResponse()
						.getAssignedSensorId();
			} else if (getConnectionParameter().getVersion().equals("2.0.0")) {
				LOG.trace("insertSensor()");
				final InsertSensorParameters insSensorParams = createInsertSensorParametersFromRS(rs);
				if (getSosBinding() != null) {
					insSensorParams.addParameterValue(
							ISOSRequestBuilder.BINDING, getSosBinding().name());
				}
				final OperationResult opResult = getSosWrapper()
						.doInsertSensor(insSensorParams);
				final InsertSensorResponseDocument response = InsertSensorResponseDocument.Factory
						.parse(opResult.getIncomingResultAsStream());
				LOG.debug("InsertSensorResponse parsed");
				getOfferings().put(
						response.getInsertSensorResponse()
								.getAssignedProcedure(),
						response.getInsertSensorResponse()
								.getAssignedOffering());
				return response.getInsertSensorResponse()
						.getAssignedProcedure();
			}
		} catch (final ExceptionReport e) {
			// Handle already registered sensor case here (happens when the
			// sensor is registered but not listed in the capabilities):
			final Iterator<OWSException> iter = e.getExceptionsIterator();
			while (iter.hasNext()) {
				final OWSException owsEx = iter.next();
				if (owsEx.getExceptionCode().equals(
						OwsExceptionCode.NoApplicableCode.name())
						&& owsEx.getExceptionTexts() != null
						&& owsEx.getExceptionTexts().length > 0) {
//					for (final String string : owsEx.getExceptionTexts()) {
//						if (string
//								.indexOf(Configuration.SOS_SENSOR_ALREADY_REGISTERED_MESSAGE_START) > -1
//								&& string
//										.indexOf(Configuration.SOS_SENSOR_ALREADY_REGISTERED_MESSAGE_END) > -1) {
//							return rs.getId();
//						}
//					}
				}
				// handle offering already contained case here
				else if (owsEx.getExceptionCode().equals(
						OwsExceptionCode.InvalidParameterValue.name())
						&& owsEx.getLocator().equals("offeringIdentifier")
						&& owsEx.getExceptionTexts() != null
						&& owsEx.getExceptionTexts().length > 0) {
//					for (final String string : owsEx.getExceptionTexts()) {
//						if (string
//								.indexOf(Configuration.SOS_200_OFFERING_ALREADY_REGISTERED_MESSAGE_START) > -1
//								&& string
//										.indexOf(Configuration.SOS_200_OFFERING_ALREADY_REGISTERED_MESSAGE_END) > -1) {
//							getOfferings().put(rs.getId(),
//									rs.getOffering().getId());
//							return rs.getId();
//						}
//					}
				}

			}
			LOG.error(String.format("Exception thrown: %s", e.getMessage()), e);
		} catch (final OXFException e) {
			// TODO Auto-generated catch block generated on 21.06.2012 around
			// 14:53:40
			LOG.error(String.format("Exception thrown: %s", e.getMessage()), e);
		} catch (final XmlException e) {
			// TODO Auto-generated catch block generated on 21.06.2012 around
			// 14:53:54
			LOG.error(String.format("Exception thrown: %s", e.getMessage()), e);
		} catch (final IOException e) {
			// TODO Auto-generated catch block generated on 21.06.2012 around
			// 14:53:54
			LOG.error(String.format("Exception thrown: %s", e.getMessage()), e);
		}
		return null;
	}

	@Override
	public String insertObservation(String sensorId, FeatureOfInterest foi, Observation observation) {

		Sos_observation ins_obs = new Sos_observation();
		String xml_doc_ins_obs = "";
		ins_obs.set_obs(sensorId,observation.getTimeAsSTring(), foi.getId(), foi.getName(),
				foi.getPosition(), observation.getValue().toString());
		xml_doc_ins_obs = Sos_xml_doc.insert_observation(ins_obs);
		LOG.info(xml_doc_ins_obs);
		String responserequest;
		responserequest = Http_connect.excutePost(getConnectionParameter()
				.getUrl(), xml_doc_ins_obs);

		return responserequest;

	}

//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see de.i3mainz.springframework.ows.n52.sos.core.
//	 * SensorObservationServiceOperations
//	 * #insertObservation(org.n52.sos.importer.
//	 * feeder.model.requests.InsertObservation)
//	 */
//	@Override
//	public String insertObservation(final InsertObservation io)
//			throws IOException {
//		LOG.trace("insertObservation()");
//		OperationResult opResult = null;
//		org.n52.oxf.sos.request.InsertObservationParameters parameters = null;
//
//		try {
//			parameters = createParameterAssemblyFromIO(io);
//			try {
//				LOG.debug("\n\nBEFORE OXF - doOperation \"InsertObservation\"\n\n");
//				opResult = getSosWrapper().doInsertObservation(parameters);
//				LOG.debug("\n\nAFTER OXF - doOperation \"InsertObservation\"\n\n");
//				if (getConnectionParameter().getVersion().equals("1.0.0")) {
//					try {
//						final InsertObservationResponse response = InsertObservationResponseDocument.Factory
//								.parse(opResult.getIncomingResultAsStream())
//								.getInsertObservationResponse();
//						LOG.debug(String
//								.format("Observation inserted succesfully. Returned id: %s",
//										response.getAssignedObservationId()));
//						return response.getAssignedObservationId();
//					} catch (final XmlException e) {
//						// TODO Auto-generated catch block generated on
//						// 20.06.2012 around 10:43:01
//						LOG.error(
//								String.format("Exception thrown: %s",
//										e.getMessage()), e);
//					} catch (final IOException e) {
//						// TODO Auto-generated catch block generated on
//						// 20.06.2012 around 10:43:01
//						LOG.error(
//								String.format("Exception thrown: %s",
//										e.getMessage()), e);
//					}
//				} else if (getConnectionParameter().getVersion()
//						.equals("2.0.0")) {
//					try {
//						net.opengis.sos.x20.InsertObservationResponseDocument.Factory
//								.parse(opResult.getIncomingResultAsStream())
//								.getInsertObservationResponse();
//						LOG.debug("Observation inserted successfully.");
//						return "SOS 2.0 InsertObservation doesn't return the assigned id";
//					} catch (final XmlException e) {
//						LOG.error("Exception thrown: {}", e.getMessage(), e);
//					}
//				}
//			} catch (final ExceptionReport e) {
//				final Iterator<OWSException> iter = e.getExceptionsIterator();
//				StringBuffer buf = new StringBuffer();
//				while (iter.hasNext()) {
//					final OWSException owsEx = iter.next();
//					// check for observation already contained exception
//					if (owsEx
//							.getExceptionCode()
//							.equals(Configuration.SOS_EXCEPTION_CODE_NO_APPLICABLE_CODE)
//							&& owsEx.getExceptionTexts().length > 0
//							&& (owsEx.getExceptionTexts()[0]
//									.indexOf(Configuration.SOS_EXCEPTION_OBSERVATION_DUPLICATE_CONSTRAINT) > -1
//									|| owsEx.getExceptionTexts()[0]
//											.indexOf(Configuration.SOS_EXCEPTION_OBSERVATION_ALREADY_CONTAINED) > -1 || owsEx
//									.getExceptionTexts()[0]
//									.indexOf(Configuration.SOS_200_DUPLICATE_OBSERVATION_CONSTRAINT) > -1)) {
//						return Configuration.SOS_OBSERVATION_ALREADY_CONTAINED;
//					}
//					buf = buf.append(String.format(
//							"ExceptionCode: '%s' because of '%s'\n",
//							owsEx.getExceptionCode(),
//							Arrays.toString(owsEx.getExceptionTexts())));
//				}
//				LOG.error(String.format("Exception thrown: %s\n%s",
//						e.getMessage(), buf.toString()));
//				LOG.debug(e.getMessage(), e);
//			}
//
//		} catch (final OXFException e) {
//			LOG.error(
//					String.format("Problem with OXF. Exception thrown: %s",
//							e.getMessage()), e);
//		}
//		return null;
//	}

}
