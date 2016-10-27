/* Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package de.i3mainz.springframework.integration.ogc.sos.config.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.channel.AbstractMessageChannel;
import org.springframework.integration.endpoint.SourcePollingChannelAdapter;
import org.springframework.integration.test.util.TestUtils;

import de.i3mainz.springframework.integration.ogc.sos.core.OGCSOSExecutor;

/**
 * @author Nikolai Bock
 * @since 1.0
 *
 */
public class OGCSOSInboundChannelAdapterParserTests {

    private ConfigurableApplicationContext context;

    private SourcePollingChannelAdapter consumer;

    @Test
    public void testInboundChannelAdapterParser() throws Exception {

        setUp("InboundChannelAdapterParserTests.xml", getClass(), "sosadapterInboundChannelAdapter");

        final AbstractMessageChannel outputChannel = TestUtils.getPropertyValue(this.consumer, "outputChannel",
                AbstractMessageChannel.class);

        assertEquals("out", outputChannel.getComponentName());

        final OGCSOSExecutor sosadapterExecutor = TestUtils.getPropertyValue(this.consumer,
                "source.sosadapterExecutor", OGCSOSExecutor.class);

        assertNotNull(sosadapterExecutor);

        // final String exsampleProperty =
        // TestUtils.getPropertyValue(sosadapterExecutor, "exampleProperty",
        // String.class);
        //
        // assertEquals("I am a sample property", exsampleProperty);

    }

    @Test
    public void testLifeCycleAttributes() throws Exception {

        setUp("InboundChannelAdapterParserTests.xml", getClass(), "sosadapterInboundChannelAdapter");

        assertFalse(this.consumer.isAutoStartup());
        // assertEquals(Integer.valueOf(Integer.MAX_VALUE),
        // Integer.valueOf(this.consumer.getPhase()));

    }

    @Test
    public void testLifeCycleAttributesStarted() throws Exception {
        setUp("InboundChannelAdapterParserTestsStopped.xml", getClass(), "sosadapterInboundChannelAdapter");
        assertTrue(this.consumer.isAutoStartup());
    }

    @Test
    public void testExecutorBeanIdNaming() throws Exception {

        this.context = new ClassPathXmlApplicationContext("InboundChannelAdapterParserTests.xml", getClass());
        assertNotNull(context.getBean("sosadapterInboundChannelAdapter.sosadapterExecutor", OGCSOSExecutor.class));

    }

    @After
    public void tearDown() {
        if (context != null) {
            context.close();
        }
    }

    public void setUp(String name, Class<?> cls, String consumerId) {
        context = new ClassPathXmlApplicationContext(name, cls);
        consumer = this.context.getBean(consumerId, SourcePollingChannelAdapter.class);
    }

}
