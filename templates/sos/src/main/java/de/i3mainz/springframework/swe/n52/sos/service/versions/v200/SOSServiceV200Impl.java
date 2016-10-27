package de.i3mainz.springframework.swe.n52.sos.service.versions.v200;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.EXTENSION;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_CATEGORY_OBSERVATION_RESULT_CODESPACE;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_FOI_ID_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_DESC;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_ID_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_NAME;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_POSITION;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_POSITION_SRS;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_OFFERINGS_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_PHENOMENON_TIME;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_POSITION_SRS;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_PROCEDURE_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_RESULT_TIME;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_TYPE;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_VALUE_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.REGISTER_SENSOR_FEATURE_TYPE_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.REGISTER_SENSOR_ML_DOC_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.REGISTER_SENSOR_OBSERVATION_TYPE;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.REGISTER_SENSOR_OBSERVED_PROPERTY_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.REGISTER_SENSOR_PROCEDURE_DESCRIPTION_FORMAT_PARAMETER;
import static org.n52.oxf.sos.adapter.SOSAdapter.INSERT_SENSOR;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.xmlbeans.XmlException;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.capabilities.Operation;
import org.n52.oxf.ows.capabilities.OperationsMetadata;
import org.n52.oxf.sos.adapter.ISOSRequestBuilder;
import org.n52.oxf.sos.adapter.SOSAdapter;
import org.n52.oxf.sos.observation.ObservationParameters;
import org.n52.oxf.sos.request.InsertObservationParameters;
import org.n52.oxf.sos.request.v100.RegisterSensorParameters;
import org.n52.oxf.sos.request.v200.InsertSensorParameters;
import org.slf4j.Logger;

import de.i3mainz.springframework.swe.n52.sos.SOSConnectionParameter;
import de.i3mainz.springframework.swe.n52.sos.exceptions.GetFOIServiceException;
import de.i3mainz.springframework.swe.n52.sos.exceptions.GetObservationException;
import de.i3mainz.springframework.swe.n52.sos.model.TemporalRequestFilter;
import de.i3mainz.springframework.swe.n52.sos.model.requests.InsertObservation;
import de.i3mainz.springframework.swe.n52.sos.model.requests.RegisterSensor;
import de.i3mainz.springframework.swe.n52.sos.service.SOSServiceImpl;
import de.i3mainz.springframework.swe.n52.sos.util.Configuration;
import de.i3mainz.springframework.swe.n52.sos.util.DescriptionBuilder;
import net.opengis.swes.x20.InsertSensorResponseDocument;

public class SOSServiceV200Impl extends SOSServiceImpl implements SOSServiceV200 {

    private static final Logger LOG = getLog();
    private static final String SML_101_FORMAT_URI = "http://www.opengis.net/sensorML/1.0.1";
    private static final String OM_200_SAMPLING_FEATURE = "http://www.opengis.net/def/samplingFeatureType/OGC-OM/2.0/SF_SamplingPoint";
    private Map<String, String> offerings;

    public SOSServiceV200Impl(SOSConnectionParameter connectionParameter) {
        super(connectionParameter);
        offerings = new HashMap<>();
        this.sensorDescBuilder = new DescriptionBuilder(false);
    }

    @Override
    public OperationResult getFeatureOfInterest(String foiID) throws GetFOIServiceException {
        throw new GetFOIServiceException(new OXFException("Version 2.0.0 is not supported yet!"));
    }

    @Override
    public OperationResult getObservation(String offering, List<String> sensor, List<String> observedProperties,
            String srsName, TemporalRequestFilter tempFilter) throws GetObservationException {
        throw new GetObservationException(new OXFException("Version 2.0.0 is not supported yet!"));
    }

    @Override
    public String insertSensor(RegisterSensor sensor) throws ExceptionReport {
        LOG.trace("insertSensor()");
        InsertSensorParameters insSensorParams;
        try {
            insSensorParams = createInsertSensorParametersFromRS(sensor);
            if (getSosBinding() != null) {
                insSensorParams.addParameterValue(ISOSRequestBuilder.BINDING, getSosBinding().name());
            }
            setMimetype(insSensorParams);
            final OperationResult opResult = doInsertSensor(insSensorParams);
            final InsertSensorResponseDocument response = InsertSensorResponseDocument.Factory
                    .parse(opResult.getIncomingResultAsAutoCloseStream());
            LOG.debug("InsertSensorResponse parsed");
            this.offerings.put(response.getInsertSensorResponse().getAssignedProcedure(),
                    response.getInsertSensorResponse().getAssignedOffering());
            return response.getInsertSensorResponse().getAssignedProcedure();
        } catch (XmlException e) {
            LOG.error("XMLExcepion", e);
        } catch (IOException e) {
            LOG.error("IOException", e);
        } catch (OXFException e) {
            LOG.error("OXFException", e);
        }

        return null;

    }

    /**
     * @throws OXFException
     * @throws ExceptionReport
     * @see {@link #doRegisterSensor(RegisterSensorParameters)}
     */
    private OperationResult doInsertSensor(final InsertSensorParameters insertSensorParameters)
            throws OXFException, ExceptionReport {
        final SOSAdapter adapter = new SOSAdapter(getServiceDescriptor().getVersion());
        if (checkOperationAvailability(INSERT_SENSOR)) {
            final Operation operation = getServiceDescriptor().getOperationsMetadata()
                    .getOperationByName(INSERT_SENSOR);
            final ParameterContainer parameterContainer = getParameterContainer(insertSensorParameters);
            return adapter.doOperation(operation, parameterContainer, getConnectionTimeout(), getReadTimeout());
        } else {
            throw new OXFException(operationNotSupportedMessage(INSERT_SENSOR));
        }
    }

    private ParameterContainer getParameterContainer(final InsertSensorParameters insertSensorParameters)
            throws OXFException {
        final ParameterContainer paramContainer = createParameterContainerWithCommonServiceParameters();
        paramContainer.addParameterShell(REGISTER_SENSOR_ML_DOC_PARAMETER,
                insertSensorParameters.getSingleValue(InsertSensorParameters.PROCEDURE_DESCRIPTION));
        paramContainer.addParameterShell(REGISTER_SENSOR_PROCEDURE_DESCRIPTION_FORMAT_PARAMETER,
                insertSensorParameters.getSingleValue(InsertSensorParameters.PROCEDURE_DESCRIPTION_FORMAT));
        paramContainer.addParameterShell(REGISTER_SENSOR_OBSERVED_PROPERTY_PARAMETER,
                insertSensorParameters.getAllValues(InsertSensorParameters.OBSERVABLE_PROPERTIES)
                        .toArray(new String[insertSensorParameters
                                .getAllValues(InsertSensorParameters.OBSERVABLE_PROPERTIES).size()]));
        paramContainer.addParameterShell(REGISTER_SENSOR_OBSERVATION_TYPE,
                insertSensorParameters.getAllValues(InsertSensorParameters.OBSERVATION_TYPES)
                        .toArray(new String[insertSensorParameters
                                .getAllValues(InsertSensorParameters.OBSERVATION_TYPES).size()]));
        paramContainer.addParameterShell(REGISTER_SENSOR_FEATURE_TYPE_PARAMETER,
                insertSensorParameters.getAllValues(InsertSensorParameters.FEATURE_OF_INTEREST_TYPES)
                        .toArray(new String[insertSensorParameters
                                .getAllValues(InsertSensorParameters.FEATURE_OF_INTEREST_TYPES).size()]));
        processOptionalMimetypeParameter(insertSensorParameters, paramContainer);
        return paramContainer;
    }

    /* (non-Javadoc)
     * @see de.i3mainz.springframework.swe.n52.sos.service.SOSServiceImpl#callInsertObservationRequest(org.n52.oxf.sos.request.InsertObservationParameters)
     */
    @Override
    protected String callInsertObservationRequest(InsertObservationParameters parameters)
            throws OXFException, ExceptionReport {
        OperationResult opResult;
        LOG.debug("\tBEFORE OXF - doOperation 'InsertObservation'");
        opResult = doInsertObservation(parameters);
        LOG.debug("\tAFTER OXF - doOperation 'InsertObservation'");
        try {

            net.opengis.sos.x20.InsertObservationResponseDocument.Factory
                    .parse(opResult.getIncomingResultAsAutoCloseStream()).getInsertObservationResponse();
            LOG.debug("Observation inserted successfully.");
            return "SOS 2.0 InsertObservation doesn't return the assigned id";
        } catch (final XmlException e) {
            LOG.error("Exception thrown: {}", e.getMessage(), e);
        } catch (IOException e) {
            LOG.error(String.format("Exception thrown: %s", e.getMessage()), e);
        }
        return null;
    }

    private InsertSensorParameters createInsertSensorParametersFromRS(final RegisterSensor sensor) throws OXFException {
        return new InsertSensorParameters(this.sensorDescBuilder.createSML(sensor), SML_101_FORMAT_URI,
                getObservedPropertyURIs(null), Collections.singleton(OM_200_SAMPLING_FEATURE),
                getObservationTypeURIs(sensor));
    }

    private static String getURIForObservationType(final String measuredValueType) {
        if ("NUMERIC".equals(measuredValueType)) {
            return "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement";
        }
        if ("COUNT".equals(measuredValueType)) {
            return "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_CountObservation";
        }
        if ("BOOLEAN".equals(measuredValueType)) {
            return "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_TruthObservation";
        }
        if ("TEXT".equals(measuredValueType)) {
            return "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_TextObservation";
        }
        final String errorMsg = String.format("Observation type '%s' not supported!", measuredValueType);
        LOG.error(errorMsg);
        throw new IllegalArgumentException(errorMsg);
    }

    private Collection<String> getObservedPropertyURIs(final Collection<?> observedProperties) {
        if (observedProperties == null || observedProperties.isEmpty()) {
            final Collection<String> result = new ArrayList<>();
            result.add("urn:ogc:def:dataType:OGC:1.1:string");
            return result;
        }
        final Collection<String> result = new ArrayList<>(observedProperties.size());
        // for (final ObservedProperty observedProperty : observedProperties) {
        // result.add(observedProperty.getUri());
        // }
        return result;
    }

    private Collection<String> getObservationTypeURIs(final RegisterSensor sensor) {
        if (sensor == null) {
            final Set<String> tmp = new HashSet<>();
            tmp.add(getURIForObservationType("TEXT"));
            return tmp;
        }
        final Set<String> tmp = new HashSet<>();
        // for (final ObservedProperty obsProp : rs.getObservedProperties()) {
        // final String measuredValueType = rs.getMeasuredValueType(obsProp);
        // if (measuredValueType != null) {
        // tmp.add(getURIForObservationType(measuredValueType));
        // }
        // }
        tmp.add(getURIForObservationType("TEXT"));
        return tmp;
    }

    @Override
    public Map<String, String> getOfferings() {
        return this.offerings;
    }

    @Override
    protected InsertObservationParameters createParameterAssemblyFromIO(InsertObservation io) throws OXFException {
        return new org.n52.oxf.sos.request.v200.InsertObservationParameters(this.createInsertObservationParameters(io),
                Collections.singletonList(io.getOffering().getUri()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.i3mainz.springframework.swe.n52.sos.service.SOSServiceImpl#
     * createInsertObservationParameters(de.i3mainz.springframework.swe.n52.sos.
     * model.requests.InsertObservation)
     */
    @Override
    protected ObservationParameters createInsertObservationParameters(InsertObservation io) throws OXFException {
        ObservationParameters obsParameter = super.createInsertObservationParameters(io);
        obsParameter.addSrsPosition(Configuration.SOS_200_EPSG_CODE_PREFIX + io.getEpsgCode());
        obsParameter.addPhenomenonTime(io.getTimeStamp().toString());
        obsParameter.addResultTime(io.getTimeStamp().toString());
        return obsParameter;
    }

    @Override
    public boolean isTransactional() {
        final OperationsMetadata opMeta = getCapabilities().getOperationsMetadata();
        LOG.debug(String.format("OperationsMetadata found: %s", opMeta));
        if (opMeta.getOperationByName(SOSAdapter.INSERT_SENSOR) != null
                && opMeta.getOperationByName(SOSAdapter.INSERT_OBSERVATION) != null) {
            LOG.debug(String.format("Found all required operations: %s, %s", SOSAdapter.INSERT_SENSOR,
                    SOSAdapter.INSERT_OBSERVATION));
            return true;
        }
        return false;
    }

    @Override
    protected void addSosValues(InsertObservationParameters param, ParameterContainer parameterContainer)
            throws OXFException {

        final org.n52.oxf.sos.request.v200.InsertObservationParameters parameters = (org.n52.oxf.sos.request.v200.InsertObservationParameters) param;

        parameterContainer.addParameterShell(INSERT_OBSERVATION_TYPE,
                parameters.getSingleValue(INSERT_OBSERVATION_TYPE));
        parameterContainer.addParameterShell(INSERT_OBSERVATION_PROCEDURE_PARAMETER,
                parameters.getSingleValue(INSERT_OBSERVATION_PROCEDURE_PARAMETER));
        parameterContainer.addParameterShell(INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER,
                parameters.getSingleValue(INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER));
        if (parameters.contains(INSERT_OBSERVATION_NEW_FOI_ID_PARAMETER)) {
            parameterContainer.addParameterShell(INSERT_OBSERVATION_NEW_FOI_NAME,
                    parameters.getSingleValue(INSERT_OBSERVATION_NEW_FOI_NAME));
            parameterContainer.addParameterShell(INSERT_OBSERVATION_NEW_FOI_ID_PARAMETER,
                    parameters.getSingleValue(INSERT_OBSERVATION_NEW_FOI_ID_PARAMETER));
            parameterContainer.addParameterShell(INSERT_OBSERVATION_NEW_FOI_DESC,
                    parameters.getSingleValue(INSERT_OBSERVATION_NEW_FOI_DESC));
            parameterContainer.addParameterShell(INSERT_OBSERVATION_NEW_FOI_POSITION,
                    parameters.getSingleValue(INSERT_OBSERVATION_NEW_FOI_POSITION));
            parameterContainer.addParameterShell(INSERT_OBSERVATION_NEW_FOI_POSITION_SRS,
                    parameters.getSingleValue(INSERT_OBSERVATION_NEW_FOI_POSITION_SRS));
        } else {
            parameterContainer.addParameterShell(INSERT_OBSERVATION_FOI_ID_PARAMETER,
                    parameters.getSingleValue(INSERT_OBSERVATION_FOI_ID_PARAMETER));
        }
        if (parameters.contains(INSERT_OBSERVATION_POSITION_SRS)) {
            parameterContainer.addParameterShell(INSERT_OBSERVATION_POSITION_SRS,
                    parameters.getSingleValue(INSERT_OBSERVATION_POSITION_SRS));
        }
        parameterContainer.addParameterShell(INSERT_OBSERVATION_RESULT_TIME,
                parameters.getSingleValue(INSERT_OBSERVATION_RESULT_TIME));
        parameterContainer.addParameterShell(INSERT_OBSERVATION_PHENOMENON_TIME,
                parameters.getSingleValue(INSERT_OBSERVATION_PHENOMENON_TIME));
        parameterContainer.addParameterShell(INSERT_OBSERVATION_OFFERINGS_PARAMETER,
                parameters.getAllValues(INSERT_OBSERVATION_OFFERINGS_PARAMETER)
                        .toArray(new String[parameters.getAllValues(INSERT_OBSERVATION_OFFERINGS_PARAMETER).size()]));
        if (parameters.contains(INSERT_OBSERVATION_CATEGORY_OBSERVATION_RESULT_CODESPACE)) {
            parameterContainer.addParameterShell(INSERT_OBSERVATION_CATEGORY_OBSERVATION_RESULT_CODESPACE,
                    parameters.getSingleValue(INSERT_OBSERVATION_CATEGORY_OBSERVATION_RESULT_CODESPACE));
        }
        if (parameters.contains(INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE)) {
            parameterContainer.addParameterShell(INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE,
                    parameters.getSingleValue(INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE));
        }
        if (parameters.contains(INSERT_OBSERVATION_VALUE_PARAMETER)) {
            parameterContainer.addParameterShell(INSERT_OBSERVATION_VALUE_PARAMETER,
                    parameters.getSingleValue(INSERT_OBSERVATION_VALUE_PARAMETER));
        }
        if (parameters.contains(EXTENSION)) {
            parameterContainer.addParameterShell(EXTENSION, parameters.getSingleValue(EXTENSION));
        }

    }

}