/**
 * 
 */
package de.i3mainz.springframework.swe.n52.sos.init;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import de.i3mainz.springframework.swe.n52.sos.SOSConnectionParameter;

/**
 * This class provides the initialization process on deploying the module, as
 * well as the destroying process when module will be undeployed.
 * 
 * @author Nikolai Bock
 *
 */
public class SOSInitializer implements InitializingBean, DisposableBean {

    /** The connection parameter of the SOS */
    private SOSConnectionParameter parameter;
    /** The Initializer which will run */
    private SOSPopulator initializer;
    /** The Destroyer which will run */
    private SOSPopulator destroyer;
    /** The process should be run or not */
    private boolean enabled;

    /**
     * @param parameter
     *            the parameter to set
     */
    public void setParameter(SOSConnectionParameter parameter) {
        this.parameter = parameter;
    }

    /**
     * @param initializer
     *            the initializer to set
     */
    public void setInitializer(SOSPopulator initializer) {
        this.initializer = initializer;
    }

    /**
     * @param destroyer
     *            the destroyer to set
     */
    public void setDestroyer(SOSPopulator destroyer) {
        this.destroyer = destroyer;
    }

    /**
     * @param enabled
     *            the enabled to set
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.beans.factory.DisposableBean#destroy()
     */
    @Override
    public void destroy() throws Exception {
        execute(this.destroyer);

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        execute(this.initializer);

    }

    /**
     * Check if the parameter are set and the populator should run. Whether it
     * should run, execute the populator.
     * 
     * @param populator
     *            the populator which should be executed
     */
    private void execute(SOSPopulator populator) {
        Assert.state(this.parameter != null, "SOS-Parameter has to be set");
        if (this.enabled && populator != null) {
            populator.populate(parameter);
        }
    }

}
