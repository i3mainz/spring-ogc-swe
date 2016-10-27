/**
 * 
 */
package de.i3mainz.springframework.xd.swe.sos;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

import de.i3mainz.springframework.swe.n52.sos.SOSConnectionParameter;
import de.i3mainz.springframework.swe.n52.sos.core.SOSTemplate;
import de.i3mainz.springframework.swe.n52.sos.exceptions.RegisterSensorException;
import de.i3mainz.springframework.swe.n52.sos.init.SOSPopulator;
import de.i3mainz.springframework.swe.n52.sos.model.requests.RegisterSensor;
import de.i3mainz.springframework.swe.n52.sos.util.RegisterSensorFactory;

/**
 * This class provides initialization of a sensor in the Sensor Observation
 * Service.
 * 
 * @author Nikolai Bock
 *
 */
public class InsertSensorSOSInitializer implements InitializingBean,
        SOSPopulator {

    private static final Log LOGGER = LogFactory
            .getLog(InsertSensorSOSInitializer.class);
    /** The sensor object */
    private RegisterSensor sensor;
    /** The Spring SOS-Template instance which will be used for connections */
    private SOSTemplate template = new SOSTemplate();

    /**
     * @param sensor
     *            the sensor to set
     */
    public void setSensor(RegisterSensorFactory sensorFactory) {
        this.sensor = sensorFactory.create();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        // Not needed at the moment.
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.i3mainz.springframework.cloud.swe.sos.SOSPopulator#populate(de.i3mainz
     * .springframework.swe.n52.sos.SOSConnectionParameter)
     */
    @Override
    public void populate(SOSConnectionParameter serviceParam) {
        this.template.setConnectionParameter(serviceParam);
        try {
            this.template.registerSensor(this.sensor);
        } catch (RegisterSensorException e) {
            LOGGER.error("Interner Fehler beim anlegen des Sensors", e);
        }
    }

}
