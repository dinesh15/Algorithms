import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
public class closestPairTest {
    static Coordinate closestPairPoint1 = null;
    static Coordinate closestPairPoint2 = null;
    static class Coordinate implements Comparable<Coordinate> {
        double x;
        double y;

        public Coordinate(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int compareTo(Coordinate o) {
            int compX = Double.compare(this.x, o.x);
            if (compX != 0)
                return compX;
            return Double.compare(this.y, o.y);
        }

        @Override
        public String toString() {
            return "(" + x + "," + y + ")";
        }
    }
    public static double getDistance(Coordinate c1, Coordinate c2) {
        return Math.hypot(c1.x - c2.x, c1.y - c2.y);
    }
    public static double bruteForce(ArrayList<Coordinate> coordinates) {
        double minDistance = Double.MAX_VALUE;
        for (int i = 0; i < coordinates.size(); ++i) {
            for (int j = i + 1; j < coordinates.size(); ++j) {
                double distance = getDistance(coordinates.get(i), coordinates.get(j));
                if (distance < minDistance) {
                    minDistance = distance;
                    // Update the closest pair of points
                    closestPairPoint1 = coordinates.get(i);
                    closestPairPoint2 = coordinates.get(j);
                }
            }
        }
        return minDistance;
    }
    public static double findClosestPair(ArrayList<Coordinate> xCoordinates, ArrayList<Coordinate> yCoordinates) {
        if (xCoordinates.size() <= 3) {
            return bruteForce(xCoordinates);
        }

        int mid = xCoordinates.size() / 2;
        Coordinate midpoint = xCoordinates.get(mid);

        ArrayList<Coordinate> leftX = new ArrayList<>(xCoordinates.subList(0, mid));
        ArrayList<Coordinate> rightX = new ArrayList<>(xCoordinates.subList(mid, xCoordinates.size()));

        ArrayList<Coordinate> leftY = new ArrayList<>();
        ArrayList<Coordinate> rightY = new ArrayList<>();

        // No need for contains check; we use direct comparison
        for (Coordinate c : yCoordinates) {
            if (c.x <= midpoint.x) {
                leftY.add(c);
            } else {
                rightY.add(c);
            }
        }

        double delta = Math.min(findClosestPair(leftX, leftY), findClosestPair(rightX, rightY));
        ArrayList<Coordinate> strip = new ArrayList<>();

        // Collecting points that are closer than delta to the midpoint line
        for (Coordinate c : yCoordinates) {
            if (Math.abs(midpoint.x - c.x) < delta) {
                strip.add(c);
            }
        }

        return Math.min(delta, stripClosest(strip, delta));
    }
    // Function to find the distance between the closest points of strip of a given
    // size
    private static double stripClosest(ArrayList<Coordinate> strip, double delta) {
        double min = delta;
        for (int i = 0; i < strip.size(); ++i) {
            for (int j = i + 1; j < strip.size() && (strip.get(j).y - strip.get(i).y) < min; ++j) {
                double distance = getDistance(strip.get(i), strip.get(j));
                if (distance < min) {
                    min = distance;
                    // Update the closest pair of points
                    closestPairPoint1 = strip.get(i);
                    closestPairPoint2 = strip.get(j);
                }
            }
        }
        return min;
    }
    public static void main(String[] args) {
        File myFile = new File(args[0]);
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        int n = 1, points = 0;
        try (Scanner sc = new Scanner(myFile)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.startsWith("*")) {
                    // Skipping the line with the asterisk
                } else if (line.startsWith("-")) {
                    // This is the end of the current test case, process it
                    ArrayList<Coordinate> xSorted = new ArrayList<>(coordinates);
                    ArrayList<Coordinate> ySorted = new ArrayList<>(coordinates);
                    Collections.sort(xSorted);
                    Collections.sort(ySorted, (c1, c2) -> Double.compare(c1.y, c2.y));
                    long startTime = System.currentTimeMillis();
                    double closestDistance = findClosestPair(xSorted, ySorted);
                    long endTime = System.currentTimeMillis();
                    System.out.println("Set No " + n + ": " + points + " points");
                    System.out.println("\tClosest pair: " + closestPairPoint1 + "-" + closestPairPoint2);
                    System.out.println(
                            "\tdistance = " + closestDistance + " (" + (endTime - startTime) + " ms)");
                    // System.out.println("Time taken: " + (endTime - startTime) + " ms");
                    coordinates.clear();
                    n++;
                    points = 0;
                } else {
                    // System.out.println(line);
                    line = line.replaceAll("[^0-9.,]", "");
                    String[] parts = line.split(",\\s*");
                    // System.out.println(parts[0] + " " + parts[1]);
                    double x = Double.parseDouble(parts[0]);
                    double y = Double.parseDouble(parts[1]);
                    coordinates.add(new Coordinate(x, y));
                    points++;
                }
                // points = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
