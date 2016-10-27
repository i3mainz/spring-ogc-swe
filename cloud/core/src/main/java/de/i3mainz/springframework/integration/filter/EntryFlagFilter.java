package de.i3mainz.springframework.integration.filter;

import org.springframework.integration.annotation.Filter;

/**
 * Filter to check if message should be processed.
 * 
 * @author Nikolai Bock
 * 
 */
public class EntryFlagFilter {
    
    /**
     * Check if filter should be ignored or object could be processed.
     * 
     * @param entry object which should be checked
     * @return if module should filter the message stream
     */
    @Filter
    public boolean filter(Object entry) {
        return entry != null && !(entry.toString().isEmpty());
    }

}
