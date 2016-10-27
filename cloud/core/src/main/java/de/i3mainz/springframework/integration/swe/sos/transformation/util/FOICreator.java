/**
 * 
 */
package de.i3mainz.springframework.integration.swe.sos.transformation.util;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.springframework.integration.json.JsonPropertyAccessor.ToStringFriendlyJsonNode;
import org.springframework.messaging.Message;
import org.springframework.util.Assert;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

import de.i3mainz.springframework.integration.swe.sos.exceptions.FoiAttributeNotInExpressionException;
import de.i3mainz.springframework.swe.n52.sos.model.FeatureOfInterest;

/**
 * This class provides a creator for an Feature of Interest attribute.
 * 
 * @author Nikolai Bock
 *
 */
public final class FOICreator extends AbstractCreator {

    private static final Log LOG = LogFactory.getLog(FOICreator.class);
    /** The Expression for the spatial information */
    private String spatialFieldExpression;
    /** The Expression for the ID from FOI */
    private String foiIDExpression;
    /** The Expression for the name from FOI */
    private String foiNameExpression;

    /**
     * @param spatialFieldExpression
     *            the spatialFieldExpression to set
     */
    public void setSpatialFieldExpression(String spatialFieldExpression) {
        this.spatialFieldExpression = spatialFieldExpression;
    }

    /**
     * @param foiIDExpression
     *            the foiIDExpression to set
     */
    public void setFoiIDExpression(String foiIDExpression) {
        this.foiIDExpression = foiIDExpression;
    }

    /**
     * @param foiNameExpression
     *            the foiNameExpression to set
     */
    public void setFoiNameExpression(String foiNameExpression) {
        this.foiNameExpression = foiNameExpression;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.i3mainz.springframework.integration.swe.sos.transformation.util.
     * Creator#create (org.springframework.messaging.Message)
     */
    @Override
    public FeatureOfInterest create(Message<?> message) {
        Object spatialField = getEvaluator().evaluate(message, this.spatialFieldExpression, Object.class);

        Point geom = (Point) getMessageGeometry(spatialField);

        if (geom == null) {
            LOG.error("Geometry object cannot be loaded!");
            return null;
        }

        if (geom.getSRID() == 0) {
            geom.setSRID(4326);
        }

        String foiID = createFOIAttribute(spatialField, foiIDExpression, geom, FOIAttribute.ID);
        String foiName = createFOIAttribute(spatialField, foiNameExpression, geom, FOIAttribute.NAME);

        return new FeatureOfInterest(foiName, foiID, geom);
    }

    /**
     * Get the geometry object out of the message information.
     * 
     * @param object
     *            the spatial field
     * @return the geometry
     */
    private Geometry getMessageGeometry(Object object) {
        if (Validator.checkInstance(object, String.class)) {
            return getGeomFromJSON(object.toString());
        } else if (Validator.checkInstance(object, Map.class)) {
            return getGeomFromMapCoords((Map<?, ?>) object);
        } else if (Validator.checkInstance(object, ToStringFriendlyJsonNode.class)) {
            return getGeomFromJSON(object.toString());
        }
        LOG.error("Hier will ich nicht landen");
        return null;
    }

    /**
     * Create the FOI attribute.
     * 
     * @param root
     *            object the information should be extracted
     * @param expression
     *            the expression which will be used
     * @param geom
     *            the geometry
     * @param attribute
     *            the attribute enum
     * @return the result
     */
    private String createFOIAttribute(Object root, String expression, Geometry geom, FOIAttribute attribute) {
        if (expression != null && !expression.isEmpty()) {
            return getFOIfromExpression(root, expression, geom, attribute);
        } else {
            try {
                return createFOIAttribute(geom, attribute);
            } catch (Exception e) {
                LOG.error("FOI-" + attribute.toString() + " cannot created with " + geom.toText(), e);
                return null;
            }
        }
    }

    /**
     * Create FOI attribute with existing attribute and geometry.
     * 
     * @param geom
     *            the geometry
     * @param attribute
     *            the attribute enum
     * @return the result
     */
    private String createFOIAttribute(Geometry geom, FOIAttribute attribute) {
        Assert.notNull(geom, "Geometry Object out of JTS needed");
        StringBuilder builder = new StringBuilder();
        if (attribute.equals(FOIAttribute.ID)) {
            builder.append("foi_");
        }
        return builder.append(geom.getCentroid().getX()).append("_").append(geom.getCentroid().getY()).toString()
                .replace(".", "");
    }

    /**
     * Enum for FOI attributes.
     * 
     * @author Nikolai Bock
     *
     */
    private enum FOIAttribute {
        ID, NAME
    }

    /**
     * Create the FOI object out of the expression given.
     * 
     * @param root
     *            the content object
     * @param expression
     *            the expression used for extraction
     * @param geom
     *            the geometry
     * @param attribute
     *            the attribute should be filled
     * @return the result / attribute
     */
    private String getFOIfromExpression(Object root, String expression, Geometry geom, FOIAttribute attribute) {
        try {
            String foiAttributeString = getEvaluator().evaluate(root, expression, String.class);
            if (foiAttributeString == null || foiAttributeString.isEmpty()) {
                throw new FoiAttributeNotInExpressionException();
            } else {
                return foiAttributeString;
            }
        } catch (FoiAttributeNotInExpressionException e) {
            LOG.info("FOI not in Expression. Create FOI out of attributes");
            LOG.debug("Create FOI from attributes. Not in Expression: ", e);
            return createFOIAttribute(root, null, geom, attribute);
        }
    }

    /**
     * Create Geometry object from GeoJSON.
     * 
     * @param json
     *            the GeoJSON string
     * @return the geometry object
     */
    private Geometry getGeomFromJSON(String json) {
        try {
            LOG.debug("JSON-STRING: " + json);
            if (json.contains("Feature")) {
                return (Geometry) new FeatureJSON().readFeature(json).getDefaultGeometry();
            } else {
                return new GeometryJSON().read(json);
            }
        } catch (IOException e) {
            LOG.error("Kein JSON", e);
            return null;
        }
    }

    /**
     * Create geometry from a Map with coordinates inside.
     * 
     * @param map
     *            of coordinates
     * @return the geometry
     */
    private Geometry getGeomFromMapCoords(Map<?, ?> map) {
        if (map.containsKey("geometry")) {
            return getMessageGeometry((map).get("geometry"));
        } else if ((map).containsKey("x")) {
            @SuppressWarnings({ "unchecked", "rawtypes" })
            int srid = (int) ((Map) map).getOrDefault("EPSG", 4326);
            PrecisionModel model = new PrecisionModel(PrecisionModel.FLOATING);
            GeometryFactory geometryFactory = new GeometryFactory(model, srid);
            double x = Double.valueOf(map.get("x").toString());
            double y = Double.valueOf(map.get("y").toString());
            Coordinate coord = new Coordinate(x, y);
            return getMessageGeometry(geometryFactory.createPoint(coord));
        }
        return null;
    }
}
