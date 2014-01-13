package de.i3mainz.springframework.swe.n52.sos.util;

/**
 * Sensor contains:
 * 		- Sensor ID -->
 * 		- Offering ID-->
 * 		- Offering Name -->
 * 
 * @author sos_sn
 *
 */
public class Sos_sensor {
	//String reg_sensor_id, String reg_offering_id, String reg_offering_name
	
	String sensor_id;
	String offering_id;
	String offering_name;

	public Sos_sensor(){
		sensor_id = "";
		offering_id = "";
		offering_name = "";
	}
	
	public void set_sensor(String sensor_id_in, String offering_id_in, String offering_name_in){
		sensor_id = sensor_id_in;
		offering_id = offering_id_in;
		offering_name = offering_name_in;
	}
	
	public void set_sensor_id(String sensor_id_in){
		sensor_id = sensor_id_in;
		
	}

	public String get_sensor_id(){
		return sensor_id;
		
	}
	public String get_offering_id(){
		return offering_id;
		
	}
	public String get_offering_name(){
		return offering_name;
		
	}
}
