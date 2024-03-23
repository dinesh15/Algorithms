import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Stack;
import java.util.stream.Collectors;

class VertexWeighted implements Comparable<VertexWeighted> {
    private int vertex_1, vertex_2;
    private double weight;
    private int counter;

    VertexWeighted(int vertex_1, int vertex_2, double weight, int counter) {
        this.vertex_1 = vertex_1;
        this.vertex_2 = vertex_2;
        this.weight = weight;
        this.counter = counter;
    }

    public int[] getVertex() {
        int[] vertex = new int[2];
        vertex[0] = vertex_1;
        vertex[1] = vertex_2;
        return vertex;
    }

    public void setVertex(int[] vertex) {
        this.vertex_1 = vertex[0];
        this.vertex_2 = vertex[1];
    }

    public double getWeight() {
        return weight;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    @Override
    public String toString() {
        return "(" + vertex_1 + " ," + vertex_2 + ", " + weight + ", " + counter + ")";
    }

    @Override
    public int compareTo(VertexWeighted o) {

        return Double.compare(this.weight, o.weight);
    }
}

public class Edmond_copy {

    static Stack<Hashtable<Integer, PriorityQueue<VertexWeighted>>> graphs = new Stack<>();

    public static boolean equalToCycleNode(List<Integer> cycleNodes, int node) {
        for (int i = 0; i < cycleNodes.size(); i++) {
            if (cycleNodes.get(i) == node) {
                return true;
            }
        }
        return false;
    }

    public static int noOfCycleNodes(List<List<Integer>> cycleNodes, int presentNode) {
        int count = 0;
        for (int i = 0; i < cycleNodes.size(); i++) {
            for (int j = 0; j < cycleNodes.get(i).size(); j++) {
                if (cycleNodes.get(i).get(j) < presentNode) {
                    count++;
                }
            }
        }
        return count;
    }

    public static List<List<Integer>> findAllZeroCycles(
            Hashtable<Integer, PriorityQueue<VertexWeighted>> allValuesClone, int lastVertex) {
        List<List<Integer>> allCycles = new ArrayList<>();
        List<Integer> glVisisted = new ArrayList<>();

        for (int i = 1; i <= lastVertex; i++) {
            if (glVisisted.contains(i)) {
                continue;
            }

            List<Integer> loVisited = new ArrayList<>();
            Stack<Integer> stack = new Stack<>();
            stack.push(i);
            loVisited.add(i);
            glVisisted.add(i);

            while (!stack.isEmpty()) {
                int currentVertex = stack.peek();
                PriorityQueue<VertexWeighted> originalValues = allValuesClone.get(currentVertex);
                PriorityQueue<VertexWeighted> valuesCopy = (originalValues == null) ? new PriorityQueue<>()
                        : new PriorityQueue<>(originalValues);

                if (valuesCopy.isEmpty()) {
                    stack.pop();
                    continue;
                }
                VertexWeighted nextVertex = valuesCopy.poll();

                if (nextVertex == null) {
                    stack.pop();
                    continue;
                }

                if (nextVertex.getVertex()[0] == i && nextVertex.getWeight() == 0) {
                    List<Integer> cycle = new ArrayList<>();
                    Iterator stackIterator = stack.iterator();
                    while (stackIterator.hasNext()) {
                        cycle.add((Integer) stackIterator.next());
                    }
                    allCycles.add(cycle);
                    break; // Only break out of the current loop, not the outer loop
                }

                if (!loVisited.contains(nextVertex.getVertex()[0])) {
                    stack.push(nextVertex.getVertex()[0]);
                    loVisited.add(nextVertex.getVertex()[0]);
                    glVisisted.add(nextVertex.getVertex()[0]);
                } else {
                    stack.pop();
                }
            }
        }
        System.out.println(allCycles + "Cycles\n");
        return allCycles;
    }

    public static boolean checkSameCyleNodes(VertexWeighted v, HashMap<Integer, Integer> map) {
        // if key value of the map of vertex 1 and vertex 2 are same then return true
        if (map.containsKey(v.getVertex()[0]) && map.containsKey(v.getVertex()[1])) {
            if (map.get(v.getVertex()[0]) == map.get(v.getVertex()[1])) {
                return true;
            }
        }
        return false;

    }

    public static boolean checkVertexIsSuperNode(int vertex2, HashMap<Integer, Integer> map) {
        if (map.containsKey(vertex2)) {
            return true;
        }
        return false;
    }

    public static Hashtable<Integer, PriorityQueue<VertexWeighted>> reverseGraph(
            Hashtable<Integer, PriorityQueue<VertexWeighted>> allValuesClone) {
        Hashtable<Integer, PriorityQueue<VertexWeighted>> allValuesClone2 = new Hashtable<Integer, PriorityQueue<VertexWeighted>>();
        // get all egdes from the graph and store them in a list
        List<VertexWeighted> allEdges = new ArrayList<VertexWeighted>();
        for (PriorityQueue<VertexWeighted> temp : allValuesClone.values()) {
            if (temp != null) {
                for (VertexWeighted vw : temp) {
                    allEdges.add(vw);
                }
            }
        }
        // make the keys of the hashtable as the vertex 0 and put the values into each
        // key which as vertex 0
        for (VertexWeighted vw : allEdges) {
            if (!allValuesClone2.containsKey(vw.getVertex()[0])) {
                allValuesClone2.put(vw.getVertex()[0], new PriorityQueue<VertexWeighted>());
            }
            allValuesClone2.get(vw.getVertex()[0]).add(vw);
        }
        System.out.println("all edges" + allEdges);
        System.out.println("all reversed clone" + allValuesClone2);
        return allValuesClone2;
    }

    public static void contractSuperNodes(Hashtable<Integer, PriorityQueue<VertexWeighted>> allValuesClone,
            List<List<Integer>> cycleNodes, int lastVertex, int counter) {
        // create a super node for the cycle which will be the last vertex + 1
        // create a new hashtable to keep track of the new values

        // add the new super node to the hashtable
        int size = 0;
        for (int i = 0; i < cycleNodes.size(); i++) {
            size = size + cycleNodes.get(i).size();
        }
        int lastSuperNode = lastVertex - (size - cycleNodes.size()); // have to update the lastvertec for the next
        int lastIndex = lastSuperNode;

        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        System.out.println("last super node" + lastSuperNode);

        for (List<Integer> b : cycleNodes) {
            for (int a : b) {
                map.put(a, lastSuperNode);
            }
            lastSuperNode--;
        }

        Hashtable<Integer, PriorityQueue<VertexWeighted>> newValues = new Hashtable<Integer, PriorityQueue<VertexWeighted>>();
        Hashtable<Integer, PriorityQueue<VertexWeighted>> allValuesClone2 = new Hashtable<Integer, PriorityQueue<VertexWeighted>>();
        allValuesClone2 = reverseGraph(allValuesClone);
        System.out.println("reversed graph" + allValuesClone2);
        for (PriorityQueue<VertexWeighted> temp : allValuesClone2.values()) {
            // for each vertex in the values
            for (VertexWeighted vw : temp) {
                // get the vertex 1 and vertex 2
                int vertex1 = vw.getVertex()[0];
                int vertex2 = vw.getVertex()[1];
                // update the vertex 1 and vertex 2 with the new values
                if (checkVertexIsSuperNode(vertex2, map)) {
                    // if the vertex 2 is super node then only update the vertex 1
                    // which is vertex 1 - no of smaller nodes in the cycle
                    if (!checkVertexIsSuperNode(vertex1, map)) {
                        vertex1 = vertex1 - noOfCycleNodes(cycleNodes, vertex1);
                    } else {
                        vertex1 = map.get(vertex1);
                    }
                    vertex2 = map.get(vertex2);

                } else {
                    // if the vertex 2 is not super node then update the vertex 2 with the new value
                    // which is vertex 2 - no of smaller nodes in the cycle
                    if (!checkVertexIsSuperNode(vertex2, map)) {

                        vertex2 = vertex2 - noOfCycleNodes(cycleNodes, vertex2);
                    }
                    if (!checkVertexIsSuperNode(vertex1, map)) {
                        // if the vertex 1 is not super node then update the vertex 1 with the new value
                        // which is vertex 1 - no of smaller nodes in the cycle
                        vertex1 = vertex1 - noOfCycleNodes(cycleNodes, vertex1);
                    } else {
                        // if the vertex 1 is super node then update the vertex 1 with the new new super
                        // node which is map value of vertex 1
                        vertex1 = map.get(vertex1);
                    }
                }
                // after updating both vertex 1 and vertex 2 add the edge to the newValues
                // hashtable with key as vertex 2
                if (vertex1 == vertex2) {
                    continue;
                }
                newValues.computeIfAbsent(vertex2, k -> new PriorityQueue<>())
                        .add(new VertexWeighted(vertex1, vertex2, vw.getWeight(), vw.getCounter()));
            }
        }
        System.out.println("final new values" + newValues);
        // now find the zero cycles in the newValues
        List<List<Integer>> newCycleNodes = findAllZeroCycles(newValues, newValues.size());
        System.out.println("new cycle nodes" + newCycleNodes);
        // if there are no zero cycles then we have found the minimum arborescence
        // update the allvalues with newValues
        graphs.push(newValues);
        allValuesClone = newValues;

        // if (newCycleNodes.isEmpty()) {

        // System.out.println("Minimum Arborescence found with root node 0 and edges:
        // ");
        // // now start the process of expalnsion from the last contracted graph

        // }
        // // if there are zero cycles then we have to contract the super nodes again
        // else {
        // contractSuperNodes(newValues, newCycleNodes, lastIndex, counter);
        // }
    }

    public static void edmondAlgorithm(Hashtable<Integer, PriorityQueue<VertexWeighted>> allValues, int lastVertex,
            int counter) {
        while (true) {
            // clone the allvaluse to keep the original values
           // Hashtable<Integer, PriorityQueue<VertexWeighted>> allValuesClone = new Hashtable<Integer, PriorityQueue<VertexWeighted>>();
            // for (Integer key : allValues.keySet()) {
            //     PriorityQueue<VertexWeighted> values = allValues.get(key);
            //     PriorityQueue<VertexWeighted> valuesClone = new PriorityQueue<VertexWeighted>();
            //     for (VertexWeighted vw : values) {
            //         valuesClone.add(vw);
            //     }
            //     allValues.put(key, valuesClone);
            // }
            // System.out.print(allValuesClone + "Original\n");
            // now except the root vertex that is 0 we will find the least incoming edge for
            // all the vertices and
            // make the weight of that edge 0
            // then remove the weight of that edge from all the other edges of that vertex
            // then update the allValuesClone with the new values
            for (int i = 1; i <= lastVertex; i++) {
                PriorityQueue<VertexWeighted> values = allValues.get(i);
                VertexWeighted leastIncomingEdge = null;
                for (VertexWeighted vw : values) {
                    if (vw.getVertex()[1] == i && (leastIncomingEdge == null || vw.getWeight() < leastIncomingEdge.getWeight())) {
                        leastIncomingEdge = vw;
                    }
                }
                if (leastIncomingEdge != null) {
                    double weight = leastIncomingEdge.getWeight();
                    // values.remove(leastIncomingEdge);
                    // leastIncomingEdge = new VertexWeighted(leastIncomingEdge.getVertex()[0],
                    // leastIncomingEdge.getVertex()[1], 0);
                    // values.add(leastIncomingEdge);
                    // System.out.println(leastIncomingEdge);
                    List<VertexWeighted> toRemove = new ArrayList<>();
                    List<VertexWeighted> toAdd = new ArrayList<>();
                    for (VertexWeighted vw : values) {
                        // allValuesClone.get(vw.getVertex()[1]).remove(vw);
                        toRemove.add(vw);
                        vw = new VertexWeighted(vw.getVertex()[0], vw.getVertex()[1], vw.getWeight() - weight,
                                vw.getCounter());
                        // allValuesClone.get(vw.getVertex()[1]).add(vw);
                        toAdd.add(vw);
                    }
                    for (VertexWeighted vw : toRemove) {
                        values.remove(vw);
                    }
                    for (VertexWeighted vw : toAdd) {
                        values.add(vw);
                    }
                    // System.out.println(allValuesClone);
                }
                // allValuesClone.put(i, values);
            }

            List<List<Integer>> cycleNodes = findAllZeroCycles(allValues, lastVertex);
            if (cycleNodes.isEmpty()) {
                System.out.println("Minimum Arborescence found with root node 0 and edges: ");
                // now start the process of expalnsion from the last contracted graph
                break;
            }
            System.out.println("cycle nodes" + cycleNodes);
            contractSuperNodes(allValues, cycleNodes, lastVertex, counter);

        }
    }

    public static void main(String[] args) {
        File myFile = new File(args[0]);
        System.out.print("Minimum Arborescences in" + myFile);
        int k = 1;
        try (Scanner sc = new Scanner(myFile)) {
            sc.nextLine(); // to skip the first 2 lines
            sc.nextLine();
            while (sc.hasNextLine()) {
                String str;
                if ((str = sc.nextLine()).startsWith("*")) {
                    String[] tstr = str.split(":");
                    String result = tstr[1].replaceAll("[^0-9]", " ");
                    result = result.replaceAll("\\s+", " ");
                    String[] allVertices = result.split(" ");
                    int lastVertex = Integer.parseInt(allVertices[allVertices.length - 1]) - 1;
                    System.out.print("\n** G" + k + ":\n");
                    k++;
                    PriorityQueue<VertexWeighted> values = new PriorityQueue<VertexWeighted>();
                    Hashtable<Integer, PriorityQueue<VertexWeighted>> allValues = new Hashtable<Integer, PriorityQueue<VertexWeighted>>();
                    Hashtable<Integer, PriorityQueue<VertexWeighted>> org_allValues = new Hashtable<Integer, PriorityQueue<VertexWeighted>>();
                    sc.nextLine();
                    int counter = 0;
                    for (int i = 0; i <= lastVertex; i++) {
                        allValues.put(i, new PriorityQueue<VertexWeighted>());
                        org_allValues.put(i, new PriorityQueue<VertexWeighted>());
                    }
                    while (!(str = sc.nextLine()).startsWith("---")) {
                        str = str.replaceAll("[^0-9.]", " ");
                        str = str.replaceAll("\\s+", " ");
                        String[] parts = str.split(" ");
                        if (parts.length >= 3) {
                            int vertex1 = Integer.parseInt(parts[1]);
                            int vertex2 = Integer.parseInt(parts[2]);
                            double weight = Double.parseDouble(parts[3]);
                            counter++;
                            // values.add(new VertexWeighted(vertex1, vertex2, weight, counter));
                            allValues.get(vertex2).add(new VertexWeighted(vertex1, vertex2, weight, counter));
                            org_allValues.get(vertex2).add(new VertexWeighted(vertex1, vertex2, weight, counter));
                            
                        }

                    }
                    edmondAlgorithm(allValues, lastVertex, counter);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}