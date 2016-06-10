import java.util.*;
import java.util.Map.Entry;

// TO DO:
// 1. fix hasAugmentingPath (DONE)
// 2. add undoRemoveStack and implement the removeEdge functions by calling super() (DONE)
// 3. add edgeSet functionality (DONE)
// 4. write FordFulkerson code

// CHANGES:
// 1. Added undoRemoveStack functionality
// 2. Added edgeSet functionality

public class FordFulkerson<E> extends Graph<E> {
    private int maxFlow;
    private HashMap<Vertex<E>, Edge<E>> edgeSet;
    private List<List<Vertex<E>>> paths;
    private LinkedStack<Pair<Vertex<E>, Edge<E>>> undoRemoveStack;

    /*
    public FordFulkerson(Vertex<E> _source, Vertex<E> _sink) {
        super();
        maxFlow = 0;

        // applyFordFulkerson(_source, _sink);
    }
    */
    
    public FordFulkerson(){
    	super();
    	maxFlow = 0;
        edgeSet = new HashMap<Vertex<E>, Edge<E>>();
        undoRemoveStack = new LinkedStack<Pair<Vertex<E>, Edge<E>>>();
    }

    public int getMaxFlow() {
        return maxFlow;
    }

    public List<List<Vertex<E>>> getPaths() {
        return paths;
    }
    
    public Vertex<E> getVertex(E key){
    	return vertexSet.get(key);
    }

    public boolean contains(E key) {
        return edgeSet.containsKey(vertexSet.get(key));
    }

    public void clear() {
        super.clear();
        edgeSet.clear();
        paths.clear();
        while(undoRemoveStack.peek() != null) {
            undoRemoveStack.pop();
        }
        maxFlow = 0;
    }

    public void addEdge(E source, E dest, int maxFlow) {
        super.addEdge(source, dest, maxFlow);
        Vertex<E> from = getVertex(source);
        Vertex<E> to = getVertex(dest);
        edgeSet.put(from, new Edge<E>(from, to, maxFlow));
    }

    public boolean remove(E start, E end) {
        Vertex<E> from = getVertex(start);
        if(from == null) {
            throw new IllegalArgumentException("ERROR: The element to be removed is null or does not exist in the graph.");
        }

        Edge<E> temp = edgeSet.get(from);
        if(temp == null) {
            throw new NullPointerException("ERROR: Edge associated with the vertex does not exist.");
        }

        edgeSet.remove(from);
        undoRemoveStack.push(new Pair<Vertex<E>, Edge<E>>(from, temp));
        return super.remove(start, end);
    }

    public void undoRemove() {
        if(undoRemoveStack.peek() == null) {
            throw new NullPointerException("ERROR: The undo stack is null. There is nothing to undo.");
        }

        Pair<Vertex<E>, Edge<E>> undo = undoRemoveStack.pop();
        Vertex<E> vertex = undo.first;
        Edge<E> edge = undo.second;
        edgeSet.put(vertex, edge);
        addEdge(vertex.data, edge.to.data, edge.maxFlow);
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
        
        return hasAugmentingPathRecursive(source, sink, new LinkedList<Vertex<E>>());
    }

    private boolean hasAugmentingPathRecursive(Vertex<E> source, Vertex<E> sink, List<Vertex<E>> currPath) {
        currPath.add(source);

        if(source.equals(sink)) {
            paths.add(new LinkedList<Vertex<E>>(currPath));
            currPath.remove(source);
            return true;
        }

        Iterator<Map.Entry<E, Pair<Vertex<E>, Double>>> iterator = source.iterator();

        while(iterator.hasNext()) {
            Vertex<E> edge = iterator.next().getValue().first;
            if(!edge.isVisited()) {
                edge.visit();
                return hasAugmentingPathRecursive(edge, sink, currPath);
            }
        }

        currPath.remove(source);
        return false;
    }

    public void applyFordFulkerson(Vertex<E> source, Vertex<E> sink) {
        paths.clear();
        if(hasAugmentingPath(source, sink)) {
            // iterate through all paths and compute flow values
        }

    }

    class Edge<E> implements Comparable<Edge<E>> {
	    Vertex<E> from, to;
	    int maxFlow;
        int currFlow;
	
	    Edge(Vertex<E> _from, Vertex<E> _to, int _maxFlow) {
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
