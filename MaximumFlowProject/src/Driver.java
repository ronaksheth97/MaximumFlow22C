public class Driver {
    public static void main(String[] args) {
        FordFulkerson<String> ff = new FordFulkerson<String>();
        ff.addEdge("Los Angeles", "San Jose", 1);
        ff.addEdge("San Jose", "Sacramento", 1);
        ff.addEdge("San Jose", "San Francisco", 1);
        
        Vertex<String> from = ff.getVertex("Los Angeles");
        Vertex<String> to = ff.getVertex("San Francisco");

        // from.showAdjList();
        // ff.showAdjTable();

        System.out.println("Finding path from " + from.data.toString() + " to " + to.data.toString() + ": " + ff.hasAugmentingPath(from, to));
    }
}
