package de.i3mainz.springframework.swe.sos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.i3mainz.springframework.swe.n52.sos.model.FeatureOfInterest;
import de.i3mainz.springframework.swe.n52.sos.model.Observation;
import de.i3mainz.springframework.swe.n52.sos.model.Sensor;

@SuppressWarnings("deprecation")
@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class SOSTest {

	@Autowired
	public SOSService service;
	@Autowired
	public Sensor sensor;
	@Autowired
	public FeatureOfInterest foi;
	@Autowired
	public Observation observation;

	@Test
	public void testSimpleProperties() throws Exception {
		assertNotNull(service);
	}

	@Test
	public void testSOS() throws Exception {
		assertEquals("1.0.0", service.getSosVersion());
		Assert.assertTrue(service.isSOSAvailable());
		Assert.assertTrue(service.isSOSTransactional());
	}

	@Test
	public void testRegisterSensor() throws Exception {
		System.out.println(service.insertSensor(sensor));
	}

	@Test
	public void testInsertObservation() throws Exception {
		System.out.println(service.insertObservation(sensor, foi, observation));

	}
	// @Test
	// public void testSOSGetObservation() throws Exception{
	// assertNotNull(service.getObservation());
	// }

	// @Test
	// public void testSOSInsertSensor() throws Exception {
	// service.insertSensor();
	// }
}
