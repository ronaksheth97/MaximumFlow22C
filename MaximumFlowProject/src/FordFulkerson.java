import java.util.*;
import java.util.Map.Entry;

// TO DO:
// 1. add undoRemoveStack and implement the removeEdge functions by calling super()
// 2. write FordFulkerson code
// 3.

public class FordFulkerson<E> extends Graph<E> {
    private int maxFlow;
    private Vertex<E> source;
    private Vertex<E> sink;
    private HashMap<E, Edge<E>> edgeSet;
    private List<List<E>> paths;
    private LinkedStack<Vertex<E>> undoRemoveStack;

    public FordFulkerson(Vertex<E> _source, Vertex<E> _sink) {
        super();
        maxFlow = 0;
        source = _source;
        sink = _sink;

        applyFordFulkerson();
    }
    
    public FordFulkerson(){
    	super();
    	maxFlow = 0;
    	source = null;
    	sink = null;
    }

    public int getMaxFlow() {
        return maxFlow;
    }

    public List<List<E>> getPaths() {
        return paths;
    }

    public boolean hasAugmentingPath(Vertex<E> source, Vertex<E> sink) {
        if(source == null) {
            throw new NullPointerException("ERROR: The source parameter cannot be null.");
        }

        if(sink == null) {
            throw new NullPointerException("ERROR: The sink parameter cannot be null.");
        }

        if(source.equals(sink)) {
            throw new IllegalArgumentException("ERROR: The source and sink cannot be the same.");
        }

        paths = new ArrayList<List<Vertex<E>>>();
        hasAugmentingPathRecursive(source, sink, new LinkedHashSet<Vertex<E>>());
        
        return paths.isEmpty();
    }

    // use LinkedHashSet to allow for easy searching of elements within a path
    private void hasAugmentingPathRecursive(Vertex<E> source, Vertex<E> sink, LinkedHashSet<Vertex<E>> path) {
        path.add(source);

        if(source.equals(sink)) {
            paths.add(new LinkedList<Vertex<E>>(path));
            path.remove(source);
            return;
        }

        Iterator iterator = source.iterator();

        while(iterator.hasNext()) {
            Vertex<E> edge = iterator.next();
            if(!edge.isVisited()) {
                edge.visit();
                hasAugmentingPathRecursive(edge, sink, path);
            }
        }

        path.remove(source);
    }

    public void applyFordFulkerson() {
        while(hasAugmentingPath(source, sink)) {
            // iterate through all paths and compute flow values
        }
    }
    
    public void setSource(Vertex<E> _source){
    	source = _source;
    }
    
    public void setSink(Vertex<E> _sink){
    	sink = _sink;
    }
    
    public Vertex<E> getVertex(E name){
    	return vertexSet.get(name);
    }

    class Edge<E> implements Comparable<Edge<E>> {
	    Vertex<E> source, dest;
	    int maxFlow;
        int currFlow;
	
	    Edge( Vertex<E> _source, Vertex<E> _dest, int _maxFlow) {
	        source = _source;
	        dest = _dest;
	        maxFlow = _maxFlow;
            currFlow = 0;
	    }

        public void setCurrFlow(Vertex<E> v, int amount) {
            if(v.equals(source)) {
                currFlow -= amount;
            } else if(v.equals(dest)) {
                currFlow += amount;
            }
        }

        public Vertex<E> getOpposite(Vertex<E> v) {
            if(v.equals(source)) {
                return sink;
            }

            if(v.equals(sink)) {
                return source;
            }
        }
	     
	    public String toString(){
            return "Edge: " + source.getData() + " to " + dest.getData() + ", max capacity: " + maxFlow + ", current capacity: " + currFlow;
	    }
	     
	    public int compareTo( Edge<E> rhs ) {
	        return (cost < rhs.cost? -1 : cost > rhs.cost? 1 : 0);
	    }

        public int getResidualCapacity(Vertex<E> v) {
            if(v.equals(source)) {
                return currFlow;
            }

            if(v.equals(sink)) {
                return maxFlow - currFlow;
            }
        }
        
        
    }
}
