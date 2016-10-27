/**
 * 
 */
package de.i3mainz.springframework.integration.ogc.sos.config.xml;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import de.i3mainz.springframework.integration.ogc.sos.outbound.OGCSOSOutboundGateway;

/**
 * @author Nikolai Bock
 *
 */
public class OGCSOSCoreOutboundGatewayParser extends OGCSOSOutboundGatewayParser {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.integration.config.xml.AbstractConsumerEndpointParser
     * #parseHandler(org.w3c.dom.Element,
     * org.springframework.beans.factory.xml.ParserContext)
     */
    @Override
    protected BeanDefinitionBuilder parseHandler(Element element, ParserContext parserContext) {
        return parseHandler(element, parserContext, OGCSOSOutboundGateway.class,
                new OGCSOSGetObservationExecutorFactory());
    }

    /* (non-Javadoc)
     * @see de.i3mainz.springframework.integration.ogc.sos.config.xml.OGCSOSOutboundGatewayParser#parseHandler(org.w3c.dom.Element, org.springframework.beans.factory.xml.ParserContext, java.lang.Class, de.i3mainz.springframework.integration.ogc.sos.config.xml.OGCSOSExecutorFactory)
     */
    @Override
    protected final BeanDefinitionBuilder parseHandler(Element gatewayElement, ParserContext parserContext,
            Class<? extends OGCSOSOutboundGateway> clazz, OGCSOSExecutorFactory executorFactory) {
        return super.parseHandler(gatewayElement, parserContext, clazz, executorFactory);
    }
    
    

}
