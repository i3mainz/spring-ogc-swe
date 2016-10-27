/**
 * 
 */
package de.i3mainz.springframework.swe.n52.sos.service.versions.v100;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_FOI_EVENT_TIME_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_FOI_ID_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_FOI_LOCATION_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_OBSERVATION_OFFERING_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_OBSERVATION_PROCEDURE_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_OBSERVATION_RESPONSE_MODE_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_OBSERVATION_RESULT_MODEL_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_OBSERVATION_RESULT_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_CATEGORY_OBSERVATION_RESULT_CODESPACE;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_FOI_ID_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_DESC;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_NAME;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_POSITION;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_POSITION_SRS;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_PROCEDURE_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_SAMPLING_TIME;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_TYPE;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_VALUE_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.REGISTER_SENSOR_DEFAULT_RESULT_VALUE;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.REGISTER_SENSOR_ML_DOC_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.REGISTER_SENSOR_OBSERVATION_TEMPLATE;
import static org.n52.oxf.sos.adapter.SOSAdapter.GET_FEATURE_OF_INTEREST;
import static org.n52.oxf.sos.adapter.SOSAdapter.GET_OBSERVATION;
import static org.n52.oxf.sos.adapter.SOSAdapter.REGISTER_SENSOR;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.xmlbeans.XmlException;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.adapter.ParameterShell;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.capabilities.Operation;
import org.n52.oxf.ows.capabilities.OperationsMetadata;
import org.n52.oxf.sos.adapter.ISOSRequestBuilder;
import org.n52.oxf.sos.adapter.SOSAdapter;
import org.n52.oxf.sos.observation.ObservationParameters;
import org.n52.oxf.sos.request.InsertObservationParameters;
import org.n52.oxf.sos.request.v100.RegisterSensorParameters;
import org.slf4j.Logger;

import de.i3mainz.springframework.swe.n52.sos.SOSConnectionParameter;
import de.i3mainz.springframework.swe.n52.sos.exceptions.GetFOIServiceException;
import de.i3mainz.springframework.swe.n52.sos.exceptions.GetObservationException;
import de.i3mainz.springframework.swe.n52.sos.model.TemporalRequestFilter;
import de.i3mainz.springframework.swe.n52.sos.model.requests.InsertObservation;
import de.i3mainz.springframework.swe.n52.sos.model.requests.RegisterSensor;
import de.i3mainz.springframework.swe.n52.sos.service.SOSServiceImpl;
import de.i3mainz.springframework.swe.n52.sos.service.builder.ObservationTemplateBuilder;
import de.i3mainz.springframework.swe.n52.sos.service.versions.v100.builder.GetFeatureOfInterestParameterBuilder_v100;
import de.i3mainz.springframework.swe.n52.sos.service.versions.v100.builder.GetObservationParameterBuilder_v100;
import de.i3mainz.springframework.swe.n52.sos.util.Configuration;
import de.i3mainz.springframework.swe.n52.sos.util.DescriptionBuilder;
import net.opengis.sos.x10.InsertObservationResponseDocument;
import net.opengis.sos.x10.InsertObservationResponseDocument.InsertObservationResponse;
import net.opengis.sos.x10.RegisterSensorResponseDocument;

/**
 * @author Nikolai Bock
 *
 */
public class SOSServiceV100Impl extends SOSServiceImpl implements SOSServiceV100 {

    private static final Logger LOG = getLog();

    public SOSServiceV100Impl(SOSConnectionParameter connectionParameter) {
        super(connectionParameter);
        this.sensorDescBuilder = new DescriptionBuilder();
    }

    @Override
    public OperationResult getFeatureOfInterest(String foiID) throws GetFOIServiceException {
        GetFeatureOfInterestParameterBuilder_v100 builder = new GetFeatureOfInterestParameterBuilder_v100(foiID,
                ISOSRequestBuilder.GET_FOI_ID_PARAMETER);
        try {
            return doGetFeatureOfInterest(builder);
        } catch (OXFException | ExceptionReport e) {
            throw new GetFOIServiceException(e);
        }
    }

    /**
     * Requests a feature of interest.
     *
     * @param builder
     *            parameter assembler
     * @return Request result
     * @throws OXFException
     * @throws ExceptionReport
     */
    private OperationResult doGetFeatureOfInterest(final GetFeatureOfInterestParameterBuilder_v100 builder)
            throws OXFException, ExceptionReport {
        final SOSAdapter adapter = new SOSAdapter(getServiceDescriptor().getVersion());
        if (checkOperationAvailability(GET_FEATURE_OF_INTEREST)) {
            final Operation operation = getServiceDescriptor().getOperationsMetadata()
                    .getOperationByName(GET_FEATURE_OF_INTEREST);
            final ParameterContainer parameterContainer = createParameterContainerForGetFeatureOfInterest(
                    builder.getParameters());
            return adapter.doOperation(operation, parameterContainer, getConnectionTimeout(), getReadTimeout());
        } else {
            throw new OXFException(operationNotSupportedMessage(GET_FEATURE_OF_INTEREST));
        }
    }

    private ParameterContainer createParameterContainerForGetFeatureOfInterest(final Map<String, String> parameters)
            throws OXFException, ExceptionReport {
        final ParameterContainer parameterContainer = createParameterContainerWithCommonServiceParameters();
        // mandatory parameters from builder
        if (parameters.get(GET_FOI_ID_PARAMETER) != null) {
            parameterContainer.addParameterShell(GET_FOI_ID_PARAMETER, parameters.get(GET_FOI_ID_PARAMETER));
        } else {
            parameterContainer.addParameterShell(GET_FOI_LOCATION_PARAMETER,
                    parameters.get(GET_FOI_LOCATION_PARAMETER));
        }
        // optional parameters from builder
        if (parameters.get(GET_FOI_EVENT_TIME_PARAMETER) != null) {
            parameterContainer.addParameterShell(GET_FOI_EVENT_TIME_PARAMETER,
                    parameters.get(GET_FOI_EVENT_TIME_PARAMETER));
        }
        processOptionalMimetypeParameter(parameters, parameterContainer);
        return parameterContainer;
    }

    @Override
    public OperationResult getObservation(String offering, List<String> sensors, List<String> observedProperties,
            String srsName, TemporalRequestFilter tempFilter) throws GetObservationException {
        GetObservationParameterBuilder_v100 builder = new GetObservationParameterBuilder_v100(offering,
                observedProperties.get(0), "text/xml;subtype=\"om/1.0.0\"");
        observedProperties.listIterator(1).forEachRemaining(builder::addObservedProperty);
        if (sensors != null && !sensors.isEmpty()) {
            sensors.forEach(builder::addProcedure);
        }
        if (srsName != null && !srsName.isEmpty()) {
            builder.addSrsName(srsName);
        }
        if (tempFilter != null && tempFilter.toString() != null) {
            builder.addEventTime(tempFilter.toString());
        }
        try {
            return doGetObservation(builder);
        } catch (OXFException | ExceptionReport e) {
            throw new GetObservationException(e);
        }
    }

    /**
     * Requests observation(s).
     *
     * @param builder
     *            parameter assembler
     * @return Request result
     * @throws OXFException
     * @throws ExceptionReport
     */
    private OperationResult doGetObservation(final GetObservationParameterBuilder_v100 builder)
            throws OXFException, ExceptionReport {
        final SOSAdapter adapter = new SOSAdapter(getServiceDescriptor().getVersion());
        if (checkOperationAvailability(GET_OBSERVATION)) {
            final Operation operation = getServiceDescriptor().getOperationsMetadata()
                    .getOperationByName(GET_OBSERVATION);
            final ParameterContainer parameterContainer = createParameterContainerForGetObservation(
                    builder.getParameters());
            return adapter.doOperation(operation, parameterContainer, getConnectionTimeout(), getReadTimeout());
        } else {
            throw new OXFException(operationNotSupportedMessage(GET_OBSERVATION));
        }
    }

    private ParameterContainer createParameterContainerForGetObservation(final Map<String, Object> parameters)
            throws OXFException, ExceptionReport {
        final ParameterContainer parameterContainer = createParameterContainerWithCommonServiceParameters();
        // mandatory parameters from builder
        parameterContainer.addParameterShell(GET_OBSERVATION_OFFERING_PARAMETER,
                (String) parameters.get(GET_OBSERVATION_OFFERING_PARAMETER));
        parameterContainer
                .addParameterShell((ParameterShell) parameters.get(GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER));
        parameterContainer.addParameterShell(GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER,
                (String) parameters.get(GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER));
        // optional parameters from builder
        if (parameters.get(ISOSRequestBuilder.GET_OBSERVATION_SRS_NAME_PARAMETER) != null) {
            parameterContainer.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_SRS_NAME_PARAMETER,
                    (String) parameters.get(ISOSRequestBuilder.GET_OBSERVATION_SRS_NAME_PARAMETER));
        }
        if (parameters.get(GET_OBSERVATION_EVENT_TIME_PARAMETER) != null) {
            parameterContainer.addParameterShell((ParameterShell) parameters.get(GET_OBSERVATION_EVENT_TIME_PARAMETER));
        }
        if (parameters.get(GET_OBSERVATION_PROCEDURE_PARAMETER) != null) {
            parameterContainer.addParameterShell((ParameterShell) parameters.get(GET_OBSERVATION_PROCEDURE_PARAMETER));
        }
        if (parameters.get(GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER) != null) {
            parameterContainer.addParameterShell(GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER,
                    (String) parameters.get(GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER));
        }
        if (parameters.get(GET_OBSERVATION_RESULT_PARAMETER) != null) {
            parameterContainer.addParameterShell(GET_OBSERVATION_RESULT_PARAMETER,
                    (String) parameters.get(GET_OBSERVATION_RESULT_PARAMETER));
        }
        if (parameters.get(GET_OBSERVATION_RESULT_MODEL_PARAMETER) != null) {
            parameterContainer.addParameterShell(GET_OBSERVATION_RESULT_MODEL_PARAMETER,
                    (String) parameters.get(GET_OBSERVATION_RESULT_MODEL_PARAMETER));
        }
        if (parameters.get(GET_OBSERVATION_RESPONSE_MODE_PARAMETER) != null) {
            parameterContainer.addParameterShell(GET_OBSERVATION_RESPONSE_MODE_PARAMETER,
                    (String) parameters.get(GET_OBSERVATION_RESPONSE_MODE_PARAMETER));
        }
        processOptionalMimetypeParameter(parameters, parameterContainer);
        return parameterContainer;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.i3mainz.springframework.swe.n52.sos.service.versions.v100.
     * SOSServiceV100
     * #registerSensor(de.i3mainz.springframework.swe.n52.sos.model.Sensor)
     */
    @Override
    public String registerSensor(RegisterSensor sensor) throws ExceptionReport {
        LOG.trace("registerSensor()");
        RegisterSensorParameters regSensorParameter;
        try {
            regSensorParameter = createRegisterSensorParametersFromRS(sensor);
            setMimetype(regSensorParameter);
            LOG.debug(regSensorParameter.getParameterNames().toString());
            regSensorParameter.getParameterNames().stream().map(n -> regSensorParameter.getAllValues(n).toString())
                    .forEach(LOG::debug);
            final OperationResult opResult = doRegisterSensor(regSensorParameter);
            final RegisterSensorResponseDocument response = RegisterSensorResponseDocument.Factory
                    .parse(opResult.getIncomingResultAsAutoCloseStream());
            LOG.debug("RegisterSensorResponse parsed");
            return response.getRegisterSensorResponse().getAssignedSensorId();
        } catch (OXFException | XmlException | IOException e) {
            LOG.error("Exception", e);
        }

        return null;

    }

    /**
     * Requests the registration of a sensor.
     *
     * @param parameters
     *            parameter assembler
     * @return Request result
     * @throws OXFException
     *             if the operation RegisterSensor is not supported
     * @throws ExceptionReport
     *             if the operation RegisterSensor failed
     */
    public OperationResult doRegisterSensor(final RegisterSensorParameters parameters)
            throws OXFException, ExceptionReport {
        final SOSAdapter adapter = new SOSAdapter(getServiceDescriptor().getVersion());
        if (checkOperationAvailability(REGISTER_SENSOR)) {
            final Operation operation = getServiceDescriptor().getOperationsMetadata()
                    .getOperationByName(REGISTER_SENSOR);
            final ParameterContainer parameterContainer = createParameterContainerForRegisterSensor(parameters);
            return adapter.doOperation(operation, parameterContainer, getConnectionTimeout(), getReadTimeout());
        } else {
            throw new OXFException(operationNotSupportedMessage(REGISTER_SENSOR));
        }
    }

    private ParameterContainer createParameterContainerForRegisterSensor(final RegisterSensorParameters parameters)
            throws OXFException, ExceptionReport {
        final ParameterContainer parameterContainer = createParameterContainerWithCommonServiceParameters();
        parameterContainer.addParameterShell(REGISTER_SENSOR_ML_DOC_PARAMETER,
                parameters.getSingleValue(REGISTER_SENSOR_ML_DOC_PARAMETER));
        parameterContainer.addParameterShell(REGISTER_SENSOR_OBSERVATION_TEMPLATE,
                parameters.getSingleValue(REGISTER_SENSOR_OBSERVATION_TEMPLATE));
        if (parameters.contains(REGISTER_SENSOR_DEFAULT_RESULT_VALUE)) {
            final String defaultResult = parameters.getSingleValue(REGISTER_SENSOR_DEFAULT_RESULT_VALUE);
            parameterContainer.addParameterShell(REGISTER_SENSOR_DEFAULT_RESULT_VALUE, defaultResult);
        }
        processOptionalMimetypeParameter(parameters, parameterContainer);
        return parameterContainer;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.i3mainz.springframework.swe.n52.sos.service.SOSServiceImpl#
     * callInsertObservationRequest(org.n52.oxf.sos.request.
     * InsertObservationParameters)
     */
    @Override
    protected String callInsertObservationRequest(InsertObservationParameters parameters)
            throws OXFException, ExceptionReport {
        OperationResult opResult;
        LOG.debug("\tBEFORE OXF - doOperation 'InsertObservation'");
        opResult = doInsertObservation(parameters);
        LOG.debug("\tAFTER OXF - doOperation 'InsertObservation'");

        try {
            final InsertObservationResponse response = InsertObservationResponseDocument.Factory
                    .parse(opResult.getIncomingResultAsAutoCloseStream()).getInsertObservationResponse();
            LOG.debug(String.format("Observation inserted succesfully. Returned id: %s",
                    response.getAssignedObservationId()));
            return response.getAssignedObservationId();
        } catch (final XmlException e) {
            LOG.error(String.format("Exception thrown: %s", e.getMessage()), e);
        } catch (final IOException e) {
            LOG.error(String.format("Exception thrown: %s", e.getMessage()), e);
        }
        return null;
    }

    @Override
    protected void addSosValues(final InsertObservationParameters param, final ParameterContainer parameterContainer)
            throws OXFException {

        org.n52.oxf.sos.request.v100.InsertObservationParameters parameters = (org.n52.oxf.sos.request.v100.InsertObservationParameters) param;
        // mandatory parameters
        parameterContainer.addParameterShell(INSERT_OBSERVATION_PROCEDURE_PARAMETER,
                parameters.getSingleValue(INSERT_OBSERVATION_PROCEDURE_PARAMETER));
        parameterContainer.addParameterShell(INSERT_OBSERVATION_TYPE,
                parameters.getSingleValue(INSERT_OBSERVATION_TYPE));
        parameterContainer.addParameterShell(INSERT_OBSERVATION_SAMPLING_TIME,
                parameters.getSingleValue(INSERT_OBSERVATION_SAMPLING_TIME));
        parameterContainer.addParameterShell(INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER,
                parameters.getSingleValue(INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER));
        parameterContainer.addParameterShell(INSERT_OBSERVATION_FOI_ID_PARAMETER,
                parameters.getSingleValue(INSERT_OBSERVATION_FOI_ID_PARAMETER));
        // optional parameters
        if (parameters.contains(INSERT_OBSERVATION_NEW_FOI_NAME)) {
            parameterContainer.addParameterShell(INSERT_OBSERVATION_NEW_FOI_NAME,
                    parameters.getSingleValue(INSERT_OBSERVATION_NEW_FOI_NAME));
        }
        if (parameters.contains(INSERT_OBSERVATION_NEW_FOI_DESC)) {
            parameterContainer.addParameterShell(INSERT_OBSERVATION_NEW_FOI_DESC,
                    parameters.getSingleValue(INSERT_OBSERVATION_NEW_FOI_DESC));
        }
        if (parameters.contains(INSERT_OBSERVATION_NEW_FOI_POSITION)) {
            parameterContainer.addParameterShell(INSERT_OBSERVATION_NEW_FOI_POSITION,
                    parameters.getSingleValue(INSERT_OBSERVATION_NEW_FOI_POSITION));
        }
        if (parameters.contains(INSERT_OBSERVATION_POSITION_SRS)) {
            parameterContainer.addParameterShell(INSERT_OBSERVATION_POSITION_SRS,
                    parameters.getSingleValue(INSERT_OBSERVATION_POSITION_SRS));
        }
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
    }

    protected RegisterSensorParameters createRegisterSensorParametersFromRS(final RegisterSensor sensor)
            throws OXFException, XmlException, IOException {
        LOG.trace("createParameterContainterFromRS()");
        ObservationTemplateBuilder observationTemplate;
        observationTemplate = ObservationTemplateBuilder.createObservationTemplateBuilderForTypeText();
        observationTemplate.setDefaultValue(" ");

        return new RegisterSensorParameters(this.sensorDescBuilder.createSML(sensor),
                observationTemplate.generateObservationTemplate());
    }

    @Override
    protected final InsertObservationParameters createParameterAssemblyFromIO(InsertObservation io)
            throws OXFException {
        return new org.n52.oxf.sos.request.v100.InsertObservationParameters(this.createInsertObservationParameters(io));
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
        obsParameter.addSrsPosition(Configuration.SOS_100_EPSG_CODE_PREFIX + io.getEpsgCode());
        obsParameter.addSamplingTime(io.getTimeStamp().toString());
        return obsParameter;
    }

    @Override
    public boolean isTransactional() {
        final OperationsMetadata opMeta = getCapabilities().getOperationsMetadata();
        LOG.debug(String.format("OperationsMetadata found: %s", opMeta));
        if (opMeta.getOperationByName(SOSAdapter.REGISTER_SENSOR) != null
                && opMeta.getOperationByName(SOSAdapter.INSERT_OBSERVATION) != null) {
            LOG.debug(String.format("Found all required operations: %s, %s", SOSAdapter.REGISTER_SENSOR,
                    SOSAdapter.INSERT_OBSERVATION));
            return true;
        }
        return false;
    }

}