package org.dzhyrma.advent.week1.graph.algo

import org.dzhyrma.advent.week1.graph.*
import org.dzhyrma.advent.week1.graph.edge.WeightedEdge
import org.dzhyrma.advent.week1.graph.path.*
import org.dzhyrma.advent.week1.graph.util.FibonacciHeap
import java.util.*

/**
 * A* is an informed search algorithm, or a best-first search, meaning that it is formulated in terms of weighted
 * graphs: starting from a specific starting node of a graph, it aims to find a path to the given goal node having
 * the smallest cost (least distance travelled, shortest time, etc.). It does this by maintaining a tree of paths
 * originating at the start node and extending those paths one edge at a time until its termination criterion is
 * satisfied.
 *
 * Peter Hart, Nils Nilsson and Bertram Raphael of Stanford Research Institute (now SRI International) first published
 * the algorithm in 1968.[3] It can be seen as an extension of Edsger Dijkstra's 1959 algorithm. A* achieves better
 * performance by using heuristics to guide its search.
 */
object AStarSearch {

	/**
	 * Performs an A* search on a weighted graph and finds one of the optimal paths.
	 *
	 * @param graph weighted graph
	 * @param source source vertex, from which the search should start
	 * @param target target vertex, where the search should end
	 * @param heuristic admissible function that estimates the cost of the cheapest path from one vertex to another
	 * @return a shortest(if [heuristic] is admissible) path if it is found, null otherwise
	 */
	fun <V, E : WeightedEdge<V>> perform(graph: WeightedGraph<V, E>, source: V, target: V, heuristic: (V, V) -> Double): WeightedPath<V, E>? {
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
					graph.forEachEdge(cur.vertex) { edge, adjVertex, weight ->
						/* A* search algorithm works only with non-negative weights. */
						check(weight >= 0) { "A* search algorithm can be applied only for graphs with non negative weights." }

						/* Calculate the distance using a sum of the passed distance and weight of the edge. */
						val newDistance = cur.bestDistance + weight

						val adjNode = heapNodes[adjVertex]

						/* If this vertex has never been added to the heap or its previous distance was larger the the new one. */
						if (adjNode == null || newDistance < adjNode.value.bestDistance) {
							/* Check, whether the vertex is in the heap. */
							if (adjNode == null || adjNode.isDequeued) {
								heapNodes[adjVertex] = heap.enqueue(Tuple(adjVertex, newDistance), newDistance + heuristic.invoke(adjVertex, target))
							} else {
								/* Otherwise decrease the key of the node in the heap. */
								adjNode.value.bestDistance = newDistance
								heap.decreaseKey(adjNode, newDistance + heuristic.invoke(adjVertex, target))
							}

							/* Change a preceding vertex. */
							previous[adjVertex] = edge
						}
					}
				}
			} ?: break
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
		var bestDistance: Double
	)
}