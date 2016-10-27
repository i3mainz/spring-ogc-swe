/**
 * 
 */
package org.springframework.cloud.stream.app.sos.sink;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.i3mainz.springframework.boot.autoconfigure.ogc.swe.sos.InsertSensorSOSPopulator;
import de.i3mainz.springframework.swe.n52.sos.SOSConnectionParameter;
import de.i3mainz.springframework.swe.n52.sos.init.SOSInitializer;
import de.i3mainz.springframework.swe.n52.sos.model.FeatureOfInterest;
import de.i3mainz.springframework.swe.n52.sos.model.ObservationSensorInformation;
import de.i3mainz.springframework.swe.n52.sos.model.ObservationSensorInformationImpl;
import de.i3mainz.springframework.swe.n52.sos.model.Offering;
import de.i3mainz.springframework.swe.n52.sos.model.Sensor;
import de.i3mainz.springframework.swe.n52.sos.util.RegisterSensorFactory;

/**
 * @author Nikolai Bock
 *
 */
@Configuration
@ConditionalOnProperty(name = { "initSensor" })
@EnableConfigurationProperties({ SensorProperties.class,
        OfferingProperties.class, SystemFOIProperties.class,
        SensorPropertyInformationProperties.class,
        SOSInitializationProperties.class })
public class SOSIntializingConfiguration {

    @Autowired
    private SensorProperties sensorProp;

    @Autowired
    private SensorPropertyInformationProperties sensorInfoProp;

    @Autowired
    private OfferingProperties offeringProp;

    @Autowired
    private SystemFOIProperties sysFoIProp;

    @Bean
    @ConditionalOnProperty(name = "dynamicSensorCreation", havingValue = "false")
    public SOSInitializer sosSensorInitializer(
            SOSConnectionParameter connection,
            RegisterSensorFactory regSensorFactory) {
        SOSInitializer sosInitializer = new SOSInitializer();
        sosInitializer.setParameter(connection);
        InsertSensorSOSPopulator insertSensorPopulator = new InsertSensorSOSPopulator();
        insertSensorPopulator.setSensor(regSensorFactory);
        sosInitializer.setInitializer(insertSensorPopulator);
        sosInitializer.setEnabled(true);
        return sosInitializer;
    }

    @Bean
    @ConditionalOnProperty(name = "dynamicSensorCreation", havingValue = "false")
    public RegisterSensorFactory regSensorFactory() {
        Sensor sensor = new Sensor(sensorProp.getUri(), sensorProp.getName(),
                new Offering(offeringProp.getUri(), offeringProp.getName()));
        FeatureOfInterest foi = new FeatureOfInterest(sysFoIProp.getUri(),
                sysFoIProp.getName(), sysFoIProp.getPosition(),
                sysFoIProp.getSrid());
        ObservationSensorInformation io = new ObservationSensorInformationImpl(
                sensor, foi);
        RegisterSensorFactory regSensorF = new RegisterSensorFactory(io,
                sensorInfoProp.getRegisterSensorPropertiesInformation());
        return regSensorF;
    }

}
