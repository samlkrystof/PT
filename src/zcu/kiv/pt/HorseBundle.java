package zcu.kiv.pt;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/******************************************************************************
 * Instances of class HorseBundle are connection between nodes of the instance
 * of the Graph, it contains array with the horses and array of all nodes of the
 * Graph containing information if in the node is horse or not
 *
 *
 * @author Krystof Saml
 * @version 1.00.0000
 */

public class HorseBundle {
    //== CONSTANT INSTANCE ATTRIBUTES ==========================================
    /** list of nodes in the graph containing info if in the node is horse or not */
    private final List<List<Horse>> horsesInGraph;
    /** width of the graph */
    public final int width;
    /** height of the graph */
    public final int height;
    //== VARIABLE INSTANCE ATTRIBUTES ==========================================
    /** total number of horses in the graph */
    private int numberOfHorses;

    /** constant which multiplies xCoordinate */
    public final double xMultiplier;

    /** constant which multiplies yCoordinate */
    public final double yMultiplier;

    //==========================================================================
    //== CONSTRUCTORS AND FACTORY METHODS ======================================
    /**
     * constructor which creates everything possible to work with horseBundle
     * @param horses array of all the horses in the simulation
     * @param dimensions array with double elements representing width, height, xMultiplier
     *                   and yMultiplier
     * @param minPoint point with minimal coordinates of the grid graph
     */
    public HorseBundle(Horse[] horses, double[] dimensions, Point2D minPoint) {
        this.width = (int) (dimensions[0]) + 1;
        this.height = (int) (dimensions[1]) + 1;
        this.xMultiplier = dimensions[2];
        this.yMultiplier = dimensions[3];
        this.horsesInGraph = new ArrayList<>(width * height);
        for (int i = 0; i < width * height; i++) {
            horsesInGraph.add(i, new ArrayList<>(1));
        }
        addHorseReferences(horses, minPoint);
        this.numberOfHorses = horses.length;
    }

    //==========================================================================
    //== PUBLIC METHODS OF INSTANCES ===========================================

    /**
     * returns number of horses in the graph
     * @return number of horses
     */
    public int getNumberOfHorses() {
        return numberOfHorses;
    }

    /**
     * returns if graph has horse on specified index
     * @param index index of node in the graph
     * @return true/false if there is/isn't horse on this node
     */
    public boolean hasHorseAt(int index) {
        return !horsesInGraph.get(index).isEmpty();
    }

    /**
     * returns weight of the horse at node specified by index
     * @throws NullPointerException when there is not any horse at specified
     *         index
     * @param index index of the node
     * @return weight of the horse at specified index
     */
    public int getHorseWeight(int index) {
        if (hasHorseAt(index)) {
            return horsesInGraph.get(index).get(0).weight;
        } else {
            throw new NullPointerException("There is no horse on this index");
        }
    }
    //== PRIVATE METHODS OF INSTANCES ==========================================
    /**
     * makes connections between nodes of the graph and the horses
     * @param horses array of horses
     * @param minPoint minimal point of the graph
     */
    private void addHorseReferences(Horse[] horses, Point2D minPoint) {
        for (Horse h : horses) {
            int x = (int) ((h.position.getX() - minPoint.getX()) * xMultiplier);
            int y = (int) ((h.position.getY() - minPoint.getY()) * yMultiplier);
            horsesInGraph.get(y * width + x).add(h);
        }
    }

    /**
     * removes horse and its reference at specified index and
     * return the horse
     * @throws IllegalArgumentException if the index is not valid
     * @throws NullPointerException if there are no more horses
     * @param index index of the node where horse is
     * @return removed horse
     */
    public Horse removeHorse(int index) {
        if (index < 0 || index >= width * height) {
            throw new IllegalArgumentException("Index is invalid");
        }
        if (hasHorses()) {
            numberOfHorses--;
            return horsesInGraph.get(index).remove(0);
        }
        throw new NullPointerException("Don't have any horses");
    }

    /**
     * returns if there are any horses
     * @return true/false if there are/aren't horses
     */
    private boolean hasHorses() {
        return numberOfHorses > 0;
    }



}
