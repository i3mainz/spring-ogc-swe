package de.i3mainz.springframework.swe.n52.sos.model;

public class Sensor {

    private String id;
    private String name;
    private Offering offering;

    public Sensor(String id, Offering offering) {
        super();
        setId(id);
        setOffering(offering);
    }

    public Sensor(String id, String offeringId, String offeringName) {
        this(id, new Offering(offeringId, offeringName));
    }

    /**
     * @return the id
     */
    public final String getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public final void setId(String id) {
        this.id = id;
    }
    
    

    /**
     * @return the name
     */
    public final String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public final void setName(String name) {
        this.name = name;
    }

    /**
     * @return the offering
     */
    public final Offering getOffering() {
        return offering;
    }

    /**
     * @param offering
     *            the offering to set
     */
    public final void setOffering(Offering offering) {
        this.offering = offering;
    }

}