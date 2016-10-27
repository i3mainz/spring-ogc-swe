package de.i3mainz.springframework.xd.swe.sos;

import org.springframework.xd.module.options.spi.Mixin;
import org.springframework.xd.module.options.spi.ModuleOption;
import org.springframework.xd.module.options.spi.ProfileNamesProvider;

/**
 * Parameter management class for SOS-Sink module. Using also
 * {@link SensorMixin} and {@link SOSConnectionMixin}
 * 
 * @author Nikolai Bock
 *
 */
@Mixin({ SensorMixin.class, SOSConnectionMixin.class, SystemFOIMixin.class, SensorPropertyInformationMixin.class })
public class OGCSOSSinkModuleOptionsMetadata implements ProfileNamesProvider {

    /** should the sensor be initialized on module startup */
    private boolean initSensor = false;
    private boolean dynamicSensorCreation = false;
    private String spatialField;
    private String timeField;
    private String value;
    private String expressionpattern;

    /**
     * @param initSensor
     *            the initSensor to set
     */
    @ModuleOption("Whether the Sensor should be registered through initialization")
    public void setInitSensor(boolean initSensor) {
        this.initSensor = initSensor;
    }

    /**
     * @return the initSensor
     */
    public boolean isInitSensor() {
        return initSensor;
    }

    /**
     * @return the dynamicSensorCreation
     */
    public boolean isDynamicSensorCreation() {
        return dynamicSensorCreation;
    }

    /**
     * @param dynamicSensorCreation
     *            the dynamicSensorCreation to set
     */
    @ModuleOption("Whether RegisterSensor can be executed from message information (dynamic) --> initSensor must be true)")
    public void setDynamicSensorCreation(boolean dynamicSensorCreation) {
        this.dynamicSensorCreation = dynamicSensorCreation;
    }

    /**
     * @return the spatialField
     */
    public String getSpatialField() {
        return spatialField;
    }

    /**
     * @param spatialField
     *            the spatialField to set
     */
    @ModuleOption("Expression where the spatial information is located in the message")
    public void setSpatialField(String spatialField) {
        this.spatialField = spatialField;
    }

    /**
     * @return the timeField
     */
    public String getTimeField() {
        return timeField;
    }

    /**
     * @param timeField
     *            the timeField to set
     */
    @ModuleOption("Expression where the temporal information is located in the message")
    public void setTimeField(String timeField) {
        this.timeField = timeField;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    @ModuleOption("Expression where the value is stored in the message")
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the expressionpattern
     */
    public String getExpressionpattern() {
        return expressionpattern;
    }

    /**
     * @param expressionpattern
     *            the expressionpattern to set
     */
    @ModuleOption("Pattern of Expression information")
    public void setExpressionpattern(String expressionpattern) {
        this.expressionpattern = expressionpattern;
    }

    @Override
    public String[] profilesToActivate() {
        if ("twitter".equalsIgnoreCase(this.getExpressionpattern())) {
            if (this.getSpatialField() == null || this.getSpatialField().isEmpty()) {
                this.setSpatialField("headers.location");
            }
            if (this.getTimeField() == null || this.getTimeField().isEmpty()) {
                this.setTimeField("payload.created_at");
            }
            if (this.getValue() == null || this.getValue().isEmpty()) {
                this.setValue("headers.sentiment");
            }
        }
        return new String[] { this.getExpressionpattern(),
                isDynamicSensorCreation() ? "use-dynamicCreation" : "use-staticCreation" };
    }

}
