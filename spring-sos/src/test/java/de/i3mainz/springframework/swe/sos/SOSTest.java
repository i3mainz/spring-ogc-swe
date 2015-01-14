package de.i3mainz.springframework.swe.sos;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.annotation.Resource;

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
    @Resource(name = "observedProperties")
    public List<String> observedProperties;
    @Resource(name = "sensors")
    public List<String> sensors;

    @Test
    public void testSimpleProperties() throws Exception {
        assertNotNull(service);
    }

    @Test
    public void testSOS() throws Exception {
        Assert.assertTrue(service.isSOSAvailable());
        // Assert.assertTrue(service.isSOSTransactional());
    }

    @Test
    public void testRegisterSensor() throws Exception {
        System.out.println(service.insertSensor(sensor));
    }

    @Test
    public void testInsertObservation() throws Exception {
        System.out.println(service.insertObservation(sensor, foi, observation));

    }

    @Test
    public void testGetFeatureOfInterest() throws Exception {
        System.out.println(service.getFeatureOfInterest(foi.getId()));

    }

    @Test
    public void testSOSGetObservation() throws Exception {
        // assertNotNull(service.getObservation());
        System.out.println("GET-Observation-RESPONSE-Featurelist for ID "
                + sensor.getOffering().getId()
                + ": "
                + service.getObservation(sensor.getOffering().getId(),null,
                        observedProperties));
    }

    @Test
    public void testSOSGetObservationOfSensor() throws Exception {
        // assertNotNull(service.getObservation());
        System.out.println(service.getObservation(sensor.getOffering().getId(), sensors,
                observedProperties));
    }
    // @Test
    // public void testSOSInsertSensor() throws Exception {
    // service.insertSensor();
    // }
}
