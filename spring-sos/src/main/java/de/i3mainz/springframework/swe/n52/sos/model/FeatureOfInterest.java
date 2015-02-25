package de.i3mainz.springframework.swe.n52.sos.model;

public class FeatureOfInterest {

    private String id;
    private String name;
    private String position;

    public FeatureOfInterest(String id, String name, String position) {
        super();
        setId(id);
        setName(name);
        setPosition(position);
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
     * @param name
     *            the name to set
     */
    public final void setName(String name) {
        this.name = name;
    }

    /**
     * @return the position
     */
    public final String getPosition() {
        return position;
    }

    /**
     * @param position
     *            the position to set
     */
    public final void setPosition(String position) {
        this.position = position;
    }

}