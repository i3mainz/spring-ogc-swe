/**
 * 
 */
package org.springframework.cloud.stream.app.sos.sink;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.annotation.Bindings;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Some nice integration tests for the IsoChrone Processor Stream interface
 * 
 * @author Nikolai Bock
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SOSSinkIntegrationTests.SOSSinkApplication.class, webEnvironment = RANDOM_PORT)
public abstract class SOSSinkIntegrationTests {

    @Autowired
    @Bindings(SOSSinkConfiguration.class)
    protected Sink sink;

    @SpringBootTest(value = { "dynamicSensorCreation=false", "initSensor=true",
            "sos.offering.uri=TESTOFFERING", "sos.offering.name=TESTOFFERING",
            "sos.sensor.uri=TESTSENSOR", "sos.sensor.name=TESTSENSOR",
            "sos.system.foi.uri=foi_MAINZ_TEST",
            "sos.system.foi.name=Mainz Hochschule Test",
            "sos.system.foi.position=POINT(49.99984232 8.23548545)",
            "sos.system.foi.srid=4326",
            "sos.sensor.propertyinfo.registerSensorPropertiesInformation=[{\"name\":\"text\", \"uri\":\"urn:ogc:def:dataType:OGC:1.1:string\",\"mvt\": \"TEXT\", \"uom\":\"mg/m³\"}]" })
    public static class TestInsertObservation extends SOSSinkIntegrationTests {

        @Test
        public void testInsertObservation() throws JsonProcessingException {
            sink.input().send(new GenericMessage<String>(""));
        }
    }

    @SpringBootApplication
    public static class SOSSinkApplication {

    }
}
