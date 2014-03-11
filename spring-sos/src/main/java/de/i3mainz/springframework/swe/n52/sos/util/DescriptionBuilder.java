package de.i3mainz.springframework.swe.n52.sos.util;

import java.io.IOException;

import org.apache.xmlbeans.XmlException;
import org.n52.oxf.sos.adapter.wrapper.builder.SensorDescriptionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.i3mainz.springframework.swe.n52.sos.model.Sensor;

public class DescriptionBuilder {

	private final boolean shouldAddOfferingMetadataToOutputs;

	public DescriptionBuilder(final boolean shouldAddOfferingMetadataToOutputs) {
		this.shouldAddOfferingMetadataToOutputs = shouldAddOfferingMetadataToOutputs;
	}

	public DescriptionBuilder() {
		this(true);
	}

	private static final Logger LOG = LoggerFactory
			.getLogger(DescriptionBuilder.class);

	public String createSML(final Sensor rs) throws XmlException, IOException {
		LOG.trace("createSML()");
		final SensorDescriptionBuilder builder = new SensorDescriptionBuilder();

		builder.setAddOfferingMetadataToOutputs(shouldAddOfferingMetadataToOutputs);

//		final StringBuilder intendedApplication = new StringBuilder();

		// add keywords
		builder.addKeyword("Twitter Sentiment");
		builder.addKeyword(rs.getName());

		// add all identifier
		builder.setIdentifierUniqeId(rs.getId());
		builder.setIdentifierLongName(rs.getName());
		builder.setIdentifierShortName(rs.getName());

		// set capabilities - status
		builder.setCapabilityCollectingStatus("status", true);

		builder.addCapability("offerings", rs.getOffering().getName(),
				"urn:ogc:def:identifier:OGC:1.0:offeringID", rs.getOffering()
						.getId());

		// set position data
		builder.setPosition("sensorPosition", "EPSG:4326", "SYSTEM_LOCATION",
				"degree", 8.27, "degree", 50.00, "m", 88.00);

		builder.addInput("text", "urn:ogc:def:dataType:OGC:1.1:string");
		builder.addOutputText("text", "urn:ogc:def:dataType:OGC:1.1:string", rs
				.getOffering().getId(), rs.getOffering().getName());
		
		// add keyword
//		builder.addKeyword("text");
//					intendedApplication.append("text");
//					intendedApplication.append(", ");

		

		// add all classifier
//		builder.setClassifierIntendedApplication(intendedApplication.substring(
//				0, intendedApplication.length() - 2));

		// add validTime starting from now
//		builder.setValidTime(new Timestamp().set(System.currentTimeMillis())
//				.toString(), "unknown");

		return builder.buildSensorDescription();
	}
}
