package org.dzhyrma.advent.week1.graph.algo

import org.dzhyrma.advent.week1.graph.Graph
import java.util.LinkedList
import java.util.HashMap



object AStarSearch {

	fun <V> perform(graph: Graph<V>, source: V, target: V, heuristic: (V, V) -> Double): Sequence<V> {
		/*val previous = HashMap(graph.size)

		/* Keep track on vertices with distance passed inserted to Fibonacci heap. */
		val heapNodes = HashMap(graph.sizeOfVertices())
		val heap = FibonacciHeap()

		/* Insert the source vertex to the heap. */
		heapNodes.put(source, heap.enqueue(Tuple(source, 0.0), 0.0))
		while (!heap.isEmpty()) {
			val cur = heap.dequeueMin()

			/* Target vertex is reached. */
			if (cur.equals(target))
				break

			/* Go through each edge from the currently picked vertex. */
			for (e in graph.getEdgesFromSource(cur.getValue().getItem1())) {

				/* A* search algorithm works only with non-negative weights. */
				if (e.getWeight() < 0)
					throw IllegalArgumentException("A* search algorithm can be applied only for graphs with non negative weights.")

				val adj = e.getTarget()

				/* Calculate the distance using a sum of the passed distance and weight of the edge. */
				val newDistance = cur.getValue().getItem2().doubleValue() + e.getWeight()

				val next = heapNodes.get(adj)

				/* If this vertex has never been added to the heap or its previous distance was larger the the new one. */
				if (next == null || next!!.getValue().getItem2().doubleValue() > newDistance) {
					/* Check, whether the vertex is in the heap. */
					if (next == null || next!!.isDequeued())
						heapNodes.put(adj, heap.enqueue(Tuple(adj, newDistance), newDistance + heuristic.apply(adj, target)))
					else
						heap.decreaseKey(next, newDistance + heuristic.apply(adj, target))/* Otherwise decrease the key of the node in the heap. */

					/* Change a preceding vertex. */
					previous.put(adj, e)
				}
			}
		}

		/* Check, whether target node has been reached. */
		if (!heapNodes.containsKey(target))
			return null

		/* Path reconstruction. */
		val edges = LinkedList<E>()
		var cur = target
		var curEdge: E
		while (!cur.equals(source)) {
			curEdge = previous.get(cur)
			edges.push(curEdge)
			cur = curEdge.getSource()
		}*/
	}
}