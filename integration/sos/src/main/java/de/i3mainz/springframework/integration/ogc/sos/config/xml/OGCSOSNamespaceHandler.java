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

import org.springframework.integration.config.xml.AbstractIntegrationNamespaceHandler;

/**
 * The namespace handler for the SOSAdapter namespace
 * 
 * @author Nikolai Bock
 * @since 1.0
 * 
 */
public class OGCSOSNamespaceHandler extends AbstractIntegrationNamespaceHandler {

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.beans.factory.xml.NamespaceHandler#init()
     */
    @Override
    public void init() {
        this.registerBeanDefinitionParser("inbound-channel-adapter", new OGCSOSInboundChannelAdapterParser());
        this.registerBeanDefinitionParser("outbound-gateway", new OGCSOSCoreOutboundGatewayParser());
        this.registerBeanDefinitionParser("transactional-outbound-channel-adapter",
                new OGCSOSTransactionalOutboundChannelAdapterParser());
        this.registerBeanDefinitionParser("transactional-outbound-gateway",
                new OGCSOSTransactionalOutboundGatewayParser());
    }
}
