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

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.integration.config.xml.AbstractOutboundChannelAdapterParser;
import org.w3c.dom.Element;

import de.i3mainz.springframework.integration.ogc.sos.outbound.OGCSOSOutboundGateway;

/**
 * The parser for the SOSAdapter Outbound Channel Adapter.
 * 
 * @author Nikolai Bock
 * @since 1.0
 * 
 */
public abstract class OGCSOSOutboundChannelAdapterParser extends AbstractOutboundChannelAdapterParser {

    @Override
    protected boolean shouldGenerateId() {
        return false;
    }

    @Override
    protected boolean shouldGenerateIdAsFallback() {
        return true;
    }

    protected AbstractBeanDefinition parseConsumer(Element element, ParserContext parserContext,
            Class<? extends OGCSOSOutboundGateway> clazz, OGCSOSExecutorFactory executorFactory) {

        final BeanDefinitionBuilder sosadapterOutboundChannelAdapterBuilder = BeanDefinitionBuilder
                .genericBeanDefinition(clazz);
        final String channelAdapterId = this.resolveId(element,
                sosadapterOutboundChannelAdapterBuilder.getRawBeanDefinition(), parserContext);

        final String sosadapterExecutorBeanName = executorFactory.createBean(element, parserContext, channelAdapterId);

        sosadapterOutboundChannelAdapterBuilder.addConstructorArgReference(sosadapterExecutorBeanName);
        sosadapterOutboundChannelAdapterBuilder.addPropertyValue("producesReply", Boolean.FALSE);

        return sosadapterOutboundChannelAdapterBuilder.getBeanDefinition();

    }

}
