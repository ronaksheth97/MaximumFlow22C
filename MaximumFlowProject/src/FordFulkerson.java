import java.util.*;
import java.util.Map.Entry;

// TO DO:
// 1. fix hasAugmentingPath (DONE)
// 2. add undoRemoveStack and implement the removeEdge functions by calling super() (DONE)
// 3. add edgeSet functionality (DONE)
// 4. write FordFulkerson code

// CHANGES:
// 1. successfully computes maximum flow but uses a greedy approach (not the same as FF)

public class FordFulkerson<E> extends Graph<E> {
    private int maxFlow;
    private HashMap<Vertex<E>, LinkedList<Edge<E>>> edgeTable; // separate chaining
    private LinkedStack<Pair<Vertex<E>, Edge<E>>> undoRemoveStack;
    private LinkedList<LinkedList<Vertex<E>>> paths;

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
        paths = new LinkedList<LinkedList<Vertex<E>>>();
        edgeTable = new HashMap<Vertex<E>, LinkedList<Edge<E>>>();
        undoRemoveStack = new LinkedStack<Pair<Vertex<E>, Edge<E>>>();
    }

    public int getMaxFlow() {
        return maxFlow;
    }

    public Vertex<E> getVertex(E key){
    	return vertexSet.get(key);
    }

    public boolean containsVertex(E key) {
        return vertexSet.get(key) != null;
    }

    public boolean containsEdge(E source, E dest) {
        Vertex<E> sourceVertex = vertexSet.get(source);
        Vertex<E> destVertex = vertexSet.get(dest);

        LinkedList<Edge<E>> edgeList = edgeTable.get(sourceVertex);
        if(edgeList == null) {
            throw new IllegalArgumentException("ERROR: The given vertex has no associated edges.");
        }

        Iterator<Edge<E>> iterator = edgeList.iterator();
        while(iterator.hasNext()) {
            Edge<E> edge = iterator.next();
            if(edge.from.equals(sourceVertex) && edge.to.equals(destVertex)) {
                return true;
            }
        }

        return false;
    }

    public Edge<E> getEdge(Vertex<E> source, Vertex<E> dest) {
        LinkedList<Edge<E>> edgeList = edgeTable.get(source);
        if(edgeList == null) {
            throw new IllegalArgumentException("ERROR: The given vertex has no associated edges.");
        }

        Iterator<Edge<E>> iterator = edgeList.iterator();
        while(iterator.hasNext()) {
            Edge<E> edge = iterator.next();
            if(edge.from.equals(source) && edge.to.equals(dest)) {
                return edge;
            }
        }

        return null;
    }

    public void clear() {
        super.clear();
        paths.clear();
        edgeTable.clear();

        while(undoRemoveStack.peek() != null) {
            undoRemoveStack.pop();
        }
        maxFlow = 0;
    }

    public void addEdge(E source, E dest, int capacity) {
        super.addEdge(source, dest, maxFlow);

        Vertex<E> from = getVertex(source);
        Vertex<E> to = getVertex(dest);

        addEdgeHelper(from, to, capacity);
        // addEdgeHelper(to, from, capacity);
    }

    private void addEdgeHelper(Vertex<E> key, Vertex<E> dest, int capacity) {
        LinkedList<Edge<E>> edgeList;
        Edge<E> edge = new Edge<E>(key, dest, capacity);
        if(!edgeTable.containsKey(key)) {
            edgeList = new LinkedList<Edge<E>>();
            edgeList.add(edge);
            edgeTable.put(key, edgeList);
        } else {
            edgeList = edgeTable.get(key);
            edgeList.add(edge);
        }
    }

    public boolean remove(E start, E end) {
        Vertex<E> source = getVertex(start);
        if(source == null) {
            throw new IllegalArgumentException("ERROR: The element to be removed is null or does not exist in the graph.");
        }

        Vertex<E> dest = getVertex(end);
        if(dest == null) {
            throw new IllegalArgumentException("ERROR: The source vertex of the element to be removed is null or does not exist in the graph.");
        }

        removeHelper(source, dest);
        removeHelper(dest, source);
        
        return super.remove(start, end);
    }

    private void removeHelper(Vertex<E> source, Vertex<E> dest) {
        LinkedList<Edge<E>> tempList = edgeTable.get(source);
        if(tempList.isEmpty() || tempList == null) {
            throw new NullPointerException("ERROR: Edge associated with the vertex does not exist.");
        }

        Iterator<Edge<E>> iterator = tempList.iterator();
        while(iterator.hasNext()) {
            Edge<E> edge = iterator.next();
            if(edge.from.equals(source) && edge.to.equals(dest)) {
                undoRemoveStack.push(new Pair<Vertex<E>, Edge<E>>(source, new Edge<E>(edge.from, edge.to, edge.maxFlow)));
                iterator.remove();
            }
        }
    }

    public void undoRemove() {
        if(undoRemoveStack.peek() == null) {
            throw new NullPointerException("ERROR: The undo stack is null. There is nothing to undo.");
        }

        Pair<Vertex<E>, Edge<E>> undo = undoRemoveStack.pop();
        Vertex<E> vertex = undo.first;
        Edge<E> edge = undo.second;
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

        paths.clear();
        unvisitVertices();
        hasAugmentingPathRecursive(source, sink, new LinkedList<Vertex<E>>());

        return sink.isVisited();
    }

    private void hasAugmentingPathRecursive(Vertex<E> source, Vertex<E> sink, LinkedList<Vertex<E>> currPath) {
        currPath.add(source);
        source.visit();

        if(source.equals(sink)) {
            paths.add(new LinkedList<Vertex<E>>(currPath));
            currPath.remove(source);
            return;
        }

        LinkedList<Edge<E>> edgeList = edgeTable.get(source);
        Iterator<Edge<E>> iterator = edgeList.iterator();
        while(iterator.hasNext()) {
            Edge<E> edge = iterator.next();
            Vertex<E> opposite = edge.getOpposite(source);
            if(!currPath.contains(opposite) && edge.getResidualCapacity(opposite) > 0) {
                hasAugmentingPathRecursive(opposite, sink, currPath);
            }
        }

        currPath.remove(source);
    }

    public void applyFordFulkerson(Vertex<E> source, Vertex<E> sink) {
        maxFlow = 0;
        if(hasAugmentingPath(source, sink)) {
            Iterator<LinkedList<Vertex<E>>> listIterator = paths.iterator();

            while(listIterator.hasNext()) {
                LinkedList<Vertex<E>> path = listIterator.next();
                LinkedList<Vertex<E>> flowPath = new LinkedList<Vertex<E>>(path);
                Iterator<Vertex<E>> pathIterator = path.iterator();

                // LinkedList<Edge<E>> edgeList = new LinkedList<Edge<E>>();
                Vertex<E> prev = null, curr;
                int bottleneck = Integer.MAX_VALUE;
                while(pathIterator.hasNext()) {
                    curr = pathIterator.next();

                    System.out.print(curr.data.toString());
                    if(pathIterator.hasNext()) {
                        System.out.print(" -> ");
                    }

                    if(prev != null) {
                        Edge<E> edge = getEdge(prev, curr);
                        if(edge != null) {
                            bottleneck = Math.min(bottleneck, edge.getResidualCapacity(curr));
                        }
                    }

                    prev = curr;
                }

                // repeat but this time make each currFlow equal to the bottleneck
                prev = null;
                curr = null;
                Iterator<Vertex<E>> flowPathIterator = flowPath.iterator();
                while(flowPathIterator.hasNext()) {
                    curr = flowPathIterator.next();

                    if(prev != null) {
                        Edge<E> edge = getEdge(prev, curr);
                        if(edge != null) {
                            edge.setCurrFlow(curr, bottleneck);
                        }
                    }

                    prev = curr;
                }

                System.out.print(". Bottleneck: " + bottleneck + "\n");
                maxFlow += bottleneck;
            }
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
                if((currFlow - amount) >= 0) {
                    currFlow -= amount;
                } else {
                    currFlow = 0;
                }
            } else if(v.equals(to)) {
                if((currFlow + amount) <= maxFlow) {
                    currFlow += amount;
                } else {
                    currFlow = maxFlow;
                }
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

