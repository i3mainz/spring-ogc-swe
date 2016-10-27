package de.i3mainz.springframework.integration.ogc.sos.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.integration.handler.ExpressionEvaluatingMessageProcessor;
import org.springframework.integration.handler.MessageProcessor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.util.Assert;

import de.i3mainz.springframework.integration.ogc.sos.OGCSOSHeaders;
import de.i3mainz.springframework.swe.n52.sos.exceptions.InsertObservationException;
import de.i3mainz.springframework.swe.n52.sos.model.FeatureOfInterest;
import de.i3mainz.springframework.swe.n52.sos.model.Observation;
import de.i3mainz.springframework.swe.n52.sos.model.requests.InsertObservation;
import de.i3mainz.springframework.swe.n52.sos.util.InsertObservationFactory;

public class OGCSOSInsertObservationExecutor extends OGCSOSExecutor {

    private static final Log LOGGER = LogFactory.getLog(OGCSOSInsertObservationExecutor.class);

    private volatile InsertObservationFactory insObservFac;
    private volatile MessageProcessor<InsertObservationFactory> iOFExpression;
    private volatile MessageProcessor<Observation> expression;
    private volatile String propertyInformation;

    /**
     * Executes the outbound SOSAdapter Operation.
     * 
     * @param message
     *            The Spring Integration input message
     * @return The result message payload
     * 
     */
    /*
     * (non-Javadoc)
     * 
     * @see de.i3mainz.springframework.integration.ogc.sos.core.OGCSOSExecutor#
     * executeOutboundOperation(org.springframework.messaging.Message)
     */
    @Override
    public Object executeOutboundOperation(final Message<?> message) {
        try {
            getSosTemplate().insertObservation(handleInsertObservation(message));
        } catch (InsertObservationException e) {
            throw new MessagingException(message, "Observation can not inserted!", e);
        }

        return message.getPayload();

    }

    private FeatureOfInterest handleFOI(Message<?> message) {
        FeatureOfInterest toFOI = getFoi();
        if (toFOI == null) {
            if (getFoi() != null) {
                toFOI = getFoiMP().processMessage(message);
            }
            if (toFOI == null) {
                Assert.isTrue(message.getHeaders().get(OGCSOSHeaders.FOI) instanceof FeatureOfInterest, "The header "
                        + OGCSOSHeaders.FOI
                        + " must contain an complete FeatureOfInterest object or set the foi attribute with a reference");
                toFOI = (FeatureOfInterest) message.getHeaders().get(OGCSOSHeaders.FOI);
            }
        }

        return toFOI;
    }

    /**
     * InsertObservationFactory to create InsertObservation Object for request
     * 
     * @param insertObservationFactory
     *            the factory object
     */
    public void setInsertObservationFactory(InsertObservationFactory insertObservationFactory) {
        this.insObservFac = insertObservationFactory;
    }

    /**
     * @param expression
     *            the expression to set
     */
    public void setExpression(ExpressionEvaluatingMessageProcessor<Observation> expression) {
        this.expression = expression;
    }

    /**
     * @param iOFExpression
     *            the iOFExpression to set
     */
    public void setiOFExpression(ExpressionEvaluatingMessageProcessor<InsertObservationFactory> iOFExpression) {
        this.iOFExpression = iOFExpression;
    }

    /**
     * @param propertyInformation
     *            the propertyInformation to set
     */
    public void setPropertyInformation(String propertyInformation) {
        this.propertyInformation = propertyInformation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.i3mainz.springframework.integration.sos.core.SOSAdapterExecutor#
     * afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        this.expression = new ExpressionEvaluatingMessageProcessor<>(
                new SpelExpressionParser().parseExpression("payload"), Observation.class);
    }

    private InsertObservation handleInsertObservation(Message<?> message) {

        handleInsertObservationFactory(message);

        Assert.isTrue(message.getPayload() instanceof Observation, "Only payload of type Observation is supported. "
                + "Consider adding a transformer to the message flow in front of this adapter.");

        Observation payload = this.expression.processMessage(message);

        LOGGER.info(payload.getValue());
        LOGGER.info(payload.getTimeAsSTring());
        FeatureOfInterest toFOI = this.handleFOI(message);
        LOGGER.info(toFOI.getUri());
        LOGGER.info(toFOI.getPosition());

        return this.insObservFac.create(toFOI, payload);
    }

    /**
     * @param message
     */
    private void handleInsertObservationFactory(Message<?> message) {

        if (this.insObservFac == null) {
            if (this.iOFExpression != null) {
                LOGGER.debug("InsertFactory processMessage");
                this.insObservFac = this.iOFExpression.processMessage(message);
            }
            if (this.insObservFac == null) {
                Assert.isTrue(getSensorID() != null && !getSensorID().isEmpty(), "SensorID has to be set");
                LOGGER.info(getSensorID());
                Assert.isTrue(this.propertyInformation != null && !this.propertyInformation.isEmpty(),
                        "PropertyInformation has to be set");

                this.insObservFac = new InsertObservationFactory(getSensorID(), this.propertyInformation);
            }
        }

        Assert.notNull(this.insObservFac, "InsertObservationFactory can't be set or created");
    }

}
