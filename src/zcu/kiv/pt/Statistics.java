package zcu.kiv.pt;

import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/******************************************************************************
 * Instances of class Statistics are dealing with statistics of the flight
 *
 * @author Krystof Saml
 * @version 1.00.0000
 */

public class Statistics {
    //== VARIABLE INSTANCE ATTRIBUTES ==========================================
    /** total time of loading and unloading horses */
    private long loadingTime;
    /** array with statistics of all the horses */
    HorseStats[] horseStats;
    /** array with statistics of all the airplanes */
    AirplaneStats[] airplaneStats;
    /** time when all horses are transported to Paris */
    public long endTime;
    /** total time of all flights */
    private long flightTime;

    //==========================================================================
    //== CONSTRUCTORS AND FACTORY METHODS ======================================

    /**
     * simple constructor
     * @param horses all horses in the simulation
     * @param airplanes all airplanes in the simulation
     */
    public Statistics(List<Horse> horses, List<Airplane> airplanes) {
        horseStats = new HorseStats[horses.size()];
        for (int i = 0; i < horseStats.length; i++) {
            horseStats[i] = new HorseStats();
            horses.get(i).horseStats = horseStats[i];
        }
        airplaneStats = new AirplaneStats[airplanes.size()];
        for (int i = 0; i < airplaneStats.length; i++) {
            airplaneStats[i] = new AirplaneStats();
            airplanes.get(i).airplaneStats = airplaneStats[i];
        }
    }
    //==========================================================================
    //== PUBLIC METHODS OF INSTANCES ===========================================
    /**
     * Creates and writes statistics to file with input name
     * @param filename name of file with statistics
     */
    public void writeToFile(String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            bw.write("Statistics of airplanes");
            bw.newLine();
            for (int i = 0; i < airplaneStats.length; i++) {
                bw.write("Airplane number " + i + "\npoints: ");
                for(Point2D point : airplaneStats[i].flightPath) {
                    bw.write(String.format(Locale.US,"[%.3f;%.3f] ", point.getX(), point.getY()));
                }
                bw.newLine();
                bw.write("Horses: ");
                for (Integer integer : airplaneStats[i].horses) {
                    bw.write(integer + " ");
                }
                bw.newLine();
                bw.write(String.format(Locale.US, "Flight time: %d, Waiting time: %d",
                        airplaneStats[i].flightTime, airplaneStats[i].loadingTime));
                loadingTime += airplaneStats[i].loadingTime;
                flightTime += airplaneStats[i].flightTime;
                bw.newLine();
            }
            bw.write("Statistics of horses");
            bw.newLine();
            for (int i = 0; i < horseStats.length; i++) {
                bw.write("Horse number " + i + "\npoints: ");
                for (Point2D point : horseStats[i].pathOfFlight) {
                    bw.write(String.format(Locale.US,"[%.3f;%.3f] ", point.getX(), point.getY()));
                }
                bw.newLine();
                bw.write("Travel time: " + (horseStats[i].unloadTime - horseStats[i].loadTime) + " ");
                bw.write("Waiting time: " + (endTime - horseStats[i].unloadTime));
                bw.newLine();
            }
            bw.write(String.format(Locale.US, "Total time: %d, Waiting time: %d, Flight time %d"
            , flightTime, loadingTime, flightTime));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
/** instances are representing statistics of horses */
class HorseStats {
    /** path which was the horse traveling */
    List<Point2D> pathOfFlight;
    /** time when the horse was loaded */
    long loadTime;
    /** time when the horse was unloaded */
    long unloadTime;

    /** simple constructor */
    public HorseStats() {
        pathOfFlight = new ArrayList<>();
    }
}

/** instances are representing statistics of airplanes */
class AirplaneStats {
    /** total time of flight of this this airplane */
    long flightTime;
    /** total time of waiting to load and unload the horses */
    long loadingTime;
    /** all horses in the airplane */
    List<Integer> horses;

    /** flight route of the airplane */
    List<Point2D> flightPath;

    /** simple constructor */
    public AirplaneStats() {
        flightTime = 0;
        loadingTime = 0;
        horses = new ArrayList<>();
        flightPath = new ArrayList<>();
    }
}
