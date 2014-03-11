package de.i3mainz.springframework.swe.sos;

import java.net.URL;

import org.n52.oxf.sos.capabilities.ObservationOffering;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Component;

import de.i3mainz.springframework.swe.n52.sos.core.SensorObservationServiceOperations;
import de.i3mainz.springframework.swe.n52.sos.model.FeatureOfInterest;
import de.i3mainz.springframework.swe.n52.sos.model.Observation;
import de.i3mainz.springframework.swe.n52.sos.model.Sensor;
import de.i3mainz.springframework.swe.sos.SOSService;

/**
 * {@link SOSService} with hard-coded input data.
 */
@Component
public class SOSServiceImp implements SOSService {

	private SensorObservationServiceOperations sos;
	URL url;

	public String getSosVersion() {
		if (sos.getCapabilities() != null) {
			System.out.println(sos.getCapabilities().getServiceIdentification()
					.getTitle());
			ObservationOffering obsOff = (ObservationOffering) sos
					.getCapabilities().getContents().getDataIdentification(3);
			System.out.println("Offering: " + obsOff.getIdentifier());
			return sos.getCapabilities().getVersion();
		}
		return "";
	}

	@Required
	@Autowired
	public final void setSos(SensorObservationServiceOperations sos) {
		this.sos = sos;
	}

	// public int getObservation() {
	// return sos.getObservation().size();
	// }

	// public void insertSensor() {
	// // SENSOR
	// final Sensor sensor = new Sensor(name, uri);
	// // FEATURE OF INTEREST incl. Position
	// final FeatureOfInterest foi = new FeatureOfInterest(name, uri, new
	// Position(values, units, epsgCode));
	// // VALUE
	// final Object value = "Hallo Welt";
	// // TODO implement using different templates in later version depending
	// // on the class of value
	// // TIMESTAMP
	// final String timeStamp = "test";
	// // UOM CODE
	// final UnitOfMeasurement uom = new UnitOfMeasurement(timeStamp,
	// timeStamp);
	// // OBSERVED_PROPERTY
	// final ObservedProperty observedProperty = new ObservedProperty(name,
	// uri);
	// final Offering offer = new Offering(name, uri);
	// InsertObservation io = new InsertObservation(sensor, foi, value,
	// timeStamp, uom,
	// observedProperty, offer, dataFile.getType(mVColumnId));
	//
	// RegisterSensor rs = new RegisterSensor(io, observedProperties,
	// measuredValueTypes, unitOfMeasurements);
	// sos.registerSensor(null);
	//
	// }

	@Override
	public String insertSensor(Sensor sensor) {
		return sos.registerSensor_1(sensor);
	}

	@Override
	public boolean isSOSAvailable() {
		return sos.isSOSAvailable();
	}

	@Override
	public boolean isSOSTransactional() {
		return sos.isSOSTransactional();
	}

	@Override
	public String insertObservation(Sensor sensor, FeatureOfInterest foi,
			Observation observation) {
		return sos.insertObservation(sensor.getId(), foi, observation);
	}

}
