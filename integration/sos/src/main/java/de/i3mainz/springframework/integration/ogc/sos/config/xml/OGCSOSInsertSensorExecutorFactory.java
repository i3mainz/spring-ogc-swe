/**
 * 
 */
package de.i3mainz.springframework.integration.ogc.sos.config.xml;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.integration.config.ExpressionFactoryBean;
import org.springframework.integration.config.xml.IntegrationNamespaceUtils;
import org.springframework.integration.handler.ExpressionEvaluatingMessageProcessor;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import de.i3mainz.springframework.integration.ogc.sos.core.OGCSOSInsertSensorExecutor;
import de.i3mainz.springframework.swe.n52.sos.model.requests.RegisterSensor;

/**
 * @author Nikolai Bock
 *
 */
public class OGCSOSInsertSensorExecutorFactory extends OGCSOSExecutorFactory {

    @Override
    public String createBean(Element element, ParserContext parserContext, String channelAdapterId) {
        return this.createBean(element, parserContext, OGCSOSInsertSensorExecutor.class, channelAdapterId);
    }

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
        IntegrationNamespaceUtils.setValueIfAttributeDefined(builder, element, "sensor");
        addExpression(element, parserContext, builder);
    }

    private void addExpression(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        String expressionString = element.getAttribute("expression");
        boolean isExpression = StringUtils.hasText(expressionString);

        if (isExpression) {
            RootBeanDefinition expressionDef = new RootBeanDefinition(ExpressionFactoryBean.class);
            expressionDef.getConstructorArgumentValues().addGenericArgumentValue(expressionString);

            RootBeanDefinition messageProdDef = new RootBeanDefinition(ExpressionEvaluatingMessageProcessor.class);
            messageProdDef.getConstructorArgumentValues().addGenericArgumentValue(expressionDef);
            messageProdDef.getConstructorArgumentValues().addGenericArgumentValue(RegisterSensor.class);
            builder.addPropertyValue("expression", messageProdDef);
        }

    }

}
