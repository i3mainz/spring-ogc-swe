package de.i3mainz.springframework.integration.swe.sos.transformation.util;

import de.i3mainz.springframework.integration.expressions.ExpressionEvaluator;

/**
 * This class provides an evaluator using inside this module.
 * 
 * @author Nikolai Bock
 *
 */
public class SOSExpressionEvaluator extends ExpressionEvaluator {

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.i3mainz.springframework.integration.expressions.ExpressionEvaluator#
     * evaluate(java.lang.Object, java.lang.String)
     */
    @Override
    public Object evaluate(Object root, String expression) {
        try {
            return this.evaluateExpression(expression, root);
        } catch (Exception e) {
            logger.debug("Konnte keinen Inhalt unter der Expression finden", e);
            return null;
        }
    }

}
