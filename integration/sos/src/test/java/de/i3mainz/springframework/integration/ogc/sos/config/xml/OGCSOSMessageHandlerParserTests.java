/*
 * Copyright 2002-2013 the original author or authors.
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
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.channel.AbstractMessageChannel;
import org.springframework.integration.endpoint.EventDrivenConsumer;
import org.springframework.integration.test.util.TestUtils;

import de.i3mainz.springframework.integration.ogc.sos.core.OGCSOSExecutor;
import de.i3mainz.springframework.swe.n52.sos.core.SOSTemplate;
import de.i3mainz.springframework.swe.n52.sos.model.FeatureOfInterest;

/**
 *
 * @author Nikolai Bock
 * @since 1.0
 *
 */
public class OGCSOSMessageHandlerParserTests {

    private ConfigurableApplicationContext context;

    private EventDrivenConsumer consumer;

    @Test
    public void testMessageHandlerParser() throws Exception {
        setUp("MessageHandlerParserTests.xml", getClass());

        final AbstractMessageChannel inputChannel = TestUtils.getPropertyValue(
                this.consumer, "inputChannel", AbstractMessageChannel.class);

        assertEquals("target", inputChannel.getComponentName());

        final OGCSOSExecutor sosadapterExecutor = TestUtils.getPropertyValue(
                this.consumer, "handler.sosadapterExecutor",
                OGCSOSExecutor.class);

        assertNotNull(sosadapterExecutor);

        final SOSTemplate connectionProperty = TestUtils.getPropertyValue(
                sosadapterExecutor, "sosTemplate", SOSTemplate.class);

        assertEquals("1.0.0",
                connectionProperty.getConnectionParameter().getVersion());

        final String sensorProperty = TestUtils
                .getPropertyValue(sosadapterExecutor, "sensorID", String.class);

        assertEquals("Test3000", sensorProperty);

        final FeatureOfInterest foiProperty = TestUtils.getPropertyValue(
                sosadapterExecutor, "foi", FeatureOfInterest.class);

        assertEquals("foi_Mainz_1", foiProperty.getUri());

    }

    @Test
    public void testExecutorBeanIdNaming() throws Exception {

        this.context = new ClassPathXmlApplicationContext(
                "MessageHandlerParserTests.xml", getClass());
        assertNotNull(context.getBean(
                "sosadapterOutboundChannelAdapter.sosadapterExecutor",
                OGCSOSExecutor.class));

    }

    // @Test
    // public void testInsertObservation() throws Exception {
    // setUp("MessageHandlerParserTests.xml", getClass());
    // final OGCSOSExecutor sosadapterExecutor = TestUtils
    // .getPropertyValue(this.consumer, "handler.sosadapterExecutor",
    // OGCSOSExecutor.class);
    //
    // assertNotNull(sosadapterExecutor);
    // Message<Observation> message = MessageBuilder.withPayload(new
    // Observation(new Date(), "TESTVALUE")).build();
    // sosadapterExecutor.executeOutboundOperation(message);
    //
    // }

    @After
    public void tearDown() {
        if (context != null) {
            context.close();
        }
    }

    public void setUp(String name, Class<?> cls) {
        context = new ClassPathXmlApplicationContext(name, cls);
        consumer = this.context.getBean("sosadapterOutboundChannelAdapter",
                EventDrivenConsumer.class);
    }

}
