import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Stack;
import java.util.Iterator;

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
        return "(" + vertex_1 + " ," + vertex_2 + ", " + weight + ", " + counter +")";
    }

    @Override
    public int compareTo(VertexWeighted o) {

        return Double.compare(this.weight, o.weight);
    }
}



public class Edmond_Test {

public static boolean equalToCycleNode(List<Integer> cycleNodes, int node) {
    for (int i = 0; i < cycleNodes.size(); i++) {
        if (cycleNodes.get(i) == node) {
            return true;
        }
    }
    return false;
}

public static int noOfCycleNodes(List<Integer> cycleNodes, int presentNode) {
    int count = 0;
    for (int i = 0; i < cycleNodes.size(); i++) {
        if (cycleNodes.get(i) < presentNode) {
            count++;
        }
    }
    return count;
}
public static List<List<Integer>> findAllZeroCycles(Hashtable<Integer, PriorityQueue<VertexWeighted>> allValuesClone, int lastVertex) {
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
            PriorityQueue<VertexWeighted> valuesCopy = (originalValues == null) ? new PriorityQueue<>() : new PriorityQueue<>(originalValues);

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



public static void updateIndices(PriorityQueue<VertexWeighted> values, int superNode, List<Integer> cycleNodes) {
    List<VertexWeighted> updatedValues = new ArrayList<>();

    while (!values.isEmpty()) {
        VertexWeighted vw = values.poll();
        int src = vw.getVertex()[0];
        int dest = vw.getVertex()[1];

        // Update source vertex
        if (cycleNodes.contains(src)) {
            src = superNode;
        } else if (src > cycleNodes.get(cycleNodes.size() - 1)) {
            src -= cycleNodes.size();
        }

        // Update destination vertex
        if (cycleNodes.contains(dest)) {
            dest = superNode;
        } else if (dest > cycleNodes.get(cycleNodes.size() - 1)) {
            dest -= cycleNodes.size();
        }

        updatedValues.add(new VertexWeighted(src, dest, vw.getWeight(), vw.getCounter()));
    }

    values.addAll(updatedValues);
}





public static void contractSuperNodes(Hashtable<Integer, PriorityQueue<VertexWeighted>> allValuesClone, List<List<Integer>> allCycleNodes, int lastVertex, int counter) {
    for (List<Integer> cycleNodes : allCycleNodes) {
        int superNode = lastVertex + 1 - cycleNodes.size(); // Update the lastVertex for the next iteration
        Hashtable<Integer, PriorityQueue<VertexWeighted>> newValues = new Hashtable<>();

        // Handle outgoing edges from cycle nodes
        for (int node : cycleNodes) {
            PriorityQueue<VertexWeighted> values = new PriorityQueue<>();
            for (PriorityQueue<VertexWeighted> temp : allValuesClone.values()) {
                for (VertexWeighted vw : temp) {
                    if (vw.getVertex()[0] == node) {
                        values.add(vw);
                    }
                }
            }
            updateIndices(values, superNode, cycleNodes);
            newValues.put(node, values);
        }

        // Handle incoming edges to cycle nodes
        PriorityQueue<VertexWeighted> incomingEdges = new PriorityQueue<>();
        for (int incomingNode : cycleNodes) {
            PriorityQueue<VertexWeighted> temp = allValuesClone.get(incomingNode);
            for (VertexWeighted vw : temp) {
                incomingEdges.add(vw);
            }
        }
        updateIndices(incomingEdges, superNode, cycleNodes);

        // Add the updated edges to the newValues hashtable
        for (VertexWeighted vw : incomingEdges) {
            if (!newValues.containsKey(vw.getVertex()[0])) {
                newValues.put(vw.getVertex()[0], new PriorityQueue<>());
            }
            newValues.get(vw.getVertex()[0]).add(vw);
        }

        // Update the original hashtable
        allValuesClone.clear();
        allValuesClone.putAll(newValues);
        System.out.println(allValuesClone + "Updated\n");
    }
}






    public static void edmondAlgorithm(Hashtable<Integer, PriorityQueue<VertexWeighted>> allValues, int lastVertex , int counter) {
        // clone the allvaluse to keep the original values
        Hashtable<Integer, PriorityQueue<VertexWeighted>> allValuesClone = new Hashtable<Integer, PriorityQueue<VertexWeighted>>();
        for (Integer key : allValues.keySet()) {
            PriorityQueue<VertexWeighted> values = allValues.get(key);
            PriorityQueue<VertexWeighted> valuesClone = new PriorityQueue<VertexWeighted>();
            for (VertexWeighted vw : values) {
                valuesClone.add(vw);
            }
            allValuesClone.put(key, valuesClone);
        }
        System.out.print(allValuesClone + "Original\n");
        // now except the root vertex that is 0 we will find the least incoming edge for
        // all the vertices and
        // make the weight of that edge 0
        // then remove the weight of that edge from all the other edges of that vertex
        // then update the allValuesClone with the new values
        for (int i = 1; i <= lastVertex; i++) {
            PriorityQueue<VertexWeighted> values = allValuesClone.get(i);
            VertexWeighted leastIncomingEdge = null;
            for (VertexWeighted vw : values) {
                if (vw.getVertex()[1] == i
                        && (leastIncomingEdge == null || vw.getWeight() < leastIncomingEdge.getWeight())) {
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
                    vw = new VertexWeighted(vw.getVertex()[0], vw.getVertex()[1], vw.getWeight() - weight, vw.getCounter());
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


        List<List<Integer>> cycleNodes = findAllZeroCycles(allValuesClone, lastVertex);


        contractSuperNodes(allValuesClone, cycleNodes, lastVertex , counter);
        

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
                    sc.nextLine();
                    int counter = 0;
                    for (int i = 0; i <= lastVertex; i++) {
                        allValues.put(i, new PriorityQueue<VertexWeighted>());
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
                            values.add(new VertexWeighted(vertex1, vertex2, weight, counter));
                            allValues.get(vertex2).add(new VertexWeighted(vertex1, vertex2, weight, counter));
                        }

                    }
                    
                    edmondAlgorithm(allValues, lastVertex , counter);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}