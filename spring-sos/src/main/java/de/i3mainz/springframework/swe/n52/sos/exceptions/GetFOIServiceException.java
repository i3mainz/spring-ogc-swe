/**
 * 
 */
package de.i3mainz.springframework.swe.n52.sos.exceptions;

/**
 * @author Nikolai Bock
 *
 */
public class GetFOIServiceException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param cause
     */
    public GetFOIServiceException(Throwable cause) {
        super.initCause(cause);
    }

}
