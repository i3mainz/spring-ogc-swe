/**
 * 
 */
package de.i3mainz.springframework.swe.n52.sos.service;

import static org.n52.oxf.request.MimetypeAwareRequestParameters.CHARSET;
import static org.n52.oxf.request.MimetypeAwareRequestParameters.MIME_TYPE;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.BINDING;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.ENCODING;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.MIMETYPE;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.SERVICE;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.VERSION;
import static org.n52.oxf.sos.adapter.SOSAdapter.INSERT_OBSERVATION;

import java.nio.charset.Charset;
import java.util.Map;

import org.apache.http.entity.ContentType;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.ServiceDescriptor;
import org.n52.oxf.ows.capabilities.Operation;
import org.n52.oxf.request.MimetypeAwareRequestParameters;
import org.n52.oxf.sos.adapter.ISOSRequestBuilder;
import org.n52.oxf.sos.adapter.ISOSRequestBuilder.Binding;
import org.n52.oxf.sos.adapter.SOSAdapter;
import org.n52.oxf.sos.observation.BooleanObservationParameters;
import org.n52.oxf.sos.observation.CountObservationParameters;
import org.n52.oxf.sos.observation.MeasurementObservationParameters;
import org.n52.oxf.sos.observation.ObservationParameters;
import org.n52.oxf.sos.observation.TextObservationParameters;
import org.n52.oxf.sos.request.InsertObservationParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.i3mainz.springframework.swe.n52.sos.SOSConnectionParameter;
import de.i3mainz.springframework.swe.n52.sos.exceptions.InsertObservationException;
import de.i3mainz.springframework.swe.n52.sos.model.FeatureOfInterest;
import de.i3mainz.springframework.swe.n52.sos.model.Observation;
import de.i3mainz.springframework.swe.n52.sos.model.requests.InsertObservation;
import de.i3mainz.springframework.swe.n52.sos.util.Configuration;
import de.i3mainz.springframework.swe.n52.sos.util.DescriptionBuilder;

/**
 * @author nikolai
 *
 */
public abstract class SOSServiceImpl implements SOSService {

    private static final Logger LOG = LoggerFactory.getLogger(SOSService.class);

    private static final String SERVICE_TYPE = "SOS";

    private ServiceDescriptor serviceDescriptor;
    private Binding sosBinding;
    protected DescriptionBuilder sensorDescBuilder;
    private SOSConnectionParameter connectionParameter;
    private int connectionTimeout = -1;
    private int readTimeout = -1;

    public SOSServiceImpl(SOSConnectionParameter connectionParameter) {

        this.connectionParameter = connectionParameter;

        this.sosBinding = this.connectionParameter.getBinding();
        try {
            this.serviceDescriptor = doInitialGetCapabilities(this.connectionParameter.getUrl(),
                    this.connectionParameter.getVersion(), this.sosBinding);

        } catch (ExceptionReport e) {
            LOG.error("SOS-Server throws Exception.", e);
        } catch (OXFException e) {
            LOG.error("Error while accessing SOS-Service", e);
        } catch (Exception e) {
            LOG.error("Something is wrong creating sosWrapper.", e);
        }

    }

    private ServiceDescriptor doInitialGetCapabilities(final String serviceEndpoint, final String serviceVersion,
            final Binding binding) throws ExceptionReport, OXFException {
        final SOSAdapter adapter = new SOSAdapter(serviceVersion);
        return adapter.initService(serviceEndpoint, binding);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.i3mainz.springframework.swe.n52.sos.service.SOSService#getCapabilities
     * ()
     */
    @Override
    public ServiceDescriptor getCapabilities() {
        return this.serviceDescriptor;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.i3mainz.springframework.swe.n52.sos.service.SOSService#
     * insertObservation (java.lang.String,
     * de.i3mainz.springframework.swe.n52.sos.model.FeatureOfInterest,
     * de.i3mainz.springframework.swe.n52.sos.model.Observation)
     */
    @Override
    public String insertObservation(String sensorId, FeatureOfInterest foi, Observation observation)
            throws InsertObservationException {
        throw new InsertObservationException(new OXFException("Method not supported yet!"));
    }

    @Override
    public String insertObservation(InsertObservation io) throws ExceptionReport {
        try {
            InsertObservationParameters parameters = this.createParameterAssemblyFromIO(io);
            setMimetype(parameters);
            return this.callInsertObservationRequest(parameters);
        } catch (final OXFException e) {
            LOG.error(String.format("Problem with OXF. Exception thrown: %s", e.getMessage()), e);
            return null;
        }

    }

    protected abstract String callInsertObservationRequest(InsertObservationParameters parameters)
            throws OXFException, ExceptionReport;

    protected ObservationParameters createInsertObservationParameters(final InsertObservation io) throws OXFException {
        ObservationParameters obsParameter;
        switch (io.getMeasuredValueType()) {
        case BOOLEAN:
            obsParameter = new BooleanObservationParameters();
            ((BooleanObservationParameters) obsParameter).addObservationValue((Boolean) io.getResultValue());
            break;
        case COUNT:
            obsParameter = new CountObservationParameters();
            ((CountObservationParameters) obsParameter).addObservationValue((Integer) io.getResultValue());
            break;
        case MEASUREMENT:
            obsParameter = new MeasurementObservationParameters();
            ((MeasurementObservationParameters) obsParameter).addUom(io.getUnitOfMeasurementCode());
            ((MeasurementObservationParameters) obsParameter).addObservationValue(io.getResultValue().toString());
            break;
        case TEXT:
            obsParameter = new TextObservationParameters();
            ((TextObservationParameters) obsParameter).addObservationValue(io.getResultValue().toString());
            break;
        default:
            throw new OXFException("ValueType " + io.getMeasuredValueType() + " is not supported");
        }
        obsParameter.addObservedProperty(io.getObservedPropertyURI());
        obsParameter.addNewFoiId(io.getFeatureOfInterestURI());
        obsParameter.addNewFoiName(io.getFeatureOfInterestName());
        obsParameter.addFoiDescription(io.getFeatureOfInterestURI());
        obsParameter.addFoiId(io.getFeatureOfInterestURI());

        boolean eastingFirst;
        if (Configuration.EPSGEastingFirst.valueOf("E" + (io.getEpsgCode())) == null) {
            eastingFirst = Configuration.EPSGEastingFirst.valueOf("EDEFAULT").getEastingFirst();
        } else {
            eastingFirst = Configuration.EPSGEastingFirst.valueOf("E" + io.getEpsgCode()).getEastingFirst();
        }
        String pos = eastingFirst ? String.format("%s %s", io.getLongitudeValue(), io.getLatitudeValue())
                : String.format("%s %s", io.getLatitudeValue(), io.getLongitudeValue());

        if (io.isSetAltitudeValue()) {
            pos = String.format("%s %s", pos, io.getAltitudeValue());
        }

        obsParameter.addFoiPosition(pos);
        obsParameter.addObservedProperty(io.getObservedPropertyURI());
        obsParameter.addProcedure(io.getSensorURI());
        return obsParameter;

    }

    protected abstract InsertObservationParameters createParameterAssemblyFromIO(final InsertObservation io)
            throws OXFException;

    /**
     * @return the log
     */
    protected static final Logger getLog() {
        return LOG;
    }

    /**
     * @return the sosBinding
     */
    protected final Binding getSosBinding() {
        return sosBinding;
    }

    protected final void setMimetype(final MimetypeAwareRequestParameters parameters) {
        String mimeType = "text/xml";
        if (sosBinding != null) {
            parameters.addParameterValue(ISOSRequestBuilder.BINDING, sosBinding.name());
            if (sosBinding.equals(Binding.SOAP)) {
                mimeType = "application/soap+xml";
            }
        }

        parameters.setCharset(Charset.forName("UTF-8"));
        parameters.setType(mimeType);
    }

    protected boolean checkOperationAvailability(final String operationName) {
        return serviceDescriptor.getOperationsMetadata().getOperationByName(operationName) != null;
    }

    /**
     * @return the connectionTimeout
     */
    protected int getConnectionTimeout() {
        return connectionTimeout;
    }

    /**
     * @param connectionTimeout
     *            the connectionTimeout to set
     */
    protected void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    /**
     * @return the readTimeout
     */
    protected int getReadTimeout() {
        return readTimeout;
    }

    /**
     * @param readTimeout
     *            the readTimeout to set
     */
    protected void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    /**
     * @return the serviceDescriptor
     */
    protected ServiceDescriptor getServiceDescriptor() {
        return serviceDescriptor;
    }

    protected ParameterContainer createParameterContainerWithCommonServiceParameters() throws OXFException {
        final ParameterContainer parameterContainer = new ParameterContainer();
        parameterContainer.addParameterShell(SERVICE, SERVICE_TYPE);
        parameterContainer.addParameterShell(VERSION, serviceDescriptor.getVersion());
        if (sosBinding != null) {
            parameterContainer.addParameterShell(BINDING, sosBinding.name());
        }
        return parameterContainer;
    }

    protected void processOptionalMimetypeParameter(final Map<String, ?> parameters,
            final ParameterContainer parameterContainer) throws OXFException {
        if (parameters.get(MIME_TYPE) != null && parameters.get(CHARSET) != null) {
            final String mimeType = (String) parameters.get(MIME_TYPE);
            final String charSet = (String) parameters.get(CHARSET);
            if (!mimeType.isEmpty() && !charSet.isEmpty()) {
                final Charset charset = Charset.forName(charSet);
                final ContentType contentType = ContentType.create(mimeType, charset);
                parameterContainer.addParameterShell(MIMETYPE, contentType.getMimeType());
                parameterContainer.addParameterShell(ENCODING, charSet);
            }
        }
    }

    protected void processOptionalMimetypeParameter(final MimetypeAwareRequestParameters parameters,
            final ParameterContainer parameterContainer) throws OXFException {
        if (parameters.isSetMimetype() && parameters.isValid()) {
            final String type = parameters.getSingleValue(MimetypeAwareRequestParameters.MIME_TYPE);
            final String charSet = parameters.getSingleValue(MimetypeAwareRequestParameters.CHARSET);
            final ContentType contentType = ContentType.create(type, Charset.forName(charSet));
            parameterContainer.addParameterShell(MIMETYPE, contentType.getMimeType());
            parameterContainer.addParameterShell(ENCODING, charSet);
        }
    }

    /**
     * Requests the insertion of an observation.
     *
     * @param parameters
     *            parameter assembler
     * @return Request result
     * @throws OXFException
     *             if operation InsertObservation is not supported by the SOS
     * @throws ExceptionReport
     *             if operation InsertObservation failed
     */
    protected OperationResult doInsertObservation(final InsertObservationParameters parameters)
            throws OXFException, ExceptionReport {
        // wrapped SOSAdapter instance
        final SOSAdapter adapter = new SOSAdapter(getServiceDescriptor().getVersion());
        // if there are operations defined
        if (checkOperationAvailability(INSERT_OBSERVATION)) {
            final Operation operation = getServiceDescriptor().getOperationsMetadata()
                    .getOperationByName(INSERT_OBSERVATION);
            final ParameterContainer parameterContainer = createParameterContainerForInsertObservation(parameters);
            return adapter.doOperation(operation, parameterContainer, getConnectionTimeout(), getReadTimeout());
        } else {
            throw new OXFException(operationNotSupportedMessage(INSERT_OBSERVATION));
        }
    }

    private ParameterContainer createParameterContainerForInsertObservation(
            final InsertObservationParameters parameters) throws OXFException, ExceptionReport {
        final ParameterContainer parameterContainer = createParameterContainerWithCommonServiceParameters();
        if (parameters instanceof org.n52.oxf.sos.request.v100.InsertObservationParameters) {
            addSosValues((org.n52.oxf.sos.request.v100.InsertObservationParameters) parameters, parameterContainer);
        } else if (parameters instanceof org.n52.oxf.sos.request.v200.InsertObservationParameters) {
            addSosValues((org.n52.oxf.sos.request.v200.InsertObservationParameters) parameters, parameterContainer);
        } else {
            throw new OXFException(String.format("Subtype of %s: %s not supported by this implemenation.",
                    InsertObservationParameters.class.getName(), parameters.getClass().getName()));
        }
        processOptionalMimetypeParameter(parameters, parameterContainer);
        return parameterContainer;
    }

    protected abstract void addSosValues(final InsertObservationParameters parameters,
            final ParameterContainer parameterContainer) throws OXFException;

    protected String operationNotSupportedMessage(String operation) {
        return "Operation: '" + operation + "' not supported by the SOS instance!";
    }
}