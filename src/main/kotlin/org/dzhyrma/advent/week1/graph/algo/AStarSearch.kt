package org.dzhyrma.advent.week1.graph.algo

import org.dzhyrma.advent.week1.graph.WeightedGraph
import org.dzhyrma.advent.week1.graph.edge.WeightedEdge
import org.dzhyrma.advent.week1.graph.path.SimpleWeightedPath
import org.dzhyrma.advent.week1.graph.util.FibonacciHeap
import java.util.*

object AStarSearch {

	fun <V, E : WeightedEdge<V>> perform(graph: WeightedGraph<V, E>, source: V, target: V, heuristic: (V, V) -> Double): SimpleWeightedPath<V, E>? {
		val previous = HashMap<V, E>(graph.vertices.size)

		/* Keep track on vertices with distance passed inserted to Fibonacci heap. */
		val heapNodes = HashMap<V, FibonacciHeap.Node<Tuple<V>>>(graph.vertices.size)
		val heap = FibonacciHeap<Tuple<V>>()

		/* Insert the source vertex to the heap. */
		heapNodes[source] = heap.enqueue(Tuple(source, 0.0), heuristic.invoke(source, target))
		var targetReached = false
		while (!heap.isEmpty && !targetReached) {
			heap.dequeueMin()?.value?.also { cur ->
				/* Target vertex is reached. */
				if (target == cur.vertex) {
					targetReached = true
				} else {
					/* Go through each edge from the currently picked vertex. */
					graph.getEdgesFromSource(cur.vertex).forEach { edge ->
						/* A* search algorithm works only with non-negative weights. */
						check(edge.weight >= 0) { "A* search algorithm can be applied only for graphs with non negative weights." }

						val adj = if (edge.source == cur.vertex) edge.target else edge.source

						/* Calculate the distance using a sum of the passed distance and weight of the edge. */
						val newDistance = cur.distance + edge.weight

						val next = heapNodes[adj]

						/* If this vertex has never been added to the heap or its previous distance was larger the the new one. */
						if (next == null || next.value.distance > newDistance) {
							/* Check, whether the vertex is in the heap. */
							if (next == null || next.isDequeued) {
								heapNodes[adj] = heap.enqueue(Tuple(adj, newDistance), newDistance + heuristic.invoke(adj, target))
							} else {
								/* Otherwise decrease the key of the node in the heap. */
								next.value.distance = newDistance
								heap.decreaseKey(next, newDistance + heuristic.invoke(adj, target))
							}

							/* Change a preceding vertex. */
							previous[adj] = edge
						}
					}
				}
			}
		}

		/* Check, whether target node has been reached. */
		if (!targetReached) {
			return null
		}

		/* Path reconstruction. */
		val edges = LinkedList<E>()
		var current: V = target
		var curEdge: E
		while (current != source) {
			curEdge = previous[current] ?: return null
			edges.push(curEdge)
			current = if (curEdge.source == current) curEdge.target else curEdge.source
		}
		return SimpleWeightedPath(source, target, edges)
	}

	data class Tuple<V>(
		val vertex: V,
		var distance: Double
	)
}