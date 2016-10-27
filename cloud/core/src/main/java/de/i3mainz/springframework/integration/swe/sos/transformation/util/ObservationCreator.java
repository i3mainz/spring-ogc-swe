/**
 * 
 */
package de.i3mainz.springframework.integration.swe.sos.transformation.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.integration.json.JsonPropertyAccessor.ToStringFriendlyJsonNode;
import org.springframework.messaging.Message;

import de.i3mainz.springframework.swe.n52.sos.model.Observation;

/**
 * This class provides a creator for an Observation attribute.
 * 
 * @author Nikolai Bock
 *
 */
public class ObservationCreator extends AbstractCreator {

    private static final Log LOG = LogFactory.getLog(ObservationCreator.class);
    /** The expression for temporal information */
    private String temporalFieldExpression;
    /** The expression for the value information */
    private String valueExpression;

    /**
     * @param temporalFieldExpression
     *            the temporalFieldExpression to set
     */
    public void setTemporalFieldExpression(String temporalFieldExpression) {
        this.temporalFieldExpression = temporalFieldExpression;
    }

    /**
     * @param valueExpression
     *            the valueExpression to set
     */
    public void setValueExpression(String valueExpression) {
        this.valueExpression = valueExpression;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.i3mainz.springframework.integration.swe.sos.transformation.util.
     * Creator#create (org.springframework.messaging.Message)
     */
    @Override
    public Observation create(Message<?> message) {

        Date time = createCreationTime(message);
        Object value = getEvaluator().evaluate(message, valueExpression, Object.class);
        if (time == null || value == null) {
            LOG.info("Observation can't be created. Return null");
            return null;
        }
        return new Observation(time, value);
    }

    /**
     * Create the time and date information for the observation creation value.
     * 
     * @param message
     *            complete message with all information
     * @return the date object including time
     */
    private Date createCreationTime(Message<?> message) {
        try {

            Object dateObject = getEvaluator().evaluate(message, temporalFieldExpression, Object.class);
            if (Validator.checkInstance(dateObject, String.class)) {
                return createDateFromString(dateObject);
            } else if (Validator.checkInstance(dateObject, ToStringFriendlyJsonNode.class)) {
                return createDateFromString(dateObject);
            } else {
                throw new DateTimeException("Kein Erstellungsdatum vorhanden.");
            }

        } catch (DateTimeException e) {
            LOG.warn(e.getMessage() + " Verwende aktuelles Datum!");
            LOG.debug("Es wird das aktuelle Datum verwendet: ", e);
            return new Date();
        }
    }

    /**
     * @param format
     * @param dateObject
     * @return
     */
    private static Date createDateFromString(Object dateObject) {
        DateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH);
        try {
            return format.parse(dateObject.toString());
        } catch (ParseException e) {
            return new Date(new Long(dateObject.toString()));
        }
    }
}
