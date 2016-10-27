/**
 * 
 */
package de.i3mainz.springframework.integration.swe.sos.transformation.util;

/**
 * 
 * This class provide static functions to provide validations, like
 * "instanceOf".
 * 
 * @author Nikolai Bock
 *
 */
public abstract class Validator {

    /**
     * Private constructor to hide the implicit public one.
     */
    private Validator() {
    }

    /**
     * Check if a object is of a given class type.
     * 
     * @param object
     *            the object which should be checked
     * @param class1
     *            the class type
     * @param <T>
     *            type parameter for class
     * @return whether object is instanceof class type
     */
    public static <T> boolean checkInstance(Object object, Class<T> class1) {
        try {
            class1.cast(object);
        } catch (ClassCastException e) {
            return false;
        }
        return true;
    }

}