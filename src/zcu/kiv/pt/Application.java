package zcu.kiv.pt;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/******************************************************************************
 * Application is static class which runs the whole program
 *
 * @author Krystof Saml
 * @version 1.00.0000
 */

public class Application {
    //== CONSTANT CLASS ATTRIBUTES =============================================
    /** emoji string */
    private static final String EMOJI = ":-)";

    //== VARIABLE CLASS ATTRIBUTES =============================================
    /** all airplanes */
    private static List<Airplane> airPlanes;
    /** coordinates of paris saves as Point2D */
    private static Point2D paris;
    /** all horses */
    private static List<Horse> horses;
    /** Horsebundle instance */
    private static HorseBundle bundle;
    /** maybe not necessary point with Minimal coordinates */
    private static Point2D.Double minPoint;
    /** graph of our horses and airplanes */
    private static Graph graph;
    /** maybe not necessary clock */
    private static long timer = 0L;

    //==========================================================================
    //== CONSTRUCTORS AND FACTORY METHODS ======================================
    /** static class */
    private Application() {}
    //==========================================================================


    //== PUBLIC CLASS METHODS ==================================================
    /**
     * method which parses the data, creates all needed structures and flies with the planes
     */
    public static void run() {
        Statistics statistics = null;
        parse(Inputs.inputPath());
        Inputs.addingNewItems(horses, airPlanes, paris);
        Inputs.deletingOldItems(horses, airPlanes);
        if(Inputs.inputStatistics()) {
            statistics = new Statistics(horses, airPlanes);
        }
        initialize();
        makeItHappen();
        if (statistics != null) {
            statistics.endTime = timer;
            statistics.writeToFile("statistics.txt");
        }
    }


    //== PRIVATE CLASS METHODS =================================================
    /**
     * This method parses data from file and loads them into memory
     */
    private static void parse(Path path) {
        DLinkedList list = new DLinkedList();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path.toFile())))) {
            String line;
            while ((line = br.readLine()) != null) {
                int i = line.indexOf(EMOJI);
                if (line.length() == 0 || i == 0) {
                    continue;
                } else if (i != -1) {
                    line = line.substring(0, i);
                }

                String[] array = line.split("\\s+");
                for (String s: array) {
                    list.add(Double.parseDouble(s));
                }
            }
        } catch (IOException e) {
            System.out.println("Fatal error occurred while loading data, terminating");
            System.exit(1);
        }
        paris = new Point2D.Double(list.remove(), list.remove());
        int size = (int) list.remove();
        horses = new ArrayList<>();
        airPlanes = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            double x = list.remove();
            double y = list.remove();

            horses.add(new Horse(x, y, (int) list.remove(), (int)list.remove(), paris.distance(x, y)));
        }

        size = (int) list.remove();
        for (int i = 0; i < size; i++) {
            airPlanes.add(new Airplane(list.remove(), list.remove(), (int) list.remove(), list.remove()));
        }
    }

    /**
     * prepares all needed structures to run the program
     */
    private static void initialize() {
        horses.sort(null);
        for (int i = 0; i < airPlanes.size(); i++) {
            if (airPlanes.get(i).getSpeed() <= 0) {
                System.out.printf("Airplane number %d was removed due to broken engine (speed was 0  or less)\n"
                        ,airPlanes.remove(i).number);
            }
        }
        airPlanes.sort(Collections.reverseOrder());

        double minX = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = -Double.MAX_VALUE;

        for (Horse horse : horses) {
            double x = horse.position.getX();
            double y = horse.position.getY();
            minX = Double.min(minX, x);
            maxX = Double.max(maxX, x);
            minY = Double.min(minY, y);
            maxY = Double.max(maxY, y);
        }

        for (Airplane airplane : airPlanes) {
            double x = airplane.position.getX();
            double y = airplane.position.getY();
            minX = Double.min(minX, x);
            maxX = Double.max(maxX, x);
            minY = Double.min(minY, y);
            maxY = Double.max(maxY, y);
        }

        minPoint = new Point2D.Double(minX, minY);
        Point2D maxPoint = new Point2D.Double(maxX, maxY);

        double[] dimensions = Computing.computeDimensions(minPoint, maxPoint);
        bundle = new HorseBundle(horses.toArray(new Horse[0]), dimensions, minPoint);
        graph = new Graph(bundle);
    }

    /**
     * flies with the airplanes and moves oll of the horses to Paris, prints messages
     * about airplanes schedule
     */
    private static void makeItHappen() {
        //first round
        for (Airplane airPlane : airPlanes) {
            firstFlight(airPlane);
            flyToParis(airPlane);
        }

        while (bundle.getNumberOfHorses() > 0) {
            for (Airplane airPlane: airPlanes) {
                flyIfYouCan(airPlane);
                flyToParis(airPlane);
            }
        }
        System.out.printf("Vylozeno v: %d", timer);
    }

    /**
     * flies with the chosen airplane from its initial position
     * @param airPlane which should fly
     */
    private static void firstFlight(Airplane airPlane) {
        System.out.printf("Cas: %d, letoun: %d, Start z mista: %d, %d\n", airPlane.timer, airPlane.number,
                (int) airPlane.position.getX(), (int) airPlane.position.getY());
        if (bundle.getNumberOfHorses() > 0 && airPlane.getFreeWeight() > 0) {

            int index = graph.getIndex((int) (airPlane.position.getX() - minPoint.getX()),
                    (int) (airPlane.position.getY() - minPoint.getY()), airPlane.getFreeWeight());

            if (index != -1) {
                Horse h = bundle.removeHorse(index);
                airPlane.flyToDestination(h.position);
                System.out.printf("Cas: %d, Letoun: %d, Naklad kone: %d, Odlet v: %d, ", airPlane.timer, airPlane.number,
                        h.number, airPlane.timer + h.timeToLoad);

                airPlane.loadHorse(h);
            }
        }
        flyIfYouCan(airPlane);
    }

    /**
     * flies with chosen plane to Paris, unloads all horses there
     * @param airPlane which should fly
     */
    private static void flyToParis(Airplane airPlane) {
        System.out.println("Let do Francie");
        airPlane.flyToDestination(paris);
        System.out.printf("Cas: %d, Letoun: %d, Pristani ve Francii, ", airPlane.timer, airPlane.number);
        airPlane.unloadAllHorses();
        timer = Math.max(timer, airPlane.timer);
        if (bundle.getNumberOfHorses() != 0) {
            System.out.printf("Odlet v: %d, ", airPlane.timer);
        }
    }

    /**
     * method flies with the airplane to the closest horses until the plane is full
     * @param airPlane airplane which should fly
     */
    private static void flyIfYouCan(Airplane airPlane) {
        while (bundle.getNumberOfHorses() > 0 && airPlane.getFreeWeight() > 0) {

            int index = graph.getIndex((int) (airPlane.position.getX() - minPoint.getX()),
                    (int) (airPlane.position.getY() - minPoint.getY()), airPlane.getFreeWeight());

            if (index == -1) {
                break;
            }

            Horse h = bundle.removeHorse(index);
            System.out.printf("Let ke koni %d\n", h.number);
            airPlane.flyToDestination(h.position);
            System.out.printf("Cas: %d, Letoun: %d, Naklad kone: %d, Odlet v: %d, ", airPlane.timer, airPlane.number,
                    h.number, airPlane.timer + h.timeToLoad);

            airPlane.loadHorse(h);

        }
    }
}
