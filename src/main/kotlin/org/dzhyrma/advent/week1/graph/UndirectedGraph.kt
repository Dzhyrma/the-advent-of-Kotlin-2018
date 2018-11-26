package org.dzhyrma.advent.week1.graph

class UndirectedGraph<V> : Graph<V> {

	private val vertexMap: MutableMap<V, MutableSet<V>> = HashMap()

	override val size: Int
		get() = vertexMap.size

	override fun addEdge(v1: V, v2: V): Boolean {
		return vertexMap.getOrPut(v1) { hashSetOf() }.add(v2)
			&& vertexMap.getOrPut(v2) { hashSetOf() }.add(v1)
	}

	override fun addVertex(vertex: V): Boolean {
		vertexMap.getOrPut(vertex) { hashSetOf() }
		return true
	}

	override fun clear() {
		vertexMap.clear()
	}

	override fun containsEdge(v1: V, v2: V): Boolean = vertexMap[v1]?.contains(v2) ?: false

	override fun containsVertex(vertex: V): Boolean = vertexMap.containsKey(vertex)

	override fun getAllVertices(): Set<V> = vertexMap.keys

	override fun getOutDegree(vertex: V): Int = vertexMap[vertex]?.size ?: -1

	override fun getInDegree(vertex: V): Int = vertexMap[vertex]?.size ?: -1

	override fun removeEdge(v1: V, v2: V): Boolean {
		return (vertexMap[v1]?.remove(v2) ?: false)
			&& (vertexMap[v2]?.remove(v1) ?: false)
	}

	override fun removeVertex(vertex: V): Boolean = vertexMap.remove(vertex)?.let { adjVertices ->
		adjVertices.fold(true) { result, adjVertex -> result && removeEdge(adjVertex, vertex) }
	} ?: false
}