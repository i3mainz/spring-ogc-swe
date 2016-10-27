package de.i3mainz.springframework.swe.n52.sos.model;

public final class Sensor extends Resource {

    private Offering offering;

    public Sensor(String name, String uri, Offering offering) {
        super(name, uri);
        this.offering = offering;
    }

    public Sensor(String name, String uri, String offeringName, String offeringURI) {
        this(name, uri, new Offering(offeringName, offeringURI));
    }

    /**
     * @return the id
     */
    public final String getId() {
        return getUri();
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

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((offering == null) ? 0 : offering.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Sensor other = (Sensor) obj;
        if (offering == null) {
            if (other.offering != null)
                return false;
        } else if (!offering.equals(other.offering))
            return false;
        return true;
    }

}