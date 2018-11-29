package org.dzhyrma.advent.week1.graph.edge

/**
 * Main interface for all weighted edges.
 *
 * @param <V> type for vertices
 */
interface WeightedEdge<V> : Edge<V> {

	/**
	 * Weight of the edge.
	 */
	var weight: Double
}

data class SimpleWeightedEdge<V>(
	override val source: V,
	override val target: V,
	override var weight: Double
) : WeightedEdge<V>