package de.i3mainz.springframework.integration.ogc.sos.config.xml;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import de.i3mainz.springframework.integration.ogc.sos.outbound.OGCSOSOutboundGateway;

public class OGCSOSTransactionalOutboundGatewayParser extends OGCSOSOutboundGatewayParser {

    @Override
    protected BeanDefinitionBuilder parseHandler(Element gatewayElement, ParserContext parserContext) {
        String operationString = gatewayElement.getAttribute("operation");

        OGCSOSTransactionalOperations operation = OGCSOSTransactionalOperations.valueOf(operationString);

        switch (operation) {
        case INSERTSENSOR:
            return parseHandler(gatewayElement, parserContext, OGCSOSOutboundGateway.class,
                    new OGCSOSInsertSensorExecutorFactory());

        default:
            parserContext.getReaderContext().error("The operation " + operationString + " is not supported!",
                    gatewayElement);
        }

        return null;

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
