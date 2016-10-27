package de.i3mainz.springframework.integration.ogc.sos.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.expression.EvaluationException;
import org.springframework.integration.handler.ExpressionEvaluatingMessageProcessor;
import org.springframework.integration.handler.MessageProcessor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.util.Assert;

import de.i3mainz.springframework.swe.n52.sos.exceptions.RegisterSensorException;
import de.i3mainz.springframework.swe.n52.sos.model.requests.RegisterSensor;

public class OGCSOSInsertSensorExecutor extends OGCSOSExecutor {

    private static final Log LOGGER = LogFactory.getLog(OGCSOSInsertSensorExecutor.class);

    private volatile MessageProcessor<RegisterSensor> messageProcessor;

    /**
     * Executes the outbound SOSAdapter Operation.
     * 
     * @param message
     *            The Spring Integration input message
     * @return The result message payload
     * 
     */
    @Override
    public Object executeOutboundOperation(final Message<?> message) {

        RegisterSensor rs;

        if (message.getPayload() instanceof RegisterSensor && messageProcessor == null) {
            rs = (RegisterSensor) message.getPayload();
        } else {
            try {
                rs = messageProcessor.processMessage(message);
            } catch (EvaluationException e) {
                LOGGER.error(
                        "Only type RegisterSensor is supported. Define a expression which returns RegisterSensor object or transform message payload to RegisterSensor.");
                throw new MessagingException(message, e);
            }
        }

        Assert.notNull(rs, "Only data of type RegisterSensor is supported. "
                + "Consider adding a transformer to the message flow in front of this adapter or define a expression which return a object of this type.");

        try {
            getSosTemplate().registerSensor(rs);
            LOGGER.debug("Sensor " + rs.getSensorURI() + " registered!");
        } catch (RegisterSensorException e) {
            LOGGER.error("Sensor could not registered!", e);
        }
        return message.getPayload();

    }

    /**
     * RegisterSensor Object in SpEL format (optional). Creates MessageProcessor
     * to process message.
     * 
     * @param expression
     *            RegisterSensor as SpEL
     */
    public void setExpression(ExpressionEvaluatingMessageProcessor<RegisterSensor> expression) {
        this.messageProcessor = expression;
    }

}
