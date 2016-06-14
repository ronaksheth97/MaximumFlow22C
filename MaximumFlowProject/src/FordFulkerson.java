// Name: FordFulkerson.java
// Last Date Modified: June 12, 2016
// Purpose: Finds the maximum flow between a source and sink node using the Ford-Fulkerson algorithm by finding all augmenting paths and pushing flow.

import java.util.*;
import java.util.Map.Entry;

public class FordFulkerson<E> extends Graph<E> {
    private int maxFlow; // maximum flow from the given source to node
    private HashMap<Vertex<E>, LinkedList<Edge<E>>> edgeTable; // stores all the adjacent edges to a node using separate chaining
    private LinkedStack<Pair<Vertex<E>, Edge<E>>> undoRemoveStack; // stack for storing removed elements in the case for undoing
    private LinkedList<LinkedList<Vertex<E>>> paths; // list of all paths from source to vertex
	private int maxEdges = 0;
    
    /* FordFulkerson()
     * initializes a graph class which has the capability to find the maximum flow from a source to sink in the graph
     */
    public FordFulkerson(){
    	super();
    	maxFlow = 0;
        paths = new LinkedList<LinkedList<Vertex<E>>>();
        edgeTable = new HashMap<Vertex<E>, LinkedList<Edge<E>>>();
        undoRemoveStack = new LinkedStack<Pair<Vertex<E>, Edge<E>>>();
    }

    /* int getMaxFlow()
     * returns the maximum flow from the last source to sink
     * @return int - the maximum flow from source to sink
     */
    public int getMaxFlow() {
        return maxFlow;
    }

    /* Vertex<E> getVertex(E key)
     * returns the vertex associated with the given data
     * @return Vertex<E> - returns the vertex associated with the data E key
     */
    public Vertex<E> getVertex(E key){
    	return vertexSet.get(key);
    }

    /* boolean containsVertex(E key)
     * checks if the graph contains a given vertex
     * @return boolean - returns true if the graph contains the vertex associated with data E key and false if not
     */
    public boolean containsVertex(E key) {
        return vertexSet.get(key) != null;
    }

    /* boolean containsEdge(E source, E dest)
     * checks if the graph contains an edge from the vertex associated with source to the vertex associated with dest
     * @return boolean - returns true if there is an existing edge and else if not
     */
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

    /* Edge<E> getEdge(Vertex<E> source, Vertex<E> dest)
     * returns the edge associated with a source node and a destination node
     * @return boolean - returns the edge if there is an existing edge from source to dest and null if there isn't
     */
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

    /* void clear()
     * clears the graph of all vertices, edges, and paths
     */
    public void clear() {
        super.clear();
        paths.clear();
        edgeTable.clear();

        while(undoRemoveStack.peek() != null) {
            undoRemoveStack.pop();
        }
        maxFlow = 0;
		maxEdges = 0;
    }

    /* void addEdge(E source, E dest, int capacity)
     * adds an edge from the vertex associated with source to the vertex associated with dest with the given capacity
     * @param source   - the data of the source vertex
     * @param dest     - the data of the destination vertex
     * @param capacity - the maximum flow of the edge
     */
    public void addEdge(E source, E dest, int capacity) {
        super.addEdge(source, dest, capacity); // typo problem fixed: it was super.addEdge(source, dest, maxFlow), 
        					// so capacity for all edges are showed 0.0 at console and ui diplay box

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
		
		if(maxEdges < edgeList.size()) {
			maxEdges = edgeList.size();
		}
    }

    /* booleam remove(E start, E end)
     * removes the edge associated with the vertices associated with start and end
     * @param start - the data of the start vertex
     * @param end   - the data of the end vertex
     */
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
		
		if(tempList.size() > maxEdges) {
			maxEdges = tempList.size();
		}
        
        return super.remove(start, end);
    }

    /* void undoRemove()
     * adds back the last removed vertex
     */
    public Edge<E> undoRemove() {
        if(undoRemoveStack.peek() == null) {
			return null;
        }

        Pair<Vertex<E>, Edge<E>> undo = undoRemoveStack.pop();
        Vertex<E> vertex = undo.first;
        Edge<E> edge = undo.second;
        addEdge(vertex.data, edge.to.data, edge.maxFlow);
		return edge;
    }

    /* boolean hasAugmentingPath(Vertex<E> source, Vertex<E> sink)
     * gets all paths from the given source to the given sink using a depth-first search approach
     * @param source   - the source of the flow network
     * @param sink     - the sink of the flow network
     * @return boolean - returns true if there is at least one path from the source to the sink
     */
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

        unvisitVertices();
		try {
			hasAugmentingPathRecursive(source, sink, new LinkedList<Vertex<E>>());
		} catch(NullPointerException e) {
			throw new NullPointerException("ERROR: Source and sink must have a path connecting them.");
		}

        return sink.isVisited();
    }

    /* boolean hasAugmentingPath(Vertex<E> source, Vertex<E> sink, LinkedList<Vertex<E>> currPath)
     * performs a depth-first search from source to sink to find a path
     * @param source   - the source of the flow network
     * @param sink     - the sink of the flow network
     * @param currPath - the current path from source to sink
     */
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

    /* void applyFordFulkerson(Vertex<E> source, Vertex<E> sink)
     * finds the maximum flow from source to sink using the Ford-Fulkerson algorithm
     * @param source   - the source of the flow network
     * @param sink     - the sink of the flow network
     */
    public void applyFordFulkerson(Vertex<E> source, Vertex<E> sink) {
        maxFlow = 0;
		if(!paths.isEmpty()) {
			clearFlowValues();
		}
		
        if(hasAugmentingPath(source, sink)) {
            Iterator<LinkedList<Vertex<E>>> listIterator = paths.iterator();

            while(listIterator.hasNext()) {
                // gets the bottleneck capacity of a given edge
                LinkedList<Vertex<E>> path = listIterator.next();
                LinkedList<Vertex<E>> flowPath = new LinkedList<Vertex<E>>(path);
                Iterator<Vertex<E>> pathIterator = path.iterator();

                Vertex<E> prev = null, curr;
                int bottleneck = Integer.MAX_VALUE;
                while(pathIterator.hasNext()) {
                    curr = pathIterator.next();

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

                maxFlow += bottleneck;
            }
        }
    }
	
	private void clearFlowValues() {
        Iterator<LinkedList<Vertex<E>>> listIterator = paths.iterator();

        while(listIterator.hasNext()) {
			LinkedList<Vertex<E>> path = listIterator.next();
            LinkedList<Vertex<E>> flowPath = new LinkedList<Vertex<E>>(path);
            Iterator<Vertex<E>> pathIterator = path.iterator();

            Vertex<E> prev = null, curr;
            while(pathIterator.hasNext()) {
                curr = pathIterator.next();

                if(prev != null) {
                    Edge<E> edge = getEdge(prev, curr);
                    if(edge != null) {
						edge.currFlow = 0;
                     }
                }
				prev = curr;
            }
		}
	}

    /* String getPathsToString()
     * returns a string containing all the given paths from source to sink
     * @return String - returns a string containing all the paths
     */
    public String getPathsToString() {
        String stringPath = "";
        if(!paths.isEmpty()) {
            Iterator<LinkedList<Vertex<E>>> listIterator = paths.iterator();

            while(listIterator.hasNext()) {
                LinkedList<Vertex<E>> path = listIterator.next();
                LinkedList<Vertex<E>> flowPath = new LinkedList<Vertex<E>>(path);
                Iterator<Vertex<E>> pathIterator = path.iterator();

                Vertex<E> prev = null, curr;
                int bottleneck = Integer.MAX_VALUE;
                while(pathIterator.hasNext()) {
                    curr = pathIterator.next();

                    stringPath += curr.data.toString();
                    if(pathIterator.hasNext()) {
                        stringPath += " -> ";
                    }

                    if(prev != null) {
                        Edge<E> edge = getEdge(prev, curr);
                        if(edge != null) {
                            bottleneck = Math.min(bottleneck, edge.getResidualCapacity(curr));
                        }
                    }

                    prev = curr;
                }

                if(listIterator.hasNext()) {
                    stringPath += "\n";
                }
            }
        } else {
			throw new IllegalStateException("ERROR: The paths may only be displayed after computing for the maximum flow in a non-empty graph.");
		}

		
        return stringPath;
    }
    
    public String displayAdjacencyList() {
		if(vertexSet.isEmpty()) {
			throw new IllegalStateException("ERROR: Cannot display adjacency list for an empty list.");
		}
		
    	String matrix = "";
		
        matrix += (String.format("%-25s", "VERTEX") + "|| ADJACENT VERTEX/VERTICES\n");
    	
		Iterator<Map.Entry<E, Vertex<E>>> adjacencyIterator = vertexSet.entrySet().iterator();
		while(adjacencyIterator.hasNext()) {
			Map.Entry<E, Vertex<E>> mapEntry = adjacencyIterator.next();
        	matrix += String.format("%-25s", mapEntry.getValue().data.toString());
        	matrix += "|| ";
			HashMap<E, Pair<Vertex<E>, Double>> adjMap = mapEntry.getValue().adjList;
			Iterator<Map.Entry<E, Pair<Vertex<E>, Double>>> hashIterator = adjMap.entrySet().iterator();
			for(int i = 0; i < maxEdges; ++i) {
        		if(hashIterator.hasNext()) {
					Map.Entry<E, Pair<Vertex<E>, Double>> vertexEntry = hashIterator.next();
					Vertex<E> vertex = vertexEntry.getValue().first;
					String temp = vertex.data.toString() + " (" + vertexEntry.getValue().second.intValue() + ")";
        			matrix += String.format("%-24s", temp);
        		} else {
        			matrix += String.format("%-24s", "---");
        		}
        		matrix += "| ";
        	}
        	matrix += "\n";
		}
    	
    	return matrix;
    }

    // Basic class definition for an edge from vertex to vertex
    class Edge<E> implements Comparable<Edge<E>> {
	    Vertex<E> from, to; // source and destination vertices for an edge
	    int maxFlow; // capacity from source to destionation vertices
        int currFlow; // the amount of flow currently being pushed through the edge
	
        /* Edge(Vertex<E> _from, Vertex<E> _to, int _maxFlow)
         * initializes an Edge object
         * @param _from     - the source vertex
         * @param _to       - the destination vertex
         * @param _maxFlow  - the capacity of the edge
         * @param _currFlow - the amount of flow currently being pushed
         */
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

        /* setCurrFlow(Vertex<E> v, int amount)
         * gets the flow from the edge associated with this vertex. throws an exception for unconnected vertices
         * @param v      - the vertex associated with the edge
         * @param amount - the amount to be pushed or pulled from the edge
         */
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
            } else {
                throw new IllegalArgumentException("ERROR: The given vertex is not connected to this edge.");
            }
        }

        /* Vertex<E> getOpposite(Vertex<E> v)
         * gets the opposite vertex from the edge. throws an exception for unconnected vertices
         * @param v - the vertex whose opposite vertex shall be obtained
         */
        public Vertex<E> getOpposite(Vertex<E> v) {
            if(v.equals(from)) {
                return to;
            }

            if(v.equals(to)) {
                return from;
            }
            
            throw new IllegalArgumentException("ERROR: Invalid vertex argument. The given vertex is not connected to this edge.");
        }
	     
        /* String toString()
         * returns the String representation of the edge in string form
         * @return String - the edge in string form
         */
	    public String toString(){
            return "Edge: " + from.getData() + " to " + to.getData() + ", max capacity: " + maxFlow + ", current capacity: " + currFlow;
	    }
	     
        /* int compareTo(Edge<E> rhs)
         * compares two edges
         * @return int - returns -1 if this > rhs, 1 if rhs > this, and 0 if they are equal
         */
	    public int compareTo(Edge<E> rhs) {
	        return (maxFlow < rhs.maxFlow? -1 : maxFlow > rhs.maxFlow? 1 : 0);
	    }

        /* int getResidualCapacity(Vertex<E> v)
         * gets the residual flow of an edge based on the residual flow graph
         * @param v    - the vertex whose residual flow shall be obtained
         * @return int - the residual flow of the vertex v on the current edge
         */
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

