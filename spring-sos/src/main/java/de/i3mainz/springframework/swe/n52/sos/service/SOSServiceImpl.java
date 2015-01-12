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
    
    private static final Logger LOG = LoggerFactory
            .getLogger(SOSService.class);

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

            
        } catch (ExceptionReport e1) {
            LOG.error("SOS-Server throws Exception.", e1);
        } catch (OXFException e1) {
            LOG.error("Error while accessing SOS-Service", e1);
        } catch (Exception e) {
           LOG.error("Something is wrong creating sosWrapper.");
           e.printStackTrace();
        }
        
    }
   


    /* (non-Javadoc)
     * @see de.i3mainz.springframework.swe.n52.sos.service.SOSService#getCapabilities()
     */
    @Override
    public ServiceDescriptor getCapabilities() {
        return this.sosWrapper.getServiceDescriptor();
    }

    /* (non-Javadoc)
     * @see de.i3mainz.springframework.swe.n52.sos.service.SOSService#insertObservation(java.lang.String, de.i3mainz.springframework.swe.n52.sos.model.FeatureOfInterest, de.i3mainz.springframework.swe.n52.sos.model.Observation)
     */
    @Override
    public String insertObservation(String sensorId, FeatureOfInterest foi,
            Observation observation) {
        SosObservation insertObsertion = new SosObservation();
        String insertObservationXMLDoc = "";
        insertObsertion.setObservation(sensorId, observation.getTimeAsSTring(), foi
                .getId(), foi.getName(), foi.getPosition(), observation
                .getValue().toString());
        insertObservationXMLDoc = SosXMLDoc.insertObservation(insertObsertion);
        LOG.debug(insertObservationXMLDoc);
        String responserequest;
        responserequest = HttpConnect.excutePost(this.connectionParameter
                .getUrl(), insertObservationXMLDoc);

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
