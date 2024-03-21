/*Dinesh Raj Pampati
Student ID- 806659955
I'm giving a pledge of honesty that I did not copy/modify from other resources.
I declare that this code is created by me and is protected under copyright law. No part of this code is modified without my express permission. I reserve all rights to the code.
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;

import java.util.ArrayList;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public class graphcc {
    public static boolean checkFlags(boolean[] bfsFlags, int size) {
        for (int i = 0; i < size; i++) {
            if (bfsFlags[i] == false) {
                return true;
            }
        }
        return false;
    }

    public static int selectTheFirstNode(boolean[] flag) {
        for (int i = 0; i < flag.length; i++) {
            if (!flag[i]) {
                return i;
            }
        }
        return -1; // Return -1 if all nodes are visited
    }

    public static void main(String[] args) throws FileNotFoundException {
        File myFile = new File(args[0]);
        System.out.print("Connected components of graphs in" + myFile);
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
                    System.out.print("\n** G" + k + "'s connected components: \n");
                    k++;
                    ArrayList<LinkedList<Integer>> bfsList = new ArrayList<LinkedList<Integer>>(lastVertex);
                    ArrayList<LinkedList<Integer>> dfsList = new ArrayList<LinkedList<Integer>>(lastVertex);
                    for (int i = 0; i <= lastVertex; i++) {
                        bfsList.add(new LinkedList<Integer>());
                        dfsList.add(new LinkedList<Integer>());
                    }
                    sc.nextLine();
                    while (!(str = sc.nextLine()).startsWith("---")) {
                        str = str.replaceAll("[^0-9]", " ");
                        str = str.replaceAll("\\s+", " ");
                        bfsList.get((Integer.parseInt(str.split(" ")[1]))).add((Integer.parseInt(str.split(" ")[2])));
                        bfsList.get((Integer.parseInt(str.split(" ")[2]))).add((Integer.parseInt(str.split(" ")[1])));
                        dfsList.get((Integer.parseInt(str.split(" ")[1]))).add((Integer.parseInt(str.split(" ")[2])));
                        dfsList.get((Integer.parseInt(str.split(" ")[2]))).add((Integer.parseInt(str.split(" ")[1])));
                    }
                    // System.out.println("Size of the adjacent list: " + bfsList.size());
                    for (int i = 0; i < lastVertex; i++) {
                        Collections.sort(dfsList.get(i));
                        Collections.sort(bfsList.get(i));
                    }
                    System.out.println("\tBreadth First Search: ");
                    boolean bfsFlags[] = new boolean[bfsList.size()];
                    Queue<Integer> q = new LinkedList<>();

                    while (checkFlags(bfsFlags, lastVertex)) {

                        {
                            int i;
                            if (selectTheFirstNode(bfsFlags) == -1) {
                                break;
                            } else {
                                i = selectTheFirstNode(bfsFlags);
                            }
                            System.out.print("\t\t");
                            q.add(i);
                            System.out.print(i + " ");
                            bfsFlags[i] = true;
                            while (!q.isEmpty()) {
                                // add the all the childs to the q of i

                                while (!bfsList.get(i).isEmpty()) {
                                    int temp = bfsList.get(i).pop();
                                    if (bfsFlags[temp] == true) {
                                        break;
                                    }
                                    System.out.print(temp + " ");
                                    bfsFlags[temp] = true;
                                    q.add(temp); // add the all the children to the q

                                }
                                if (bfsList.get(i).isEmpty()) {
                                    q.remove();
                                    if (q.isEmpty()) {
                                        break;
                                    }
                                    i = q.peek();
                                }
                            }
                            System.out.println();
                        }
                    }
                    System.out.println("\tDepth First Search: ");
                    // int sdfaf = dfsList.get(0).size();
                    boolean dfsFlags[] = new boolean[dfsList.size()];
                    Stack<Integer> st = new Stack<Integer>();
                    while (checkFlags(dfsFlags, lastVertex)) {
                        int j;
                        if (selectTheFirstNode(dfsFlags) == -1) {
                            break;
                        } else {
                            j = selectTheFirstNode(dfsFlags);
                        }
                        System.out.print("\t\t");
                        st.push(j);
                        System.out.print(j + " ");
                        dfsFlags[j] = true;
                    
                        while (!st.empty()) {
                            j = st.peek();
                            boolean hasUnvisitedNeighbor = false;
                    
                            for (int neighbor : dfsList.get(j)) {
                                if (!dfsFlags[neighbor]) {
                                    hasUnvisitedNeighbor = true;
                                    st.push(neighbor);
                                    System.out.print(neighbor + " ");
                                    dfsFlags[neighbor] = true;
                                    break;
                                }
                            }
                    
                            if (!hasUnvisitedNeighbor) {
                                st.pop();
                            }
                        }
                        System.out.println();
                    }
                    

                }
            }
        }
        System.out.println("\n\n*** Asg 1 by Pampati Dinesh Raj.");
    }
}
