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
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.handler.ExpressionEvaluatingMessageProcessor;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import de.i3mainz.springframework.integration.ogc.sos.core.OGCSOSInsertObservationExecutor;
import de.i3mainz.springframework.integration.ogc.sos.core.OGCSOSInsertSensorExecutor;
import de.i3mainz.springframework.integration.ogc.sos.outbound.OGCSOSOutboundGateway;
import de.i3mainz.springframework.integration.swe.sos.transformation.InsertObservationFactoryHeaderEnricher;
import de.i3mainz.springframework.integration.swe.sos.transformation.RegisterSensorHeaderEnricher;
import de.i3mainz.springframework.integration.swe.sos.transformation.SensorPropertiesHeaderEnricher;
import de.i3mainz.springframework.integration.swe.sos.transformation.util.FOICreator;
import de.i3mainz.springframework.integration.swe.sos.transformation.util.SOSExpressionEvaluator;
import de.i3mainz.springframework.integration.swe.sos.transformation.util.SensorCreator;
import de.i3mainz.springframework.swe.n52.sos.core.SensorObservationServiceOperations;
import de.i3mainz.springframework.swe.n52.sos.util.InsertObservationFactory;

/**
 * @author Nikolai Bock
 *
 */
@Configuration
@ConditionalOnProperty(name = "dynamicSensorCreation", havingValue = "true")
@EnableConfigurationProperties({ SensorProperties.class,
        OfferingProperties.class, SystemFOIProperties.class,
        SensorPropertyInformationProperties.class,
        SOSInitializationProperties.class })
public class DynamicSensorCreationConfiguration {

    @Autowired
    private SOSExpressionEvaluator evaluator;

    @Autowired
    @Qualifier("start")
    private MessageChannel start;

    @Autowired
    @Qualifier("sensorinput")
    private MessageChannel sensorinput;

    @Autowired
    private SensorCreator sensorC;

    @Autowired
    private SensorProperties sensorProp;

    @Autowired
    private SensorPropertyInformationProperties sensorInfoProp;

    @Autowired
    private OfferingProperties offeringProp;

    @Autowired
    private SystemFOIProperties sysFoIProp;

    @Qualifier("registerSensorExecutor")
    private OGCSOSInsertSensorExecutor executor;

    @Bean
    public IntegrationFlow insertSensorFlow() {
        return IntegrationFlows.from(start)
                .enrichHeaders(h -> h.headerFunction("INSERTOBSERVATIONFACTORY",
                        iOFHEnricher()::enrich))
                .enrichHeaders(h -> h.headerFunction(
                        sensorInfoProp
                                .getRegisterSensorPropertiesInformationKey(),
                        sensProEnricher()::enrich, false))
                .enrichHeaders(h -> h.headerFunction("REGISTERSENSOR",
                        regSensEnricher()::enrich))
                .handle(registerSensor()).channel(sensorinput).get();
    }

    private RegisterSensorHeaderEnricher regSensEnricher() {
        RegisterSensorHeaderEnricher enricher = new RegisterSensorHeaderEnricher(
                sensorC);
        FOICreator foiCreator = new FOICreator();
        foiCreator.setEvaluator(evaluator);
        foiCreator.setFoiIDExpression(sysFoIProp.getUri());
        foiCreator.setFoiNameExpression(sysFoIProp.getName());
        foiCreator.setSpatialFieldExpression(sysFoIProp.getPosition());
        enricher.setFoiC(foiCreator);
        return enricher;

    }

    private SensorPropertiesHeaderEnricher sensProEnricher() {
        SensorPropertiesHeaderEnricher enricher = new SensorPropertiesHeaderEnricher();
        enricher.setEvaluator(evaluator);
        enricher.setPropertyExpression(sensorInfoProp.getPropertyField());
        enricher.setUomExpression(sensorInfoProp.getUomField());
        return enricher;
    }

    private InsertObservationFactoryHeaderEnricher iOFHEnricher() {
        InsertObservationFactoryHeaderEnricher enricher = new InsertObservationFactoryHeaderEnricher(
                sensorC);
        enricher.setEvaluator(evaluator);
        return enricher;
    }

    @Bean
    public SensorCreator sensorC() {
        SensorCreator sensorCreator = new SensorCreator();
        sensorCreator.setEvaluator(evaluator);
        sensorCreator.setSensorURIExpression(sensorProp.getUri());
        sensorCreator.setSensorNameExpression(sensorProp.getName());
        sensorCreator.setOfferingURIExpression(offeringProp.getUri());
        sensorCreator.setOfferingNameExpression(offeringProp.getName());
        return sensorCreator;
    }

    @Bean
    public MessageHandler registerSensor() {
        return new OGCSOSOutboundGateway(executor);

    }

    @Bean
    @Qualifier("registerSensorExecutor")
    private OGCSOSInsertSensorExecutor rsExecutor(
            SensorObservationServiceOperations operations) {
        OGCSOSInsertSensorExecutor rsExecutor = new OGCSOSInsertSensorExecutor();
        rsExecutor.setSosConnection(operations);
        return rsExecutor;
    }

    @Bean
    @Qualifier("insertObservationExecutor")
    public OGCSOSInsertObservationExecutor iOExecutor(
            SensorObservationServiceOperations operations) {
        OGCSOSInsertObservationExecutor executor = new OGCSOSInsertObservationExecutor();
        executor.setSosConnection(operations);
        ExpressionEvaluatingMessageProcessor<InsertObservationFactory> processor = new ExpressionEvaluatingMessageProcessor<>(
                new SpelExpressionParser().parseExpression(
                        "headers['INSERTOBSERVATIONFACTORY']"));
        executor.setiOFExpression(processor);
        return executor;
    }

}
