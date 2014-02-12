package de.i3mainz.springframework.swe.n52.sos.core;

//import static org.n52.sos.importer.feeder.Configuration.SOS_OBSERVATION_TYPE_BOOLEAN;
//import static org.n52.sos.importer.feeder.Configuration.SOS_OBSERVATION_TYPE_COUNT;
//import static org.n52.sos.importer.feeder.Configuration.SOS_OBSERVATION_TYPE_TEXT;

import java.util.HashMap;
import java.util.Map;

import org.n52.oxf.OXFException;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.sos.adapter.ISOSRequestBuilder.Binding;
import org.n52.oxf.sos.adapter.wrapper.SOSWrapper;
import org.n52.oxf.sos.adapter.wrapper.SosWrapperFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.i3mainz.springframework.ogc.OWSConnectionParameter;
import de.i3mainz.springframework.ogc.core.OWSAccessor;

public abstract class SOSAccessor extends OWSAccessor {

	protected static final Logger LOG = LoggerFactory
			.getLogger(SOSTemplate.class);
	private static final String SML_101_FORMAT_URI = "http://www.opengis.net/sensorML/1.0.1";
	private static final String OM_200_SAMPLING_FEATURE = "http://www.opengis.net/def/samplingFeatureType/OGC-OM/2.0/SF_SamplingPoint";

	private SOSWrapper sosWrapper;
	private Binding sosBinding;
	private Map<String, String> offerings;
//	private DescriptionBuilder sensorDescBuilder;

	@Override
	public void setConnectionParameter(
			OWSConnectionParameter owsConnectionParameter) {
		super.setConnectionParameter(owsConnectionParameter);
		this.sosBinding = ((de.i3mainz.springframework.swe.n52.sos.SOSConnectionParameter) getConnectionParameter())
				.getBinding();
//		sensorDescBuilder = new DescriptionBuilder();
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

//	/**
//	 * @return the sensorDescBuilder
//	 */
//	protected final DescriptionBuilder getSensorDescBuilder() {
//		return sensorDescBuilder;
//	}

//	protected Collection<String> getObservationTypeURIs(final RegisterSensor rs) {
//		if (rs == null || rs.getObservedProperties() == null
//				|| rs.getObservedProperties().size() < 1) {
//			return Collections.emptyList();
//		}
//		final Set<String> tmp = new HashSet<String>(rs.getObservedProperties()
//				.size());
//		for (final ObservedProperty obsProp : rs.getObservedProperties()) {
//			final String measuredValueType = rs.getMeasuredValueType(obsProp);
//			if (measuredValueType != null) {
//				tmp.add(getURIForObservationType(measuredValueType));
//			}
//		}
//		return tmp;
//	}

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

//	protected RegisterSensorParameters createRegisterSensorParametersFromRS(
//			final RegisterSensor registerSensor) throws OXFException,
//			XmlException, IOException {
//		LOG.trace("createParameterContainterFromRS()");
//
//		// create SensorML
//		// create template --> within the 52N 1.0.0 SOS implementation this
//		// template is somehow ignored --> take first observed property to get
//		// values for template
//		ObservationTemplateBuilder observationTemplate;
//		final ObservedProperty firstObservedProperty = registerSensor
//				.getObservedProperties().iterator().next();
//		if (registerSensor.getMeasuredValueType(firstObservedProperty).equals(
//				SOS_OBSERVATION_TYPE_TEXT)) {
//			observationTemplate = ObservationTemplateBuilder
//					.createObservationTemplateBuilderForTypeText();
//		} else if (registerSensor.getMeasuredValueType(firstObservedProperty)
//				.equals(SOS_OBSERVATION_TYPE_COUNT)) {
//			observationTemplate = ObservationTemplateBuilder
//					.createObservationTemplateBuilderForTypeCount();
//		} else if (registerSensor.getMeasuredValueType(firstObservedProperty)
//				.equals(SOS_OBSERVATION_TYPE_BOOLEAN)) {
//			observationTemplate = ObservationTemplateBuilder
//					.createObservationTemplateBuilderForTypeTruth();
//		} else {
//			observationTemplate = ObservationTemplateBuilder
//					.createObservationTemplateBuilderForTypeMeasurement(registerSensor
//							.getUnitOfMeasurementCode(firstObservedProperty));
//		}
//		observationTemplate.setDefaultValue(registerSensor.getDefaultValue());
//
//		return new RegisterSensorParameters(getSensorDescBuilder().createSML(
//				registerSensor),
//				observationTemplate.generateObservationTemplate());
//	}

//	protected InsertSensorParameters createInsertSensorParametersFromRS(
//			final RegisterSensor rs) throws XmlException, IOException {
//		return new InsertSensorParameters(getSensorDescBuilder().createSML(rs),
//				SML_101_FORMAT_URI,
//				getObservedPropertyURIs(rs.getObservedProperties()),
//				Collections.singleton(OM_200_SAMPLING_FEATURE),
//				getObservationTypeURIs(rs));
//	}

//	private Collection<String> getObservedPropertyURIs(
//			final Collection<ObservedProperty> observedProperties) {
//		if (observedProperties == null || observedProperties.size() < 1) {
//			return Collections.emptyList();
//		}
//		final Collection<String> result = new ArrayList<String>(
//				observedProperties.size());
//		for (final ObservedProperty observedProperty : observedProperties) {
//			result.add(observedProperty.getUri());
//		}
//		return result;
//	}

//	protected org.n52.oxf.sos.request.InsertObservationParameters createParameterAssemblyFromIO(
//			final InsertObservation io) throws OXFException {
//
//		LOG.trace("createParameterBuilderFromIO()");
//		ObservationParameters obsParameter = null;
//
//		if (io.getMeasuredValueType().equals(
//				Configuration.SOS_OBSERVATION_TYPE_TEXT)) {
//			// set text
//			obsParameter = new TextObservationParameters();
//			((TextObservationParameters) obsParameter).addObservationValue(io
//					.getResultValue().toString());
//		} else if (io.getMeasuredValueType().equals(
//				Configuration.SOS_OBSERVATION_TYPE_COUNT)) {
//			// set count
//			obsParameter = new CountObservationParameters();
//			((CountObservationParameters) obsParameter)
//					.addObservationValue((Integer) io.getResultValue());
//		} else if (io.getMeasuredValueType().equals(
//				Configuration.SOS_OBSERVATION_TYPE_BOOLEAN)) {
//			// set boolean
//			obsParameter = new BooleanObservationParameters();
//			((BooleanObservationParameters) obsParameter)
//					.addObservationValue((Boolean) io.getResultValue());
//		} else {
//			// set default value type
//			obsParameter = new MeasurementObservationParameters();
//			((MeasurementObservationParameters) obsParameter).addUom(io
//					.getUnitOfMeasurementCode());
//			((MeasurementObservationParameters) obsParameter)
//					.addObservationValue(io.getResultValue().toString());
//		}
//		obsParameter.addObservedProperty(io.getObservedPropertyURI());
//		obsParameter.addNewFoiId(io.getFeatureOfInterestURI());
//		obsParameter.addNewFoiName(io.getFeatureOfInterestName());
//		obsParameter.addFoiDescription(io.getFeatureOfInterestURI());
//		// position
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
//		obsParameter.addFoiPosition(pos);
//		obsParameter.addObservedProperty(io.getObservedPropertyURI());
//		obsParameter.addProcedure(io.getSensorURI());
//
//		if (getConnectionParameter().getVersion().equalsIgnoreCase("2.0.0")) {
//			obsParameter.addSrsPosition(Configuration.SOS_200_EPSG_CODE_PREFIX
//					+ io.getEpsgCode());
//			obsParameter.addPhenomenonTime(io.getTimeStamp());
//			obsParameter.addResultTime(io.getTimeStamp());
//			return new org.n52.oxf.sos.request.v200.InsertObservationParameters(
//					obsParameter, Collections.singletonList(io.getSensorURI()));
//		}
//
//		obsParameter.addSrsPosition(Configuration.SOS_100_EPSG_CODE_PREFIX
//				+ io.getEpsgCode());
//		obsParameter.addSamplingTime(io.getTimeStamp());
//		return new org.n52.oxf.sos.request.v100.InsertObservationParameters(
//				obsParameter);
//	}

	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}

}
