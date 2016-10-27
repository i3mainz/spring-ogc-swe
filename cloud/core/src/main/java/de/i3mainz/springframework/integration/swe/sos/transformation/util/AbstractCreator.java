/**
 * 
 */
package de.i3mainz.springframework.integration.swe.sos.transformation.util;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * @author Nikolai Bock
 *
 */
public abstract class AbstractCreator implements Creator, InitializingBean {
    /** The Expression-Evaluator */
    private SOSExpressionEvaluator evaluator;
    

    /**
     * @param evaluator
     *            the evaluator to set
     */
    public final void setEvaluator(SOSExpressionEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    /**
     * @return the evaluator
     */
    protected final SOSExpressionEvaluator getEvaluator() {
        return this.evaluator;
    }
    
    /* (non-Javadoc)
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(evaluator, "Evaluator has to be set");
    }
    
    
}
