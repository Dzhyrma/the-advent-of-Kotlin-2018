package org.dzhyrma.advent.week1

import org.dzhyrma.advent.week1.graph.*
import org.dzhyrma.advent.week1.graph.algo.AStarSearch
import org.dzhyrma.advent.week1.graph.edge.*
import org.dzhyrma.advent.week1.graph.path.WeightedPath

class PathFinder(
	private val validChar: Char = '.',
	private val wallChar: Char = 'B',
	private val startChar: Char = 'S',
	private val endChar: Char = 'X'
) {

	fun findShortestPath(mapString: String): WeightedPath<Point, out WeightedEdge<Point>>? {
		val lines = mapString.split('\n')
		val (graph, source, target) = constructGraph(lines)

		return AStarSearch.perform(graph, source, target) { a, b ->
			val difX = Math.abs(a.x - b.x)
			val difY = Math.abs(a.y - b.y)
			if (difX < CACHE_SIZE_SQRT && difY < CACHE_SIZE_SQRT) {
				DISTANCE_CACHE[difX][difY]
			} else {
				Math.hypot(difX.toDouble(), difY.toDouble())
			}
		}
	}

	private fun constructGraph(mapLines: List<String>): Triple<WeightedGraph<Point, out WeightedEdge<Point>>, Point, Point> {
		check(mapLines.isNotEmpty() && mapLines.all { mapLines.first().length == it.length })

		return UndirectedWeightedGraph<Point, SimpleWeightedEdge<Point>>(::SimpleWeightedEdge).let { graph ->
			var source: Point? = null
			var target: Point? = null
			mapLines.forEachIndexed { y, line ->
				line.forEachIndexed { x, char ->
					when (char) {
						wallChar -> {
						}
						else -> {
							val vertex = Point(x, y)
							when (char) {
								startChar -> source = vertex
								endChar -> target = vertex
								validChar -> {}
								else -> error( "Encountered an invalid char '$char'" )
							}

							if (x + 1 < line.length && y - 1 >= 0 && mapLines[y - 1][x + 1] != wallChar) {
								graph.addEdge(vertex, Point(x + 1, y - 1), DIAGONAL_NEIGHBOUR_DISTANCE)
							}
							if (x - 1 >= 0 && y - 1 >= 0 && mapLines[y - 1][x - 1] != wallChar) {
								graph.addEdge(vertex, Point(x - 1, y - 1), DIAGONAL_NEIGHBOUR_DISTANCE)
							}
							if (x - 1 >= 0 && line[x - 1] != wallChar) {
								graph.addEdge(vertex, Point(x - 1, y), STRAIGHT_NEIGHBOUR_DISTANCE)
							}
							if (y - 1 >= 0 && mapLines[y - 1][x] != wallChar) {
								graph.addEdge(vertex, Point(x, y - 1), STRAIGHT_NEIGHBOUR_DISTANCE)
							}
						}
					}
				}
			}

			check(source != null) { "Start position couldn't be found" }
			check(target != null) { "End position couldn't be found" }
			Triple(graph, source!!, target!!)
		}
	}

	data class Point(val x: Int, val y: Int)

	companion object {
		private const val STRAIGHT_NEIGHBOUR_DISTANCE = 1.0
		private const val DIAGONAL_NEIGHBOUR_DISTANCE = 1.5

		private const val CACHE_SIZE_SQRT = 200
		private val DISTANCE_CACHE = (0 until CACHE_SIZE_SQRT).map { dx ->
			(0 until CACHE_SIZE_SQRT).map { dy ->
				Math.hypot(dx.toDouble(), dy.toDouble())
			}
		}
	}
}