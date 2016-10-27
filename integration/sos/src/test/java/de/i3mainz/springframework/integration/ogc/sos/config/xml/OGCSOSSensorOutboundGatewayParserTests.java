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
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.test.util.TestUtils;
import org.springframework.messaging.Message;

import de.i3mainz.springframework.integration.ogc.sos.core.OGCSOSExecutor;
import de.i3mainz.springframework.integration.ogc.sos.core.OGCSOSInsertSensorExecutor;
import de.i3mainz.springframework.integration.ogc.sos.outbound.OGCSOSOutboundGateway;
import de.i3mainz.springframework.swe.n52.sos.model.requests.RegisterSensor;
import de.i3mainz.springframework.swe.n52.sos.util.RegisterSensorFactory;

/**
 * @author Nikolai Bock
 * @since 1.0
 *
 */
public class OGCSOSSensorOutboundGatewayParserTests {

    private ConfigurableApplicationContext context;

    private EventDrivenConsumer consumer;

    @Test
    public void testOutboundGatewayParser() throws Exception {
        setUp("SensorOutboundGatewayParserTests.xml", getClass(), "sosadapterSensorOutboundGateway2");

        final AbstractMessageChannel inputChannel = TestUtils.getPropertyValue(this.consumer, "inputChannel",
                AbstractMessageChannel.class);

        assertEquals("in", inputChannel.getComponentName());

        final OGCSOSOutboundGateway sosadapterOutboundGateway = TestUtils.getPropertyValue(this.consumer, "handler",
                OGCSOSOutboundGateway.class);

        long sendTimeout = TestUtils.getPropertyValue(sosadapterOutboundGateway, "messagingTemplate.sendTimeout",
                Long.class);

        assertEquals(100, sendTimeout);

        final OGCSOSExecutor sosadapterExecutor = TestUtils.getPropertyValue(this.consumer,
                "handler.sosadapterExecutor", OGCSOSInsertSensorExecutor.class);

        assertNotNull(sosadapterExecutor);

    }

    @Test
    public void testExecutorBeanIdNaming() throws Exception {

        this.context = new ClassPathXmlApplicationContext("SensorOutboundGatewayParserTests.xml", getClass());
        assertNotNull(context.getBean("sosadapterSensorOutboundGateway.sosadapterExecutor", OGCSOSExecutor.class));

    }

    //@Test
    public void testRegisterSensor() throws Exception {
        setUp("SensorOutboundGatewayParserTests.xml", getClass(), "sosadapterSensorOutboundGateway2");
        final OGCSOSExecutor sosadapterExecutor = TestUtils.getPropertyValue(this.consumer,
                "handler.sosadapterExecutor", OGCSOSExecutor.class);

        assertNotNull(sosadapterExecutor);
        RegisterSensor rs = context.getBean("registerSensor",RegisterSensorFactory.class).create();
        Message<?> message = MessageBuilder.withPayload("").setHeader("REGISTERSENSOR", rs).build();
        sosadapterExecutor.executeOutboundOperation(message);

    }

    @After
    public void tearDown() {
        if (context != null) {
            context.close();
        }
    }

    public void setUp(String name, Class<?> cls, String gatewayId) {
        context = new ClassPathXmlApplicationContext(name, cls);
        consumer = this.context.getBean(gatewayId, EventDrivenConsumer.class);
    }

}
