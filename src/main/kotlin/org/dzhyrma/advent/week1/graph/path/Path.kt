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
	 * Returns the source of the path.
	 */
	val source: V

	/**
	 * Returns the target of the path.
	 */
	val target: V

	/**
	 * Returns the list of vertices used in the path.
	 */
	val vertices: List<V>

	/**
	 * Returns the list of edges used in the path.
	 */
	val edges: List<E>
}