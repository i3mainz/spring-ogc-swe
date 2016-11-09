/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.i3mainz.springframework.integration.ogc.sos.support;

import de.i3mainz.springframework.swe.n52.sos.model.Sensor;

/**
 * Contains utility methods used by the SOSAdapter components.
 *
 * @author Nikolai Bock
 * @since 1.0
 *
 */
public final class OGCSOSUtils {

    /** Prevent instantiation. */
    private OGCSOSUtils() {
        throw new AssertionError();
    }
    
    public static String getSensorId(Object toSensor) {
        if (toSensor instanceof Sensor) {
            return ((Sensor) toSensor).getId();
        } else if (toSensor instanceof String) {
            return (String) toSensor;
        }
        return null;
    }

}