package de.i3mainz.springframework.swe.n52.sos.core;

//import static org.n52.sos.importer.feeder.Configuration.SOS_OBSERVATION_TYPE_BOOLEAN;
//import static org.n52.sos.importer.feeder.Configuration.SOS_OBSERVATION_TYPE_COUNT;
//import static org.n52.sos.importer.feeder.Configuration.SOS_OBSERVATION_TYPE_TEXT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.xmlbeans.XmlException;
import org.n52.oxf.OXFException;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.sos.adapter.ISOSRequestBuilder;
import org.n52.oxf.sos.adapter.ISOSRequestBuilder.Binding;
import org.n52.oxf.sos.adapter.wrapper.SOSWrapper;
import org.n52.oxf.sos.adapter.wrapper.SosWrapperFactory;
import org.n52.oxf.sos.adapter.wrapper.builder.ObservationTemplateBuilder;
import org.n52.oxf.sos.observation.ObservationParameters;
import org.n52.oxf.sos.observation.TextObservationParameters;
import org.n52.oxf.sos.request.v100.RegisterSensorParameters;
import org.n52.oxf.sos.request.v200.InsertSensorParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.i3mainz.springframework.ogc.OWSConnectionParameter;
import de.i3mainz.springframework.ogc.core.OWSAccessor;
import de.i3mainz.springframework.swe.n52.sos.model.FeatureOfInterest;
import de.i3mainz.springframework.swe.n52.sos.model.Observation;
import de.i3mainz.springframework.swe.n52.sos.model.Sensor;
import de.i3mainz.springframework.swe.n52.sos.util.Configuration;
import de.i3mainz.springframework.swe.n52.sos.util.DescriptionBuilder;

public abstract class SOSAccessor extends OWSAccessor {

	protected static final Logger LOG = LoggerFactory
			.getLogger(SOSTemplate.class);
	private static final String SML_101_FORMAT_URI = "http://www.opengis.net/sensorML/1.0.1";
	private static final String OM_200_SAMPLING_FEATURE = "http://www.opengis.net/def/samplingFeatureType/OGC-OM/2.0/SF_SamplingPoint";

	private SOSWrapper sosWrapper;
	private Binding sosBinding;
	private Map<String, String> offerings;
	private DescriptionBuilder sensorDescBuilder;

	@Override
	public void setConnectionParameter(
			OWSConnectionParameter owsConnectionParameter) {
		super.setConnectionParameter(owsConnectionParameter);
		this.sosBinding = ((de.i3mainz.springframework.swe.n52.sos.SOSConnectionParameter) getConnectionParameter())
				.getBinding();
		// sensorDescBuilder = new DescriptionBuilder();
		try {
			this.sosWrapper = SosWrapperFactory.newInstance(
					owsConnectionParameter.getUrl(),
					owsConnectionParameter.getVersion(), this.sosBinding);

			if (getConnectionParameter().getVersion().equals("2.0.0")) {
				offerings = new HashMap<String, String>();
			}
		} catch (ExceptionReport e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (OXFException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * @return the sosWrapper
	 */
	protected final SOSWrapper getSosWrapper() {
		return sosWrapper;
	}

	/**
	 * @return the sosBinding
	 */
	protected final Binding getSosBinding() {
		return sosBinding;
	}

	protected final Map<String, String> getOfferings() {
		return offerings;
	}

	// /**
	// * @return the sensorDescBuilder
	// */
	// protected final DescriptionBuilder getSensorDescBuilder() {
	// return sensorDescBuilder;
	// }

	private String getURIForObservationType(final String measuredValueType) {
		if (measuredValueType.equals("NUMERIC")) {
			return "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement";
		}
		if (measuredValueType.equals("COUNT")) {
			return "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_CountObservation";
		}
		if (measuredValueType.equals("BOOLEAN")) {
			return "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_TruthObservation";
		}
		if (measuredValueType.equals("TEXT")) {
			return "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_TextObservation";
		}
		final String errorMsg = String.format(
				"Observation type '%s' not supported!", measuredValueType);
		LOG.error(errorMsg);
		throw new IllegalArgumentException(errorMsg);
	}

	protected RegisterSensorParameters createRegisterSensorParametersFromRS(
			final Sensor registerSensor) throws OXFException, XmlException,
			IOException {
		LOG.trace("createParameterContainterFromRS()");
		ObservationTemplateBuilder observationTemplate;
		observationTemplate = ObservationTemplateBuilder
				.createObservationTemplateBuilderForTypeText();
		observationTemplate.setDefaultValue(" ");

		return new RegisterSensorParameters(
				this.sensorDescBuilder.createSML(registerSensor),
				observationTemplate.generateObservationTemplate());
	}

	protected InsertSensorParameters createInsertSensorParametersFromRS(
			final Sensor rs) throws XmlException, IOException {
		return new InsertSensorParameters(this.sensorDescBuilder.createSML(rs),
				SML_101_FORMAT_URI, getObservedPropertyURIs(null),
				Collections.singleton(OM_200_SAMPLING_FEATURE),
				getObservationTypeURIs(rs));
	}

	private Collection<String> getObservedPropertyURIs(
			final Collection<?> observedProperties) {
		if (observedProperties == null || observedProperties.size() < 1) {
			final Collection<String> result = new ArrayList<String>();
			result.add("urn:ogc:def:dataType:OGC:1.1:string");
			return result;
		}
		final Collection<String> result = new ArrayList<String>(
				observedProperties.size());
		// for (final ObservedProperty observedProperty : observedProperties) {
		// result.add(observedProperty.getUri());
		// }
		return result;
	}

	private Collection<String> getObservationTypeURIs(final Sensor rs) {
		if (rs == null) {
			final Set<String> tmp = new HashSet<String>();
			tmp.add(getURIForObservationType("TEXT"));
			return tmp;
		}
		final Set<String> tmp = new HashSet<String>();
		// for (final ObservedProperty obsProp : rs.getObservedProperties()) {
		// final String measuredValueType = rs.getMeasuredValueType(obsProp);
		// if (measuredValueType != null) {
		// tmp.add(getURIForObservationType(measuredValueType));
		// }
		// }
		return tmp;
	}

	protected org.n52.oxf.sos.request.InsertObservationParameters createParameterAssemblyFromIO(
			final String sensorID, final FeatureOfInterest foi,
			final Observation observation) throws OXFException {

		LOG.trace("createParameterBuilderFromIO()");
		ObservationParameters obsParameter = null;

		// if (io.getMeasuredValueType().equals(
		// Configuration.SOS_OBSERVATION_TYPE_TEXT)) {
		// set text
		obsParameter = new TextObservationParameters();
		((TextObservationParameters) obsParameter)
				.addObservationValue(observation.getValue().toString());
		// } else if (io.getMeasuredValueType().equals(
		// Configuration.SOS_OBSERVATION_TYPE_COUNT)) {
		// // set count
		// obsParameter = new CountObservationParameters();
		// ((CountObservationParameters) obsParameter)
		// .addObservationValue((Integer) io.getResultValue());
		// } else if (io.getMeasuredValueType().equals(
		// Configuration.SOS_OBSERVATION_TYPE_BOOLEAN)) {
		// // set boolean
		// obsParameter = new BooleanObservationParameters();
		// ((BooleanObservationParameters) obsParameter)
		// .addObservationValue((Boolean) io.getResultValue());
		// } else {
		// // set default value type
		// obsParameter = new MeasurementObservationParameters();
		// ((MeasurementObservationParameters) obsParameter).addUom(io
		// .getUnitOfMeasurementCode());
		// ((MeasurementObservationParameters) obsParameter)
		// .addObservationValue(io.getResultValue().toString());
		// }
		//obsParameter.addObservedProperty("urn:ogc:def:dataType:OGC:1.1:string");
		obsParameter.addNewFoiId(foi.getId());
		obsParameter.addNewFoiName(foi.getName());
		obsParameter.addFoiDescription(foi.getId());
		// position
//		boolean eastingFirst = false;
//		if (Configuration.EPSG_EASTING_FIRST_MAP.get(io.getEpsgCode()) == null) {
//			Configuration.EPSG_EASTING_FIRST_MAP.get("default");
//		} else {
//			eastingFirst = Configuration.EPSG_EASTING_FIRST_MAP.get(io
//					.getEpsgCode());
//		}
//		String pos = eastingFirst ? String.format("%s %s",
//				io.getLongitudeValue(), io.getLatitudeValue()) : String.format(
//				"%s %s", io.getLatitudeValue(), io.getLongitudeValue());
//		if (io.isSetAltitudeValue()) {
//			pos = String.format("% %", pos, io.getAltitudeValue());
//		}
		
		String pos = foi.getPosition();
		obsParameter.addFoiPosition(pos);
		//obsParameter.addObservedProperty("urn:ogc:def:dataType:OGC:1.1:string");
		obsParameter.addProcedure(sensorID);

		if (getConnectionParameter().getVersion().equalsIgnoreCase("2.0.0")) {
			obsParameter.addSrsPosition(Configuration.SOS_200_EPSG_CODE_PREFIX+"4326");
			obsParameter.addPhenomenonTime(observation.getTimeAsSTring());
			obsParameter.addResultTime(observation.getTimeAsSTring());
			return new org.n52.oxf.sos.request.v200.InsertObservationParameters(
					obsParameter, Collections.singletonList(sensorID));
		}
		
//		obsParameter.addSrsPosition(Configuration.SOS_100_EPSG_CODE_PREFIX+"4326");
		//obsParameter.addParameterValue("srsName", Configuration.SOS_100_EPSG_CODE_PREFIX+"4326");
		//obsParameter.addSamplingTime(observation.getTimeAsSTring());
		obsParameter.addParameterValue(ISOSRequestBuilder.INSERT_OBSERVATION_SAMPLING_TIME,observation.getTimeAsSTring());
		return new org.n52.oxf.sos.request.v100.InsertObservationParameters(
				obsParameter);
	}

	public void afterPropertiesSet() throws Exception {
		if (getConnectionParameter().getVersion().equals("2.0.0")) {
			sensorDescBuilder = new DescriptionBuilder(false);
		} else {
			sensorDescBuilder = new DescriptionBuilder();
		}
	}

}
