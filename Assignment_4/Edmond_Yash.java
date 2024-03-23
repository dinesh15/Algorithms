import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Stack;

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

public class Edmond_Yash {

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
public static void updateIndices(PriorityQueue<VertexWeighted> values,int leastNode, int node, int superNode, List<Integer> cycleNodes , boolean[] updatedEdges1 , boolean[] updatedEdges2 , int counter) {
    // copy the values to a new list
    List<VertexWeighted> valuesCopy = new ArrayList<VertexWeighted>();
    for (VertexWeighted vw : values) {
        valuesCopy.add(vw);
    }
    for (int i = 0 ; i < valuesCopy.size() ; i++){
        if (!updatedEdges1[valuesCopy.get(i).getCounter()] && equalToCycleNode(cycleNodes, valuesCopy.get(i).getVertex()[0])) {
           int newIndexs[] = new int[2];
           newIndexs[0] = superNode;
            newIndexs[1] = valuesCopy.get(i).getVertex()[1];
           valuesCopy.get(i).setVertex(newIndexs);
           updatedEdges1[valuesCopy.get(i).getCounter()] = true;
        }
        else {
            int temp = noOfCycleNodes(cycleNodes, valuesCopy.get(i).getVertex()[0]);
            if (!updatedEdges1[valuesCopy.get(i).getCounter()] && temp > 0) {
            int newIndexs[] = new int[2];
            newIndexs[0] = valuesCopy.get(i).getVertex()[0] - temp;
            newIndexs[1] = valuesCopy.get(i).getVertex()[1];
            valuesCopy.get(i).setVertex(newIndexs);
            updatedEdges1[valuesCopy.get(i).getCounter()] = true;
        }
    }
        if (!updatedEdges2[valuesCopy.get(i).getCounter()] && equalToCycleNode(cycleNodes, valuesCopy.get(i).getVertex()[1])) {
            int newIndexs[] = new int[2];
            newIndexs[0] = valuesCopy.get(i).getVertex()[0];
            newIndexs[1] = superNode;
            valuesCopy.get(i).setVertex(newIndexs);
            updatedEdges2[valuesCopy.get(i).getCounter()] = true;
        }
        else {
            int temp_1 = noOfCycleNodes(cycleNodes, valuesCopy.get(i).getVertex()[1]);
            if (!updatedEdges2[valuesCopy.get(i).getCounter()] && temp_1 > 0) {
            int newIndexs[] = new int[2];
            newIndexs[0] = valuesCopy.get(i).getVertex()[0];
            newIndexs[1] = valuesCopy.get(i).getVertex()[1] - temp_1;
            valuesCopy.get(i).setVertex(newIndexs);
            updatedEdges1[valuesCopy.get(i).getCounter()] = true;
        }
    }
       System.out.println("updated values"+valuesCopy.get(i));
    }
    System.out.println("updated values copy"+valuesCopy);
    System.out.println("updated values"+values);
}



public static void contractSuperNodes(Hashtable<Integer, PriorityQueue<VertexWeighted>> allValuesClone, List<List<Integer>> cycleNodes, int lastVertex , int counter) {
    // create a super node for the cycle which will be the last vertex + 1
    // create a new hashtable to keep track of the new values
    boolean[] updatedEdges = new boolean[counter + 1];
    boolean[] updatedEdges2 = new boolean[counter + 1];

    Hashtable<Integer, PriorityQueue<VertexWeighted>> newValues = new Hashtable<Integer, PriorityQueue<VertexWeighted>>();
    // add the new super node to the hashtable
    int superNode = lastVertex + 1  - cycleNodes.size(); // have to update the lastvertec for the next interation 
    newValues.put(superNode, new PriorityQueue<VertexWeighted>());
    // for every node in the cycle
    for (int i = 0; i < cycleNodes.size(); i++) {
        // get the node
        int node = cycleNodes.get(i);
        // get all the outgoing edges from the node
        PriorityQueue<VertexWeighted> values = new PriorityQueue<VertexWeighted>();// = allValuesClone.get(node);
        // for every outgoing edge
        for  (PriorityQueue<VertexWeighted> temp :allValuesClone.values()){
               // System.out.println(temp);
                for(VertexWeighted vw : temp){
                    if(vw.getVertex()[0] == node){
                       
                        values.add(vw);
                    }
                }
        }
        System.out.println("outgoing edges from the cycle nodes before updating"+ values); // these should be all the outgoing edges from the node 
        // updating the indices of the edges of outgoing edges
        Collections.sort(cycleNodes);
        int leastNode = cycleNodes.get(0);
        updateIndices(values, leastNode,node, superNode, cycleNodes, updatedEdges , updatedEdges2 , counter);

        System.out.println();
        for (VertexWeighted vw : values) {
            // if the edge is not in the cycle     
            if (!cycleNodes.contains(vw.getVertex()[1])) {
                // if the edge is not in the new hashtable, add it
                if (!newValues.containsKey(vw.getVertex()[1])) {
                    
                    newValues.put(vw.getVertex()[1], new PriorityQueue<VertexWeighted>());
                }
                // add the edge to the new hashtable
                newValues.get(vw.getVertex()[1]).add(vw);
            }
        }
        // remove the node from the hashtable
       // allValuesClone.remove(node);
        System.out.println(allValuesClone);
        System.out.println("the new graph "+newValues); // have removed all the cycle outgoing cycle edges from the graph
        // now we need to add the incoming edges to the new super node
        // for every node in the graph
        }
        PriorityQueue<VertexWeighted> incomingEdges = new PriorityQueue<VertexWeighted>();
        for (int j = 0; j < cycleNodes.size(); j++) {
            // get all the incoming edges
            int incomingNode = cycleNodes.get(j);
            PriorityQueue<VertexWeighted> temp = allValuesClone.get(incomingNode);
            for(VertexWeighted vw : temp){
                    incomingEdges.add(vw);
         }
         Collections.sort(cycleNodes);
         int leastNode = cycleNodes.get(0);
         updateIndices(incomingEdges, leastNode,incomingNode, superNode, cycleNodes , updatedEdges, updatedEdges2 , counter);
        }
         System.out.println("incoming edges to the cycle nodes"+incomingEdges); // these should be all the incoming edges to the node
            // for every incoming edge
            for (VertexWeighted vw : incomingEdges) {
                // if the edge is not in the cycle
                if (!cycleNodes.contains(vw.getVertex()[0])) {
                    // if the edge is not in the new hashtable, add it
                    if (!newValues.containsKey(vw.getVertex()[0])) {
                        newValues.put(vw.getVertex()[0], new PriorityQueue<VertexWeighted>());
                    }
                    // add the edge to the new hashtable
                    // if(vw.getVertex()[1] = 
                    newValues.get(vw.getVertex()[1]).add(vw);
                }
            }
            System.out.println();
            System.out.println("the final new graph "+newValues); // have removed all the cycle outgoing cycle edges from the graph
        
        // create a new graph with the new super node 
        // PriorityQueue<VertexWeighted> conValues = new PriorityQueue<VertexWeighted>();

        // now give this connection to the super node and these are incoming edges to the new super node
        // update the indices of the edges
            // for each index in the graph if the index is greater than the index of nodes present in cycle 
            // decrement the index by no of nodes in cycle

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
        if (cycleNodes.isEmpty()) {
            System.out.println("No cycles found.");
        } else {
            System.out.println("Cycle found with nodes: " + cycleNodes);
        }

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