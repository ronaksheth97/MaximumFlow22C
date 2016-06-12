public class Driver {
    public static void main(String[] args) {
        FordFulkerson<String> ff = new FordFulkerson<String>();
        ff.addEdge("s", "a", 9);
        ff.addEdge("a", "c", 8);
        ff.addEdge("c", "t", 10);
        ff.addEdge("s", "b", 9);
        ff.addEdge("b", "d", 3);
        ff.addEdge("d", "t", 7);
        ff.addEdge("a", "b", 10);
        ff.addEdge("b", "c", 1);
        ff.addEdge("d", "c", 8);
        ff.addEdge("f", "g", 10);
        
        Vertex<String> from = ff.getVertex("s");
        Vertex<String> to = ff.getVertex("t");

        System.out.println("Path from " + from.data.toString() + " to " + to.data.toString() + ": " + ff.hasAugmentingPath(from, to));

        System.out.println();
        ff.applyFordFulkerson(from, to);
        System.out.println("Max flow from s to t: " + ff.getMaxFlow()); // should be 12
    }
}

