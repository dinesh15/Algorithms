/*Dinesh Raj Pampati
Student ID- 806659955
I'm giving a pledge of honesty that I did not copy/modify from other resources.
I declare that this code is created by me and is protected under copyright law. No part of this code is modified without my express permission. I reserve all rights to the code.
*/
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

class Edges {
    int source;
    int destination;
    int capacity;
    int flow;

    public Edges(int source, int destination, int capacity) {
        this.source = source;
        this.destination = destination;
        this.capacity = capacity;
        this.flow = 0;
    }
    public int getCapacity() {
        return capacity;
    }
    public int getDestination() {
        return destination;
    }
    public int getFlow() {
        return flow;
    }
    public int getSource() {
        return source;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    public void setDestination(int destination) {
        this.destination = destination;
    }
    public void setFlow(int flow) {
        this.flow = flow;
    }
    public void setSource(int source) {
        this.source = source;
    }
    @Override
    public String toString() {
        return "( " + source + " ," + destination + " ," + capacity + " ," + flow + " )";
    }
}

class BipartiteParts {
    List<Integer> part1;
    List<Integer> part2;

    BipartiteParts(List<Integer> part1, List<Integer> part2) {
        this.part1 = part1;
        this.part2 = part2;
    }

    List<Integer> getPart1() {
        return part1;
    }

    List<Integer> getPart2() {
        return part2;
    }
}


public class maxMatch {
    private static final int UNMATCHED = -1;
    static long startTime, endTime;

    public static void main(String[] args) {
        File myFile = new File(args[0]);
        int counter = 0;
        try (Scanner sc = new Scanner(myFile)) {
            System.out.println();
            System.out.println("Maximum number of matches in bipartite graphs in [" + args[0] + "]" );
            System.out.println();
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.startsWith("*")) {
                    line = line.replaceAll("[^0-9]", " ");
                    line = line.replaceAll("\\s+", " ");
                    String[] liness = line.split(" ");
                    int vertices = Integer.parseInt(liness[2]);
                    System.out.println("** G" + ++counter + ":\t" + "|V|=" + vertices);
                    // System.out.println("Vertices: " + vertices);
                    ArrayList<ArrayList<Edges>> graph = new ArrayList<>();
                    for (int i = 0; i < vertices + 1; i++) {
                        graph.add(new ArrayList<>());
                    }

    
                    // Construct the graph
                    // Add edges from the bipartite graph
                    sc.nextLine();
                    while (!(line = sc.nextLine()).startsWith("-")) {
                        line = line.replaceAll("[^0-9,]", "");
                        String[] parts = line.split(",");
                        int u = Integer.parseInt(parts[0]) + 1; // Offset by 1 for source
                        int v = Integer.parseInt(parts[1]) + 1; // Same here
    
                        graph.get(u).add(new Edges(u, v, 1)); // Edge from X to Y
                        graph.get(v).add(new Edges(v, u, 1)); // Reverse edge from Y to X
                    }
                    
                    startTime = System.currentTimeMillis();
                    BipartiteParts parts = findBipartiteParts(graph, vertices);
                    if (parts !=null) { // Check bipartite for the entire graph including source and sink
                        // System.out.println("Graph is bipartite.");
                        // System.out.println("Bipartite graph: " + graph);
                        // construct flow network from bipartite graph with source and sink
                        int maxMatch = constructFlowNetwork(graph, vertices , parts);

                        
                        
                        System.out.println("    Matches: " + maxMatch + " pairs " + "(" + (endTime - startTime) + "ms)" );
                        System.out.println();
                    } else {
                        System.out.println("    Not a bipartite graph " + "(" + (0 - 0) + "ms)");
                        System.out.println();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("\n***by Pampati Dinesh Raj.");
    }

    private static int constructFlowNetwork(ArrayList<ArrayList<Edges>> graph, int vertices , BipartiteParts parts) {
        int source = 0;
        int sink = vertices + 1;
        // contruct flow network with source and sink
        // from the scratch
        ArrayList<ArrayList<Edges>> flowNetwork = new ArrayList<>();
        for (int i = 0; i < vertices + 2; i++) {
            flowNetwork.add(new ArrayList<>());
        }

        // Add edges from the bipartite graph
        // don't add the reverse edges
        for (int i = 1; i <= vertices; i++) {
            for (Edges edge : graph.get(i)) {
                int u = edge.source;
                int v = edge.destination;
                flowNetwork.get(u).add(new Edges(u, v, 1)); // Edge from X to Y
            }
        }

        // Add edges from source to one side of the bipartite graph
        for (Integer u : parts.getPart1()) {
            flowNetwork.get(source).add(new Edges(source, u, 1));
        }


        // Add edges from another side of bipartite graph to sink
        for (Integer v : parts.getPart2()) {
            flowNetwork.get(v).add(new Edges(v, sink, 1));
        }


        // System.out.println("Flow network: " + flowNetwork);
        int maxMatch = fordFulkerson(flowNetwork, source, sink);
        
        return maxMatch;
    }

private static BipartiteParts findBipartiteParts(ArrayList<ArrayList<Edges>> graph, int vertices) {
    int[] colors = new int[vertices + 1]; // Increase the size of the colors array
    Arrays.fill(colors, UNMATCHED);

    List<Integer> part1 = new ArrayList<>();
    List<Integer> part2 = new ArrayList<>();

    for (int i = 1; i <= vertices; i++) { // Start from index 1
        if (colors[i] == UNMATCHED) {
            if (!bfsCheckBipartite(graph, i, colors, part1, part2)) {
                return null; // Graph is not bipartite
            }
        }
    }

    return new BipartiteParts(part1, part2);
}

private static boolean bfsCheckBipartite(ArrayList<ArrayList<Edges>> graph, int src, int[] colors, List<Integer> part1, List<Integer> part2) {
    Queue<Integer> queue = new LinkedList<>();
    queue.add(src);
    colors[src] = 0;
    part1.add(src);

    while (!queue.isEmpty()) {
        int u = queue.poll();

        for (Edges edge : graph.get(u)) {
            int v = edge.destination;
            if (colors[v] == UNMATCHED) {
                colors[v] = 1 - colors[u];
                queue.add(v);
                if (colors[v] == 0) {
                    part1.add(v);
                } else {
                    part2.add(v);
                }
            } else if (colors[v] == colors[u]) {
                return false; // Graph is not bipartite
            }
        }
    }
    return true; // Graph is bipartite
}

private static int fordFulkerson(ArrayList<ArrayList<Edges>> graph, int source, int sink) {
    int maxFlow = 0;
    int[] parent = new int[graph.size()]; // Array to store path back to the source

    List<Edges> matchedEdges = new ArrayList<>(); // List to store matched edges

    while (true) {
        boolean hasPath = bfs(graph, source, sink, parent); // Execute BFS to find path to sink
        if (!hasPath) {
            break; // Exit if no path is found
        }

        int pathFlow = Integer.MAX_VALUE;
        for (int v = sink; v != source; v = parent[v]) {
            int u = parent[v];
            for (Edges edge : graph.get(u)) {
                if (edge.destination == v) {
                    pathFlow = Math.min(pathFlow, edge.capacity - edge.flow);
                    break;
                }
            }
        }

        // Update flow along the path
        for (int v = sink; v != source; v = parent[v]) {
            int u = parent[v];
            for (Edges edge : graph.get(u)) {
                if (edge.destination == v) {
                    edge.flow += pathFlow;
                    break;
                }
            }
        }

        maxFlow += pathFlow;

        // Update matched edges
        for (int v = sink; v != source; v = parent[v]) {
            int u = parent[v];
            for (Edges edge : graph.get(u)) {
                if (edge.destination == v && edge.flow > 0) {
                    matchedEdges.add(edge);
                    break;
                }
            }
        }
    }

    // Sort matched edges
    Collections.sort(matchedEdges, new Comparator<Edges>() {
    @Override
    public int compare(Edges edge1, Edges edge2) {
        // Compare based on source vertex
        int sourceComparison = Integer.compare(edge1.source, edge2.source);
        if (sourceComparison != 0) {
            return sourceComparison;
        }
        // If source vertices are equal, compare based on destination vertex
        return Integer.compare(edge1.destination, edge2.destination);
    }
});

    // Print matched edges
    // System.out.println("Matched Edges:");
    endTime = System.currentTimeMillis();
    for (Edges edge : matchedEdges) {
        if (edge.source != source && edge.destination != sink) {
        System.out.println("    (" + (edge.source - 1) + ", " + (edge.destination - 1)  + ")");
    }
}

    return maxFlow;
}

    private static boolean bfs(ArrayList<ArrayList<Edges>> graph, int source, int sink, int[] parent) {
        Arrays.fill(parent, UNMATCHED); // Reset the parent array
        boolean[] visited = new boolean[graph.size()]; // Visited array for BFS
        Queue<Integer> queue = new LinkedList<>();
        queue.add(source);
        visited[source] = true;

        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (Edges edge : graph.get(u)) {
                int v = edge.destination;
                if (!visited[v] && edge.capacity > edge.flow) {
                    queue.add(v);
                    parent[v] = u;
                    visited[v] = true;
                    if (v == sink) {
                        return true; // Found path to sink
                    }
                }
            }
        }

        return false; // No path found to sink
    }
}

