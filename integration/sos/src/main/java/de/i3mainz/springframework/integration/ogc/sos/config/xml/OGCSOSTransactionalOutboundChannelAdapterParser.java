package de.i3mainz.springframework.integration.ogc.sos.config.xml;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import de.i3mainz.springframework.integration.ogc.sos.outbound.OGCSOSOutboundGateway;

public class OGCSOSTransactionalOutboundChannelAdapterParser extends OGCSOSOutboundChannelAdapterParser {

    @Override
    protected AbstractBeanDefinition parseConsumer(Element element, ParserContext parserContext) {
        String operationString = element.getAttribute("operation");

        OGCSOSTransactionalOperations operation = OGCSOSTransactionalOperations.valueOf(operationString);

        switch (operation) {
        case INSERTOBSERVATION:
            return parseConsumer(element, parserContext, OGCSOSOutboundGateway.class,
                    new OGCSOSInsertObservationExecutorFactory());
        default:
            parserContext.getReaderContext().error("The operation " + operationString + " is not supported!", element);
        }

        return null;
    }

    /* (non-Javadoc)
     * @see de.i3mainz.springframework.integration.ogc.sos.config.xml.OGCSOSOutboundChannelAdapterParser#parseConsumer(org.w3c.dom.Element, org.springframework.beans.factory.xml.ParserContext, java.lang.Class, de.i3mainz.springframework.integration.ogc.sos.config.xml.OGCSOSExecutorFactory)
     */
    @Override
    protected final AbstractBeanDefinition parseConsumer(Element element, ParserContext parserContext,
            Class<? extends OGCSOSOutboundGateway> clazz, OGCSOSExecutorFactory executorFactory) {
        return super.parseConsumer(element, parserContext, clazz, executorFactory);
    }
    
    

}
