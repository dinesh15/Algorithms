/*Dinesh Raj Pampati
Student ID- 806659955
I'm giving a pledge of honesty that I did not copy/modify from other resources.
I declare that this code is created by me and is protected under copyright law. No part of this code is modified without my express permission. I reserve all rights to the code.
*/
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.PriorityQueue;
import java.util.Scanner;

// This class is used to store the vertices and the weight of the edge between them
class VertexWeighted implements Comparable<VertexWeighted> {
    private int vertex_1, vertex_2;
    private double weight;


    public VertexWeighted(int vertex_1, int vertex_2, double weight) {
        this.vertex_1 = vertex_1;
        this.vertex_2 = vertex_2;
        this.weight = weight;
    }

    public int[] getVertex() {
        int[] vertex = new int[2];
        vertex[0] = vertex_1;
        vertex[1] = vertex_2;
        return vertex;
    }

    public double getWeight() {
        return weight;
    }

    public String toString() {
        return "(" + vertex_1 +" ," +vertex_2 + ", " + weight + ")";
    }

    @Override
    public int compareTo(VertexWeighted other) {
        return Double.compare(this.weight, other.weight);
}
}
public class minSPT {
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
        PriorityQueue<VertexWeighted> values = new PriorityQueue<VertexWeighted>();
        ArrayList<Integer> visitedList = new ArrayList<Integer>();
        visitedList.add(0);
        PrimVisited[0] = true;
        double PrimWeight = 0;
        while (visitedList.size() != lastVertex + 1) {
            for (int i = 0; i < visitedList.size(); i++) {
                int vertex = visitedList.get(i);
                PriorityQueue<VertexWeighted> temp = edgeWeightDict.get(vertex);
                if (temp == null) {
                    continue;
                }
                while (!temp.isEmpty()) {
                    VertexWeighted tempVertex = temp.poll();
                    int[] tempVertexInt = tempVertex.getVertex();
                    if (!PrimVisited[tempVertexInt[1]]) {
                        values.add(tempVertex);
                    }
                }
            }
            VertexWeighted temp = values.poll();
            if(temp == null){
                break;
            }
            int[] vertex = temp.getVertex();
            if (PrimVisited[vertex[0]] && PrimVisited[vertex[1]]) {
                continue;
            } else {
                PrimVisited[vertex[0]] = true;
                PrimVisited[vertex[1]] = true;
                visitedList.add(vertex[0]);
                visitedList.add(vertex[1]);
                System.out.println("\t"+temp);
                PrimWeight += temp.getWeight();
            }
        }
        System.out.println("\t==> Total weight " + String.format("%.3f",PrimWeight));
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
                            values.add(new VertexWeighted(vertex1, vertex2, weight));
                            allValues.get(vertex1).add(new VertexWeighted(vertex1, vertex2, weight));
                            allValues.get(vertex2).add(new VertexWeighted(vertex2, vertex1, weight));
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
                        int[] vertex = temp.getVertex();
                        if (findCycle(vertex, cycleDectecIntegers)) {
                            continue;
                        } else {
                            visited[vertex[0]] = true;
                            visited[vertex[1]] = true;

                            System.out.println("\t"+temp);
                            KrukalWeight += temp.getWeight();;
                        }
                    }

                    System.out.println("\t==> Total weight " + String.format("%.3f",KrukalWeight));
                    System.out.println();
                    System.out.println("  Prim\'s algorithm:");
                    boolean[] PrimVisited = new boolean[lastVertex + 1];
                    primsAlgorithm(allValues, PrimVisited, lastVertex);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        System.out.println("*** Asg 3 by Pampati Dinesh Raj.");
    }
    // 
}