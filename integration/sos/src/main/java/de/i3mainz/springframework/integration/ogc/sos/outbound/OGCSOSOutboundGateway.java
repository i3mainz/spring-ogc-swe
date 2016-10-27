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

package de.i3mainz.springframework.integration.ogc.sos.outbound;

import org.springframework.integration.handler.AbstractReplyProducingMessageHandler;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.util.Assert;

import de.i3mainz.springframework.integration.ogc.sos.core.OGCSOSExecutor;

/**
 * 
 * @author Nikolai Bock
 * @since 1.0
 * 
 */
public class OGCSOSOutboundGateway extends
        AbstractReplyProducingMessageHandler {

    private final OGCSOSExecutor sosadapterExecutor;

    /**
     * Field producesReply
     * False for Outbound-Channel-Adapter
     * True for Outbound-Gateway
     */
    private boolean producesReply = true; 

    /**
     * Constructor taking an {@link OGCSOSExecutor} that wraps common
     * SOSAdapter Operations.
     * 
     * @param sosadapterExecutor
     *            Must not be null
     * 
     */
    public OGCSOSOutboundGateway(OGCSOSExecutor sosadapterExecutor) {
        Assert.notNull(sosadapterExecutor,
                "sosadapterExecutor must not be null.");
        this.sosadapterExecutor = sosadapterExecutor;
    }

    @Override
    protected Object handleRequestMessage(Message<?> requestMessage) {

        final Object result;

        result = this.sosadapterExecutor
                .executeOutboundOperation(requestMessage);

        if (result == null || !producesReply) {
            return null;
        }

        return MessageBuilder.withPayload(result)
                .copyHeaders(requestMessage.getHeaders()).build();

    }

    /**
     * If set to 'false', this component will act as an Outbound Channel
     * Adapter. If not explicitly set this property will default to 'true'.
     * 
     * @param producesReply
     *            Defaults to 'true'.
     * 
     */
    public void setProducesReply(boolean producesReply) {
        this.producesReply = producesReply;
    }

}
