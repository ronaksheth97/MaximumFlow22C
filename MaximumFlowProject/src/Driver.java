public class Driver {
    public static class Direction {
        String from;
        String to;
        int capacity;

        public Direction(String _from, String _to, int _capacity) {
            from = _from;
            to = _to;
            capacity = _capacity;
        }
    }

    private static Direction[] test = { new Direction("San Francisco", "Redwood City", 12), 
                                        new Direction("Redwood City", "Palo Alto", 18),
                                        new Direction("Palo Alto", "Sunnyvale", 20),
                                        new Direction("Sunnyvale", "Santa Clara", 8),
                                        new Direction("Santa Clara", "San Jose", 200),
                                        new Direction("San Francisco", "Oakland", 9),
                                        new Direction("Redwood City", "Oakland", 6),
                                        new Direction("Oakland", "Palo Alto", 3),
                                        new Direction("Oakland", "Fremont", 4),
                                        new Direction("Fremont", "Milpitas", 12),
                                        new Direction("Sunnyvale", "Fremont", 11),
                                        new Direction("Sunnyvale", "Milpitas", 2),
                                        new Direction("Milpitas", "San Jose", 200) };
    private static int size = 13;

    public static void main(String[] args) {
        System.out.println("TESTING FORD-FULKERSON:");
        FordFulkerson<String> ff = new FordFulkerson<String>();
        for(int i = 0; i < size; i++) {
            ff.addEdge(test[i].from, test[i].to, test[i].capacity);
        }     

        long startTime, stopTime;
        double elapsedTime = 0;

        
        Vertex<String> from = ff.getVertex("San Francisco");
        Vertex<String> to = ff.getVertex("San Jose");

        startTime = System.nanoTime();
        System.out.println("Path from " + from.data.toString() + " to " + to.data.toString() + ": " + ff.hasAugmentingPath(from, to));
        ff.applyFordFulkerson(from, to);
        stopTime = System.nanoTime();  

        System.out.println("Max flow from SF to SJ using Ford-Fulkerson: " + ff.getMaxFlow()); // should be 12
        System.out.println("\nPaths:\n" + ff.getPathsToString());

        elapsedTime =(double)(stopTime - startTime)/1000000.0;
        System.out.println("\nFord-Fulkerson Time: " + elapsedTime + " milliseconds.");

        System.out.println("\n--------------------------------------------\nTESTING EDMONDS-KARP:");
        EdmondsKarp<String> ek = new EdmondsKarp<String>();
        for(int i = 0; i < size; i++) {
            ek.addEdge(test[i].from, test[i].to, test[i].capacity);
        }     

        startTime = 0;
        stopTime = 0;
        elapsedTime = 0;

        from = ek.getVertex("San Francisco");
        to = ek.getVertex("San Jose");

        startTime = System.nanoTime();
        System.out.println("Path from " + from.data.toString() + " to " + to.data.toString() + ": " + ek.hasAugmentingPath(from, to));
        ek.applyEdmondsKarp(from, to);
        stopTime = System.nanoTime();  

        System.out.println("Max flow from SF to SJ using Edmonds-Karp: " + ek.getMaxFlow()); // should be 12
        // System.out.println("\nPaths:\n" + ek.getPathsToString());

        elapsedTime =(double)(stopTime - startTime)/1000000.0;
        System.out.println("\nEdmonds-Karp Time: " + elapsedTime + " milliseconds.");
    }
}

