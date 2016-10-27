package de.i3mainz.springframework.integration.swe.sos.transformation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.messaging.Message;
import org.springframework.util.Assert;

import de.i3mainz.springframework.integration.swe.sos.transformation.util.FOICreator;
import de.i3mainz.springframework.integration.swe.sos.transformation.util.SensorCreator;
import de.i3mainz.springframework.swe.n52.sos.model.ObservationSensorInformation;
import de.i3mainz.springframework.swe.n52.sos.model.ObservationSensorInformationImpl;
import de.i3mainz.springframework.swe.n52.sos.model.requests.RegisterSensor;
import de.i3mainz.springframework.swe.n52.sos.util.RegisterSensorFactory;

public class RegisterSensorHeaderEnricher extends AbstractHeaderEnricher implements InitializingBean {

	private static final Log LOG = LogFactory.getLog(RegisterSensorHeaderEnricher.class);

	private FOICreator foiC;
	private SensorCreator sensorC;

	public RegisterSensorHeaderEnricher(SensorCreator sensorC) {
		this.sensorC = sensorC;
	}

	/**
	 * @param foiC
	 *            the foiC to set
	 */
	public final void setFoiC(FOICreator foiC) {
		this.foiC = foiC;
	}

	public RegisterSensor enrich(Message<?> message) {
		LOG.error("Durchlaufe den RegisterSensorHeaderEnricher");
		ObservationSensorInformation io = new ObservationSensorInformationImpl(sensorC.create(message),
				foiC.create(message));

		return new RegisterSensorFactory(io, createPropertiesInfo(null, null, null, null)).create();

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.isTrue(foiC != null, "FOICreator is to be set");
	}

}
