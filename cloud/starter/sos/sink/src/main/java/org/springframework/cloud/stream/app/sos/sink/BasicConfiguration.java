/**
 * 
 */
package org.springframework.cloud.stream.app.sos.sink;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.messaging.MessageChannel;

import de.i3mainz.springframework.integration.swe.sos.transformation.ObservationTransformer;
import de.i3mainz.springframework.integration.swe.sos.transformation.util.FOICreator;
import de.i3mainz.springframework.integration.swe.sos.transformation.util.ObservationCreator;
import de.i3mainz.springframework.integration.swe.sos.transformation.util.SOSExpressionEvaluator;

/**
 * @author Nikolai Bock
 *
 */
@Configuration
@ImportResource("classpath:/baseProcess.xml")
@EnableConfigurationProperties(value = { ObservationProperties.class })
public class BasicConfiguration {

    @Autowired
    private SOSExpressionEvaluator evaluator;

    @Autowired
    private ObservationProperties properties;

    @Bean
    @Qualifier("start")
    public MessageChannel start() {
        return MessageChannels.direct().get();
    }

    @Bean
    @Qualifier("sensorinput")
    public MessageChannel sensorinput() {
        return MessageChannels.direct().get();
    }

    @Bean(name = "observationTransformer")
    public ObservationTransformer transformer() {
        FOICreator foic = new FOICreator();
        foic.setEvaluator(evaluator);
        foic.setSpatialFieldExpression(properties.getSpatialField());

        ObservationCreator obsc = new ObservationCreator();
        obsc.setEvaluator(evaluator);
        obsc.setTemporalFieldExpression(properties.getTimeField());
        obsc.setValueExpression(properties.getValue());

        return new ObservationTransformer(foic, obsc);

    }
}
