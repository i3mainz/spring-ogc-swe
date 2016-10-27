/**
 * 
 */
package de.i3mainz.springframework.swe.n52.sos.model;

/**
 * @author Nikolai Bock
 *
 */
public class UnitOfMeasurement extends Resource {

    public UnitOfMeasurement(String code, String uri) {
        super(code, uri);
    }

    public String getCode() {
        return getName();
    }

}
