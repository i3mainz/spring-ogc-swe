package de.i3mainz.springframework.swe.n52.sos.service.versions.v200;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.opengis.swes.x20.InsertSensorResponseDocument;

import org.apache.xmlbeans.XmlException;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.sos.adapter.ISOSRequestBuilder;
import org.n52.oxf.sos.request.v200.InsertSensorParameters;
import org.slf4j.Logger;

import de.i3mainz.springframework.swe.n52.sos.SOSConnectionParameter;
import de.i3mainz.springframework.swe.n52.sos.model.Sensor;
import de.i3mainz.springframework.swe.n52.sos.service.SOSServiceImpl;
import de.i3mainz.springframework.swe.n52.sos.util.DescriptionBuilder;

public class SOSServiceV200Impl extends SOSServiceImpl implements
        SOSServiceV200 {

    private static final Logger LOG = getLog();
    private static final String SML_101_FORMAT_URI = "http://www.opengis.net/sensorML/1.0.1";
    private static final String OM_200_SAMPLING_FEATURE = "http://www.opengis.net/def/samplingFeatureType/OGC-OM/2.0/SF_SamplingPoint";
    private Map<String, String> offerings;

    public SOSServiceV200Impl(SOSConnectionParameter connectionParameter) {
        super(connectionParameter);
        offerings = new HashMap<String, String>();
        this.sensorDescBuilder = new DescriptionBuilder(false);
    }

    @Override
    public OperationResult getFeatureOfInterest(String foiID)
            throws OXFException, ExceptionReport {
        throw new OXFException("Version 2.0.0 is not supported yet!");
    }
    
    @Override
    public OperationResult getObservation(String offering, List<String> sensor,
            List<String> observedProperties, String srsName) throws OXFException,
            ExceptionReport {
        throw new OXFException("Version 2.0.0 is not supported yet!");
    }


    @Override
    public String insertSensor(Sensor sensor) throws ExceptionReport {
        LOG.trace("insertSensor()");
        InsertSensorParameters insSensorParams;
        try {
            insSensorParams = createInsertSensorParametersFromRS(sensor);
            if (getSosBinding() != null) {
                insSensorParams.addParameterValue(ISOSRequestBuilder.BINDING,
                        getSosBinding().name());
            }
            LOG.info("SOSWrapper is null?: " + (getSosWrapper() == null));
            final OperationResult opResult = getSosWrapper().doInsertSensor(
                    insSensorParams);
            final InsertSensorResponseDocument response = InsertSensorResponseDocument.Factory
                    .parse(opResult.getIncomingResultAsStream());
            LOG.debug("InsertSensorResponse parsed");
            this.offerings.put(response.getInsertSensorResponse()
                    .getAssignedProcedure(), response.getInsertSensorResponse()
                    .getAssignedOffering());
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

    private InsertSensorParameters createInsertSensorParametersFromRS(
            final Sensor rs) throws XmlException, IOException {
        return new InsertSensorParameters(this.sensorDescBuilder.createSML(rs),
                SML_101_FORMAT_URI, getObservedPropertyURIs(null),
                Collections.singleton(OM_200_SAMPLING_FEATURE),
                getObservationTypeURIs(rs));
    }

    // /**
    // * @return the sensorDescBuilder
    // */
    // protected final DescriptionBuilder getSensorDescBuilder() {
    // return sensorDescBuilder;
    // }

    private String getURIForObservationType(final String measuredValueType) {
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
        final String errorMsg = String.format(
                "Observation type '%s' not supported!", measuredValueType);
        LOG.error(errorMsg);
        throw new IllegalArgumentException(errorMsg);
    }

    private Collection<String> getObservedPropertyURIs(
            final Collection<?> observedProperties) {
        if (observedProperties == null || observedProperties.isEmpty()) {
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
        tmp.add(getURIForObservationType("TEXT"));
        return tmp;
    }

    @Override
    public Map<String, String> getOfferings() {
        return this.offerings;
    }

}