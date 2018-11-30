package org.dzhyrma.advent.week1.graph

import org.dzhyrma.advent.week1.graph.edge.Edge

/**
 * Main interface for all graphs.
 *
 * @param <V> type for vertices
 * @param <E> type for edges. Should implement Edge interface
 */
interface Graph<V, E: Edge<V>> {

	/**
	 * Returns a [Set] view of the vertices contained in this graph.
	 */
	val vertices: Set<V>

	/**
	 * Returns a [Set] view of the edges contained in this graph.
	 */
	val edges: Set<E>

	/**
	 * Returns <tt>true</tt> if this graph contains the specified edge.
	 *
	 * This graph will check existence of an edge using vertex `v1` as a source and vertex `v2` as a target.
	 *
	 * @param v1 source vertex of the edge
	 * @param v2 target vertex of the edge
	 * @return <tt>true</tt> if this graph contains at least one edge between specified vertices.
	 */
	fun containsEdge(v1: V, v2: V): Boolean

	/**
	 * Returns <tt>true</tt> if this graph contains the specified edge.
	 *
	 * This graph uses `equals()` to distinguish edges between each other.
	 *
	 * @param edge edge to be checked
	 * @return <tt>true</tt> if this graph contains the specified edge
	 */
	fun containsEdge(edge: E): Boolean

	/**
	 * Returns <tt>true</tt> if this graph contains the specified vertex.
	 *
	 * This graph uses `equals()` to distinguish vertices between each other.
	 *
	 * @param vertex vertex to be checked
	 * @return <tt>true</tt> if this graph contains the specified vertex.
	 */
	fun containsVertex(vertex: V): Boolean

	/**
	 * Finds edge that connects specified vertices.
	 *
	 * @param v1 source vertex of the edge
	 * @param v2 target vertex of the edge
	 * @return <tt>edge</tt> if this graph contains it, null otherwise
	 */
	fun findEdge(v1: V, v2: V): E?

	/**
	 * Returns a [Set] view of the edges from the source <tt>vertex</tt> contained in this graph.
	 *
	 * @param vertex source vertex of the edges
	 * @return a Set view of the edges from the source <tt>vertex</tt> contained in this graph
	 */
	fun getEdgesFromSource(vertex: V): Set<E>

	/**
	 * Returns a [Set] view of the edges to the target <tt>vertex</tt> contained in this graph.
	 *
	 * @param vertex target vertex of the edges
	 * @return a Set view of the edges to the target <tt>vertex</tt> contained in this graph
	 */
	fun getEdgesToTarget(vertex: V): Set<E>

	/**
	 * Returns the number of edges with `vertex` as their initial vertex.
	 *
	 * @param vertex initial vertex for getting the "out" degree
	 * @return the number of edges with `vertex` as their initial vertex. Returns
	 * -1, if the graph doesn't contain the specified vertex.
	 */
	fun getOutDegree(vertex: V): Int

	/**
	 * Returns the number of edges with `vertex` as their terminal vertex.
	 *
	 * @param vertex terminal vertex for getting the "in" degree
	 * @return the number of edges with `vertex` as their terminal vertex. Returns
	 * -1, if the graph doesn't contain the specified vertex.
	 */
	fun getInDegree(vertex: V): Int
}

/**
 * Main interface for all mutable graphs.
 *
 * @param <V> type for vertices
 * @param <E> type for edges. Should implement Edge interface
 */
interface MutableGraph<V, E: Edge<V>> : Graph<V, E> {

	/**
	 * Adds a new edge to the graph. In order to use this method, graph should have an edge factory specified.
	 *
	 * If current graph doesn't contain any of the vertices from the edge, they will be added to the graph
	 * automatically.
	 *
	 * @param v1 source vertex of the edge
	 * @param v2 target vertex of the edge
	 * @return <tt>true</tt> if this graph did not already contain the specified edge
	 */
	fun addEdge(v1: V, v2: V): Boolean

	/**
	 * Adds a new edge to the graph.
	 *
	 * If current graph doesn't contain any of the vertices from the edge, they will be added to the graph
	 * automatically.
	 *
	 * @param edge edge to be added to the graph
	 * @return <tt>true</tt> if this graph did not already contain the specified
	 * edge
	 */
	fun addEdge(edge: E): Boolean

	/**
	 * Adds a new vertex to the graph.
	 *
	 * @param vertex vertex to be added to the graph
	 * @return <tt>true</tt> if this graph did not already contain the specified vertex
	 */
	fun addVertex(vertex: V): Boolean

	/**
	 * Removes all of the vertices and edges from this graph. The graph will be empty after this call returns.
	 */
	fun clear()

	/**
	 * Removes an edge from this graph.
	 *
	 * @param v1 source vertex of the edge
	 * @param v2 target vertex of the edge
	 * @return <tt>true</tt> if the edge has been successfully removed
	 */
	fun removeEdge(v1: V, v2: V): Boolean

	/**
	 * Removes an edge from this graph.
	 *
	 * @param edge edge to be removed
	 * @return <tt>true</tt> if the edge has been successfully removed
	 */
	fun removeEdge(edge: E): Boolean

	/**
	 * Removes a vertex from this graph.
	 *
	 * @param vertex vertex to be removed
	 * @return <tt>true</tt> if the vertex has been successfully removed
	 */
	fun removeVertex(vertex: V): Boolean
}