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

import de.i3mainz.springframework.integration.ogc.sos.core.OGCSOSInsertObservationExecutor;
import de.i3mainz.springframework.swe.n52.sos.model.FeatureOfInterest;
import de.i3mainz.springframework.swe.n52.sos.model.Observation;
import de.i3mainz.springframework.swe.n52.sos.util.InsertObservationFactory;

/**
 * @author Nikolai Bock
 *
 */
public class OGCSOSInsertObservationExecutorFactory extends OGCSOSExecutorFactory {

    @Override
    public String createBean(Element element, ParserContext parserContext, String channelAdapterId) {
        return this.createBean(element, parserContext, OGCSOSInsertObservationExecutor.class, channelAdapterId);
    }

    @Override
    public void addProperties(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        IntegrationNamespaceUtils.setValueIfAttributeDefined(builder, element, "sensor");
        IntegrationNamespaceUtils.setValueIfAttributeDefined(builder, element, "propertyInformation");
        addFOI(element, parserContext, builder);
        addInsertObservationFactory(element, parserContext, builder);
        addExpression(element, parserContext, builder);
    }

    private void addFOI(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        String foiRef = element.getAttribute("foi-ref");
        String foiExpressionString = element.getAttribute("foi-expression");

        boolean isRef = StringUtils.hasText(foiRef);
        boolean isExpression = StringUtils.hasText(foiExpressionString);

        if (isRef && isExpression) {
            parserContext.getReaderContext()
                    .error("The 'foi-ref' attribute cannot be used with the 'foi-expression' attribute.", element);
        }

        if (isExpression) {
            RootBeanDefinition expressionDef = new RootBeanDefinition(ExpressionFactoryBean.class);
            expressionDef.getConstructorArgumentValues().addGenericArgumentValue(foiExpressionString);

            RootBeanDefinition messageProdDef = new RootBeanDefinition(ExpressionEvaluatingMessageProcessor.class);
            messageProdDef.getConstructorArgumentValues().addGenericArgumentValue(expressionDef);
            messageProdDef.getConstructorArgumentValues().addGenericArgumentValue(FeatureOfInterest.class);
            builder.addPropertyValue("foiExpression", messageProdDef);
        } else if (isRef) {
            IntegrationNamespaceUtils.setReferenceIfAttributeDefined(builder, element, "foi-ref");
        }
    }

    private void addInsertObservationFactory(Element element, ParserContext parserContext,
            BeanDefinitionBuilder builder) {
        String insObservFRef = element.getAttribute("insertObservationFactory");
        String insObservFExpressionString = element.getAttribute("iOF-expression");
        boolean isRef = StringUtils.hasText(insObservFRef);
        boolean isExpression = StringUtils.hasText(insObservFExpressionString);

        if (isRef && isExpression) {
            parserContext.getReaderContext().error(
                    "The 'insertObservationFactory' attribute cannot be used with the 'iOF-expression' attribute.",
                    element);
        }

        if (isExpression) {
            RootBeanDefinition expressionDef = new RootBeanDefinition(ExpressionFactoryBean.class);
            expressionDef.getConstructorArgumentValues().addGenericArgumentValue(insObservFExpressionString);

            RootBeanDefinition messageProdDef = new RootBeanDefinition(ExpressionEvaluatingMessageProcessor.class);
            messageProdDef.getConstructorArgumentValues().addGenericArgumentValue(expressionDef);
            messageProdDef.getConstructorArgumentValues().addGenericArgumentValue(InsertObservationFactory.class);
            builder.addPropertyValue("iOFExpression", messageProdDef);
        } else if (isRef) {
            IntegrationNamespaceUtils.setReferenceIfAttributeDefined(builder, element, "insertObservationFactory");
        }
    }

    private void addExpression(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        String expressionString = element.getAttribute("expression");
        boolean isExpression = StringUtils.hasText(expressionString);

        if (isExpression) {
            RootBeanDefinition expressionDef = new RootBeanDefinition(ExpressionFactoryBean.class);
            expressionDef.getConstructorArgumentValues().addGenericArgumentValue(expressionString);

            RootBeanDefinition messageProdDef = new RootBeanDefinition(ExpressionEvaluatingMessageProcessor.class);
            messageProdDef.getConstructorArgumentValues().addGenericArgumentValue(expressionDef);
            messageProdDef.getConstructorArgumentValues().addGenericArgumentValue(Observation.class);
            builder.addPropertyValue("expression", messageProdDef);
        }

    }

}
