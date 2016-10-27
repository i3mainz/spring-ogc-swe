/**
 * 
 */
package de.i3mainz.springframework.integration.swe.sos.transformation.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.messaging.Message;

import de.i3mainz.springframework.swe.n52.sos.model.Sensor;

/**
 * @author Nikolai Bock
 *
 */
public final class SensorCreator extends AbstractCreator implements InitializingBean {

    private static final Log LOG = LogFactory.getLog(SensorCreator.class);

    private String sensorNameExpression;
    private String sensorURIExpression;
    private String offeringNameExpression;
    private String offeringURIExpression;

    /**
     * @param sensorNameExpression
     *            the sensorNameExpression to set
     */
    public final void setSensorNameExpression(String sensorNameExpression) {
        this.sensorNameExpression = sensorNameExpression;
    }

    /**
     * @param sensorURIExpression
     *            the sensorURIExpression to set
     */
    public final void setSensorURIExpression(String sensorURIExpression) {
        this.sensorURIExpression = sensorURIExpression;
    }

    /**
     * @param offeringNameExpression
     *            the offeringNameExpression to set
     */
    public final void setOfferingNameExpression(String offeringNameExpression) {
        this.offeringNameExpression = offeringNameExpression;
    }

    /**
     * @param offeringURIExpression
     *            the offeringURIExpression to set
     */
    public final void setOfferingURIExpression(String offeringURIExpression) {
        this.offeringURIExpression = offeringURIExpression;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.i3mainz.springframework.integration.swe.sos.transformation.util.Creator#create(
     * org.springframework.messaging.Message)
     */
    @Override
    public Sensor create(Message<?> message) {
        return new Sensor(getEvaluator().evaluate(message, this.sensorNameExpression, String.class),
                getEvaluator().evaluate(message, this.sensorURIExpression, String.class),
                getEvaluator().evaluate(message, this.offeringNameExpression, String.class),
                getEvaluator().evaluate(message, this.offeringURIExpression, String.class));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        if(this.sensorURIExpression==null){
            this.sensorURIExpression="'test7000'";
        }
        
    }

}
