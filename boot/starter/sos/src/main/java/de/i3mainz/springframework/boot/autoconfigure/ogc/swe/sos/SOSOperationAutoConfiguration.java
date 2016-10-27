/**
 * 
 */
package de.i3mainz.springframework.boot.autoconfigure.ogc.swe.sos;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.n52.oxf.OXFException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.i3mainz.springframework.swe.n52.sos.SOSConnectionParameter;
import de.i3mainz.springframework.swe.n52.sos.core.SOSTemplate;
import de.i3mainz.springframework.swe.n52.sos.core.SensorObservationServiceOperations;

/**
 * @author Nikolai Bock
 *
 */
@Configuration
@ConditionalOnClass({ SensorObservationServiceOperations.class })
@EnableConfigurationProperties(SOSConnectionProperties.class)
public class SOSOperationAutoConfiguration {

    private static final Log LOG = LogFactory.getLog(SOSOperationAutoConfiguration.class);

    @Autowired
    SOSConnectionProperties connectionProperties;

    @Bean
    @ConditionalOnMissingBean
    SensorObservationServiceOperations operations(SOSConnectionParameter connection) {

        SOSTemplate template = new SOSTemplate();
        template.setConnectionParameter(connection);
        return template;

    }

    @Bean
    @ConditionalOnMissingBean
    SOSConnectionParameter connection() {
        SOSConnectionParameter connection = new SOSConnectionParameter();
        connection.setUrl(connectionProperties.getUrl());
        connection.setVersion(connectionProperties.getVersion());
        try {
            connection.setBinding(connectionProperties.getBinding());
        } catch (OXFException e) {
            LOG.error("Binding " + connectionProperties.getBinding() + " is not supported!", e);
        }
        return connection;
    }

}
