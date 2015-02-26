/**
 * 
 */
package de.i3mainz.springframework.swe.n52.sos.service;

import java.util.List;

import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.ows.ServiceDescriptor;

import de.i3mainz.springframework.swe.n52.sos.exceptions.GetFOIServiceException;
import de.i3mainz.springframework.swe.n52.sos.exceptions.GetObservationException;
import de.i3mainz.springframework.swe.n52.sos.model.FeatureOfInterest;
import de.i3mainz.springframework.swe.n52.sos.model.Observation;

/**
 * @author Nikolai Bock
 *
 */
public interface SOSService {
    ServiceDescriptor getCapabilities();

    String insertObservation(String sensorId, FeatureOfInterest foi,
            Observation observation);

    OperationResult getFeatureOfInterest(String foiID) throws GetFOIServiceException;

    OperationResult getObservation(String offering, List<String> sensor,
            List<String> observedProperties, String srsName) throws GetObservationException;
    
}