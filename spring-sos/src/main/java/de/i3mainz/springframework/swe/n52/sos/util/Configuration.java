package de.i3mainz.springframework.swe.n52.sos.util;

public final class Configuration {
    /**
     * 
     */
    private Configuration() {
        super();
    }
    public static final String SOS_200_EPSG_CODE_PREFIX = "http://www.opengis.net/def/crs/EPSG/0/";
    public static final String SOS_100_EPSG_CODE_PREFIX = "urn:ogc:def:crs:EPSG::";
    public static final String SOS_SENSOR_ALREADY_REGISTERED_MESSAGE_START = "Sensor with ID";
    public static final String SOS_SENSOR_ALREADY_REGISTERED_MESSAGE_END = "is already registered at this SOS";
    public static final String SOS_EXCEPTION_CODE_NO_APPLICABLE_CODE = "NoApplicableCode";
    public static final String SOS_EXCEPTION_OBSERVATION_DUPLICATE_CONSTRAINT = "observation_time_stamp_key";
    public static final String SOS_OBSERVATION_ALREADY_CONTAINED = "observation already contained in sos";
    public static final String SOS_EXCEPTION_OBSERVATION_ALREADY_CONTAINED = "This observation is already contained in SOS database!";
    public static final String SOS_200_OFFERING_ALREADY_REGISTERED_MESSAGE_START = "The offering with the identifier";
    public static final String SOS_200_OFFERING_ALREADY_REGISTERED_MESSAGE_END = "still exists in this service and it is not allowed to insert more than one procedure to an offering!";
    public static final String SOS_200_DUPLICATE_OBSERVATION_CONSTRAINT = "observation_featureofinterestid_observablepropertyid_proced_key";
}