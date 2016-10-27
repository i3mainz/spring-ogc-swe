package de.i3mainz.springframework.swe.sos;

import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.n52.oxf.feature.OXFFeature;
import org.n52.oxf.feature.OXFFeatureCollection;
import org.n52.oxf.ows.capabilities.Contents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Component;

import de.i3mainz.springframework.swe.n52.sos.core.SensorObservationServiceOperations;
import de.i3mainz.springframework.swe.n52.sos.exceptions.GetFOIServiceException;
import de.i3mainz.springframework.swe.n52.sos.exceptions.GetObservationException;
import de.i3mainz.springframework.swe.n52.sos.exceptions.InsertObservationException;
import de.i3mainz.springframework.swe.n52.sos.exceptions.RegisterSensorException;
import de.i3mainz.springframework.swe.n52.sos.model.FeatureOfInterest;
import de.i3mainz.springframework.swe.n52.sos.model.Observation;
import de.i3mainz.springframework.swe.n52.sos.model.Sensor;
import de.i3mainz.springframework.swe.n52.sos.model.TemporalRequestFilter;
import de.i3mainz.springframework.swe.n52.sos.model.requests.InsertObservation;
import de.i3mainz.springframework.swe.n52.sos.model.requests.RegisterSensor;

/**
 * {@link SOSService} with hard-coded input data.
 */
@Component
public class SOSServiceImp implements SOSService {

    private static final Logger LOG = LoggerFactory.getLogger(SOSServiceImp.class);

    private SensorObservationServiceOperations sos;
    URL url;

    public String getSosVersion() {
        if (sos.getCapabilities() != null) {
            System.out.println(sos.getCapabilities().getServiceIdentification().getTitle());

            Contents contents = sos.getCapabilities().getContents();
            if (contents != null) {
                if (contents.getDataIdentificationCount() > 0) {
                    Arrays.asList(contents.getDataIdentificationIDArray()).forEach(LOG::debug);
                }
            }
            return sos.getCapabilities().getVersion();
        }
        return "";
    }

    @Required
    @Autowired
    public final void setSos(SensorObservationServiceOperations sos) {
        this.sos = sos;
    }

    @Override
    public String insertSensor(RegisterSensor sensor) throws RegisterSensorException {
        return sos.registerSensor(sensor);
    }

    @Override
    public boolean isSOSAvailable() {
        return sos.isSOSAvailable();
    }

    @Override
    public boolean isSOSTransactional() {
        return sos.isSOSTransactional();
    }

    @Override
    public String insertObservation(Sensor sensor, FeatureOfInterest foi, Observation observation)
            throws InsertObservationException {
        return sos.insertObservation(sensor.getId(), foi, observation);
    }

    @Override
    public String insertObservation(InsertObservation io) throws InsertObservationException {
        return sos.insertObservation(io);
    }

    @Override
    public String getFeatureOfInterest(String foiID) throws GetFOIServiceException {
        return sos.getFeatureOfInterest(foiID).toList().stream().map(OXFFeature::toString)
                .collect(Collectors.joining("; "));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.i3mainz.springframework.swe.sos.SOSService#getObservation(java.lang
     * .String, java.util.List, java.util.List)
     */
    @Override
    public String getObservation(String offeringId, List<String> sensors, List<String> observedProperties,
            String srsName, TemporalRequestFilter tempFilter) throws GetObservationException {
        OXFFeatureCollection features = sos.getObservation(offeringId, sensors, observedProperties, srsName,
                tempFilter);
        StringBuilder stringBuilder = new StringBuilder();
        for (Iterator<OXFFeature> iterator = features.iterator(); iterator.hasNext();) {
            OXFFeature feature = iterator.next();
            stringBuilder.append("Observation: " + feature.getID());
            stringBuilder.append("; Attributes: ");
            String[] attributeStrings = feature.getSpecifiedAttributes();
            for (String attributeName : attributeStrings) {
                stringBuilder.append(attributeName + ": ");
                if (attributeName.equalsIgnoreCase("featureOfInterest")) {
                    OXFFeature foi = (OXFFeature) feature.getAttribute(attributeName);
                    if (foi.getGeometry() != null) {
                        stringBuilder.append(foi.getGeometry().toText());
                    }
                } else {
                    stringBuilder.append(feature.getAttribute(attributeName));
                }
                stringBuilder.append(", \t\t");
            }
            stringBuilder.append("; ");
            if (feature.getGeometry() != null) {
                stringBuilder.append(feature.getGeometry().toText());
            }
            stringBuilder.append("; ");
            stringBuilder.append(" \n ");
        }
        return stringBuilder.toString();
    }

}
