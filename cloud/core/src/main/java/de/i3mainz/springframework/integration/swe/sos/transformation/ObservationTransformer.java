package de.i3mainz.springframework.integration.swe.sos.transformation;

import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.transformer.AbstractTransformer;
import org.springframework.messaging.Message;

import de.i3mainz.springframework.integration.ogc.sos.OGCSOSHeaders;
import de.i3mainz.springframework.integration.swe.sos.transformation.util.FOICreator;
import de.i3mainz.springframework.integration.swe.sos.transformation.util.ObservationCreator;
import de.i3mainz.springframework.swe.n52.sos.model.FeatureOfInterest;
import de.i3mainz.springframework.swe.n52.sos.model.Observation;

/**
 * This class provides an transformer which creates Observation and FOI out of
 * the message.
 * 
 * @author Nikolai Bock
 *
 */
public class ObservationTransformer extends AbstractTransformer {

    /** Creator for FOI Object */
    private FOICreator foiCreator;
    /** Creator for Observation Object */
    private ObservationCreator observationCreator;

    /**
     * Constructor with needed fields.
     * 
     * @param foiCreator
     *            the creator of a FOI
     * @param observationCreator
     *            the creator of an Observation
     */
    public ObservationTransformer(FOICreator foiCreator, ObservationCreator observationCreator) {
        this.foiCreator = foiCreator;
        this.observationCreator = observationCreator;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.integration.transformer.AbstractTransformer#
     * doTransform (org.springframework.messaging.Message)
     */
    /**
     * Execute the transformation.
     * 
     * @param message
     *            the orginal message which should be transformed
     * @return message with containing FOI and Observation
     */
    @Override
    protected Object doTransform(Message<?> message) throws Exception {

        if (message.getPayload() instanceof Observation) {
            return message;
        }
        FeatureOfInterest foi = foiCreator.create(message);
        Observation observation = observationCreator.create(message);

        if (observation == null) {
            logger.error("Observation cannot created!");
            return message;
        }
        if (foi == null) {
            logger.error("FeatureOfInterest cannot created!");
            return message;
        }
        return MessageBuilder.withPayload(observation).copyHeaders(message.getHeaders())
                .setHeaderIfAbsent(OGCSOSHeaders.FOI, foi).build();
    }
}
