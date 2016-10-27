package de.i3mainz.springframework.swe.n52.sos.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class FeatureOfInterest extends Resource {

    private static final Log LOG = LogFactory.getLog(FeatureOfInterest.class);
    private Point position;

    public FeatureOfInterest(String name, String uri, Point position) {
        super(name, uri);
        setPosition(position);
    }

    public FeatureOfInterest(String name, String uri, String position, int srid) {
        super(name, uri);
        WKTReader reader = new WKTReader();
        try {
            Geometry p = reader.read(position);
            if (srid == 0) {
                srid = 4326;
            }
            p.setSRID(srid);
            setPosition((Point) p);
        } catch (ParseException e) {
            LOG.error("Could not parse the position string --> " + position + "!", e);
        }
    }

    /**
     * @return the position
     */
    public final Point getPosition() {
        return position;
    }

    /**
     * @param position
     *            the position to set
     */
    public final void setPosition(Point position) {
        this.position = position;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((position == null) ? 0 : position.hashCode());
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
        FeatureOfInterest other = (FeatureOfInterest) obj;
        if (position == null) {
            if (other.position != null)
                return false;
        } else if (!position.equals(other.position))
            return false;
        return true;
    }

}