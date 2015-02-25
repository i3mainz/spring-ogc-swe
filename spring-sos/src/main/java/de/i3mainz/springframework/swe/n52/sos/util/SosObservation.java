package de.i3mainz.springframework.swe.n52.sos.util;

/**
 * 
 * @author Nikolai Bock
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