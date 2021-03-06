/**
 * 
 */
package de.i3mainz.springframework.swe.n52.sos.service.versions.v200;

import java.util.Map;

import org.n52.oxf.ows.ExceptionReport;

import de.i3mainz.springframework.swe.n52.sos.model.requests.RegisterSensor;
import de.i3mainz.springframework.swe.n52.sos.service.SOSService;

/**
 * @author Nikolai Bock
 *
 */
public interface SOSServiceV200 extends SOSService {
    String insertSensor(RegisterSensor sensor) throws ExceptionReport;
    Map<String, String> getOfferings();
}