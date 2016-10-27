/**
 * 
 */
package de.i3mainz.springframework.xd.swe.sos;

import org.springframework.xd.module.options.spi.ModuleOption;

/**
 * @author Nikolai Bock
 *
 */
public class SystemFOIMixin{

    private String systemFoiName = "'Standort des Projektsystems'";
    private String systemFoiUri = "'system_foi_service'";
    private String systemFoiPosition = "'POINT(49.99984232 8.23548545)'";
    private int systemFoiSRID = 4326;

    /**
     * @return the systemFoiName
     */
    public final String getSystemFoiName() {
        return systemFoiName;
    }

    /**
     * @param systemFoiName
     *            the systemFoiName to set
     */
    @ModuleOption("Name of the System FOI (SpEL).")
    public final void setSystemFoiName(String systemFoiName) {
        this.systemFoiName = systemFoiName;
    }

    /**
     * @return the systemFoiUri
     */
    public final String getSystemFoiUri() {
        return systemFoiUri;
    }

    /**
     * @param systemFoiUri
     *            the systemFoiUri to set
     */
    @ModuleOption("URI/ID of the System FOI (SpEL).")
    public final void setSystemFoiUri(String systemFoiUri) {
        this.systemFoiUri = systemFoiUri;
    }

    /**
     * @return the systemFoiPosition
     */
    public final String getSystemFoiPosition() {
        return systemFoiPosition;
    }

    /**
     * @param systemFoiPosition
     *            the systemFoiPosition to set
     */
    @ModuleOption("Position of the System FOI (SpEL).")
    public final void setSystemFoiPosition(String systemFoiPosition) {
        this.systemFoiPosition = systemFoiPosition;
    }

    /**
     * @return the systemFoiSRID
     */
    public final int getSystemFoiSRID() {
        return systemFoiSRID;
    }

    /**
     * @param systemFoiSRID
     *            the systemFoiSRID to set
     */
    @ModuleOption("SRID of the System FOI.")
    public final void setSystemFoiSRID(int systemFoiSRID) {
        this.systemFoiSRID = systemFoiSRID;
    }

}
