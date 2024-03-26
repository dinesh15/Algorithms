/*Dinesh Raj Pampati
Student ID- 806659955
I'm giving a pledge of honesty that I did not copy/modify from other resources.
I declare that this code is created by me and is protected under copyright law. No part of this code is modified without my express permission. I reserve all rights to the code.
*/
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

class Edge {

    int source;
    int destination;
    int capacity;
    int flow;
    Edge reverseEdge;

    public Edge(int source, int destination, int capacity, int flow, Edge reverseEdge) {
        this.source = source;
        this.destination = destination;
        this.capacity = capacity;
        this.flow = flow;
        this.reverseEdge = reverseEdge;
    }

    // Getters and setters (if necessary)

    public String toString() {
        return "( " + source + " ," + destination + " ," + capacity + " ," + flow + " )";
    }
}

class preflowPush {

    private static long endTime; // Variable to record end time
    static int increment = 1; // Variable to increment the test case number
    private static long startTime; // Variable to record start time
    // private static int vertices; // Variable to store the number of vertices
    private ArrayList<ArrayList<Edge>> graph;
    private int[] height;
    private int[] excessFlow;
    private int vertices;

    public preflowPush(ArrayList<ArrayList<Edge>> graph, int vertices) {
        this.graph = graph;
        this.vertices = vertices;
        this.height = new int[vertices];
        this.excessFlow = new int[vertices];
    }
    // Method to print the flow network and the max flow
    public void printFlowNetworkAndMaxFlow(int source, int destination) {
        
        System.out.println("** G"+ (increment++) +": |V|=" + vertices);
        if (vertices < 11) {
            System.out.println("Flow network:");
            printMatrix(false); // false for capacity matrix
            System.out.println("\nMaximum flow:");
            printMatrix(true); // true for flow matrix
            int maxFlow = getMaxFlow(source, destination);
            System.out.println("Max flow ==> " + maxFlow + " (" + (endTime - startTime) + " ms)" );
        } else {
            int maxFlow = getMaxFlow(source, destination);
            System.out.println("Max flow ==> " + maxFlow + " (" + (endTime - startTime) + " ms)" );
        }
        System.out.println();
    }
    // Method to print the matrix
    private void printMatrix(boolean includeFlow) {
        System.out.print("       ");
        for (int i = 0; i < vertices; i++) {
            System.out.printf("%-7s", i + ":");
        }
        System.out.println("\n" + "-".repeat(vertices * 7 + 4));

        for (int i = 0; i < vertices; i++) {
            System.out.printf("%-7s", i + ":");
            for (int j = 0; j < vertices; j++) {
                String value = "-";
                for (Edge edge : graph.get(i)) {
                    if (edge.destination == j) {
                        int val = includeFlow ? edge.flow : edge.capacity;
                        if (val > 0) {
                            value = String.valueOf(val);
                        }
                        break;
                    }
                }
                System.out.printf("%-7s", value);
            }
            System.out.println();
        }
    }
    // Method to initialize the preflow
    private void initializePreflow(int source) {
        height[source] = vertices;
        for (Edge edge : graph.get(source)) {
            excessFlow[edge.destination] += edge.capacity;
            edge.flow = edge.capacity;
            if (edge.reverseEdge != null) {
                edge.reverseEdge.flow = -edge.capacity;
            }
        }
    }
    // Method to push the flow
    private void push(Edge edge) {
        int flow = Math.min(excessFlow[edge.source], edge.capacity - edge.flow);
        edge.flow += flow;
        if (edge.reverseEdge != null) {
            edge.reverseEdge.flow -= flow;
        }
        excessFlow[edge.source] -= flow;
        excessFlow[edge.destination] += flow;
    }
    // Method to relabel the node
    private void relabel(int node) {
        int minHeight = Integer.MAX_VALUE;
        for (Edge edge : graph.get(node)) {
            if (edge.capacity > edge.flow) {
                minHeight = Math.min(minHeight, height[edge.destination]);
            }
        }
        if (minHeight != Integer.MAX_VALUE) {
            height[node] = minHeight + 1;
        }
    }
    // Method to find the overflow node
    private int overFlowNode() {
        for (int i = 1; i < vertices - 1; i++) {
            if (excessFlow[i] > 0) {
                return i;
            }
        }
        return -1;
    }
    // Method to find the max flow
    public int getMaxFlow(int source, int destination) {
        startTime = System.currentTimeMillis();
        initializePreflow(source);
        while (true) {
            int overflowNode = overFlowNode();
            if (overflowNode == -1) {
                break;
            }
            boolean pushed = false;
            for (Edge edge : graph.get(overflowNode)) {
                if (edge.capacity > edge.flow && height[edge.source] > height[edge.destination]) {
                    push(edge);
                    pushed = true;
                    break;
                }
            }
            if (!pushed) {
                relabel(overflowNode);
            }
        }
        endTime = System.currentTimeMillis();
        return excessFlow[destination];
    }
    public static void main(String[] args) {
        File myFile = new File(args[0]);
        // int increment = 1;
        try (Scanner sc = new Scanner(myFile)) {
            System.out.println("Preflow-Push algorithm\n");
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                int vertices = 0;
                ArrayList<ArrayList<Edge>> Graph = null;
                if (line.startsWith("*")) {
                    // System.out.println("Test Case " + increment++);
                    line = line.replaceAll("[^0-9]", " ");
                    line = line.replaceAll("\\s+", " ");
                    String[] parts = line.split(" ");
                    vertices = Integer.parseInt(parts[2]);
                    Graph = new ArrayList<>(vertices);
                    for (int i = 0; i < vertices; i++) {
                        Graph.add(new ArrayList<Edge>());
                    }
                    sc.nextLine();
                    while (!(line = sc.nextLine()).startsWith("-")) {
                        line = line.replaceAll("[^0-9,]", "");
                        parts = line.split(",");
                        int source = Integer.parseInt(parts[0]);
                        int destination = Integer.parseInt(parts[1]);
                        int capacity = Integer.parseInt(parts[2]);
                        Edge edge = new Edge(source, destination, capacity, 0, null);
                        Edge reverseEdge = new Edge(destination, source, 0, 0, edge);
                        edge.reverseEdge = reverseEdge;
                        Graph.get(source).add(edge);
                        Graph.get(destination).add(reverseEdge);
                    }
                    preflowPush preflowPush = new preflowPush(Graph, vertices);
                    int maxFlow = preflowPush.getMaxFlow(0, vertices - 1);
                    preflowPush.printFlowNetworkAndMaxFlow(0, vertices - 1);
                    // System.out.println("Max Flow using Preflow-Push: " + maxFlow);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("**by Dinesh Raj Pampati");

    }

}
