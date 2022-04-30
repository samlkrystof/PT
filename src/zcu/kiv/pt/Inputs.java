package zcu.kiv.pt;

import java.awt.geom.Point2D;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

/******************************************************************************
 * Class inputs works with all of the inputs in the application, it is not just
 * about the inputs but also working with data which were input
 *
 *
 * @author Krystof Saml
 * @version 1.00.0000
 */

public class Inputs {
    //== CONSTANT CLASS ATTRIBUTES =============================================
    /** functional interfaces to test the numbers, first is testing positive integer,
     * second is testing double third is testing positive double */
    private static final INumber[] iNumbers = new INumber[]{
            input -> {
                try {
                    int i = Integer.parseInt(input);
                    return i > 0;
                } catch (NumberFormatException e) {
                    return false;
                }
            },
            input -> {
                try {
                    Double.parseDouble(input);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            },
            input -> {
                try {
                    double d = Double.parseDouble(input);
                    return d > 0;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
    };
    /** index of functional interface */
    private static final int POSITIVE_INTEGER = 0;
    /** index of functional interface */
    private static final int NUMBER = 1;
    /** index of functional interface */
    private static final int POSITIVE_NUMBER = 2;

    /** scanner to read standard input from the console */
    private static final Scanner scanner = new Scanner(System.in);


    //==========================================================================
    //== CONSTRUCTORS AND FACTORY METHODS ======================================
    /** private constructor, because of static class */
    private Inputs(){}
    //==========================================================================

    //== PUBLIC CLASS METHODS ==================================================

    /**
     * asks if you user wants to generate statistics of flight
     * @return true/false if user wants/doesn't want to generate statistics
     */
    public static boolean inputStatistics() {
        return needToDoSomething("Do you want to generate statistics?");
    }
    /**
     * asks for input path to the file until valid path is input
     * @return Path to the data file
     */
    public static Path inputPath() {
        Path path;
        System.out.print("Enter a path to a file to load: ");
        String input = scanner.nextLine();
        path = validatePath(input);
        while (path == null) {
            System.out.print("The path was invalid, try again: ");
            input = scanner.nextLine();
            path = validatePath(input);
        }

        return path;
    }

    /**
     * method solves adding new horses and airplanes
     * @param horses list where new horses will be added
     * @param airplanes list where new airplanes will be added
     * @param paris end point of all the flights
     */
    public static void addingNewItems(List<Horse> horses, List<Airplane> airplanes, Point2D paris) {
        if (needToDoSomething("Do you want to add any horses?")) {
            addHorses(horses, paris);
        }
        if (needToDoSomething("Do you want to add any airplanes?")) {
            addAirplanes(airplanes);
        }
    }

    /**
     * method solves deleting horses and airplanes
     * @param horses list where new horses will be added
     * @param airplanes list where new airplanes will be added
     */
    public static void deletingOldItems(List<Horse> horses, List<Airplane> airplanes) {
        if (needToDoSomething("Do you want to delete any horses?")) {
            deleteThings(horses, "horses");
        }
        if (needToDoSomething("Do you want to delete any airplanes?")) {
            deleteThings(airplanes, "airplanes");
        }
    }


    //== PRIVATE CLASS METHODS =================================================
    /**
     * method asks if you want to do something, until y/n is input
     * @param question question which will be shown in the dialog
     * @return true/false if you want/don't want to do an action with thing
     */
    private static boolean needToDoSomething(String question) {
        System.out.printf("%s (Y/N) ", question);
        String input = scanner.next();
        while (true) {
            if (input.equalsIgnoreCase("Y")) {
                return true;
            } else if (input.equalsIgnoreCase("N")) {
                return false;
            }
            System.out.printf("%s (type Y for yes or N for no) ", question);
            input = scanner.next();
        }
    }

    /**
     * method asks how many things from the list should be removed until
     * valid answer is input, then it deletes selected things from the list
     * @param list list from which the things will be removed
     * @param thing which will be asked for in the dialogues
     */
    private static void deleteThings(List<? extends IPrintable> list, String thing) {
        String inputMessage = String.format("How many %s do you want to delete? ", thing);
        String errorMessage = String.format("How many %s do you want to delete? (type in a positive integer) ", thing);
        int number = (int) getNumber(inputMessage, errorMessage, POSITIVE_INTEGER);
        while (number >= list.size()) {
            System.out.printf("There are only %d %s, you cannot delete all of them\n", list.size(), thing);
            number = (int) getNumber(inputMessage, errorMessage, POSITIVE_INTEGER);
        }

        String singular = thing.substring(0, thing.length() - 1);
        inputMessage = String.format("Which %s do you want to delete? ", singular);
        errorMessage = String.format("Which %s do you want to delete? (type in a positive integer) ", singular);
        int secondNumber;
        for (int i = 0; i < number; i++) {
            secondNumber = (int) getNumber(inputMessage, errorMessage, POSITIVE_INTEGER);
            while (secondNumber >= list.size()) {
                System.out.printf("There are only %d %s\n", list.size(), thing);
                secondNumber = (int) getNumber(inputMessage, errorMessage, POSITIVE_INTEGER);
            }
            list.get(secondNumber).printInfo();
            if (needToDoSomething(String.format("Do you really want to remove this %s?", singular))) {
                list.remove(secondNumber);
            } else {
                i--;
            }
        }
    }

    /**
     * method asks how many horses do you want to add until positive integer is input
     * @param horses list where new horses will be added
     * @param paris end point of all flights
     */
    private static void addHorses(List<Horse> horses, Point2D paris) {
        String inputMessage = "How many horses do you want to add? ";
        String errorMessage = "How many horses do you want to add? (Type a positive integer) ";
        int i = (int) getNumber(inputMessage, errorMessage, POSITIVE_INTEGER);
        while (i > 0) {
            horses.add(createHorse(paris));
            i--;
        }
    }

    /**
     * method asks how many airplanes do you want to add until positive integer is input
     * @param airplanes list where new horses will be added
     */
    private static void addAirplanes(List<Airplane> airplanes) {
        final String inputMessage = "How many airplanes do you want to add? ";
        final String errorMessage = "How many airplanes do you want to add? (Type a positive integer) ";
        int i = (int) getNumber(inputMessage,
                errorMessage, POSITIVE_INTEGER);
        while (i > 0) {
            airplanes.add(createAirplane());
            i--;
        }
    }

    /**
     * asks for all needed parameters until valid parameters are input, then creates
     * new horse
     * @param paris end point of all the flights
     * @return new instance of horse
     */
    private static Horse createHorse(Point2D paris) {

        String inputXMessage = "Enter x coordinate of the horse: ";
        String errorXMessage = "X coordinate must be number: ";
        double xCoord = getNumber(inputXMessage, errorXMessage, NUMBER);

        String inputYMessage = "Enter y coordinate of the horse: ";
        String errorYMessage = "Y coordinate must be number: ";
        double yCoord = getNumber(inputYMessage, errorYMessage, NUMBER);

        String inputWMessage = "Enter weight of the horse: ";
        String errorWMessage = "Weight must be positive integer: ";
        int weight = (int) getNumber(inputWMessage, errorWMessage, POSITIVE_INTEGER);

        String inputTMessage = "Enter time to load the horse: ";
        String errorTMessage = "Time to load must be positive integer: ";
        int timeToLoad = (int) getNumber(inputTMessage, errorTMessage, POSITIVE_INTEGER);

        return new Horse(xCoord, yCoord, weight, timeToLoad, paris.distance(xCoord, yCoord));
    }


    /**
     * asks for all needed parameters until valid parameters are input, then creates
     * new airplane
     * @return new instance of airplane
     */
    private static Airplane createAirplane() {
        String inputXMessage = "Enter x coordinate of the airplane: ";
        String errorXMessage = "X coordinate must be number: ";
        double xCoord = getNumber(inputXMessage, errorXMessage, NUMBER);


        String inputYMessage = "Enter y coordinate of the airplane: ";
        String errorYMessage = "Y coordinate must be number: ";
        double yCoord = getNumber(inputYMessage, errorYMessage, NUMBER);


        String inputWMessage = "Enter maximal weight to load of the airplane: ";
        String errorWMessage = "Weight must be positive integer: ";
        int maxWeight = (int) getNumber(inputWMessage, errorWMessage, POSITIVE_INTEGER);


        String inputSMessage = "Enter speed of the airplane: ";
        String errorSMessage = "Speed must be positive number: ";
        double speed = getNumber(inputSMessage, errorSMessage, POSITIVE_NUMBER);

        return new Airplane(xCoord, yCoord, maxWeight, speed);
    }

    /**
     * validates input path name
     * @param fileName filename to be validated
     * @return return path of null if there is a problem
     */
    private static Path validatePath(String fileName) {
        if (fileName == null || fileName.length() == 0) {
            return null;
        }
        Path path;
        try {
            path = Paths.get(fileName);
        } catch (InvalidPathException e) {
            return null;
        }
        if (path.toFile().canRead()) {
            return path;
        }
        return null;
    }

    /**
     *
     * @param message message printed before first input
     * @param errorMessage error message printed when invalid input is input
     * @param numberType type of number which will be checked
     * @return input double value
     */
    private static double getNumber(String message, String errorMessage, int numberType) {
        String input;

        System.out.print(message);
        input = scanner.next();
        while (!iNumbers[numberType].test(input)) {
            System.out.print(errorMessage);
            input = scanner.next();
        }
        return Double.parseDouble(input);
    }
}
