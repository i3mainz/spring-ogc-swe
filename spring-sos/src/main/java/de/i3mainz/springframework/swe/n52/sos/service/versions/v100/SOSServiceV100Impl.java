/**
 * 
 */
package de.i3mainz.springframework.swe.n52.sos.service.versions.v100;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import net.opengis.sos.x10.RegisterSensorResponseDocument;

import org.apache.xmlbeans.XmlException;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.sos.adapter.ISOSRequestBuilder;
import org.n52.oxf.sos.adapter.wrapper.builder.GetFeatureOfInterestParameterBuilder_v100;
import org.n52.oxf.sos.adapter.wrapper.builder.GetObservationParameterBuilder_v100;
import org.n52.oxf.sos.adapter.wrapper.builder.ObservationTemplateBuilder;
import org.n52.oxf.sos.request.v100.RegisterSensorParameters;
import org.slf4j.Logger;

import de.i3mainz.springframework.swe.n52.sos.SOSConnectionParameter;
import de.i3mainz.springframework.swe.n52.sos.exceptions.GetFOIServiceException;
import de.i3mainz.springframework.swe.n52.sos.exceptions.GetObservationException;
import de.i3mainz.springframework.swe.n52.sos.model.Sensor;
import de.i3mainz.springframework.swe.n52.sos.service.SOSServiceImpl;
import de.i3mainz.springframework.swe.n52.sos.util.DescriptionBuilder;

/**
 * @author Nikolai Bock
 *
 */
public class SOSServiceV100Impl extends SOSServiceImpl implements
        SOSServiceV100 {

    private static final Logger LOG = getLog();

    public SOSServiceV100Impl(SOSConnectionParameter connectionParameter) {
        super(connectionParameter);
        this.sensorDescBuilder = new DescriptionBuilder();
    }

    @Override
    public OperationResult getFeatureOfInterest(String foiID)
            throws GetFOIServiceException{
        GetFeatureOfInterestParameterBuilder_v100 builder = new GetFeatureOfInterestParameterBuilder_v100(
                foiID, ISOSRequestBuilder.GET_FOI_ID_PARAMETER);
        try {
            return getSosWrapper().doGetFeatureOfInterest(builder);
        } catch (OXFException | ExceptionReport e) {
            throw new GetFOIServiceException(e);
        }
    }

    public OperationResult getObservation(String offering,
            List<String> sensors, List<String> observedProperties,
            String srsName) throws GetObservationException {
        GetObservationParameterBuilder_v100 builder = new GetObservationParameterBuilder_v100(
                offering, observedProperties.get(0),
                "text/xml;subtype=\"om/1.0.0\"");
        for (Iterator<String> propertiesItr = observedProperties
                .listIterator(1); propertiesItr.hasNext();) {
            builder.addObservedProperty(propertiesItr.next());
        }
        if (sensors != null && !sensors.isEmpty()) {
            for (Iterator<String> sensorsIterator = sensors.iterator(); sensorsIterator
                    .hasNext();) {
                builder.addProcedure(sensorsIterator.next());
            }
        }
        if (srsName != null && !srsName.isEmpty()) {
            builder.addSrsName(srsName);
        }
        try {
            return getSosWrapper().doGetObservation(builder);
        } catch (OXFException | ExceptionReport e) {
            throw new GetObservationException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.i3mainz.springframework.swe.n52.sos.service.versions.v100.SOSServiceV100
     * #registerSensor(de.i3mainz.springframework.swe.n52.sos.model.Sensor)
     */
    @Override
    public String registerSensor(Sensor sensor) throws ExceptionReport {
        LOG.trace("registerSensor()");
        RegisterSensorParameters regSensorParameter;
        try {
            regSensorParameter = createRegisterSensorParametersFromRS(sensor);
            LOG.debug(regSensorParameter.getParameterNames().toString());
            for (String name : regSensorParameter.getParameterNames()) {
                LOG.debug(regSensorParameter.getAllValues(name).toString());
            }
            final OperationResult opResult = getSosWrapper().doRegisterSensor(
                    regSensorParameter);
            final RegisterSensorResponseDocument response = RegisterSensorResponseDocument.Factory
                    .parse(opResult.getIncomingResultAsStream());
            LOG.debug("RegisterSensorResponse parsed");
            return response.getRegisterSensorResponse().getAssignedSensorId();
        } catch (OXFException | XmlException | IOException e) {
            LOG.error("Exception", e);
        }

        return null;

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

}