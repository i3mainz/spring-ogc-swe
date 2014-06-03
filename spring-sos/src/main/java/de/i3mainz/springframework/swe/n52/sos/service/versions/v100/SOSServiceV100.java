/**
 * 
 */
package de.i3mainz.springframework.swe.n52.sos.service.versions.v100;

import org.n52.oxf.ows.ExceptionReport;

import de.i3mainz.springframework.swe.n52.sos.model.Sensor;
import de.i3mainz.springframework.swe.n52.sos.service.SOSService;

/**
 * @author nikolai
 *
 */
public interface SOSServiceV100 extends SOSService {
    String registerSensor(Sensor sensor) throws ExceptionReport;
}
