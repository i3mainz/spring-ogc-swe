/**
 * 
 */
package de.i3mainz.springframework.swe.n52.sos.service;

import org.n52.oxf.OXFException;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.ServiceDescriptor;
import org.n52.oxf.sos.adapter.ISOSRequestBuilder.Binding;
import org.n52.oxf.sos.adapter.wrapper.SOSWrapper;
import org.n52.oxf.sos.adapter.wrapper.SosWrapperFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.i3mainz.springframework.swe.n52.sos.SOSConnectionParameter;
import de.i3mainz.springframework.swe.n52.sos.model.FeatureOfInterest;
import de.i3mainz.springframework.swe.n52.sos.model.Observation;
import de.i3mainz.springframework.swe.n52.sos.util.DescriptionBuilder;
import de.i3mainz.springframework.swe.n52.sos.util.HttpConnect;
import de.i3mainz.springframework.swe.n52.sos.util.SosObservation;
import de.i3mainz.springframework.swe.n52.sos.util.SosXMLDoc;

/**
 * @author nikolai
 *
 */
public abstract class SOSServiceImpl implements SOSService {

    private static final Logger LOG = LoggerFactory.getLogger(SOSService.class);

    private SOSWrapper sosWrapper;
    private Binding sosBinding;
    protected DescriptionBuilder sensorDescBuilder;
    private SOSConnectionParameter connectionParameter;

    public SOSServiceImpl(SOSConnectionParameter connectionParameter) {

        this.connectionParameter = connectionParameter;

        this.sosBinding = this.connectionParameter.getBinding();
        // sensorDescBuilder = new DescriptionBuilder();
        try {
            this.sosWrapper = SosWrapperFactory.newInstance(
                    this.connectionParameter.getUrl(),
                    this.connectionParameter.getVersion(), this.sosBinding);

        } catch (ExceptionReport e) {
            LOG.error("SOS-Server throws Exception.", e);
        } catch (OXFException e) {
            LOG.error("Error while accessing SOS-Service", e);
        } catch (Exception e) {
            LOG.error("Something is wrong creating sosWrapper.", e);
        }

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
        return this.sosWrapper.getServiceDescriptor();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.i3mainz.springframework.swe.n52.sos.service.SOSService#insertObservation
     * (java.lang.String,
     * de.i3mainz.springframework.swe.n52.sos.model.FeatureOfInterest,
     * de.i3mainz.springframework.swe.n52.sos.model.Observation)
     */
    @Override
    public String insertObservation(String sensorId, FeatureOfInterest foi,
            Observation observation) {
        SosObservation insertObsertion = new SosObservation();
        String insertObservationXMLDoc = "";
        insertObsertion.setObservation(sensorId, observation.getTimeAsSTring(),
                foi.getId(), foi.getName(), foi.getPosition(), observation
                        .getValue().toString());
        insertObservationXMLDoc = SosXMLDoc.insertObservation(insertObsertion);
        LOG.debug(insertObservationXMLDoc);
        String responserequest;
        responserequest = HttpConnect.excutePost(
                this.connectionParameter.getUrl(), insertObservationXMLDoc);

        return responserequest;
    }

    // /*
    // * (non-Javadoc)
    // *
    // * @see de.i3mainz.springframework.ows.n52.sos.core.
    // * SensorObservationServiceOperations
    // * #insertObservation(org.n52.sos.importer.
    // * feeder.model.requests.InsertObservation)
    // */
    // @Override
    // public String insertObservation(final String sensorId,
    // final FeatureOfInterest foi, final Observation observation)
    // throws IOException {
    // LOG.trace("insertObservation()");
    // OperationResult opResult = null;
    // org.n52.oxf.sos.request.InsertObservationParameters parameters = null;
    //
    // try {
    // parameters = createParameterAssemblyFromIO(sensorId, foi,
    // observation);
    // try {
    // LOG.debug("\n\nBEFORE OXF - doOperation \"InsertObservation\"\n\n");
    // opResult = getSosWrapper().doInsertObservation(parameters);
    // LOG.debug("\n\nAFTER OXF - doOperation \"InsertObservation\"\n\n");
    // if (getConnectionParameter().getVersion().equals("1.0.0")) {
    // try {
    // final InsertObservationResponse response =
    // net.opengis.sos.x10.InsertObservationResponseDocument.Factory
    // .parse(opResult.getIncomingResultAsStream())
    // .getInsertObservationResponse();
    // LOG.debug(String
    // .format("Observation inserted succesfully. Returned id: %s",
    // response.getAssignedObservationId()));
    // return response.getAssignedObservationId();
    // } catch (final XmlException e) {
    // LOG.error(
    // String.format("Exception thrown: %s",
    // e.getMessage()), e);
    // } catch (final IOException e) {
    // LOG.error(
    // String.format("Exception thrown: %s",
    // e.getMessage()), e);
    // }
    // } else if (getConnectionParameter().getVersion()
    // .equals("2.0.0")) {
    // try {
    // net.opengis.sos.x20.InsertObservationResponseDocument.Factory
    // .parse(opResult.getIncomingResultAsStream())
    // .getInsertObservationResponse();
    // LOG.debug("Observation inserted successfully.");
    // return "SOS 2.0 InsertObservation doesn't return the assigned id";
    // } catch (final XmlException e) {
    // LOG.error("Exception thrown: {}", e.getMessage(), e);
    // }
    // }
    // } catch (final ExceptionReport e) {
    // final Iterator<OWSException> iter = e.getExceptionsIterator();
    // StringBuffer buf = new StringBuffer();
    // while (iter.hasNext()) {
    // final OWSException owsEx = iter.next();
    // // check for observation already contained exception
    // if (owsEx
    // .getExceptionCode()
    // .equals(Configuration.SOS_EXCEPTION_CODE_NO_APPLICABLE_CODE)
    // && owsEx.getExceptionTexts().length > 0
    // && (owsEx.getExceptionTexts()[0]
    // .indexOf(Configuration.SOS_EXCEPTION_OBSERVATION_DUPLICATE_CONSTRAINT) >
    // -1
    // || owsEx.getExceptionTexts()[0]
    // .indexOf(Configuration.SOS_EXCEPTION_OBSERVATION_ALREADY_CONTAINED) > -1
    // || owsEx
    // .getExceptionTexts()[0]
    // .indexOf(Configuration.SOS_200_DUPLICATE_OBSERVATION_CONSTRAINT) > -1)) {
    // return Configuration.SOS_OBSERVATION_ALREADY_CONTAINED;
    // }
    // buf = buf.append(String.format(
    // "ExceptionCode: '%s' because of '%s'\n",
    // owsEx.getExceptionCode(),
    // Arrays.toString(owsEx.getExceptionTexts())));
    // }
    // LOG.error(String.format("Exception thrown: %s\n%s",
    // e.getMessage(), buf.toString()));
    // LOG.debug(e.getMessage(), e);
    // }
    //
    // } catch (final OXFException e) {
    // LOG.error(
    // String.format("Problem with OXF. Exception thrown: %s",
    // e.getMessage()), e);
    // }
    // return null;
    // }
    
//    protected org.n52.oxf.sos.request.InsertObservationParameters createParameterAssemblyFromIO(
//            final String sensorID, final FeatureOfInterest foi,
//            final Observation observation) throws OXFException {
//
//        LOG.trace("createParameterBuilderFromIO()");
//        ObservationParameters obsParameter = null;
//
//        // if (io.getMeasuredValueType().equals(
//        // Configuration.SOS_OBSERVATION_TYPE_TEXT)) {
//        // set text
//        obsParameter = new TextObservationParameters();
//        ((TextObservationParameters) obsParameter)
//                .addObservationValue(observation.getValue().toString());
//        // } else if (io.getMeasuredValueType().equals(
//        // Configuration.SOS_OBSERVATION_TYPE_COUNT)) {
//        // // set count
//        // obsParameter = new CountObservationParameters();
//        // ((CountObservationParameters) obsParameter)
//        // .addObservationValue((Integer) io.getResultValue());
//        // } else if (io.getMeasuredValueType().equals(
//        // Configuration.SOS_OBSERVATION_TYPE_BOOLEAN)) {
//        // // set boolean
//        // obsParameter = new BooleanObservationParameters();
//        // ((BooleanObservationParameters) obsParameter)
//        // .addObservationValue((Boolean) io.getResultValue());
//        // } else {
//        // // set default value type
//        // obsParameter = new MeasurementObservationParameters();
//        // ((MeasurementObservationParameters) obsParameter).addUom(io
//        // .getUnitOfMeasurementCode());
//        // ((MeasurementObservationParameters) obsParameter)
//        // .addObservationValue(io.getResultValue().toString());
//        // }
//        // obsParameter.addObservedProperty("urn:ogc:def:dataType:OGC:1.1:string");
//        obsParameter.addNewFoiId(foi.getId());
//        obsParameter.addNewFoiName(foi.getName());
//        obsParameter.addFoiDescription(foi.getId());
//        // position
//        // boolean eastingFirst = false;
//        // if (Configuration.EPSG_EASTING_FIRST_MAP.get(io.getEpsgCode()) ==
//        // null) {
//        // Configuration.EPSG_EASTING_FIRST_MAP.get("default");
//        // } else {
//        // eastingFirst = Configuration.EPSG_EASTING_FIRST_MAP.get(io
//        // .getEpsgCode());
//        // }
//        // String pos = eastingFirst ? String.format("%s %s",
//        // io.getLongitudeValue(), io.getLatitudeValue()) : String.format(
//        // "%s %s", io.getLatitudeValue(), io.getLongitudeValue());
//        // if (io.isSetAltitudeValue()) {
//        // pos = String.format("% %", pos, io.getAltitudeValue());
//        // }
//
//        String pos = foi.getPosition();
//        obsParameter.addFoiPosition(pos);
//        // obsParameter.addObservedProperty("urn:ogc:def:dataType:OGC:1.1:string");
//        obsParameter.addProcedure(sensorID);
//
//        if (VERSION200.equalsIgnoreCase(getConnectionParameter().getVersion())) {
//            obsParameter.addSrsPosition(Configuration.SOS_200_EPSG_CODE_PREFIX
//                    + "4326");
//            obsParameter.addPhenomenonTime(observation.getTimeAsSTring());
//            obsParameter.addResultTime(observation.getTimeAsSTring());
//            return new org.n52.oxf.sos.request.v200.InsertObservationParameters(
//                    obsParameter, Collections.singletonList(sensorID));
//        }
//
//        // obsParameter.addSrsPosition(Configuration.SOS_100_EPSG_CODE_PREFIX+"4326");
//        // obsParameter.addParameterValue("srsName",
//        // Configuration.SOS_100_EPSG_CODE_PREFIX+"4326");
//        // obsParameter.addSamplingTime(observation.getTimeAsSTring());
//        obsParameter.addParameterValue(
//                ISOSRequestBuilder.INSERT_OBSERVATION_SAMPLING_TIME,
//                observation.getTimeAsSTring());
//        return new org.n52.oxf.sos.request.v100.InsertObservationParameters(
//                obsParameter);
//    }

    /**
     * @return the log
     */
    protected static final Logger getLog() {
        return LOG;
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
}