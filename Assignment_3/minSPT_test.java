import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.PriorityQueue;
import java.util.Scanner;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.

public class minSPT_test {
    public static boolean checkvisited(boolean[] visited) {
        for (int i = 0; i < visited.length; i++) {
            if (!visited[i]) {
                return false;
            }
        }
        return true;
    }

    public static boolean findCycle(int[] vertices, int[] cycleDectecIntegers) {
        int[] absoluteRoot = findabsolute(vertices, cycleDectecIntegers);
        if (absoluteRoot[0] == absoluteRoot[1]) {
            return true; // vertices are already in the same connected component, so adding the edge will create a cycle
        } else {
            cycleDectecIntegers[absoluteRoot[0]] = absoluteRoot[1]; // add the edge to the connected component
            return false;
        }
    }

    public static int[] findabsolute(int[] vertices, int[] cycleDectecIntegers) {
        int[] root = new int[2];
        root[0] = vertices[0];
        root[1] = vertices[1];
        while (cycleDectecIntegers[root[0]] != -1) {
            root[0] = cycleDectecIntegers[root[0]];
            
        }
        while (cycleDectecIntegers[root[1]] != -1) {
            root[1] = cycleDectecIntegers[root[1]];
        }
        return root;
    }

    public static ArrayList<Integer>  visitedList(boolean[] visited) {
        ArrayList<Integer> visitedList = new ArrayList<Integer>();
        for (int i = 0; i < visited.length; i++) {
            if (visited[i]) {
                visitedList.add(i);
            }
        }
        return visitedList;
    }

public static void primsAlgorithm(Hashtable<Integer, PriorityQueue<VertexWeighted>> edgeWeightDict,
        boolean[] PrimVisited, int lastVertex) {
    VertexWeighted temp;
    temp = edgeWeightDict.get(0).poll();
    System.out.println(temp);
    PrimVisited[temp.getVertex()[0]] = true;
    PrimVisited[temp.getVertex()[1]] = true;
    double primsWeight = 0;
    primsWeight += temp.getWeight();
    PriorityQueue<VertexWeighted> tempVertex = new PriorityQueue<VertexWeighted>();
    tempVertex.addAll(edgeWeightDict.get(0));
    while (!checkvisited(PrimVisited)) {
        temp = tempVertex.poll();
        if (temp == null) break; // No more edges to consider
        if (PrimVisited[temp.getVertex()[0]] && !PrimVisited[temp.getVertex()[1]]) {
            PrimVisited[temp.getVertex()[1]] = true;
            System.out.println(temp);
            primsWeight += temp.getWeight();
            tempVertex.addAll(edgeWeightDict.get(temp.getVertex()[1]));
        } else if (!PrimVisited[temp.getVertex()[0]] && PrimVisited[temp.getVertex()[1]]) {
            PrimVisited[temp.getVertex()[0]] = true;
            System.out.println(temp);
            primsWeight += temp.getWeight();
            tempVertex.addAll(edgeWeightDict.get(temp.getVertex()[0]));
        }
    }
    System.out.println("\t==> Total weight " + primsWeight);
}
    public static void main(String[] args) {
        File myFile = new File(args[0]);
        System.out.print("Minimum Spanning Trees of graphs in" + myFile);
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
                    int lastVertex = Integer.parseInt(allVertices[allVertices.length - 1]);
                    // System.out.println(lastVertex[lastVertex.length-1]); // DEBUG
                    System.out.print("\n** G" + k + ":\n");
                    k++;
                    PriorityQueue<VertexWeighted> values = new PriorityQueue<VertexWeighted>();
                    Hashtable<Integer,PriorityQueue<VertexWeighted>> allValues = new Hashtable<Integer,PriorityQueue<VertexWeighted>>();
                    sc.nextLine();
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
                            // System.out.println(vertex1 + " " + vertex2 + " " + weight);
                            values.add(new VertexWeighted(vertex1, vertex2, weight));
                            
                            allValues.get(vertex1).add(new VertexWeighted(vertex1, vertex2, weight));
                            // values_temp.poll();
                        }

                    }

                    int size = values.size();
                    
                    boolean[] visited = new boolean[lastVertex + 1];
                    int[] cycleDectecIntegers = new int[lastVertex +1];
                    for (int i = 0; i <= lastVertex; i++) {
                        cycleDectecIntegers[i] = -1;
                    }
                    double KrukalWeight = 0;
                    System.out.println("  Kruskal\'s algorithm:");
                    for (int i = 0; i <= size -1; i++) {
                        VertexWeighted temp = values.poll();
                        // System.out.println(temp+" this is popped");
                        int[] vertex = temp.getVertex();
                        // System.out.println("vertex is " + vertex[0] + " " + vertex[1] +" "+ temp.getWeight());
                        // System.out.println("vertex is " + vertex[0] + " " + vertex[1] +" "+ temp.getWeight());
                        if (findCycle(vertex, cycleDectecIntegers)) {
                            continue;
                        } else {
                            visited[vertex[0]] = true;
                            visited[vertex[1]] = true;

                            System.out.println("\t"+temp);
                            KrukalWeight += temp.getWeight();;
                        }
                    }

                    System.out.println("\t==> Total weight " + KrukalWeight);
                    System.out.println();
                    System.out.println("  Prim\'s algorithm:");
                    boolean[] PrimVisited = new boolean[lastVertex + 1];
                    primsAlgorithm(allValues, PrimVisited, lastVertex);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

//  Maintain a Visited PriorityQueue:
// You can maintain a separate PriorityQueue for visited vertices. Whenever a vertex is marked as visited, add it to this new PriorityQueue. Then, you can simply poll from this visited PriorityQueue to get the smallest visited vertex.