/**
 * 
 */
package org.springframework.cloud.stream.app.sos.sink;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import de.i3mainz.springframework.integration.ogc.sos.core.OGCSOSInsertObservationExecutor;
import de.i3mainz.springframework.integration.ogc.sos.outbound.OGCSOSOutboundGateway;
import de.i3mainz.springframework.integration.swe.sos.transformation.ObservationTransformer;
import de.i3mainz.springframework.swe.n52.sos.model.Observation;

/**
 * @author Nikolai Bock
 *
 */
@Configuration
@EnableConfigurationProperties(value = { SOSSinkProperties.class})
@EnableBinding(Sink.class)
public class SOSSinkConfiguration {

    @Autowired
    SOSSinkProperties sinkProperties;

    @Autowired
    Sink sink;

    @Autowired
    MessageHandler insertObservationHandler;

    @Autowired
    @Qualifier("start")
    private MessageChannel start;

    @Autowired
    @Qualifier("sensorinput")
    private MessageChannel sensorinput;

    @Autowired
    private ObservationTransformer observationTransformer;

    @Bean
    public IntegrationFlow insertObservationStart() {
        return IntegrationFlows.from(sink.input()).channel(start).get();
    }

    @Bean
    public IntegrationFlow insertObservationEnd() {
        return IntegrationFlows.from(sensorinput).split()
                .transform(observationTransformer)
                .filter(p -> p instanceof Observation)
                .handle(insertObservationHandler).get();
    }

    @Bean
    public MessageHandler insertObservation(
            @Qualifier("insertObservationExecutor") OGCSOSInsertObservationExecutor executor) {
        return new OGCSOSOutboundGateway(executor);
    }
}
