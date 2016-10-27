/**
 * 
 */
package de.i3mainz.springframework.integration.ogc.sos.config.xml;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.integration.config.xml.IntegrationNamespaceUtils;
import org.w3c.dom.Element;

import de.i3mainz.springframework.integration.ogc.sos.core.OGCSOSExecutor;
import de.i3mainz.springframework.swe.n52.sos.core.SOSTemplate;

/**
 * @author Nikolai Bock
 *
 */
public abstract class OGCSOSExecutorFactory {
    
    public abstract String createBean(Element element, ParserContext parserContext, String channelAdapterId);

    protected final String createBean(Element element, ParserContext parserContext,
            Class<? extends OGCSOSExecutor> excuteClazz, String channelAdapterId) {
        final BeanDefinitionBuilder sosadapterExecutorBuilder = OGCSOSParserUtils.getExecutorBuilder(element,
                parserContext, excuteClazz);

        addSosTemplate(element, parserContext, sosadapterExecutorBuilder);
        addProperties(element, parserContext, sosadapterExecutorBuilder);

        final BeanDefinition sosadapterExecutorBuilderBeanDefinition = sosadapterExecutorBuilder.getBeanDefinition();
        final String sosadapterExecutorBeanName = channelAdapterId + ".sosadapterExecutor";

        parserContext.registerBeanComponent(
                new BeanComponentDefinition(sosadapterExecutorBuilderBeanDefinition, sosadapterExecutorBeanName));
        return sosadapterExecutorBeanName;

    }

    protected void addSosTemplate(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        final BeanDefinitionBuilder sosTemplateBuilder = BeanDefinitionBuilder.genericBeanDefinition(SOSTemplate.class);
        IntegrationNamespaceUtils.setReferenceIfAttributeDefined(sosTemplateBuilder, element, "sos-connection",
                "connectionParameter");

        final BeanDefinition sosadapterSosTemplateBuilderBeanDefinition = sosTemplateBuilder.getBeanDefinition();

        final String sosadapterSosTemplateBeanName = element.getAttribute("sos-connection") + ".sosTemplate";
        parserContext.registerBeanComponent(
                new BeanComponentDefinition(sosadapterSosTemplateBuilderBeanDefinition, sosadapterSosTemplateBeanName));

        builder.addPropertyReference("sosConnection", sosadapterSosTemplateBeanName);
    }

    protected abstract void addProperties(Element element, ParserContext parserContext, BeanDefinitionBuilder builder);
}
