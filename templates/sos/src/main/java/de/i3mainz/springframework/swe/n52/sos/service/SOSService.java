/**
 * 
 */
package de.i3mainz.springframework.swe.n52.sos.service;

import java.util.List;

import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.ServiceDescriptor;

import de.i3mainz.springframework.swe.n52.sos.exceptions.GetFOIServiceException;
import de.i3mainz.springframework.swe.n52.sos.exceptions.GetObservationException;
import de.i3mainz.springframework.swe.n52.sos.exceptions.InsertObservationException;
import de.i3mainz.springframework.swe.n52.sos.model.FeatureOfInterest;
import de.i3mainz.springframework.swe.n52.sos.model.Observation;
import de.i3mainz.springframework.swe.n52.sos.model.TemporalRequestFilter;
import de.i3mainz.springframework.swe.n52.sos.model.requests.InsertObservation;

/**
 * @author Nikolai Bock
 *
 */
public interface SOSService {
    ServiceDescriptor getCapabilities();
    boolean isTransactional();

    String insertObservation(String sensorId, FeatureOfInterest foi,
            Observation observation) throws InsertObservationException;
    String insertObservation(InsertObservation io) throws ExceptionReport;

    OperationResult getFeatureOfInterest(String foiID) throws GetFOIServiceException;

    OperationResult getObservation(String offering, List<String> sensor,
            List<String> observedProperties, String srsName, TemporalRequestFilter tempFilter) throws GetObservationException;
    
}