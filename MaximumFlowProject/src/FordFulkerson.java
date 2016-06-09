import java.util.*;
import java.util.Map.Entry;

// TO DO:
// 1. fix hasAugmentingPath (done)
// 2. add undoRemoveStack and implement the removeEdge functions by calling super()
// 3. write FordFulkerson code

// CHANGES:
// 1. Fixed iterator
// 2. Changed paths and currPath types
// 3. Added exceptions for Edge class

public class FordFulkerson<E> extends Graph<E> {
    private int maxFlow;
    private Vertex<E> source;
    private Vertex<E> sink;
    // private HashMap<E, Edge<E>> edgeSet;
    private List<List<Vertex<E>>> paths;
    private List<Vertex<E>> currPath;
    private LinkedStack<Vertex<E>> undoRemoveStack;

    public FordFulkerson(Vertex<E> _source, Vertex<E> _sink) {
        super();
        maxFlow = 0;
        source = _source;
        sink = _sink;

        // applyFordFulkerson();
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

    public List<List<Vertex<E>>> getPaths() {
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
        currPath = new LinkedList<Vertex<E>>();
        hasAugmentingPathRecursive(source, sink);
        
        return !paths.isEmpty();
    }

    private void hasAugmentingPathRecursive(Vertex<E> source, Vertex<E> sink) {
        currPath.add(source);

        if(source.equals(sink)) {
            paths.add(new LinkedList<Vertex<E>>(currPath));
            currPath.remove(source);
            return;
        }

        Iterator<Map.Entry<E, Pair<Vertex<E>, Double>>> iterator = source.iterator();

        while(iterator.hasNext()) {
            Vertex<E> edge = iterator.next().getValue().first;
            if(!edge.isVisited()) {
                edge.visit();
                hasAugmentingPathRecursive(edge, sink);
            }
        }

        currPath.remove(source);
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
	    Vertex<E> from, to;
	    int maxFlow;
        int currFlow;
	
	    Edge( Vertex<E> _from, Vertex<E> _to, int _maxFlow) {
	        from = _from;
	        to = _to;
	        maxFlow = _maxFlow;
            currFlow = 0;
	    }

        public void setCurrFlow(Vertex<E> v, int amount) {
            if(v.equals(from)) {
                currFlow -= amount;
            } else if(v.equals(to)) {
                currFlow += amount;
            }
        }

        public Vertex<E> getOpposite(Vertex<E> v) {
            if(v.equals(from)) {
                return to;
            }

            if(v.equals(to)) {
                return from;
            }
            
            throw new IllegalArgumentException("ERROR: Invalid vertex argument. The given vertex is not connected to this edge.");
        }
	     
	    public String toString(){
            return "Edge: " + from.getData() + " to " + to.getData() + ", max capacity: " + maxFlow + ", current capacity: " + currFlow;
	    }
	     
	    public int compareTo(Edge<E> rhs) {
	        return (maxFlow < rhs.maxFlow? -1 : maxFlow > rhs.maxFlow? 1 : 0);
	    }

        public int getResidualCapacity(Vertex<E> v) {
            if(v.equals(from)) {
                return currFlow;
            }

            if(v.equals(to)) {
                return maxFlow - currFlow;
            }

            throw new IllegalArgumentException("ERROR: Invalid vertex argument. The given vertex is not connected to this edge.");
        }
        
        
    }
}
