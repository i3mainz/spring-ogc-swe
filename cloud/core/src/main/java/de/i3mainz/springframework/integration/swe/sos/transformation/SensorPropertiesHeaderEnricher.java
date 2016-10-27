/**
 * 
 */
package de.i3mainz.springframework.integration.swe.sos.transformation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.messaging.Message;

/**
 * @author Nikolai Bock
 *
 */
public class SensorPropertiesHeaderEnricher extends AbstractHeaderEnricher {

	private static final Log LOG = LogFactory.getLog(SensorPropertiesHeaderEnricher.class);

	private String propertyExpression;
	private String uomExpression;

	/**
	 * @param propertyExpression
	 *            the propertyExpression to set
	 */
	public final void setPropertyExpression(String propertyExpression) {
		this.propertyExpression = propertyExpression;
	}

	/**
	 * @param uomExpression
	 *            the uomExpression to set
	 */
	public final void setUomExpression(String uomExpression) {
		this.uomExpression = uomExpression;
	}

	public String enrich(Message<?> message) {
		LOG.error("Durchlaufe den SensorPropertiesEnricher");
		return createPropertiesInfo(null, null, null, null);
	}

}
