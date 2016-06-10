public class Driver {
    public static void main(String[] args) {
        FordFulkerson<String> ff = new FordFulkerson<String>();
        ff.addEdge("Los Angeles", "San Jose", 1);
        ff.addEdge("San Jose", "Sacramento", 1);
        ff.addEdge("San Jose", "San Francisco", 1);
        ff.addEdge("Dallas", "San Francisco", 1);
        ff.addEdge("Austin", "Phoenix", 1);
        
        Vertex<String> from = ff.getVertex("Los Angeles");
        Vertex<String> to = ff.getVertex("Phoenix");

        // from.showAdjList();
        // ff.showAdjTable();

        System.out.println("Finding path from " + from.data.toString() + " to " + to.data.toString() + ": " + ff.hasAugmentingPath(from, to));

        Vertex<String> temp = ff.getVertex("Austin");
        if(ff.contains("Austin")) {
            System.out.println("Contains Austin!");
        } else {
            System.out.println("Does not contain Austin!");
        }

        System.out.println("Removing the node from " + temp.data.toString() + " to " + to.data.toString() + " from graph");
        if(ff.remove("Austin", "Phoenix")) {
            System.out.println("Removed Austin.");
        } else {
            System.out.println("Did not remove Austin.");
        }

        if(ff.contains("Austin")) {
            System.out.println("Contains Austin!");
        } else {
            System.out.println("Does not contain Austin!");
        }

        System.out.println("Undoing remove...");
        ff.undoRemove();
        if(ff.contains("Austin")) {
            System.out.println("Contains Austin!");
        } else {
            System.out.println("Does not contain Austin!");
        }
    }
}
