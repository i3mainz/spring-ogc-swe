package de.i3mainz.springframework.swe.n52.sos.core;

//import static org.n52.sos.importer.feeder.Configuration.SOS_OBSERVATION_TYPE_BOOLEAN;
//import static org.n52.sos.importer.feeder.Configuration.SOS_OBSERVATION_TYPE_COUNT;
//import static org.n52.sos.importer.feeder.Configuration.SOS_OBSERVATION_TYPE_TEXT;

import java.util.Collections;
import java.util.Iterator;

import org.apache.commons.lang.NotImplementedException;
import org.n52.oxf.OXFException;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.OWSException;
import org.n52.oxf.ows.OwsExceptionCode;
import org.n52.oxf.sos.adapter.ISOSRequestBuilder;
import org.n52.oxf.sos.observation.ObservationParameters;
import org.n52.oxf.sos.observation.TextObservationParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.i3mainz.springframework.ogc.OWSConnectionParameter;
import de.i3mainz.springframework.ogc.core.OWSAccessor;
import de.i3mainz.springframework.swe.n52.sos.SOSConnectionParameter;
import de.i3mainz.springframework.swe.n52.sos.model.FeatureOfInterest;
import de.i3mainz.springframework.swe.n52.sos.model.Observation;
import de.i3mainz.springframework.swe.n52.sos.model.Sensor;
import de.i3mainz.springframework.swe.n52.sos.service.SOSService;
import de.i3mainz.springframework.swe.n52.sos.service.versions.v100.SOSServiceV100Impl;
import de.i3mainz.springframework.swe.n52.sos.service.versions.v200.SOSServiceV200;
import de.i3mainz.springframework.swe.n52.sos.service.versions.v200.SOSServiceV200Impl;
import de.i3mainz.springframework.swe.n52.sos.util.Configuration;

public abstract class SOSAccessor extends OWSAccessor {

    private static final Logger LOG = LoggerFactory
            .getLogger(SOSTemplate.class);
    protected static final String VERSION200 = "2.0.0";
    protected static final String VERSION100 = "1.0.0";
    private SOSService service;

    /**
     * @return the log
     */
    protected static final Logger getLog() {
        return LOG;
    }

    @Override
    public void setConnectionParameter(
            OWSConnectionParameter owsConnectionParameter) {
        super.setConnectionParameter(owsConnectionParameter);
        // this.sosBinding =
        // ((de.i3mainz.springframework.swe.n52.sos.SOSConnectionParameter)
        // getConnectionParameter())
        // .getBinding();
        // // sensorDescBuilder = new DescriptionBuilder();
        // try {
        // this.sosWrapper = SosWrapperFactory.newInstance(
        // owsConnectionParameter.getUrl(),
        // owsConnectionParameter.getVersion(), this.sosBinding);
        //
        // if (VERSION200.equals(getConnectionParameter().getVersion())) {
        // offerings = new HashMap<String, String>();
        // }
        // } catch (ExceptionReport e1) {
        // LOG.error("SOS-Server throws Exception.", e1);
        // } catch (OXFException e1) {
        // LOG.error("Error while accessing SOS-Service", e1);
        // }
        if (VERSION100.equals(getConnectionParameter().getVersion())) {
            this.service = new SOSServiceV100Impl(
                    (SOSConnectionParameter) getConnectionParameter());
        } else if (VERSION200.equals(getConnectionParameter().getVersion())) {
            this.service = new SOSServiceV200Impl(
                    (SOSConnectionParameter) getConnectionParameter());
        } else {
            throw new NotImplementedException("Version "
                    + getConnectionParameter().getVersion()
                    + "not supported by SOS-Services");
        }
    }

    /**
     * @return the service
     */
    protected final SOSService getService() {
        return service;
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
        // obsParameter.addObservedProperty("urn:ogc:def:dataType:OGC:1.1:string");
        obsParameter.addNewFoiId(foi.getId());
        obsParameter.addNewFoiName(foi.getName());
        obsParameter.addFoiDescription(foi.getId());
        // position
        // boolean eastingFirst = false;
        // if (Configuration.EPSG_EASTING_FIRST_MAP.get(io.getEpsgCode()) ==
        // null) {
        // Configuration.EPSG_EASTING_FIRST_MAP.get("default");
        // } else {
        // eastingFirst = Configuration.EPSG_EASTING_FIRST_MAP.get(io
        // .getEpsgCode());
        // }
        // String pos = eastingFirst ? String.format("%s %s",
        // io.getLongitudeValue(), io.getLatitudeValue()) : String.format(
        // "%s %s", io.getLatitudeValue(), io.getLongitudeValue());
        // if (io.isSetAltitudeValue()) {
        // pos = String.format("% %", pos, io.getAltitudeValue());
        // }

        String pos = foi.getPosition();
        obsParameter.addFoiPosition(pos);
        // obsParameter.addObservedProperty("urn:ogc:def:dataType:OGC:1.1:string");
        obsParameter.addProcedure(sensorID);

        if (VERSION200.equalsIgnoreCase(getConnectionParameter().getVersion())) {
            obsParameter.addSrsPosition(Configuration.SOS_200_EPSG_CODE_PREFIX
                    + "4326");
            obsParameter.addPhenomenonTime(observation.getTimeAsSTring());
            obsParameter.addResultTime(observation.getTimeAsSTring());
            return new org.n52.oxf.sos.request.v200.InsertObservationParameters(
                    obsParameter, Collections.singletonList(sensorID));
        }

        // obsParameter.addSrsPosition(Configuration.SOS_100_EPSG_CODE_PREFIX+"4326");
        // obsParameter.addParameterValue("srsName",
        // Configuration.SOS_100_EPSG_CODE_PREFIX+"4326");
        // obsParameter.addSamplingTime(observation.getTimeAsSTring());
        obsParameter.addParameterValue(
                ISOSRequestBuilder.INSERT_OBSERVATION_SAMPLING_TIME,
                observation.getTimeAsSTring());
        return new org.n52.oxf.sos.request.v100.InsertObservationParameters(
                obsParameter);
    }

    protected String handleExceptionReportException(ExceptionReport e, Sensor rs) {
        // Handle already registered sensor case here (happens when the
        // sensor is registered but not listed in the capabilities):
        final Iterator<OWSException> iter = e.getExceptionsIterator();
        while (iter.hasNext()) {
            final OWSException owsEx = iter.next();
            if (owsEx.getExceptionCode().equals(
                    OwsExceptionCode.NoApplicableCode.name())
                    && owsEx.getExceptionTexts() != null
                    && owsEx.getExceptionTexts().length > 0) {
                handleNotApplicableCode(owsEx, rs);
            } else if (owsEx.getExceptionCode().equals(
                    OwsExceptionCode.InvalidParameterValue.name())
                    && "offeringIdentifier".equals(owsEx.getLocator())
                    && owsEx.getExceptionTexts() != null
                    && owsEx.getExceptionTexts().length > 0) {
                handleOfferingExists(owsEx, rs);
            }

        }
        LOG.error(String.format("Exception thrown: %s", e.getMessage()), e);
        return null;
    }

    private String handleNotApplicableCode(OWSException owsEx, Sensor rs) {
        for (final String string : owsEx.getExceptionTexts()) {
            if (string
                    .indexOf(Configuration.SOS_SENSOR_ALREADY_REGISTERED_MESSAGE_START) > -1
                    && string
                            .indexOf(Configuration.SOS_SENSOR_ALREADY_REGISTERED_MESSAGE_END) > -1) {
                return rs.getId();
            }
        }
        return null;
    }

    private String handleOfferingExists(OWSException owsEx, Sensor rs) {
        // handle offering already contained case here
        for (final String string : owsEx.getExceptionTexts()) {
            if (string
                    .indexOf(Configuration.SOS_200_OFFERING_ALREADY_REGISTERED_MESSAGE_START) > -1
                    && string
                            .indexOf(Configuration.SOS_200_OFFERING_ALREADY_REGISTERED_MESSAGE_END) > -1) {
                ((SOSServiceV200) getService()).getOfferings().put(rs.getId(),
                        rs.getOffering().getId());
                return rs.getId();
            }
        }
        return null;
    }

    public void afterPropertiesSet() throws Exception {
    }

}