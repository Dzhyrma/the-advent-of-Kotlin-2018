package org.dzhyrma.advent.week1.graph.path

import org.dzhyrma.advent.week1.graph.edge.Edge

/**
 * Main interface for path in graph.
 *
 * @param <V> type for vertices
 * @param <E> type for edges
 */
interface Path<V, E : Edge<V>> {

	/**
	 * Retrieves the source of the path.
	 */
	val source: V

	/**
	 * Retrieves the target of the path.
	 */
	val target: V

	/**
	 * Retrieves the list of vertices used in the path.
	 */
	val vertices: List<V>

	/**
	 * Retrieves the list of edges used in the path.
	 */
	val edges: List<E>
}