/**
 * 
 */
package de.i3mainz.springframework.integration.swe.sos.transformation;

import org.springframework.util.StringUtils;

import de.i3mainz.springframework.integration.swe.sos.transformation.util.SOSExpressionEvaluator;

/**
 * @author Nikolai Bock
 *
 */
public abstract class AbstractHeaderEnricher {

	/** The Expression-Evaluator */
	protected SOSExpressionEvaluator evaluator;

	/**
	 * @param evaluator
	 *            the evaluator to set
	 */
	public final void setEvaluator(SOSExpressionEvaluator evaluator) {
		this.evaluator = evaluator;
	}

	protected String createPropertiesInfo(String name, String uri, String mvt, String uom) {
		String nString = StringUtils.isEmpty(name) ? "text" : name;
		String uriString = StringUtils.isEmpty(uri) ? "urn:ogc:def:dataType:OGC:1.1:string" : uri;
		String mvtString = StringUtils.isEmpty(mvt) ? "TEXT" : mvt;
		String uomString = StringUtils.isEmpty(uom) ? "mg/m³" : uom;
		return "{\"name\":\"" + nString + "\", \"uri\":\"" + uriString + "\",\"mvt\": \"" + mvtString + "\", \"uom\":\""
				+ uomString + "\"}";
	}

}
