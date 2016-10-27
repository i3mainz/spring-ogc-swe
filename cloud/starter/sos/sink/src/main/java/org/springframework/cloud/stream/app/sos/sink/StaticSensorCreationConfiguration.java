/**
 * 
 */
package org.springframework.cloud.stream.app.sos.sink;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.MessageChannel;

import de.i3mainz.springframework.integration.ogc.sos.core.OGCSOSInsertObservationExecutor;
import de.i3mainz.springframework.swe.n52.sos.core.SensorObservationServiceOperations;

@Configuration
@ConditionalOnProperty(name = "dynamicSensorCreation", havingValue = "false")
@Import(SOSIntializingConfiguration.class)
@EnableConfigurationProperties({ SensorProperties.class,
        SensorPropertyInformationProperties.class,
        SOSInitializationProperties.class })
public class StaticSensorCreationConfiguration {

    @Autowired
    @Qualifier("start")
    private MessageChannel start;

    @Autowired
    @Qualifier("sensorinput")
    private MessageChannel sensorinput;

    @Autowired
    private SensorProperties sensorProp;

    @Autowired
    private SensorPropertyInformationProperties sensorInfoProp;

    @Bean
    public IntegrationFlow insertSensorFlow() {
        return IntegrationFlows.from(start).channel(sensorinput).get();
    }

    @Bean
    @Qualifier("insertObservationExecutor")
    public OGCSOSInsertObservationExecutor executor(
            SensorObservationServiceOperations operations) {
        OGCSOSInsertObservationExecutor executor = new OGCSOSInsertObservationExecutor();
        executor.setSosConnection(operations);
        executor.setSensor(sensorProp.getUri());
        executor.setPropertyInformation(
                sensorInfoProp.getInsertObservationSensorPropertyInformation());
        return executor;
    }

}
