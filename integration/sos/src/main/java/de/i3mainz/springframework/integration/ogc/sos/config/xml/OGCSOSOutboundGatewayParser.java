/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.i3mainz.springframework.integration.ogc.sos.config.xml;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.integration.config.xml.AbstractConsumerEndpointParser;
import org.springframework.integration.config.xml.IntegrationNamespaceUtils;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import de.i3mainz.springframework.integration.ogc.sos.outbound.OGCSOSOutboundGateway;

/**
 * The Parser for SOSAdapter Outbound Gateway.
 * 
 * @author Nikolai Bock
 * @since 1.0
 * 
 */
public abstract class OGCSOSOutboundGatewayParser extends AbstractConsumerEndpointParser {

    @Override
    protected String getInputChannelAttributeName() {
        return "request-channel";
    }
    
    protected BeanDefinitionBuilder parseHandler(Element gatewayElement, ParserContext parserContext,
            Class<? extends OGCSOSOutboundGateway> clazz, OGCSOSExecutorFactory executorFactory) {

        final BeanDefinitionBuilder sosadapterOutboundGatewayBuilder = BeanDefinitionBuilder
                .genericBeanDefinition(clazz);

        IntegrationNamespaceUtils.setValueIfAttributeDefined(sosadapterOutboundGatewayBuilder, gatewayElement,
                "reply-timeout", "sendTimeout");

        final String replyChannel = gatewayElement.getAttribute("reply-channel");

        if (StringUtils.hasText(replyChannel)) {
            sosadapterOutboundGatewayBuilder.addPropertyReference("outputChannel", replyChannel);
        }

        final String gatewayId = this.resolveId(gatewayElement, sosadapterOutboundGatewayBuilder.getRawBeanDefinition(),
                parserContext);
        final String sosadapterExecutorBeanName = executorFactory.createBean(gatewayElement, parserContext, gatewayId);

        sosadapterOutboundGatewayBuilder.addConstructorArgReference(sosadapterExecutorBeanName);

        return sosadapterOutboundGatewayBuilder;

    }

}
