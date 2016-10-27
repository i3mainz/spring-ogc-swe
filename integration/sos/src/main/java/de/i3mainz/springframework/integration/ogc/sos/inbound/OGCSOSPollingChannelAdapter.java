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
package de.i3mainz.springframework.integration.ogc.sos.inbound;

import org.springframework.integration.context.IntegrationObjectSupport;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.util.Assert;

import de.i3mainz.springframework.integration.ogc.sos.core.OGCSOSExecutor;

/**
 * @author Nikolai Bock
 * @since 1.0
 *
 */
public class OGCSOSPollingChannelAdapter extends IntegrationObjectSupport implements MessageSource<Object> {

    private final OGCSOSExecutor sosadapterExecutor;

    /**
     * Constructor taking a {@link OGCSOSExecutor} that provide all required
     * SOSAdapter functionality.
     *
     * @param sosadapterExecutor
     *            Must not be null.
     */
    public OGCSOSPollingChannelAdapter(OGCSOSExecutor sosadapterExecutor) {
        super();
        Assert.notNull(sosadapterExecutor, "sosadapterExecutor must not be null.");
        this.sosadapterExecutor = sosadapterExecutor;
    }

    /**
     * Check for mandatory attributes
     */
    @Override
    protected void onInit() throws Exception {
        super.onInit();
    }

    /**
     * Uses {@link OGCSOSExecutor#poll()} to executes the SOSAdapter operation.
     *
     * If {@link OGCSOSExecutor#poll()} returns null, this method will return
     * <code>null</code>. Otherwise, a new {@link Message} is constructed and
     * returned.
     */
    /* (non-Javadoc)
     * @see org.springframework.integration.core.MessageSource#receive()
     */
    @Override
    public Message<Object> receive() {

        final Object payload = sosadapterExecutor.poll();

        if (payload == null) {
            return null;
        }

        return MessageBuilder.withPayload(payload).build();
    }

    @Override
    public String getComponentType() {
        return "sosadapter:inbound-channel-adapter";
    }

}
