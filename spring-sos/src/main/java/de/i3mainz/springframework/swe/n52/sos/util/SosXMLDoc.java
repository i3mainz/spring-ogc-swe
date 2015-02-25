package de.i3mainz.springframework.swe.n52.sos.util;

/*
 * Info
 * OGC: 07-092r3_Definition_identifier_URNs_in_OGC_namespace_2009
 *      urn:ogc:def:phenomenon:OGC:1.0.30:patientCondition --> phenomenon --> physikalische Werte
 *      urn:ogc:def:dataType:OGC:1.1:string  --> Data Type
 * Sensor Position --> Rechenzentrum o.ae. --> Mainz, FOI hat eigene Position
 * 
 * Examples
 *      time_position           2008-04-02T17:44:15+02:00
 *      SamplingPoint gml:id    foi_2001
 *      gml:name                PADER
 *      gml:pos srsName=urn:ogc:def:crs:EPSG::4326      51.7167 8.76667  long, lat ohne Komma
 *      om:result codeSpace     My leg hurts
 */

/**
 * XML Documents - SOS Service
 * @author sos_sn
 *
 */
public abstract class SosXMLDoc {

    /**
     * Private Constructor 
     * Class not instanceable
     */
    private SosXMLDoc() {
        super();
    }


    /**
     * XML Document - SOS Register Sensor
     * @param sensor
     * @return
     */
    public static String registerSensor(SosSensor sensor){

        final String header =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?> "+
                "<RegisterSensor service=\"SOS\" version=\"1.0.0\"  "+
                    "xmlns=\"http://www.opengis.net/sos/1.0\"   "+
                    "xmlns:swe=\"http://www.opengis.net/swe/1.0.1\" "+
                    "xmlns:ows=\"http://www.opengeospatial.net/ows\"    "+
                    "xmlns:xlink=\"http://www.w3.org/1999/xlink\"   "+
                    "xmlns:gml=\"http://www.opengis.net/gml\"   "+
                    "xmlns:ogc=\"http://www.opengis.net/ogc\"   "+
                    "xmlns:om=\"http://www.opengis.net/om/1.0\" "+
                    "xmlns:sml=\"http://www.opengis.net/sensorML/1.0.1\"    "+
                    "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"    "+
                    "xsi:schemaLocation=\"http://www.opengis.net/sos/1.0    "+
                    "http://schemas.opengis.net/sos/1.0.0/sosRegisterSensor.xsd "+
                    "http://www.opengis.net/om/1.0  "+
                    "http://schemas.opengis.net/om/1.0.0/extensions/observationSpecialization_override.xsd\">   "+
        
                    "<!-- Sensor Description parameter; Currently, this has to be a sml:System -->"+
                    "<SensorDescription>"+
                    "<sml:SensorML version=\"1.0.1\">"+
                    "<sml:member>"+
                        "<sml:System xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">";
                        
            final String identifierList=                
                              "<!--sml:identification element must contain the ID of the sensor-->"+
                             "<sml:identification>"+
                                  "<sml:IdentifierList>"+
                                       "<sml:identifier>"+
                                            "<sml:Term definition=\"urn:ogc:def:identifier:OGC:uniqueID\">"+
                                                 "<sml:value>"+ sensor.getSensorId() +"</sml:value>"+
                                            "</sml:Term>"+
                                       "</sml:identifier>"+
                                  "</sml:IdentifierList>"+
                             "</sml:identification>";
            
            final String position=
                             "<!-- last measured position of sensor -->"+
                             "<sml:position name=\"sensorPosition\">"+
                                  "<swe:Position referenceFrame=\"urn:ogc:def:crs:EPSG::4326\">"+
                                       "<swe:location>"+
                                            "<swe:Vector gml:id=\"SENSOR_LOCATION\">"+
                                                 "<swe:coordinate name=\"longitude\">"+
                                                      "<swe:Quantity>"+
                                                           "<swe:uom code=\"degree\"/>"+
                                                           "<swe:value>8.27</swe:value>"+
                                                      "</swe:Quantity>"+
                                                 "</swe:coordinate>"+
                                                 "<swe:coordinate name=\"latitude\">"+
                                                      "<swe:Quantity>"+
                                                           "<swe:uom code=\"degree\"/>"+
                                                           "<swe:value>50.00</swe:value>"+
                                                      "</swe:Quantity>"+
                                                 "</swe:coordinate>"+
                                                 "<swe:coordinate name=\"altitude\">"+
                                                      "<swe:Quantity>"+
                                                           "<swe:uom code=\"m\"/>"+
                                                           "<swe:value>88.0</swe:value>"+
                                                      "</swe:Quantity>"+
                                                 "</swe:coordinate>"+
                                            "</swe:Vector>"+
                                       "</swe:location>"+
                                  "</swe:Position>"+
                             "</sml:position>";
            
            final String phenomena=
                        
                             "<!-- list containing the input phenomena for this sensor system -->"+
                             "<sml:inputs>"+
                                  "<sml:InputList>"+
                                       "<sml:input name=\"text\">"+
                                            "<swe:ObservableProperty definition=\"urn:ogc:def:dataType:OGC:1.1:string\"/>"+
                                       "</sml:input>"+
                                  "</sml:InputList>"+
                             "</sml:inputs>"+
                        
                             "<!-- list containing the output phenomena of this sensor system; ATTENTION: these phenomena are parsed and inserted into the database; they have to contain offering elements to determine the correct offering for the sensors and measured phenomena -->"+
                              "<sml:outputs>"+
                                   "<sml:OutputList>"+
                                        "<sml:output name=\"text\">"+
                                             "<swe:Category definition=\"urn:ogc:def:dataType:OGC:1.1:string\">"+
                                                "<gml:metaDataProperty>"+
                                                    "<offering>"+
                                                        "<id>"+ sensor.getOfferingId() +"</id>"+
                                                        "<name>"+ sensor.getOfferingName() +"</name>"+
                                                    "</offering>"+
                                                "</gml:metaDataProperty>"+
                                             "</swe:Category>"+
                                        "</sml:output>"+
                                   "</sml:OutputList>"+
                              "</sml:outputs>";
            
            final String footer=                          
                        "</sml:System>"+
                    "</sml:member>"+
                    "</sml:SensorML>"+
                    "</SensorDescription>"+
                    
                    "<!-- ObservationTemplate parameter; this has to be an empty measurement at the moment, as the 52N SOS only supports Measurements to be inserted -->"+
                    "<ObservationTemplate>"+
                         "<om:CategoryObservation>"+
                              "<om:samplingTime/>"+
                              "<om:procedure/>"+
                              "<om:observedProperty/>"+
                              "<om:featureOfInterest></om:featureOfInterest>"+
                              "<om:result codeSpace=\"\"></om:result>"+
                         "</om:CategoryObservation>"+
                    "</ObservationTemplate>"+
    
            "</RegisterSensor>";
            
            //Sensor XML Document
            
            final String xmlRequestDoc =
                    header + 
                    identifierList + 
                    position + 
                    phenomena + 
                    footer; 
        
            return xmlRequestDoc;
        }
    
    
        /**
         * XML Document - SOS InsertObservation
         * @param observation
         * @return
         */
        public static String insertObservation (SosObservation observation){
            
            final String header =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>     " +
                "<InsertObservation     " +
                    "xmlns=\"http://www.opengis.net/sos/1.0\"   " +
                    "xmlns:ows=\"http://www.opengis.net/ows/1.1\"   " +
                    "xmlns:ogc=\"http://www.opengis.net/ogc\"   " +
                    "xmlns:om=\"http://www.opengis.net/om/1.0\" " +
                    "xmlns:sos=\"http://www.opengis.net/sos/1.0\"   " +
                    "xmlns:sa=\"http://www.opengis.net/sampling/1.0\"   " +
                    "xmlns:n52=\"http://www.52north.org/1.0\"   " +
                    "xmlns:gml=\"http://www.opengis.net/gml\"   " +
                    "xmlns:swe=\"http://www.opengis.net/swe/1.0.1\" " +
                    "xmlns:xlink=\"http://www.w3.org/1999/xlink\"   " +
                    "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"    " +
                    "xsi:schemaLocation=\"http://www.opengis.net/sos/1.0    " +
                        "http://schemas.opengis.net/sos/1.0.0/sosInsert.xsd " +
                        "http://www.opengis.net/sampling/1.0    " +
                        "http://schemas.opengis.net/sampling/1.0.0/sampling.xsd     " +
                        "http://www.opengis.net/om/1.0  " +
                        "http://schemas.opengis.net/om/1.0.0/extensions/observationSpecialization_override.xsd\"    " +
                    "service=\"SOS\" version=\"1.0.0\">     ";
                    
            final String sensor =
                    "<AssignedSensorId>" + observation.getSensorId() + "</AssignedSensorId>" +
                    
                    "<om:CategoryObservation>" +                
                         "<om:samplingTime>" +
                              "<gml:TimeInstant>" +
                                   "<gml:timePosition>" + observation.getTimePostion()  + "</gml:timePosition>" +
                              "</gml:TimeInstant>" +
                         "</om:samplingTime>" +                  
                         "<om:procedure xlink:href=\"" + observation.getSensorId() + "\"/>" +
                         "<om:observedProperty xlink:href=\"urn:ogc:def:dataType:OGC:1.1:string\"/>";
            
            final String foi =     
                         "<om:featureOfInterest>" +
                             "<!-- a sampling feature is needed to insert CategoryObservations -->" +
                             "<sa:SamplingPoint gml:id=\"" + observation.getFoiId() + "\">" +
                                 "<gml:name>" + observation.getFoiName() + "</gml:name>" +
                                 "<sa:sampledFeature xlink:href=\"\"/>" +
                                 "<sa:position>" +
                                     "<gml:Point>" +
                                         "<gml:pos srsName=\"urn:ogc:def:crs:EPSG::4326\">" + observation.getFoiPosition() + "</gml:pos>" +
                                     "</gml:Point>" +
                                 "</sa:position>" +
                             "</sa:SamplingPoint>" +
                         "</om:featureOfInterest>";
            
            final String result =
                         "<om:result codeSpace=\"\">" + observation.getResult() + "</om:result>";
            
            final String footer =        
                    "</om:CategoryObservation>" +
                "</InsertObservation>";
            
            // Insert Observation Document
            
            final String insertObservationXMLDoc =
                    header + 
                    sensor + 
                    foi + 
                    result + 
                    footer;
            
            return insertObservationXMLDoc;
        }

}