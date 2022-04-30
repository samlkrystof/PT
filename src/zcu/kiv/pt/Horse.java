package zcu.kiv.pt;

import java.awt.geom.Point2D;

/******************************************************************************
 * Instances of class Horse are representing horses with position, weight and
 * time to load
 *
 *
 * @author Krystof Saml
 * @version 1.00.0000
 */

public class Horse implements Comparable<Horse>, IPrintable {
    //== VARIABLE CLASS ATTRIBUTES =============================================
    /** total number of horses */
    private static int numberOfInstances = 0;

    //== CONSTANT INSTANCE ATTRIBUTES ==========================================
    /** id of this horse */
    public final int number;
    /** position where the horse is */
    public final Point2D position;
    /** weight of the horse */
    public final int weight;
    /** how much time takes it to load and unload horse */
    public final int timeToLoad;
    /** distance to Paris */
    public final double distanceToParis;

    //== VARIABLE INSTANCE ATTRIBUTES ==========================================
    /** reference to statistics for this instance */
    public HorseStats horseStats;

    //==========================================================================
    //== CONSTRUCTORS AND FACTORY METHODS ======================================

    /**
     * simple constructor to create new instance of horse
     * @param xCoordinate x coordinate of the horse
     * @param yCoordinate y coordinate of the horse
     * @param weight weight of the horse
     * @param timeToLoad time which it takes to load this horse into airplane
     * @param distanceToParis cartesian distance to Paris
     */
    public Horse(double xCoordinate, double yCoordinate, int weight, int timeToLoad, double distanceToParis) {
        this.distanceToParis = distanceToParis;
        position = new Point2D.Double(xCoordinate, yCoordinate);
        this.weight = weight;
        this.timeToLoad = timeToLoad;
        this.number = numberOfInstances++;
    }
    //==========================================================================
    //== PUBLIC METHODS OF INSTANCES ===========================================
    /**
     * compareTo using distance to Paris
     * @param o the horse to compare
     * @return compareTo value
     */
    @Override
    public int compareTo(Horse o) {
        return  (Double.compare(this.distanceToParis, o.distanceToParis));
    }

    /**
     * method prints out info about this instance
     */
    @Override
    public void printInfo() {
        System.out.printf("Horse number: %d, position: [%.2f;%.2f], weight: %d and time to load: %d\n"
        , number, position.getX(), position.getY(), weight, timeToLoad);
    }
}
