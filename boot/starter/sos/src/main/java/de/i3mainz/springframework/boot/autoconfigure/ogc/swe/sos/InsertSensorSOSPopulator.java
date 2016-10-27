/**
 * 
 */
package de.i3mainz.springframework.boot.autoconfigure.ogc.swe.sos;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
public class InsertSensorSOSPopulator implements SOSPopulator {

    private static final Log LOG = LogFactory.getLog(InsertSensorSOSPopulator.class);
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
     * de.i3mainz.springframework.cloud.swe.sos.SOSPopulator#populate(de.i3mainz
     * .springframework.swe.n52.sos.SOSConnectionParameter)
     */
    @Override
    public void populate(SOSConnectionParameter serviceParam) {
        this.template.setConnectionParameter(serviceParam);
        try {
            this.template.registerSensor(this.sensor);
        } catch (RegisterSensorException e) {
            LOG.error("Interner Fehler beim anlegen des Sensors", e);
        }
    }

}
