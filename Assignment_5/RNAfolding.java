/*Dinesh Raj Pampati
Student ID- 806659955
I'm giving a pledge of honesty that I did not copy/modify from other resources.
I declare that this code is created by me and is protected under copyright law. No part of this code is modified without my express permission. I reserve all rights to the code.
*/

// Import required libraries
import java.io.File;
import java.util.Scanner;

public class RNAfolding {
    public static void main(String[] args) {
        File myFile = new File(args[0]);
        int sequenceNumber = 1;
        System.out.println("Folding RNA in file " + args[0]);
        System.out.println();
        try(Scanner sc = new Scanner(myFile)) {
            while(sc.hasNextLine()) {
                if(sc.nextLine().startsWith("*")){
                    String rna = sc.nextLine();
                    // System.out.println(rna);
                    System.out.println("** RNA-" + sequenceNumber + ", length=" + rna.length() + ", Optimal secondary structure:");
                    sequenceNumber++;
                    int[][] table = new int[rna.length()][rna.length()];
                    for(int i = 0; i < rna.length(); i++) {
                        table[i][i] = 0;
                    }
                    for(int i = 1; i < rna.length(); i++) {
                        for(int j = 0; j < rna.length() - i; j++) {
                            int k = j + i;
                            table[j][k] = table[j][k - 1];
                            if(k - j >= 5 && 
                            ((rna.charAt(j) == 'A' && rna.charAt(k) == 'U') || 
                             (rna.charAt(j) == 'U' && rna.charAt(k) == 'A') || 
                             (rna.charAt(j) == 'C' && rna.charAt(k) == 'G') || 
                             (rna.charAt(j) == 'G' && rna.charAt(k) == 'C'))) 
                         {
                             table[j][k] = Math.max(table[j][k], table[j + 1][k - 1] + 1);
                         }
                         
                            for(int l = j; l < k; l++) {
                                table[j][k] = Math.max(table[j][k], table[j][l] + table[l + 1][k]);
                            }
                        }
                    }
                    
                    System.out.println("Base pairs:");
                    int pairCount = track_Back_Results(rna, 0, table, rna.length() - 1);
                    System.out.println("Total number of base pairs: " + pairCount);
                    System.out.println();
                }
            }
        } catch(Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    System.out.println("All by Pampati Dinesh Raj");
    }
    private static int track_Back_Results(String rna, int i, int[][] table, int j) {
        int count = 0;
        if (i < j) {
            if (table[i][j] == table[i + 1][j]) {
                count += track_Back_Results(rna, i + 1,table, j);
            } else if (table[i][j] == table[i][j - 1]) {
                count += track_Back_Results(rna, i,  table,j - 1);
            } else if (isPair(rna.charAt(i), rna.charAt(j)) && j - i >= 5 && table[i][j] == table[i + 1][j - 1] + 1) {
                System.out.println(rna.charAt(i)+"-"+rna.charAt(j)+" (" + (i + 1) + "," + (j + 1) + ")");
                count += 1;
                count += track_Back_Results(rna, i + 1, table, j - 1 );
            } else {
                for (int x = i; x < j; x++) {
                    if (table[i][j] == table[i][x] + table[x + 1][j]) {
                        count += track_Back_Results(rna, i,table, x );
                        count += track_Back_Results(rna, x + 1, table, j );
                        break;
                    }
                }
            }
        }
        return count;
    }
    
    private static boolean isPair(char a, char b) {
        return (a == 'A' && b == 'U') || (a == 'U' && b == 'A') || (a == 'C' && b == 'G') || (a == 'G' && b == 'C');
    }

}
