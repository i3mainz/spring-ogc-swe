package de.i3mainz.springframework.swe.n52.sos.util;

/*
 * Info
 * OGC: 07-092r3_Definition_identifier_URNs_in_OGC_namespace_2009
 * 		urn:ogc:def:phenomenon:OGC:1.0.30:patientCondition --> phenomenon --> physikalische Werte
 * 		urn:ogc:def:dataType:OGC:1.1:string  --> Data Type
 * Sensor Position --> Rechenzentrum o.ae. --> Mainz, FOI hat eigene Position
 * 
 * Examples
 * 		time_position 			2008-04-02T17:44:15+02:00
 * 		SamplingPoint gml:id	foi_2001
 * 		gml:name				PADER
 * 		gml:pos srsName=urn:ogc:def:crs:EPSG::4326		51.7167 8.76667  long, lat ohne Komma
 * 		om:result codeSpace		My leg hurts
 */

/**
 * XML Documents - SOS Service
 * @author sos_sn
 *
 */
public abstract class Sos_xml_doc {
	
	/**
	 * XML Document - SOS Register Sensor
	 * @param sensor
	 * @return
	 */
	public static String register_sensor(Sos_sensor sensor){
			
			// Important: tabspace (behind >  and  before "+)  --> Example: UTF-8\"?>	"+
			String xml_register_sensor_head = (
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>	"+
				"<RegisterSensor service=\"SOS\" version=\"1.0.0\"	"+
					"xmlns=\"http://www.opengis.net/sos/1.0\"	"+
					"xmlns:swe=\"http://www.opengis.net/swe/1.0.1\"	"+
					"xmlns:ows=\"http://www.opengeospatial.net/ows\"	"+
					"xmlns:xlink=\"http://www.w3.org/1999/xlink\"	"+
					"xmlns:gml=\"http://www.opengis.net/gml\"	"+
					"xmlns:ogc=\"http://www.opengis.net/ogc\"	"+
					"xmlns:om=\"http://www.opengis.net/om/1.0\"	"+
					"xmlns:sml=\"http://www.opengis.net/sensorML/1.0.1\"	"+
					"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	"+
					"xsi:schemaLocation=\"http://www.opengis.net/sos/1.0	"+
					"http://schemas.opengis.net/sos/1.0.0/sosRegisterSensor.xsd	"+
					"http://www.opengis.net/om/1.0	"+
					"http://schemas.opengis.net/om/1.0.0/extensions/observationSpecialization_override.xsd\">	"+
		
					"<!-- Sensor Description parameter; Currently, this has to be a sml:System -->"+
					"<SensorDescription>"+
					"<sml:SensorML version=\"1.0.1\">"+
					"<sml:member>"+
						"<sml:System xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
						
			String xml_register_sensor_IdentifierList= (				
							  "<!--sml:identification element must contain the ID of the sensor-->"+
						     "<sml:identification>"+
						          "<sml:IdentifierList>"+
						               "<sml:identifier>"+
						                    "<sml:Term definition=\"urn:ogc:def:identifier:OGC:uniqueID\">"+
						                         "<sml:value>"+ sensor.sensor_id +"</sml:value>"+
						                    "</sml:Term>"+
						               "</sml:identifier>"+
						          "</sml:IdentifierList>"+
						     "</sml:identification>");
			
			String xml_register_sensor_position= (
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
						     "</sml:position>");
			
			String xml_register_sensor_phenomena= (
						
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
														"<id>"+ sensor.offering_id +"</id>"+
														"<name>"+ sensor.offering_name +"</name>"+
													"</offering>"+
												"</gml:metaDataProperty>"+
						                     "</swe:Category>"+
						                "</sml:output>"+
						           "</sml:OutputList>"+
						      "</sml:outputs>");
			
			String xml_register_sensor_foot= (					      
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
	
			"</RegisterSensor>");
			
			//Sensor XML Document
			
			String xml_reg_sensor_doc = (
					xml_register_sensor_head + 
					xml_register_sensor_IdentifierList + 
					xml_register_sensor_position + 
					xml_register_sensor_phenomena + 
					xml_register_sensor_foot); 
		
			return xml_reg_sensor_doc;
		}
	
	
		/**
		 * XML Document - SOS InsertObservation
		 * @param observation
		 * @return
		 */
		public static String insert_observation (Sos_observation observation){
			
			String xml_insert_observation_head =(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>		" +
				"<InsertObservation		" +
					"xmlns=\"http://www.opengis.net/sos/1.0\"	" +
					"xmlns:ows=\"http://www.opengis.net/ows/1.1\"	" +
					"xmlns:ogc=\"http://www.opengis.net/ogc\"	" +
					"xmlns:om=\"http://www.opengis.net/om/1.0\"	" +
					"xmlns:sos=\"http://www.opengis.net/sos/1.0\"	" +
					"xmlns:sa=\"http://www.opengis.net/sampling/1.0\"	" +
					"xmlns:n52=\"http://www.52north.org/1.0\"	" +
					"xmlns:gml=\"http://www.opengis.net/gml\"	" +
					"xmlns:swe=\"http://www.opengis.net/swe/1.0.1\"	" +
					"xmlns:xlink=\"http://www.w3.org/1999/xlink\"	" +
					"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	" +
					"xsi:schemaLocation=\"http://www.opengis.net/sos/1.0	" +
						"http://schemas.opengis.net/sos/1.0.0/sosInsert.xsd	" +
						"http://www.opengis.net/sampling/1.0	" +
						"http://schemas.opengis.net/sampling/1.0.0/sampling.xsd		" +
						"http://www.opengis.net/om/1.0	" +
						"http://schemas.opengis.net/om/1.0.0/extensions/observationSpecialization_override.xsd\"	" +
					"service=\"SOS\" version=\"1.0.0\">		");
					
			String xml_insert_observation_sensor =(
					"<AssignedSensorId>" + observation.get_sensor_id() + "</AssignedSensorId>" +
					
					"<om:CategoryObservation>" +				
					     "<om:samplingTime>" +
					          "<gml:TimeInstant>" +
					               "<gml:timePosition>" + observation.get_gml_time_position()  + "</gml:timePosition>" +
					          "</gml:TimeInstant>" +
					     "</om:samplingTime>" +				     
					     "<om:procedure xlink:href=\"" + observation.get_sensor_id() + "\"/>" +
					     "<om:observedProperty xlink:href=\"urn:ogc:def:dataType:OGC:1.1:string\"/>");
			
			String xml_insert_observation_foi =(	     
					     "<om:featureOfInterest>" +
					         "<!-- a sampling feature is needed to insert CategoryObservations -->" +
					         "<sa:SamplingPoint gml:id=\"" + observation.get_foi_gml_id() + "\">" +
					             "<gml:name>" + observation.get_foi_gml_name() + "</gml:name>" +
					             "<sa:sampledFeature xlink:href=\"\"/>" +
					             "<sa:position>" +
					                 "<gml:Point>" +
					                     "<gml:pos srsName=\"urn:ogc:def:crs:EPSG::4326\">" + observation.get_foi_gml_position() + "</gml:pos>" +
									 "</gml:Point>" +
					             "</sa:position>" +
					         "</sa:SamplingPoint>" +
					     "</om:featureOfInterest>");
			
		    String xml_insert_observation_result =(
					     "<om:result codeSpace=\"\">" + observation.get_result() + "</om:result>");
		    
			String xml_insert_observation_foot =(		     
					"</om:CategoryObservation>" +
				"</InsertObservation>");
			
			// Insert Observation Document
			
			String xml_insert_observation_doc = (
					xml_insert_observation_head + 
					xml_insert_observation_sensor + 
					xml_insert_observation_foi + 
					xml_insert_observation_result + 
					xml_insert_observation_foot);
			
			return xml_insert_observation_doc;
		}

}
