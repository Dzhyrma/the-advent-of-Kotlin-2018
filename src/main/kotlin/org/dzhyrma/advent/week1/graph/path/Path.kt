package org.dzhyrma.advent.week1.graph.path

import org.dzhyrma.advent.week1.graph.edge.Edge

interface Path<V, E : Edge<V>> {

	val source: V

	val target: V

	val vertices: List<V>

	val edges: List<E>
}