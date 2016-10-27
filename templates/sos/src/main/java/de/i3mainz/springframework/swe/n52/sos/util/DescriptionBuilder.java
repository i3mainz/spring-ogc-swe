package de.i3mainz.springframework.swe.n52.sos.util;

import java.util.Date;

import org.n52.oxf.OXFException;
import org.n52.oxf.sos.adapter.wrapper.builder.SensorDescriptionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.i3mainz.springframework.swe.n52.sos.model.ObservedProperty;
import de.i3mainz.springframework.swe.n52.sos.model.Timestamp;
import de.i3mainz.springframework.swe.n52.sos.model.requests.RegisterSensor;

public class DescriptionBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(DescriptionBuilder.class);

    private final boolean shouldAddOfferingMetadataToOutputs;

    public DescriptionBuilder(final boolean shouldAddOfferingMetadataToOutputs) {
        this.shouldAddOfferingMetadataToOutputs = shouldAddOfferingMetadataToOutputs;
    }

    public DescriptionBuilder() {
        this(true);
    }

    public String createSML(final RegisterSensor rs) throws OXFException {
        LOG.trace("createSML()");
        final SensorDescriptionBuilder builder = new SensorDescriptionBuilder();

        builder.setAddOfferingMetadataToOutputs(shouldAddOfferingMetadataToOutputs);

        final StringBuilder intendedApplication = new StringBuilder();

        // add keywords
        builder.addKeyword(rs.getFeatureOfInterestName());
        builder.addKeyword(rs.getSensorName());

        // add all identifier
        builder.setIdentifierUniqeId(rs.getSensorURI());
        builder.setIdentifierLongName(rs.getSensorName());
        builder.setIdentifierShortName(rs.getSensorName());

        // set capabilities - status
        builder.setCapabilityCollectingStatus("status", true);

        builder.addFeatureOfInterest(rs.getFeatureOfInterestName(), rs.getFeatureOfInterestURI());

        // builder.setCapabilityBbox(rs.getLongitudeUnit(),
        // rs.getLongitudeValue(), rs.getLatitudeUnit(),
        // rs.getLatitudeValue(), rs.getLongitudeUnit(),
        // rs.getLongitudeValue(), rs.getLatitudeUnit(),
        // rs.getLatitudeValue(),
        // rs.getEpsgCode().equalsIgnoreCase(Integer.toString(4979))?
        // Integer.toString(4326):
        // rs.getEpsgCode());

        builder.addCapability("offerings", rs.getOfferingName(), "urn:ogc:def:identifier:OGC:1.0:offeringID",
                rs.getOfferingUri());

        // set position data
        builder.setPosition("sensorPosition", rs.getEpsgCode(), "SYSTEM_LOCATION", rs.getLongitudeUnit(),
                rs.getLongitudeValue(), rs.getLatitudeUnit(), rs.getLatitudeValue(), rs.getAltitudeUnit(),
                rs.getAltitudeValue());

        if (rs.getObservedProperties() == null || rs.getObservedProperties().isEmpty()) {
            builder.addInput("text", "urn:ogc:def:dataType:OGC:1.1:string");
            builder.addOutputText("text", "urn:ogc:def:dataType:OGC:1.1:string", rs.getOfferingUri(),
                    rs.getOfferingName());
            // add keyword
            builder.addKeyword("text");
            intendedApplication.append("text");
            intendedApplication.append(", ");
        } else {
            for (ObservedProperty observedProperty : rs.getObservedProperties()) {
                builder.addInput(observedProperty.getName(), observedProperty.getUri());
                switch (rs.getMeasuredValueType(observedProperty)) {
                case BOOLEAN:
                    builder.addOutputBoolean(observedProperty.getName(), observedProperty.getUri(), rs.getOfferingUri(),
                            rs.getOfferingName());
                    break;
                case COUNT:
                    builder.addOutputCount(observedProperty.getName(), observedProperty.getUri(), rs.getOfferingUri(),
                            rs.getOfferingName());
                    break;
                case MEASUREMENT:
                    builder.addOutputMeasurement(observedProperty.getName(), observedProperty.getUri(),
                            rs.getOfferingUri(), rs.getOfferingName(), rs.getUnitOfMeasurementCode(observedProperty));
                    break;
                case TEXT:
                    builder.addOutputText(observedProperty.getName(), observedProperty.getUri(), rs.getOfferingUri(),
                            rs.getOfferingName());
                    break;
                default:
                    throw new OXFException(
                            "MeasuredValueType " + rs.getMeasuredValueType(observedProperty) + " is not supported!");
                }
                // add keyword
                builder.addKeyword(observedProperty.getName());
                intendedApplication.append(observedProperty.getName());
                intendedApplication.append(", ");

            }
        }
        // add all classifier
        builder.setClassifierIntendedApplication(intendedApplication.substring(0, intendedApplication.length() - 2));

        // add validTime starting from now
        builder.setValidTime(new Timestamp().setDate(new Date()).toString(), "unknown");

        return builder.buildSensorDescription();
    }
}