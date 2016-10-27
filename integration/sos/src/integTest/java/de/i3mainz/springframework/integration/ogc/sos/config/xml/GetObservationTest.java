/**
 * 
 */
package de.i3mainz.springframework.integration.ogc.sos.config.xml;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.n52.oxf.feature.OXFFeatureCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Nikolai Bock
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:GetObservationInboundChannelAdapterTest.xml")
public class GetObservationTest {

    @Autowired
    @Qualifier("out")
    private SubscribableChannel channelOne;

    @Before
    public void setup() {
        MessageHandler handler = new MessageHandler() {

            public void handleMessage(Message<?> message)
                    throws MessagingException {
                System.out.println("READING Observations from SOS!");
                OXFFeatureCollection features = (OXFFeatureCollection) message
                        .getPayload();
                System.out.println(features.size());
                features.forEach(System.out::println);
            }
        };
        channelOne.subscribe(handler);

    }

    @Test
    public void withoutUpdate() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
