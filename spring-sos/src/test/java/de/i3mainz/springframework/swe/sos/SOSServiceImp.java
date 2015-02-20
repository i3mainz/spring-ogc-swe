package de.i3mainz.springframework.swe.sos;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.n52.oxf.OXFException;
import org.n52.oxf.feature.OXFFeature;
import org.n52.oxf.feature.OXFFeatureCollection;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.capabilities.Contents;
import org.n52.oxf.ows.capabilities.Dataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Component;

import de.i3mainz.springframework.swe.n52.sos.core.SensorObservationServiceOperations;
import de.i3mainz.springframework.swe.n52.sos.model.FeatureOfInterest;
import de.i3mainz.springframework.swe.n52.sos.model.Observation;
import de.i3mainz.springframework.swe.n52.sos.model.Sensor;

/**
 * {@link SOSService} with hard-coded input data.
 */
@Component
public class SOSServiceImp implements SOSService {

    private SensorObservationServiceOperations sos;
    URL url;

    public String getSosVersion() {
        if (sos.getCapabilities() != null) {
            System.out.println(sos.getCapabilities().getServiceIdentification()
                    .getTitle());

            Contents contents = sos.getCapabilities().getContents();
            if (contents != null) {
                if (contents.getDataIdentificationCount() > 0) {
                    for (int i = 0; i < contents.getDataIdentificationCount(); i++) {
                        Dataset ds = contents.getDataIdentification(i);
                        System.out.println("DataIdentification: "
                                + ds.getIdentifier());
                    }
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

    // public int getObservation() {
    // return sos.getObservation().size();
    // }

    // public void insertSensor() {
    // // SENSOR
    // final Sensor sensor = new Sensor(name, uri);
    // // FEATURE OF INTEREST incl. Position
    // final FeatureOfInterest foi = new FeatureOfInterest(name, uri, new
    // Position(values, units, epsgCode));
    // // VALUE
    // final Object value = "Hallo Welt";
    // // TODO implement using different templates in later version depending
    // // on the class of value
    // // TIMESTAMP
    // final String timeStamp = "test";
    // // UOM CODE
    // final UnitOfMeasurement uom = new UnitOfMeasurement(timeStamp,
    // timeStamp);
    // // OBSERVED_PROPERTY
    // final ObservedProperty observedProperty = new ObservedProperty(name,
    // uri);
    // final Offering offer = new Offering(name, uri);
    // InsertObservation io = new InsertObservation(sensor, foi, value,
    // timeStamp, uom,
    // observedProperty, offer, dataFile.getType(mVColumnId));
    //
    // RegisterSensor rs = new RegisterSensor(io, observedProperties,
    // measuredValueTypes, unitOfMeasurements);
    // sos.registerSensor(null);
    //
    // }

    @Override
    public String insertSensor(Sensor sensor) {
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
    public String insertObservation(Sensor sensor, FeatureOfInterest foi,
            Observation observation) throws IOException {
        return sos.insertObservation(sensor.getId(), foi, observation);
    }

    @Override
    public String getFeatureOfInterest(String foiID) throws OXFException,
            ExceptionReport {

        OXFFeatureCollection fois = sos.getFeatureOfInterest(foiID);
        StringBuilder stringBuilder = new StringBuilder();
        for (Iterator<OXFFeature> iterator = fois.iterator(); iterator
                .hasNext();) {
            OXFFeature feature = iterator.next();
            stringBuilder.append(feature.toString());
            stringBuilder.append("; ");
        }
        return stringBuilder.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.i3mainz.springframework.swe.sos.SOSService#getObservation(java.lang
     * .String, java.util.List, java.util.List)
     */
    @Override
    public String getObservation(String offeringId, List<String> sensors,
            List<String> observedProperties, String srsName)
            throws OXFException, ExceptionReport {
        OXFFeatureCollection features = sos.getObservation(offeringId, sensors,
                observedProperties, srsName);
        StringBuilder stringBuilder = new StringBuilder();
        for (Iterator<OXFFeature> iterator = features.iterator(); iterator
                .hasNext();) {
            OXFFeature feature = iterator.next();
            stringBuilder.append("Observation: " + feature.getID());
            stringBuilder.append("; Attributes: ");
            String[] attributeStrings = feature.getSpecifiedAttributes();
            for (String attributeName : attributeStrings) {
                stringBuilder.append(attributeName + ": ");
                if (attributeName.equalsIgnoreCase("featureOfInterest")) {
                    OXFFeature foi = (OXFFeature) feature
                            .getAttribute(attributeName);
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
