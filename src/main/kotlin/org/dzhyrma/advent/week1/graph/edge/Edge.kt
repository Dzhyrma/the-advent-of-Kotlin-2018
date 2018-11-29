package org.dzhyrma.advent.week1.graph.edge

/**
 * Main interface for all edges.
 *
 * @param <V> type for vertices
 */
interface Edge<V> {

	/**
	 * Retrieves the source of this edge.
	 */
	val source: V

	/**
	 * Retrieves the target of this edge.
	 */
	val target: V
}

data class SimpleEdge<V>(
	override val source: V,
	override val target: V
) : Edge<V>