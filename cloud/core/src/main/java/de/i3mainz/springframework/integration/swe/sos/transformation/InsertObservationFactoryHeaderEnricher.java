/**
 * 
 */
package de.i3mainz.springframework.integration.swe.sos.transformation;

import org.springframework.messaging.Message;

import de.i3mainz.springframework.integration.swe.sos.transformation.util.SensorCreator;
import de.i3mainz.springframework.swe.n52.sos.util.InsertObservationFactory;

/**
 * @author Nikolai Bock
 *
 */
public class InsertObservationFactoryHeaderEnricher extends AbstractHeaderEnricher {

	private SensorCreator sensorC;

	public InsertObservationFactoryHeaderEnricher(SensorCreator sensorC) {
		this.sensorC = sensorC;
	}

	public InsertObservationFactory enrich(Message<?> message) {
		return new InsertObservationFactory(this.sensorC.create(message).getId(),
				createPropertiesInfo(null, null, null, null));
	}

}
