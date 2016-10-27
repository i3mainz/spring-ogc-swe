/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.i3mainz.springframework.integration.ogc.sos.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.integration.handler.ExpressionEvaluatingMessageProcessor;
import org.springframework.messaging.Message;

import de.i3mainz.springframework.swe.n52.sos.core.SensorObservationServiceOperations;
import de.i3mainz.springframework.swe.n52.sos.model.FeatureOfInterest;

/**
 * Bundles common core logic for the SOSAdapter components.
 * 
 * @author Nikolai Bock
 * @since 1.0
 * 
 */
public class OGCSOSExecutor implements InitializingBean {

    private static final Log LOGGER = LogFactory.getLog(OGCSOSExecutor.class);

    private volatile SensorObservationServiceOperations sosTemplate;
    private volatile String sensorID;
    private volatile FeatureOfInterest foi;
    private volatile ExpressionEvaluatingMessageProcessor<FeatureOfInterest> foiMP;

    /**
     * Verifies and sets the parameters. E.g. initializes the to be used
     */
    @Override
    public void afterPropertiesSet() {
        // Derzeit werden keine Parameter weiter verarbeitet
    }

    /**
     * Executes the outbound SOSAdapter Operation.
     * 
     * @param message
     *            The Spring Integration input message
     * @return The result message payload
     * 
     */
    public Object executeOutboundOperation(final Message<?> message) {

        if (LOGGER.isWarnEnabled()) {
            LOGGER.warn("Logic not implemented, yet.");
        }

        return message.getPayload();

    }

    /**
     * Execute the SOSAdapter operation. Delegates to
     * {@link OGCSOSExecutor#poll(Message)}.
     * 
     * @return The payload object, which may be null.
     */
    public Object poll() {
        return poll(null);
    }

    /**
     * Execute a retrieving (polling) SOSAdapter operation.
     * 
     * @param requestMessage
     *            May be null.
     * @return The payload object, which may be null.
     */
    public Object poll(final Message<?> requestMessage) {
        if (LOGGER.isWarnEnabled()) {
            LOGGER.warn("Logic not implemented, yet.");
        }

        return requestMessage.getPayload();
    }

    /**
     * Example property to illustrate usage of properties in Spring Integration
     * components. Replace with your own logic.
     * 
     * @param sosConnection
     *            Must not be null
     */
    public void setSosConnection(SensorObservationServiceOperations sosConnection) {
        this.sosTemplate = sosConnection;
    }

    /**
     * Example property to illustrate usage of properties in Spring Integration
     * components. Replace with your own logic.
     * 
     * @param sensor
     *            SensorID as String representation.
     * 
     */
    public void setSensor(String sensor) {
        this.sensorID = sensor;
    }

    /**
     * FeatureOfInterest were Observation will be stored.
     * 
     * @param foi
     *            FeatureOfInterest-Object
     */
    public void setFoiRef(FeatureOfInterest foi) {
        this.foi = foi;
    }

    /**
     * FeatureOfInterest in SpEL format (optional).
     * 
     * @param foiExpression
     *            FeatureOfInterest as SpEL
     */
    public void setFoiExpression(ExpressionEvaluatingMessageProcessor<FeatureOfInterest> foiExpression) {
        this.foiMP = foiExpression;
    }

    /**
     * @return the sosTemplate
     */
    protected final SensorObservationServiceOperations getSosTemplate() {
        return sosTemplate;
    }

    /**
     * @return the sensorID
     */
    protected final String getSensorID() {
        return sensorID;
    }

    protected final FeatureOfInterest getFoi() {
        return foi;
    }

    protected final ExpressionEvaluatingMessageProcessor<FeatureOfInterest> getFoiMP() {
        return this.foiMP;
    }

}
