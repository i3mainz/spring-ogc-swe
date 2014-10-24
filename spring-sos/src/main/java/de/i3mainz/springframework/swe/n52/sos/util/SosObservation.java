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
public class SosObservation {
	
	private String sensorId;
	private String timeposition;
	private String foiId;
	private String foiName;
	private String foiPosition;
	private String result;
	
	public SosObservation(){
		this.sensorId = "";
		this.timeposition = "";
		this.foiId = "";
		this.foiName = "";
		this.foiPosition = "";
		this.result = "";
	}
		
	public void setObservation(String sensorId, String timePosition, String foiId, String foiName, String foiPosition, String result){
		this.sensorId = sensorId;
		this.timeposition = timePosition;
		this.foiId = foiId;
		this.foiName = foiName;
		this.foiPosition = foiPosition;
		this.result = result;
	}
	
	public String getSensorId(){
		return this.sensorId;		
	}
	public String getTimePostion(){
		return this.timeposition;		
	}
	public String getFoiId(){
		return this.foiId;		
	}
	public String getFoiName(){
		return foiName;		
	}
	public String getFoiPosition(){
		return foiPosition;		
	}
	public String getResult(){
		return result;		
	}
}
