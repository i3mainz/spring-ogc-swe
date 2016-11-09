/**
 * 
 */
package de.i3mainz.springframework.ogc.core;

import org.n52.oxf.ows.ServiceDescriptor;

/**
 * @author Nikolai Bock
 *
 */
public interface OWSServiceOperations {
    ServiceDescriptor getCapabilities();

}
