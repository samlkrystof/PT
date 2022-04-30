package zcu.kiv.pt;

import java.awt.geom.Point2D;

/******************************************************************************
 * Computing class is static class which should offer useful methods to calculate
 * some stuff, we will see if this will be true
 *
 * @author Krystof Saml
 * @version 1.00.0000
 */

public class Computing {
    //==========================================================================
    //== CONSTRUCTORS AND FACTORY METHODS ======================================
    /** private constructor */
    private Computing() {}

    //==========================================================================
    //== PUBLIC CLASS METHODS ==================================================
    /**
     * computes dimensions of rectangle given by two points
     * @param minPoint point with minimal coordinates (both x and y) in the graph
     * @param maxPoint point with maximal coordinates (both x and y) in the graph
     * @return double array with four elements first is width, second is height,
     * third is xMultiplier, fourth is yMultiplier
     */
    public static double[] computeDimensions(Point2D minPoint, Point2D maxPoint) {

        double xLength = maxPoint.getX() - minPoint.getX();
        double yLength = maxPoint.getY() - minPoint.getY();
        double multiplierX = 1;
        double multiplierY = 1;
        double[] dimensions = new double[4];
        if (xLength < 50) {
            while (xLength * multiplierX < 50) {
                multiplierX *= 10;
            }
        } else if (xLength * multiplierX > 2500) {
            while (xLength * multiplierX > 2500) {
                multiplierX *= 0.1;
            }
        }

        if (yLength < 50) {
            while (yLength * multiplierY < 50) {
                multiplierY *= 10;
            }
        } else if (yLength > 2500) {
            while (yLength * multiplierY > 2500) {
                multiplierY *= 0.1;
            }
        }
        //width
        dimensions[0] = (Math.abs(xLength) + 1) * multiplierX;
        //height
        dimensions[1] = (Math.abs(yLength) + 1) * multiplierY;
        //xMultiplier
        dimensions[2] = multiplierX;
        //yMultiplier
        dimensions[3] = multiplierY;
        return dimensions;
    }
}
