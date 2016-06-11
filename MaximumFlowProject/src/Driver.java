public class Driver {
    public static void main(String[] args) {
        FordFulkerson<String> ff = new FordFulkerson<String>();
        ff.addEdge("Los Angeles", "San Jose", 1);
        ff.addEdge("San Jose", "Sacramento", 1);
        ff.addEdge("San Jose", "San Francisco", 1);
        ff.addEdge("Dallas", "San Francisco", 1);
        ff.addEdge("Austin", "Phoenix", 1);
        
        Vertex<String> from = ff.getVertex("Los Angeles");
        Vertex<String> to = ff.getVertex("San Francisco");

        // from.showAdjList();
        // ff.showAdjTable();

        System.out.println("Finding path from " + from.data.toString() + " to " + to.data.toString() + ": " + ff.hasAugmentingPath(from, to));

        if(ff.containsEdge("San Jose", "San Francisco")) {
            System.out.println("Contains San Francisco!");
        } else {
            System.out.println("Does not contain San Francisco!");
        }

        Vertex<String> temp = ff.getVertex("San Jose");
        System.out.println("Removing the edge from " + temp.data.toString() + " to " + to.data.toString() + " from graph");
        if(ff.remove("San Jose", "San Francisco")) {
            System.out.println("Removed San Francisco.");
        } else {
            System.out.println("Did not remove San Francisco.");
        }

        if(ff.containsEdge("San Jose", "San Francisco")) {
            System.out.println("Contains San Francisco!");
        } else {
            System.out.println("Does not contain San Francisco!");
        }

        System.out.println("Undoing remove...");
        ff.undoRemove();
        if(ff.containsEdge("San Jose", "San Francisco")) {
            System.out.println("Contains San Francisco!");
        } else {
            System.out.println("Does not contain San Francisco!");
        }
    }
}

