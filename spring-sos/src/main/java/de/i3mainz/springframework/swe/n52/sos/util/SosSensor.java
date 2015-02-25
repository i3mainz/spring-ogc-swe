package de.i3mainz.springframework.swe.n52.sos.util;

/**
 * Sensor contains:
 *      - Sensor ID -->
 *      - Offering ID-->
 *      - Offering Name -->
 * 
 * @author Nikolai Bock
 *
 */
public class SosSensor {
    //String reg_sensor_id, String reg_offering_id, String reg_offering_name
    
    private String sensorId;
    private String offeringId;
    private String offeringName;

    public SosSensor(){
        this.sensorId = "";
        this.offeringId = "";
        this.offeringName = "";
    }
    
    public void setSensor(String sensorId, String offeringId, String offeringName){
        this.sensorId = sensorId;
        this.offeringId = offeringId;
        this.offeringName = offeringName;
    }
    
    public void setSensorId(String sensorId){
        this.sensorId = sensorId;
        
    }

    public String getSensorId(){
        return this.sensorId;
        
    }
    public String getOfferingId(){
        return this.offeringId;
        
    }
    public String getOfferingName(){
        return this.offeringName;
        
    }
}