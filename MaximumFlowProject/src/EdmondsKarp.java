import java.util.*;
import java.util.Map.Entry;

public class EdmondsKarp<E> extends Graph<E> {
    private int maxFlow;
    private HashMap<Vertex<E>, LinkedList<Edge<E>>> edgeTable; // separate chaining
    private LinkedStack<Pair<Vertex<E>, Edge<E>>> undoRemoveStack;
    private HashMap<Vertex<E>, Edge<E>> backwardsTable;
    
    public EdmondsKarp(){
    	super();
    	maxFlow = 0;
        edgeTable = new HashMap<Vertex<E>, LinkedList<Edge<E>>>();
        backwardsTable = new HashMap<Vertex<E>, Edge<E>>();
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

        LinkedList<Edge<E>> edgeList;
        Edge<E> edge = new Edge<E>(from, to, capacity);
        if(!edgeTable.containsKey(from)) {
            edgeList = new LinkedList<Edge<E>>();
            edgeList.add(edge);
            edgeTable.put(from, edgeList);
        } else {
            edgeList = edgeTable.get(from);
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
        
        return super.remove(start, end);
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

    // uses a breadth-first traversal to find the shortest path (based on the number of nodes)
    public boolean hasAugmentingPath(Vertex<E> source, Vertex<E> sink) {
        if(source.equals(sink)) {
            throw new IllegalArgumentException("ERROR: Invalid argument. The source must be a different node than the sink.");
        }

        LinkedQueue<Vertex<E>> queue = new LinkedQueue<Vertex<E>>();
        queue.enqueue(source);
        source.visit();
        while(!queue.isEmpty() && !sink.isVisited()) {
            Vertex<E> vertex = queue.dequeue();

            Iterator<Map.Entry<E, Pair<Vertex<E>, Double>>> iterator = vertex.iterator();


		    while(iterator.hasNext()) {
			    Entry<E, Pair<Vertex<E>, Double>> entry = iterator.next();
			    Vertex<E> neighbor = entry.getValue().first;
                Edge<E> edge = getEdge(vertex, neighbor);
			    if(!neighbor.isVisited() && edge.getResidualCapacity(neighbor) > 0) {
				    queue.enqueue(neighbor);
				    neighbor.visit();
                    backwardsTable.put(neighbor, edge);
                    System.out.println("Key: " + neighbor.data.toString() + ". Data: Edge from " + edge.from.data.toString() + " to " + edge.to.data.toString());
			    }
		    }
        }
        

        return sink.isVisited();
    }

    public void applyEdmondsKarp(Vertex<E> source, Vertex<E> sink) {
        maxFlow = 0;
        while(hasAugmentingPath(source, sink)) {
            int bottleneck = Integer.MAX_VALUE;
            for(Vertex<E> vertex = sink; !vertex.equals(source); vertex = backwardsTable.get(vertex).getOpposite(vertex)) {
                bottleneck = Math.min(bottleneck, backwardsTable.get(vertex).getResidualCapacity(vertex));
            }

            for(Vertex<E> vertex = sink; !vertex.equals(source); vertex = backwardsTable.get(vertex).getOpposite(vertex)) {
                backwardsTable.get(vertex).setCurrFlow(vertex, bottleneck);
            }

            maxFlow += bottleneck;
        }
    }

    class Edge<E> implements Comparable<Edge<E>> {
	    Vertex<E> from, to;
	    int maxFlow;
        int currFlow;
	
	    Edge(Vertex<E> _from, Vertex<E> _to, int _maxFlow) {
            if(_from == null) {
                throw new IllegalArgumentException("ERROR: Source vertex must not be null.");
            }

            if(_to == null) {
                throw new IllegalArgumentException("ERROR: Destination vertex must not be null.");
            }

            if(_maxFlow <= 0) {
                throw new IllegalArgumentException("ERROR: Maximum flow must be a positive number.");
            }

            if(_from.equals(_to)) {
                throw new IllegalArgumentException("ERROR: No self-looping edges allowed.");
            }

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
            
            throw new IllegalArgumentException("ERROR: Invalid vertex argument. The given vertex is not connected to the edge.");
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

