import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

class VertexNeighbor {
	public int vertexNumber;
	public VertexNeighbor next;

	public VertexNeighbor(int vertexNum, VertexNeighbor nVertex) {
		this.vertexNumber = vertexNum;
		next = nVertex;
	}
}

class Vertices {
	String name;
	VertexNeighbor adjacentList;

	Vertices(String name, VertexNeighbor neighbors) {
		this.name = name;
		this.adjacentList = neighbors;
	}
}

public class DAG {

	Vertices[] adjacentList;


	public DAG(String textFile) throws FileNotFoundException {

		Scanner scannedVertex = new Scanner(new File(textFile));
		adjacentList = new Vertices[scannedVertex.nextInt()];

		for (int i = 0; i < adjacentList.length; i++) {
			adjacentList[i] = new Vertices(scannedVertex.next(), null);
		}

		while (scannedVertex.hasNext()) {
			int A = indexNaming(scannedVertex.next());
			int B = indexNaming(scannedVertex.next());

			adjacentList[A].adjacentList = new VertexNeighbor(B, adjacentList[A].adjacentList);

		}
	}

	int indexNaming(String name) {
		for (int i = 0; i < adjacentList.length; i++) {
			if (adjacentList[i].name.equals(name)) {
				return i;
			}
		}
		return -1;
	}

	public int[] dfsTopPublic() {
		boolean[] visitedVertex = new boolean[adjacentList.length];
		int[] vertexDeltNumber = new int[adjacentList.length];
		int largestDeltOut = adjacentList.length - 1;
		for (int i = 0; i < visitedVertex.length; i++) {
			if (!visitedVertex[i]) {
				largestDeltOut = topDfsSort(i, visitedVertex, vertexDeltNumber, largestDeltOut);
			}
		}
		return vertexDeltNumber;
	}

	private int topDfsSort(int vertex, boolean[] visited, int[] vertexDeltNumber, int largestDeltOut) {
		visited[vertex] = true;
		for (VertexNeighbor nVertex = adjacentList[vertex].adjacentList; nVertex != null; nVertex = nVertex.next) {
			if (!visited[nVertex.vertexNumber]) {
				largestDeltOut = topDfsSort(nVertex.vertexNumber, visited, vertexDeltNumber, largestDeltOut);
			}
		}
		vertexDeltNumber[largestDeltOut] = vertex;
		return largestDeltOut - 1;
	}

	public static void main(String[] args) throws IOException {
		Scanner scanPathToFile = new Scanner(System.in);
		System.out.print("Enter graph path without quotation \nfor example C:\\Users\\jehad\\Desktop\\topsort_graph.txt: " );
		String graphFile = scanPathToFile.nextLine();
		DAG graph = new DAG(graphFile);

		System.out.println("Toplogical sort: ");
		int[] sortResult = graph.dfsTopPublic();
		System.out.print(graph.adjacentList[sortResult[0]].name);
		for (int i = 1; i < sortResult.length; i++) {
			System.out.print(", " + graph.adjacentList[sortResult[i]].name);
		}
		System.out.println();
		scanPathToFile.close();
	}
}
