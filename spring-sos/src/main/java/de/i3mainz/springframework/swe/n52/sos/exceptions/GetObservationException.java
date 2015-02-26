/**
 * 
 */
package de.i3mainz.springframework.swe.n52.sos.exceptions;

/**
 * @author Nikolai Bock
 *
 */
public class GetObservationException extends Exception {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new OXFException with the specified cause.
     * 
     * @param cause
     *        the cause which is saved for later retrieval by the Throwable.getCause() method. A null value is
     *        permitted, and indicates that the cause is nonexistent or unknown.
     */
    public GetObservationException(Throwable cause) {
        super.initCause(cause);
    }

}
