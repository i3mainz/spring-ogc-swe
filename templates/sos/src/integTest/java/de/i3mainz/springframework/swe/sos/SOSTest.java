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

import de.i3mainz.springframework.swe.n52.sos.model.FeatureOfInterest;
import de.i3mainz.springframework.swe.n52.sos.model.Observation;
import de.i3mainz.springframework.swe.n52.sos.model.Sensor;
import de.i3mainz.springframework.swe.n52.sos.model.requests.RegisterSensor;
import de.i3mainz.springframework.swe.n52.sos.util.InsertObservationFactory;
import de.i3mainz.springframework.swe.n52.sos.util.RegisterSensorFactory;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class SOSTest {

    private static final Logger LOG = LoggerFactory.getLogger(SOSTest.class);

    @Autowired
    public SOSService service;

    private RegisterSensor rs;
    @Autowired
    private InsertObservationFactory ioF;
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

    @Autowired
    public void setRegisterSensor(RegisterSensorFactory registerSensor) {
        this.rs = registerSensor.create();
    }

    @Test
    public void testSimpleProperties() throws Exception {
        assertNotNull(service);
    }

    @Test
    public void testSOS() throws Exception {
        Assert.assertTrue(service.isSOSAvailable());
        Assert.assertTrue(service.isSOSTransactional());
    }

    @Test
    public void testRegisterSensor() throws Exception {
        String sID = service.insertSensor(rs);
        Assert.assertNotNull(sID);
        Assert.assertEquals(sID, rs.getSensorURI());
        LOG.debug("SensorID ist: " + sID);
    }

    @Test
    public void testInsertObservation() throws Exception {
        try {
            LOG.debug(service.insertObservation(ioF.create(foi, observation)));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    // @Test
    // public void testGetFeatureOfInterest() throws Exception {
    // LOGGER.debug(service.getFeatureOfInterest(foi.getName()));
    //
    // }
    //
    // @Test
    // public void testSOSGetObservation() throws Exception {
    // String observationString =
    // service.getObservation(sensor.getOffering().getUri(), null,
    // observedProperties, null,
    // null);
    // assertNotNull(observationString);
    // LOGGER.debug("GET-Observation-RESPONSE-Featurelist for ID " +
    // sensor.getOffering().getUri() + ": "
    // + observationString);
    // }
    //
    // @Test
    // public void testSOSGetObservationOfSensor() throws Exception {
    // String observationString =
    // service.getObservation(sensor.getOffering().getUri(), sensors,
    // observedProperties,
    // "urn:ogc:def:crs:EPSG::3857", null);
    // assertNotNull(observationString);
    // LOGGER.debug(observationString);
    // }
}
