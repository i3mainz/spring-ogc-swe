/**
 * 
 */
package de.i3mainz.springframework.integration.expressions;

import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;

/**
 * This class provides a conversion service which provides the default
 * converters from {@link DefaultConversionService} and is extendable with own
 * custom {@link Converter} using all kind of source and target.
 * 
 * @author Nikolai Bock
 *
 */
public class ExtendableConversionService extends DefaultConversionService {

    /**
     * Allows to add a {@link List} of {@link Converter} which should injected
     * in context.
     * 
     * @param converters
     *            {@link List} of {@link Converter}
     */
    public ExtendableConversionService(List<Converter<?, ?>> converters) {
        super();
        if (converters != null && !converters.isEmpty()) {
            converters.forEach(this::addConverter);
        }
    }
}
