public class Driver {
    public static void main(String[] args) {
        FordFulkerson<String> ff = new FordFulkerson<String>();
        ff.addEdge("a", "b", 1);
        ff.addEdge("b", "c", 1);
        ff.addEdge("b", "d", 1);
        
        Vertex<String> from = ff.getVertex("a");
        Vertex<String> to = ff.getVertex("d");

        from.showAdjList();
        ff.showAdjTable();

        System.out.println(ff.hasAugmentingPath(from, to));
    }
}
