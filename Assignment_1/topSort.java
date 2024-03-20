/*Dinesh Raj Pampati
Student ID- 806659955
I'm giving a pledge of honesty that I did not copy/modify from other resources.
I declare that this code is created by me and is protected under copyright law. No part of this code is modified without my express permission. I reserve all rights to the code.
*/

// Import required libraries
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class topSort {

    /**
     * Dinesh Raj Pampati
     * Date Started: 09/01/2023
     * Date Finished: 09/01/2023
     * This method checks if all the values in the boolean array are true.
     */
    public static boolean check(boolean[] temp) {
        int ssum = 0;
        for (int i = 0; i < temp.length; i++) {
            if (temp[i] == true)
                ssum++;
        }
        if (ssum == temp.length)
            return false;
        else
            return true;
    }

    /**
     * Dinesh Raj Pampati
     * Date Started: 09/01/2023
     * Date Finished: 09/01/2023
     * The main method where the topological sorting algorithm is implemented.
     */
    public static void main(String[] args) throws FileNotFoundException {
        File myFile = new File(args[0]);
        System.out.print("Topological Order:");
        int k = 1;
        try (Scanner sc = new Scanner(myFile)) {
            while (sc.hasNextLine()) {
                String str;
                if ((str = sc.nextLine()).startsWith("*")) {
                    String[] tstr = str.split(":");
                    String result = tstr[1].replaceAll("[^0-9]", " ");
                    result = result.replaceAll("\\s+", " ");
                    String[] lastVertex = result.split(" ");
                    int[] inDegree = new int[lastVertex.length - 1];
                    System.out.print("\nG" + k + ": ");
                    k++;
                    ArrayList<LinkedList<Integer>> adj_list = new ArrayList<LinkedList<Integer>>(lastVertex.length - 1);
                    for (int i = 0; i < lastVertex.length - 1; i++)
                        adj_list.add(new LinkedList<Integer>());
                    Arrays.fill(inDegree, 0, lastVertex.length - 2, 0);
                    sc.nextLine();
                    while (!(str = sc.nextLine()).startsWith("---")) {
                        str = str.replaceAll("[^0-9]", " ");
                        str = str.replaceAll("\\s+", " ");
                        inDegree[(Integer.parseInt(str.split(" ")[2]))]++;
                        adj_list.get((Integer.parseInt(str.split(" ")[1]))).add((Integer.parseInt(str.split(" ")[2])));
                    }
                    boolean[] flag = new boolean[inDegree.length];
                    int no_indegree_vertices = 0;

                    for (int i = 0; i < inDegree.length; i++) {
                        if (inDegree[i] == 0) {
                            no_indegree_vertices++;
                        }
                    }
                    if (no_indegree_vertices == 0) {
                        System.out.print("No in-degree 0 vertex; not an acyclic graph.");
                        continue;
                    }
                    while (check(flag)) {
                        boolean foundVertex = false;
                        for (int i = 0; i < inDegree.length; i++) {
                            if (inDegree[i] == 0 && !flag[i]) {
                                System.out.print(i + " ");
                                for (int x : adj_list.get(i)) {
                                    inDegree[x]--;
                                }
                                foundVertex = true;
                                flag[i] = true;
                            }
                        }
                        if (!foundVertex) {
                            System.out.print("-> no more in-degree 0 vertex; not an acyclic graph.");
                            break;
                        }
                    }
                }
            }
        }
        System.out.println("\n\n*** Asg 1 by Pampati Dinesh Raj.");
    }
}
