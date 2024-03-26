/*Dinesh Raj Pampati
Student ID- 806659955
I'm giving a pledge of honesty that I did not copy/modify from other resources.
I declare that this code is created by me and is protected under copyright law. No part of this code is modified without my express permission. I reserve all rights to the code.
*/
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

class Edges {
    int source;
    int destination;
    int capacity;
    int flow;

    public Edges(int source, int destination, int capacity, int flow) {
        this.source = source;
        this.destination = destination;
        this.capacity = capacity;
        this.flow = flow;
    }
    
    // Getters and setters
    public int getSource() {
        return source;
    }

    public int getDestination() {
        return destination;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getFlow() {
        return flow;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setFlow(int flow) {
        this.flow = flow;
    }

    // toString method to display edge details
    public String toString() {
        return "( " + source + " ," + destination + " ," + capacity + " ," + flow + " )";
    }
}

class FordFlukerson {

    private static long endTime; // Variable to record end time
    private static long startTime; // Variable to record start time
    static int increment = 1; // Variable to increment the test case number

    public static void printGraph(ArrayList<ArrayList<Edges>> Graph) {
        // System.out.println("Ford-Fulkerson algorithm\n");
        System.out.println("** G"+ increment++ +": |V|=" + Graph.size());
        if(Graph.size() < 11){
            System.out.println("Flow network:");
            printMatrix(Graph, false); // false indicates to print capacities
        }
    }
    // Function to print the matrix
    private static void printMatrix(ArrayList<ArrayList<Edges>> Graph, boolean includeFlow) {
        int vertices = Graph.size();
        System.out.print("       ");
        for (int i = 0; i < vertices; i++) {
            System.out.printf("%-7s", i + ":");
        }
        System.out.println("\n" + "-".repeat(vertices * 7 + 7));

        for (int i = 0; i < vertices; i++) {
            System.out.printf("%-7s", i + ":");
            for (int j = 0; j < vertices; j++) {
                String value = "-";
                for (Edges edge : Graph.get(i)) {
                    if (edge.getDestination() == j) {
                        value = String.valueOf(includeFlow ? edge.getFlow() : edge.getCapacity());
                        break;
                    }
                }
                System.out.printf("%-7s", value);
            }
            System.out.println();
        }
    }



    // from this function i want to find the sortest path from source to destination
    // and return the path in the form of list
    public static List<Integer> bfs(ArrayList<ArrayList<Edges>> Graph, int source, int destination, int[] parent) {
        int vertices = Graph.size();
        boolean[] visited = new boolean[vertices];
        for (int i = 0; i < vertices; i++) {
            visited[i] = false; // Mark all the vertices as not visited
        }
        // start from source
        Queue<Integer> queue = new LinkedList<>();
        queue.add(source);
        visited[source] = true;
        parent[source] = -1;
        // Standard BFS Loop
        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (int i = 0; i < Graph.get(u).size(); i++) {
                int v = Graph.get(u).get(i).getDestination();
                if (visited[v] == false && Graph.get(u).get(i).getCapacity() > 0) {
                    queue.add(v);
                    parent[v] = u;
                    visited[v] = true;
                }
            }
        }
        // If we reached sink in BFS starting from source, then return the list , else
        // return null
        if (visited[destination] == true) {
            List<Integer> path = new ArrayList<>();
            for (int v = destination; v != source; v = parent[v]) {
                path.add(v);
            }
            path.add(source);
            // System.out.println("Path is " + path);
            return path;
        }
        return null;
    }

    // Function to print the maximum flow
    public static void printMaxFlow(ArrayList<ArrayList<Edges>> Graph) {
        System.out.println("Maximum flow:");
        int vertices = Graph.size();
        String[][] flowMatrix = new String[vertices][vertices];

        // Initialize the matrix with "-" to indicate no flow
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                flowMatrix[i][j] = "-";
            }
        }

        // Populate the matrix with flow values as integers
        for (int i = 0; i < vertices; i++) {
            for (Edges edge : Graph.get(i)) {
                int destination = edge.getDestination();
                int flow = edge.getFlow();
                if (flow > 0) {
                    flowMatrix[i][destination] = String.valueOf(flow);
                }
            }
        }

        // Print the header row with colons
        System.out.print("       ");
        for (int i = 0; i < vertices; i++) {
            System.out.printf("%-7s", i + ":");
        }
        System.out.println();

        // Print the separator line starting from the beginning of the line
        for (int i = 0; i < vertices * 7 + 7; i++) {
            System.out.print("-");
        }
        System.out.println();

        // Print the matrix with row headers having colons
        for (int i = 0; i < vertices; i++) {
            System.out.printf("%-7s", i + ":");
            for (int j = 0; j < vertices; j++) {
                System.out.printf("%-7s", flowMatrix[i][j]);
            }
            System.out.println();
        }
    }

    // Function to find the maximum flow
    public static void fordFulkerson(ArrayList<ArrayList<Edges>> Graph, int source, int destination) {
        int vertices = Graph.size();
        int[] parent = new int[vertices];
        int maxFlow = 0;
        List<Integer> path;
        while ((path = bfs(Graph, source, destination, parent)) != null) {
            int pathFlow = Integer.MAX_VALUE;
            for (int v = destination; v != source; v = parent[v]) {
                int u = parent[v];
                for (int i = 0; i < Graph.get(u).size(); i++) {
                    if (Graph.get(u).get(i).getDestination() == v) {
                        pathFlow = Math.min(pathFlow, Graph.get(u).get(i).getCapacity());
                    }
                }
            }
            for (int v = destination; v != source; v = parent[v]) {
                int u = parent[v];
                for (int i = 0; i < Graph.get(u).size(); i++) {
                    if (Graph.get(u).get(i).getDestination() == v) {
                        Graph.get(u).get(i).setCapacity(Graph.get(u).get(i).getCapacity() - pathFlow);
                        Graph.get(u).get(i).setFlow(Graph.get(u).get(i).getFlow() + pathFlow);
                    }
                }
                for (int i = 0; i < Graph.get(v).size(); i++) {
                    if (Graph.get(v).get(i).getDestination() == u) {
                        Graph.get(v).get(i).setCapacity(Graph.get(v).get(i).getCapacity() + pathFlow);
                        Graph.get(v).get(i).setFlow(Graph.get(v).get(i).getFlow() - pathFlow);
                    }
                }
            }
            maxFlow += pathFlow;
            // System.out.println(Graph);
        }
        //time in milliseconds
        endTime =  System.currentTimeMillis();
        if(Graph.size() < 11){
            printMaxFlow(Graph);
        }
        System.out.println("Max Flow ==> " + maxFlow + " (" + (endTime - startTime) + " ms)");
        System.out.println();
    }

    public static void main(String[] args) {
        File myFile = new File(args[0]);
        try (Scanner sc = new Scanner(myFile)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                int vertices = 0;
                ArrayList<ArrayList<Edges>> Graph = null;
                if (line.startsWith("*")) {
                    // System.out.println("Test Case " + increment++);
                    line = line.replaceAll("[^0-9]", " ");
                    line = line.replaceAll("\\s+", " ");
                    String[] parts = line.split(" ");
                    vertices = Integer.parseInt(parts[2]);
                    Graph = new ArrayList<>(vertices); // Initialize Graph here
                    for (int i = 0; i < vertices; i++) {
                        Graph.add(new ArrayList<Edges>());
                    }
                    sc.nextLine();
                    while (!(line = sc.nextLine()).startsWith("-")) {
                        line = line.replaceAll("[^0-9,]", "");
                        parts = line.split(",");
                        int source = Integer.parseInt(parts[0]);
                        int destination = Integer.parseInt(parts[1]);
                        int capacity = Integer.parseInt(parts[2]);
                        int flow = 0;
                        Edges edge = new Edges(source, destination, capacity, flow);
                        Graph.get(source).add(edge);
                    }
                    printGraph(Graph);
                    // System.out.println();
                    startTime = System.currentTimeMillis();
                    fordFulkerson(Graph, 0, vertices - 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("\n***by Pampati Dinesh Raj.");
    }
}