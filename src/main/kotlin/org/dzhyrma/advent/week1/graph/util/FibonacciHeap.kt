package org.dzhyrma.advent.week1.graph.util

class FibonacciHeap<T> {

	/**
	 * Retrieves a minimum from the heap.
	 */
	var min: Node<T>? = null
		private set
	/**
	 * Retrieves the size of the heap.
	 */
	var size: Int = 0
		private set

	/**
	 * Returns `true` if the map is empty (contains no elements), `false` otherwise.
	 */
	val isEmpty: Boolean
		get() = min == null

	/**
	 * Enqueues the specified element into the heap with the specified priority.
	 * Its priority must be a valid double, so every value except NaN will be
	 * accepted.
	 *
	 * @param value value to insert
	 * @param priority priority, which represents order of the given value
	 * @return a node representing that element in the heap
	 */
	fun enqueue(value: T, priority: Double): Node<T> {
		if (java.lang.Double.isNaN(priority))
			throw IllegalArgumentException("Priority cannot be NaN.")
		size++
		val newNode = Node(value, priority, this)
		when (min) {
			null -> {
				min = newNode.also {
					it.right = it
					it.left = it
				}
			}
			else -> insert(newNode)
		}

		return newNode
	}

	/**
	 * Merges current heap with the given one. After merge the second heap will be
	 * empty.
	 *
	 * @param heap heap to merge with
	 */
	fun merge(heap: FibonacciHeap<T>) {
		heap.min?.also { merge(it) }
		size += heap.size
		heap.min = null
		heap.size = 0
	}

	/**
	 * Deletes node from the current heap. The node will be counting as dequeued.
	 *
	 * @param node node to delete
	 */
	fun delete(node: Node<T>) {
		check(node.creator === this) { "Given node belongs to another heap." }

		decreaseKeyUnchecked(node, java.lang.Double.NEGATIVE_INFINITY)
		dequeueMin()
	}

	/**
	 * Decrease key of the given key to a new priority. The new priority must be a
	 * valid double and be less than the old priority. In case when it is bigger
	 * than the old one, delete this node first and then add a new node with same
	 * value and new priority.
	 *
	 * @param node node to decrease
	 * @param newPriority new priority
	 */
	fun decreaseKey(node: Node<T>, newPriority: Double) {
		check(node.creator === this) { "Given node belongs to another heap." }
		check(!java.lang.Double.isNaN(newPriority)) { "New priority cannot be NaN." }
		check(newPriority <= node.priority) { "New priority cannot exceed old." }

		decreaseKeyUnchecked(node, newPriority)
	}

	/**
	 * Dequeues the minimum from the heap.
	 *
	 * @return a node representing the element with minimal priority in the heap
	 */
	/*fun dequeueMin(): Node<T>? {
		if (min == null)
			return null

		val res = min
		if (min!!.left === min) {
			min = min!!.child
		} else if (min!!.child == null) {
			min!!.left!!.right = min!!.right
			min!!.right!!.left = min!!.left
			min = min!!.right!!.left
		} else {
			min!!.left!!.right = min!!.child
			min!!.right!!.left = min!!.child!!.left
			min!!.child!!.left!!.right = min!!.right
			min!!.child!!.left = min!!.left
			min = min!!.child!!.left
		}

		if (min != null) {
			val visited = arrayOfNulls<Node<*>>(32 - Integer.numberOfLeadingZeros(size))
			var cur = min!!.right
			var temp: Node<T>
			min!!.parent = null
			visited[min!!.rank] = min
			while (visited[cur!!.rank] !== cur) {
				cur!!.parent = null
				while (visited[cur!!.rank] != null) {
					temp = visited[cur.rank]
					visited[cur.rank] = null
					temp.left!!.right = temp.right
					temp.right!!.left = temp.left
					if (temp.priority < cur.priority) {
						if (cur.left !== cur) {
							temp.left = cur.left
							temp.right = cur.right
							cur.right!!.left = temp
							cur.left!!.right = cur.right!!.left
						} else {
							temp.right = temp
							temp.left = temp.right
						}
						temp = cur
						cur = temp
					}
					if (cur.rank == 0) {
						cur.child = temp
						temp.left = temp
						temp.right = temp.left
					} else {
						temp.right = cur.child!!.right
						temp.left = cur.child
						cur.child!!.right = temp
						cur.child!!.right!!.left = cur.child!!.right
						if (cur.child!!.priority > cur.child!!.right!!.priority)
							cur.child = cur.child!!.right
					}
					temp.parent = cur
					cur.rank++
				}
				visited[cur.rank] = cur
				if (cur.priority < min!!.priority)
					min = cur
				cur = cur.right
			}
		}

		size--
		res!!.child = null
		res.right = res.child
		res.left = res.right
		res.rank = -1
		return res
	}

	private fun cut(node: Node<T>) {
		if (node.parent!!.child === node)
			node.parent!!.child = if (node.left === node) null else node.right
		node.parent!!.rank--
		node.right!!.left = node.left
		node.left!!.right = node.right
		node.isMarked = false
		insert(node)
		if (node.parent.parent != null)
			if (node.parent.isMarked)
				cut(node.parent)
			else
				node.parent.isMarked = true
		node.parent = null
	}

	private fun decreaseKeyUnchecked(node: Node<T>, newPriority: Double) {
		node.priority = newPriority
		if (node.parent == null) {
			if (node.priority <= min!!.priority)
				min = node
			return
		}
		if (node.priority > node.parent.priority)
			return

		cut(node)
	}

	private fun insert(node: Node<T>) {
		node.left = min
		node.right = min!!.right
		min!!.right!!.left = node
		min!!.right = min!!.right!!.left
		if (min!!.priority >= node.priority)
			min = node
	}

	private fun merge(node: Node<T>) {
		min?.also {
			node.left!!.right = min!!.right
			node.left = min
			min!!.right!!.left = node.left
			min!!.right = node
			if (min!!.priority > node.priority)
				min = node
		}
	}*/

	data class Node<T> constructor(
		/**
		 * Returns the value of the node.
		 */
		val value: T,
		/**
		 * Returns the priority of the node
		 */
		val priority: Double,
		internal val creator: FibonacciHeap<T>
	) {
		internal var left: Node<T>? = null
		internal var right: Node<T>? = null
		internal var child: Node<T>? = null
		internal var parent: Node<T>? = null
		internal var rank: Int = 0
		internal var isMarked: Boolean = false

		/**
		 * Returns `true` if this node was dequeued or deleted from a heap, `false` otherwise.
		 */
		val isDequeued: Boolean
			get() = rank < 0
	}
}