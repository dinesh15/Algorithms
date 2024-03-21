import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class GraphCC_1 {

    private static ArrayList<LinkedList<Integer>> adjacencyList;
    private static int lastVertex;

    public static void readGraphFromFile(Scanner sc) {
        adjacencyList = new ArrayList<>();
        String str = sc.nextLine();
        String[] tstr = str.split(":");
        String result = tstr[1].replaceAll("[^0-9]", " ").trim();
        String[] allVertices = result.split(" ");
        if (allVertices.length < 2) {
            System.err.println("Unexpected line format: " + str);
            return;
        }
        lastVertex = Integer.parseInt(allVertices[allVertices.length - 1]);
    
        for (int i = 0; i <= lastVertex; i++) {
            adjacencyList.add(new LinkedList<>());
        }
    
        sc.nextLine();
        while (!(str = sc.nextLine()).startsWith("---")) {
            str = str.replaceAll("[^0-9]", " ").trim();
            String[] vertices = str.split(" ");
            if (vertices.length < 2) {
                System.err.println("Unexpected line format: " + str);
                continue;
            }
            int vertex1 = Integer.parseInt(vertices[0]);
            int vertex2 = Integer.parseInt(vertices[1]);
            adjacencyList.get(vertex1).add(vertex2);
            adjacencyList.get(vertex2).add(vertex1);
        }
    
        for (LinkedList<Integer> neighbors : adjacencyList) {
            Collections.sort(neighbors);
        }
    }
    

    public static void BFS() {
        boolean[] visited = new boolean[lastVertex + 1];
        Queue<Integer> queue = new LinkedList<>();

        for (int i = 0; i <= lastVertex; i++) {
            if (!visited[i]) {
                System.out.print("\t\t");
                queue.add(i);
                visited[i] = true;

                while (!queue.isEmpty()) {
                    int current = queue.poll();
                    System.out.print(current + " ");
                    for (int neighbor : adjacencyList.get(current)) {
                        if (!visited[neighbor]) {
                            visited[neighbor] = true;
                            queue.add(neighbor);
                        }
                    }
                }
                System.out.println();
            }
        }
    }

    public static void DFS() {
        boolean[] visited = new boolean[lastVertex + 1];
        Stack<Integer> stack = new Stack<>();

        for (int i = 0; i <= lastVertex; i++) {
            if (!visited[i]) {
                System.out.print("\t\t");
                stack.push(i);
                visited[i] = true;

                while (!stack.isEmpty()) {
                    int current = stack.pop();
                    System.out.print(current + " ");
                    for (int neighbor : adjacencyList.get(current)) {
                        if (!visited[neighbor]) {
                            visited[neighbor] = true;
                            stack.push(neighbor);
                        }
                    }
                }
                System.out.println();
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        File myFile = new File("D:\\One Drive\\Dinesh Student\\OneDrive - IL State University\\MS Academic\\2-1\\DA\\Assignment and homework\\Assignments\\Assignment_2\\udGraphs.txt");
  
        System.out.print("Connected components of graphs in " + myFile);
        int graphCount = 1;

        try (Scanner sc = new Scanner(myFile)) {
            sc.nextLine(); // to skip the first 2 lines
            sc.nextLine();

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.startsWith("*")) {
                    readGraphFromFile(sc);
                    System.out.print("\n** G" + graphCount + "'s connected components: \n");
                    graphCount++;

                    System.out.println("\tBreadth First Search: ");
                    BFS();

                    System.out.println("\tDepth First Search: ");
                    DFS();
                }
            }
        }
    }
}
