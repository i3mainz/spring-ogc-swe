package de.i3mainz.springframework.integration.expressions;

import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.integration.util.AbstractExpressionEvaluator;

/**
 * This class offers the functionality of expression evaluation.
 * 
 * @author Nikolai Bock
 *
 */
public class ExpressionEvaluator extends AbstractExpressionEvaluator {

    /**
     * Extract part of an object using expressions. Allows to give the return
     * class type.
     * 
     * @param root
     *            Root object which will be evaluated
     * @param expression
     *            expression which will be used to extract value
     * @param type
     *            classtype of the result
     * @param <T>
     *            describes the returning type parameter
     * 
     * @return extracted value
     */
    @SuppressWarnings("unchecked")
    public <T> T evaluate(Object root, String expression, Class<T> type) {
        try {
            return this.evaluateExpression(expression, root, type);
        } catch (ConversionNotSupportedException e) {
            logger.debug(
                    "Der Inhalt konnte nicht umgewandelt werden. Gebe Object zurück",
                    e);
            return (T) this.evaluate(root, expression);
        } catch (Exception e) {
            logger.debug("Konnte keinen Inhalt unter der Expression finden", e);
            return null;
        }
    }

    /**
     * Extract part of an object using expressions. Returning object type.
     * 
     * @param root
     *            Root object which will be evaluated
     * @param expression
     *            expression which will be used to extract value
     * @return extracted value
     */
    public Object evaluate(Object root, String expression) {
        return evaluateExpression(expression, root);
    }

}
