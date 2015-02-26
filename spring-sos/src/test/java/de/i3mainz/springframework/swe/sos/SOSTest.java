package de.i3mainz.springframework.swe.sos;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sun.org.apache.bcel.internal.generic.ISTORE;

import de.i3mainz.springframework.swe.n52.sos.model.FeatureOfInterest;
import de.i3mainz.springframework.swe.n52.sos.model.Observation;
import de.i3mainz.springframework.swe.n52.sos.model.Sensor;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class SOSTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SOSTest.class);

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
    }

    @Test
    public void testRegisterSensor() throws Exception {
        String sID = service.insertSensor(sensor);
        Assert.assertNotNull(sID);
        Assert.assertSame(sID, sensor.getId());
        LOGGER.debug("SensorID ist: " + sID);
    }

    @Test
    public void testInsertObservation() throws Exception {
        LOGGER.debug(service.insertObservation(sensor, foi, observation));

    }

    @Test
    public void testGetFeatureOfInterest() throws Exception {
        LOGGER.debug(service.getFeatureOfInterest(foi.getId()));

    }

    @Test
    public void testSOSGetObservation() throws Exception {
        // assertNotNull(service.getObservation());
        LOGGER.debug("GET-Observation-RESPONSE-Featurelist for ID "
                + sensor.getOffering().getId()
                + ": "
                + service.getObservation(sensor.getOffering().getId(), null,
                        observedProperties, null));
    }

    @Test
    public void testSOSGetObservationOfSensor() throws Exception {
        // assertNotNull(service.getObservation());
        LOGGER.debug(service.getObservation(sensor.getOffering().getId(),
                sensors, observedProperties, "urn:ogc:def:crs:EPSG::3857"));
    }
}
