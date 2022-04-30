package zcu.kiv.pt;

import java.util.*;

/******************************************************************************
 * Instances of class Graph are representing undirected graph (represented by
 * link structure) with rectangle shape where each of the nodes has edges to
 * the eight closest neighbour nodes, it's comparable to sudoku, where each of
 * the cells has eight cells around itself
 *
 * @author Krystof Saml
 * @version 1.2.0
 */

public class Graph {

    //== CONSTANT CLASS ATTRIBUTES =============================================
    /** length of diagonal in square with length 1 */
    private static final double DIAGONAL = Math.sqrt(2);

    //== CONSTANT INSTANCE ATTRIBUTES ==========================================
    /** width of the graph */
    private final int width;
    /** height of the graph */
    private final int height;
    /** instance of horseBundle */
    private final HorseBundle bundle;
    /** edges of the graph */
    private final List<ArrayList<Edge>> edges;
    //==========================================================================
    //== CONSTRUCTORS AND FACTORY METHODS ======================================

    /**
     *
     * @param horseBundle bundle containing horses
     */
    public Graph(HorseBundle horseBundle) {
        this.width = horseBundle.width;
        this.height = horseBundle.height;
        this.bundle = horseBundle;

        this.edges = new ArrayList<>();
        for (int i = 0; i < width * height; i++) {
            edges.add(new ArrayList<>());
        }

        initializeGraph();
        for (ArrayList<Edge> list: edges) {
            list.sort(Comparator.comparingDouble(e -> e.weight));
        }
    }
    //==========================================================================
    //== PUBLIC METHODS OF INSTANCES ===========================================
    /**
     * finds index of the node with horse which is closest to
     * coordinates given as parameter with use of breath first
     * search
     * @param x x coordinate from which we are searching
     * @param y y coordinate from which we are searching
     * @param freeWeight maximal weight of the horse which will
     *                   be considered
     * @return index of closest horse or -1 if that horse does not exist
     */
    public int getIndex(int x, int y, int freeWeight) {
        return BFS(recomputeIndex(x, y), freeWeight);
    }


    //== PRIVATE METHODS OF INSTANCES ==========================================

    /**
     * this method initializes graph edges to the state in which
     * every inner node of the graph has eight edges to it's closest neighbours
     * nodes in corners have 3 edges, the rest of the outer nodes has five edges
     */
    private void initializeGraph() {

        //first column without the last point
        for (int y = 0; y < height - 1; y++) {
            //adds edge to the node on the right
            addEdges(0, y, 1, y);
            //adds edge to the node on the right diagonal down
            addEdges(0, y, 1, y + 1);
            //adds edge to the node under this one
            addEdges(0, y, 0, y + 1);
        }

        //all rows and all columns except for first column, last column and last row
        for (int y = 0; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                //adds edge to the node on the right
                addEdges(x, y, x + 1, y);
                //adds edge to the node on the right diagonal down
                addEdges(x, y, x + 1, y + 1);
                //adds edge to the node under this one
                addEdges(x, y, x, y + 1);
                //adds edge to the node on the left diagonal down
                addEdges(x, y, x - 1, y + 1);
            }
        }

        //last row
        for (int x = 0; x < width - 1; x++) {
            //adds edge to the node on the right
            addEdges(x, height - 1, x + 1, height - 1);
        }

        //last column
        for (int y = 0; y < height - 1; y++) {
            //adds edge to the node under this one
            addEdges(width - 1, y, width - 1, y + 1);
            //adds edge to the node on the left diagonal down
            addEdges(width - 1, y, width - 2, y + 1);
        }

    }

    /**
     * adds edges from first to second node and from second
     * to first node (undirected graph) with normal or diagonal
     * weight
     * @param x1 x coordinate of first node
     * @param y1 y coordinate of first node
     * @param x2 x coordinate of second node
     * @param y2 y coordinate of second node
     */
    private void addEdges(int x1, int y1, int x2, int y2) {
        //is the node vertical, horizontal or diagonal?
        double direction = x1 == x2 || y1 == y2 ? 1 : DIAGONAL;
        int firstIndex = computeIndex(x1, y1);
        int secondIndex = computeIndex(x2, y2);

        Edge first = new Edge(secondIndex, direction);
        Edge second = new Edge(firstIndex, direction);

        edges.get(firstIndex).add(first);
        edges.get(secondIndex).add(second);
    }

    /**
     * computes index in "2D array" this is for internal values
     * @param x x coordinate
     * @param y y coordinate
     * @return index of the node with given coordinates in graph
     */
    private int computeIndex(int x, int y) {
        return y * width + x;
    }

    /**
     * computes index in "2D array" this is for external values
     * @param x x coordinate
     * @param y y coordinate
     * @return index of the node with given coordinates in graph
     */
    private int recomputeIndex(int x, int y) {
        return (int) (y * width * bundle.yMultiplier + x * bundle.xMultiplier);
    }


    /**
     * breath first search algorithm starting from start index
     * tries to find closest free horse
     * @param start index of starting node
     * @param freeWeight maximal weight of horse to be considered
     * @return index of the closest horse matching criteria or -1
     * if there is not such horse
     */
    private int BFS(int start, int freeWeight) {
        int[] mark = new int[edges.size()];
        mark[start] = 1;
        Queue<Integer> queue = new LinkedList<>();
        queue.add(start);
        while (!queue.isEmpty()) {
            int index = queue.remove();
            if (bundle.hasHorseAt(index) && bundle.getHorseWeight(index) <= freeWeight) {
                return index;
            }

            ArrayList<Edge> neighbours = edges.get(index);
            for (Edge neighbour : neighbours) {
                int n = neighbour.to;
                if (mark[n] == 0) {
                    mark[n] = 1;
                    queue.add(n);
                }
            }
            mark[index] = 2;
        }
        return  -1;
    }

    /** class representing edge of the graph */
    private static class Edge {
        /** where the edge goes to */
        public final int to;
        /** weight of the edge */
        public final double weight;

        /** simple constructor */
        private Edge(int to, double weight) {
            this.to = to;
            this.weight = weight;
        }
    }
}
