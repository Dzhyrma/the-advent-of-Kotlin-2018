package org.dzhyrma.advent.week1.graph

import org.dzhyrma.advent.week1.graph.edge.WeightedEdge

class UndirectedWeightedGraph<V, E : WeightedEdge<V>>(
	private val weightedEdgeFactory: (V, V, Double) -> E
) : MutableWeightedGraph<V, E> {

	private val vertexMap: MutableMap<V, MutableMap<V, E>> = hashMapOf()

	private val allEdges: MutableSet<E> = hashSetOf()

	override val vertices: Set<V>
		get() = vertexMap.keys

	override val edges: Set<E>
		get() = allEdges

	override fun addEdge(v1: V, v2: V, weight: Double): Boolean {
		val edgesV1 = vertexMap.getOrPut(v1) { hashMapOf() }
		val existingEdge = edgesV1[v2]
		if (existingEdge != null) {
			allEdges.remove(existingEdge)
		}
		val newEdge = weightedEdgeFactory.invoke(v1, v2, weight)
		edgesV1[v2] = newEdge
		vertexMap.getOrPut(v2) { hashMapOf() }[v1] = newEdge
		return allEdges.add(newEdge)
	}

	override fun addEdge(edge: E, weight: Double): Boolean {
		val edgesV1 = vertexMap.getOrPut(edge.source) { hashMapOf() }
		val existingEdge = edgesV1[edge.target]
		if (existingEdge != null) {
			allEdges.remove(existingEdge)
		}
		edgesV1[edge.target] = edge
		vertexMap.getOrPut(edge.target) { hashMapOf() }[edge.source] = edge
		return allEdges.add(edge)
	}

	override fun addVertex(vertex: V): Boolean {
		vertexMap.getOrPut(vertex) { hashMapOf() }
		return true
	}

	override fun clear() {
		vertexMap.clear()
	}

	override fun containsEdge(v1: V, v2: V): Boolean = findEdge(v1, v2) != null

	override fun containsEdge(edge: E): Boolean = allEdges.contains(edge)

	override fun containsVertex(vertex: V): Boolean = vertexMap.containsKey(vertex)

	override fun findEdge(v1: V, v2: V): E? = vertexMap[v1]?.get(v2)

	override fun getEdgesFromSource(vertex: V): Set<E> = vertexMap[vertex]?.values?.toSet() ?: emptySet()

	override fun getEdgesToTarget(vertex: V): Set<E> = getEdgesFromSource(vertex)

	override fun getOutDegree(vertex: V): Int = vertexMap[vertex]?.size ?: -1

	override fun getInDegree(vertex: V): Int = vertexMap[vertex]?.size ?: -1

	override fun removeEdge(v1: V, v2: V): Boolean {
		val edgesV1 = vertexMap[v1] ?: return false
		val edgesV2 = vertexMap[v2] ?: return false

		edgesV1.remove(v2)
		edgesV2.remove(v1)?.also { return allEdges.remove(it) }
		return false
	}

	override fun removeEdge(edge: E): Boolean {
		return if (allEdges.remove(edge)) {
			vertexMap[edge.source]?.remove(edge.target)
			vertexMap[edge.target]?.remove(edge.source)
			true
		} else {
			false
		}
	}

	override fun removeVertex(vertex: V): Boolean = vertexMap.remove(vertex)?.let { adjVertices ->
		adjVertices.entries.fold(true) { result, (_, edge) -> result && removeEdge(edge) }
	} ?: false
}