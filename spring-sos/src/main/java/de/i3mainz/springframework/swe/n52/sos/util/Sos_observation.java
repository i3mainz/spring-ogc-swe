package de.i3mainz.springframework.swe.n52.sos.util;
/*
 * ToDO
 * foi_gml_id
 * 
 * Prüfung:
 * 		Variablen richtig befüllt
 */
/**
 * Sos_foi, SOS feature of interest
 * 
 * Examples:
 *		time_position:	 		2008-04-02T17:44:15+02:00
 *		SamplingPoint:	 		gml:id=foi_2001
 *		gml:name:		 		PADER
 *		gml:pos srsName: 		urn:ogc:def:crs:EPSG::4326>51.7167 8.76667
 *		om:result codeSpace:	My leg hurts
 * @author sos_sn
 *
 */
public class Sos_observation {
	
	String sensor_id;
	String gml_time_position;
	String foi_gml_id;
	String foi_gml_name;
	String foi_gml_position;
	String result;
	
	public Sos_observation(){
		sensor_id = "";
		gml_time_position = "";
		foi_gml_id = "";
		foi_gml_name = "";
		foi_gml_position = "";
		result = "";
	}
		
	public void set_obs(String sensor_id_in, String time_position_in, String foi_id_in, String foi_gml_name_in, String foi_gml_pos_in, String result_in){
		sensor_id = sensor_id_in;
		gml_time_position = time_position_in;
		foi_gml_id = foi_id_in;
		foi_gml_name = foi_gml_name_in;
		foi_gml_position = foi_gml_pos_in;
		result = result_in;
	}
	
	public String get_sensor_id(){
		return sensor_id;		
	}
	public String get_gml_time_position(){
		return gml_time_position;		
	}
	public String get_foi_gml_id(){
		return foi_gml_id;		
	}
	public String get_foi_gml_name(){
		return foi_gml_name;		
	}
	public String get_foi_gml_position(){
		return foi_gml_position;		
	}
	public String get_result(){
		return result;		
	}
}
