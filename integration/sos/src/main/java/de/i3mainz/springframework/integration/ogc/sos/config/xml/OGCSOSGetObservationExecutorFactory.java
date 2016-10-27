/**
 * 
 */
package de.i3mainz.springframework.integration.ogc.sos.config.xml;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import de.i3mainz.springframework.integration.ogc.sos.core.OGCSOSGetObservationExecutor;

/**
 * @author Nikolai Bock
 *
 */
public class OGCSOSGetObservationExecutorFactory extends OGCSOSExecutorFactory {

    /*
     * (non-Javadoc)
     * 
     * @see de.i3mainz.springframework.integration.ogc.sos.config.xml.
     * OGCSOSExecutorFactory#addProperties(org.w3c.dom.Element,
     * org.springframework.beans.factory.xml.ParserContext,
     * org.springframework.beans.factory.support.BeanDefinitionBuilder)
     */
    @Override
    protected void addProperties(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        // Derzeit werden keine zusätzlich Parameter verarbeitet.
    }

    @Override
    public String createBean(Element element, ParserContext parserContext, String channelAdapterId) {
        return this.createBean(element, parserContext, OGCSOSGetObservationExecutor.class, channelAdapterId);
    }

}
