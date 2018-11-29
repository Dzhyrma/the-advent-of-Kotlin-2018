package org.dzhyrma.advent.week1.graph

import org.dzhyrma.advent.week1.graph.edge.WeightedEdge

/**
 * Main interface for all weighted graphs.
 *
 * @param <V> type for vertices
 */
interface WeightedGraph<V, E: WeightedEdge<V>> : Graph<V, E> {

	/**
	 * Adds a new weighted edge to the graph. In order to use this method, graph should have an edge factory specified.
	 *
	 * If current graph doesn't contain any of the vertices from the edge, they will be added to the graph
	 * automatically.
	 *
	 * @param v1 source vertex of the edge
	 * @param v2 target vertex of the edge
	 * @param weight weight of the edge
	 * @return <tt>true</tt> if this graph did not already contain the specified edge
	 */
	fun addEdge(v1: V, v2: V, weight: Double): Boolean

	/**
	 * Adds a new weighted edge to the graph. In order to use this method, graph should have an edge factory specified.
	 *
	 * If current graph doesn't contain any of the vertices from the edge, they will be added to the graph
	 * automatically.
	 *
	 * @param edge edge to be added to the graph
	 * @param weight weight of the edge
	 * @return <tt>true</tt> if this graph did not already contain the specified edge
	 */
	fun addEdge(edge: E, weight: Double): Boolean

	override fun addEdge(edge: E): Boolean = addEdge(edge, DEFAULT_WEIGHT)

	override fun addEdge(v1: V, v2: V): Boolean = addEdge(v1, v2, DEFAULT_WEIGHT)

	companion object {
		const val DEFAULT_WEIGHT = 0.0
	}
}