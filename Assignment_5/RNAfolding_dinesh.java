import java.io.File;
import java.util.List;
import java.util.Scanner;

public class RNAfolding {
    public static void main(String[] args) {
        File myFile = new File(args[0]);
        int sequenceNumber = 1; 
        try(Scanner sc = new Scanner(myFile)) {
            while(sc.hasNextLine()) {
                if(sc.nextLine().startsWith("*")){
                    String rna = sc.nextLine();
                    System.out.println("** RNA-" + sequenceNumber + ", length=" + rna.length() + ", Optimal secondary structure:");
                    //System.out.println(rna);
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
                    int pairCount = traceBack(rna, 0, rna.length() - 1, table);
                    System.out.println("Total base pairs: " + pairCount);
                }
            }
        } catch(Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private static int traceBack(String rna, int i, int j, int[][] table) {
        int count = 0;
        if (i < j) {
            if (table[i][j] == table[i + 1][j]) {
                count += traceBack(rna, i + 1, j, table);
            } else if (table[i][j] == table[i][j - 1]) {
                count += traceBack(rna, i, j - 1, table);
            } else if (isPair(rna.charAt(i), rna.charAt(j)) && j - i >= 5 && table[i][j] == table[i + 1][j - 1] + 1) {
                System.out.println(rna.charAt(i)+"-"+rna.charAt(j)+"(" + i + "," + j + ")");
                count += 1;
                count += traceBack(rna, i + 1, j - 1, table);
            } else {
                for (int x = i; x < j; x++) {
                    if (table[i][j] == table[i][x] + table[x + 1][j]) {
                        count += traceBack(rna, i, x, table);
                        count += traceBack(rna, x + 1, j, table);
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
