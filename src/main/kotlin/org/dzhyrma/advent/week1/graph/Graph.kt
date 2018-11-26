package org.dzhyrma.advent.week1.graph

interface Graph<V> {
	/** Returns the number of edges in this graph.
	 *
	 * @return the number of edges in this graph
	 */
	val size: Int

	/** Adds a new edge to the graph. In order to use this method, graph should
	 * have an edge factory specified.
	 *
	 * If current graph doesn't contain any of the vertices from the edge, they
	 * will be added to the graph automatically.
	 *
	 * @param v1 source vertex of the edge
	 * @param v2 target vertex of the edge
	 * @return <tt>true</tt> if this graph did not already contain the specified
	 * edge
	 */
	fun addEdge(v1: V, v2: V): Boolean

	/** Adds a new vertex to the graph.
	 *
	 * @param v vertex to be added to the graph
	 * @return <tt>true</tt> if this graph did not already contain the specified
	 * vertex
	 */
	fun addVertex(vertex: V): Boolean

	/** Removes all of the vertices and edges from this graph. The graph will be
	 * empty after this call returns.
	 */
	fun clear()

	/** Returns <tt>true</tt> if this graph contains the specified edge.
	 *
	 * This graph will check existence of an edge using vertex `v1` as a
	 * source and vertex `v2` as a target.
	 *
	 * @param v1 source vertex of the edge
	 * @param v2 target vertex of the edge
	 *
	 * @return <tt>true</tt> if this graph contains at least one edge between
	 * specified vertices.
	 */
	fun containsEdge(v1: V, v2: V): Boolean

	/** Returns <tt>true</tt> if this graph contains the specified vertex.
	 *
	 * This graph uses `equals()` to distinguish vertices between each other.
	 *
	 * @param v vertex to be checked
	 *
	 * @return <tt>true</tt> if this graph contains the specified vertex.
	 */
	fun containsVertex(vertex: V): Boolean

	/** Returns a [Set] view of the vertices contained in this graph. The set
	 * is backed by the graph, so changes to the graph are reflected in the set,
	 * and vice-versa. The set supports vertex removal, which removes the
	 * corresponding vertex from the graph, via the <tt>Set.remove</tt>,
	 * <tt>removeAll</tt>, <tt>retainAll</tt>, and <tt>clear</tt> operations. It
	 * also supports the <tt>add</tt> and <tt>addAll</tt> operations. It does not
	 * support <tt>Iterator.remove</tt>.
	 *
	 * @return a set view of the vertices contained in this graph
	 */
	fun getAllVertices(): Set<V>

	/** Returns the number of edges with `v` as their initial vertex.
	 *
	 * @param v initial vertex for getting the "out" degree
	 *
	 * @return the number of edges with `v` as their initial vertex. Returns
	 * -1, if the graph doesn't contain the specified vertex.
	 */
	fun getOutDegree(vertex: V): Int

	/** Returns the number of edges with `v` as their terminal vertex.
	 *
	 * @param v terminal vertex for getting the "in" degree
	 *
	 * @return the number of edges with `v` as their terminal vertex. Returns
	 * -1, if the graph doesn't contain the specified vertex.
	 */
	fun getInDegree(vertex: V): Int

	/** Removes an edge from this graph.
	 *
	 * @param v1 source vertex of the edge
	 * @param v2 target vertex of the edge
	 * @return <tt>true</tt> if the edge has been successfully removed
	 */
	fun removeEdge(v1: V, v2: V): Boolean

	/** Removes a vertex from this graph.
	 *
	 * @param v vertex to be removed
	 *
	 * @return <tt>true</tt> if the vertex has been successfully removed
	 */
	fun removeVertex(vertex: V): Boolean

}