package zcu.kiv.pt;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/******************************************************************************
 * Instances of class Generator are generating input data files for this
 * application
 *
 *
 * @author Krystof Saml
 * @version 1.00.0000
 */

public class Generator {
    //== CONSTANT INSTANCE ATTRIBUTES ==========================================
    /** instance of random */
    private final Random random;

    //==========================================================================
    //== CONSTRUCTORS AND FACTORY METHODS ======================================
    /** simple constructor */
    public Generator() {
        this.random = new Random();
    }

    /**
     * constructor with seed for initializing random, useful when
     * we want to try generator with the same numbers generated
     * @param seed parameter given to random
     */
    public Generator(long seed) {
        this.random = new Random(seed);
    }
    //==========================================================================
    //== PUBLIC METHODS OF INSTANCES ===========================================

    /**
     * generates all the data to the specified file
     * @param fileName name of the output file
     * @param x range of x values defined by minimal and maximal value
     * @param y range of y values defined by minimal and maximal value
     * @param horses number of horses
     * @param airplanes number of airplanes
     * @param weight maximal weight of horses
     * @param timeToLoad maximal time to load the horse
     * @param mean mean value of airplane speed
     * @param sDeviation deviation value of airplane speed
     */
    public void generate(String fileName, Range x, Range y, int horses,
                         int airplanes, int weight, int timeToLoad, double mean,
                         double sDeviation) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))){
            bw.write(generateNumber(x.min, x.max) + " " + generateNumber(y.min, y.max));
            bw.newLine();
            bw.write("" + horses);
            bw.newLine();
            for (int i = 0; i < horses; i++) {
                bw.write(generateNumber(x.min, x.max) + " " + generateNumber(y.min, y.max)
                        + " " + (int) generateNumber(1, weight) + " " + (int) generateNumber(1, timeToLoad)
                        );
                bw.newLine();
            }
            bw.write("" + airplanes);
            bw.newLine();
            for (int i = 0; i < airplanes; i++) {
                bw.write(generateNumber(x.min, x.max) + " " + generateNumber(y.min, y.max)
                        + " " + (int) generateNumber(1, weight + 1) + " " +generateSpeed(mean, sDeviation));
                bw.newLine();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //== PRIVATE METHODS OF INSTANCES ==========================================
    /**
     * generates speed
     * @param mean mean value of speed
     * @param sDeviation deviation value of speed
     * @return speed of the airplane
     */
    private double generateSpeed(double mean, double sDeviation) {
        double value = mean + random.nextGaussian() * sDeviation;
        while (value > mean + sDeviation || value < mean - sDeviation) {
            value = mean + random.nextGaussian() * sDeviation;
        }
        return value;
    }

    /**
     * generates number between minimal and maximal value
     * @param min minimal possible value
     * @param max maximal possible value
     * @return generated number
     */
    private double generateNumber(double min, double max) {
        return  min + random.nextDouble() * (max - min);
    }

}

/**
 * class representing range of values
 */
class Range {
    /** minimal value of range */
    public final double min;
    /** maximal value of range */
    public final double max;

    /** simple constructor */
    Range(double min, double max) {
        this.min = min;
        this.max = max;
    }
}
