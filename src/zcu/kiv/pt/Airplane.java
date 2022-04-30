package zcu.kiv.pt;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/******************************************************************************
 * Instances of class AirPlane are representing airplanes which can fly to Point2D
 * destination, load horses and unload horses
 *
 *
 * @author Krystof Saml
 * @version 1.00.0000
 */

public class Airplane implements Comparable<Airplane>, IPrintable {
    //== VARIABLE CLASS ATTRIBUTES =============================================
    /** total number of existing airplanes */
    private static int numberOfInstances = 0;

    //== CONSTANT INSTANCE ATTRIBUTES ==========================================
    /** list of loaded horses */
    private final List<Horse> loadedHorses;
    /** id of airplane */
    public final int number;
    /** maximal weight with which airplane can fly */
    public final int maxLoad;
    /** speed of airplane */
    private final double speed;

    //== VARIABLE INSTANCE ATTRIBUTES ==========================================
    /** position of the airplane */
    public Point2D position;
    /** how much weight is the plane carrying now */
    private int actualLoad;
    /** time on the plane */
    public long timer;

    /** reference to statistisc of this plane */
    public AirplaneStats airplaneStats;

    //==========================================================================
    //== CONSTRUCTORS AND FACTORY METHODS ======================================

    /**
     * Constructor of the airplane
     * @throws IllegalArgumentException when maxload smaller than or equal to 0
     * or when speed is smaller than 0
     * @param xCoordinate coordinate of x
     * @param yCoordinate coordinate of y
     * @param maxLoad maximal load, must be greater than 0
     * @param speed speed of an airplane, should be greater than 0, but it is not
     */
    public Airplane(double xCoordinate, double yCoordinate, int maxLoad, double speed) {
        if (maxLoad <= 0 || speed < 0) {
            throw new IllegalArgumentException();
        }
        this.position = new Point2D.Double(xCoordinate, yCoordinate);
        this.maxLoad = maxLoad;
        this.speed = speed;
        this.actualLoad = 0;
        this.loadedHorses = new ArrayList<>();
        this.number = numberOfInstances++;
        this.timer = 0L;
    }

    //==========================================================================
    //== PUBLIC METHODS OF INSTANCES ===========================================
    /**
     * returns free weight in the airplane
     * @return free weight in the airplane
     */
    public int getFreeWeight() {
        return maxLoad - actualLoad;
    }

    public double getSpeed() {
        return this.speed;
    }

    /**
     * checks if it is possible to load a horse or not
     * @param horse horse to load
     * @return true/false can/can't load the horse
     */
    public boolean canLoadHorse(Horse horse) {
        return actualLoad + horse.weight <= maxLoad;
    }

    /**
     * Loads the horse if it is possible and returns time to load the horse
     * @throws RuntimeException when cannot load the horse
     * @param horse horse to load
     */
    public void loadHorse(Horse horse) {
        if (canLoadHorse(horse)) {
            actualLoad += horse.weight;
            loadedHorses.add(horse);
            if (airplaneStats != null) {
                horse.horseStats.loadTime = this.timer;
                horse.horseStats.pathOfFlight.add(horse.position);
                airplaneStats.loadingTime += horse.timeToLoad;
                airplaneStats.horses.add(horse.number);
            }
            timer += horse.timeToLoad;
        } else {
            throw new RuntimeException("Cannot load horse, it is too heavy");
        }
    }

    /**
     * Unloads all horses on the board of an airplane
     */
    public void unloadAllHorses() {
        int unloadTime = 0;
        while (!loadedHorses.isEmpty()) {
            Horse h = loadedHorses.remove(0);
            if (airplaneStats != null) {
                h.horseStats.unloadTime = this.timer;
            }
            unloadTime += h.timeToLoad;
        }
        actualLoad = 0;
        if (airplaneStats != null) {
            airplaneStats.loadingTime += unloadTime;
        }
        timer += unloadTime;
    }

    /**
     * flies with the plane to the point given as a parameter
     * @throws NullPointerException when point param is null
     * @param point destination to fly, cannot be null
     */
    public void flyToDestination(Point2D point) {
        if (point == null) {
            throw new NullPointerException();
        }
        double distance = this.position.distance(point);
        this.position = point;
        final long time = Math.round(distance / speed);
        if (airplaneStats != null) {
            airplaneStats.flightTime += time;
            for (Horse h: loadedHorses) {
                h.horseStats.pathOfFlight.add(point);
            }
            airplaneStats.flightPath.add(point);
        }
        timer += time;
    }

    /**
     * CompareTo method using speed of the airplanes
     * @param o second airplane
     * @return comparison value
     */
    @Override
    public int compareTo(Airplane o) {
        return Double.compare(this.speed, o.speed);
    }

    /**
     * method prints out info about this instance
     */
    @Override
    public void printInfo() {
        System.out.printf("Airplane number: %d, position: [%.2f;%.2f], maximal load: %d and speed %.2f\n",
                number, position.getX(), position.getY(), maxLoad, speed);
    }
}
