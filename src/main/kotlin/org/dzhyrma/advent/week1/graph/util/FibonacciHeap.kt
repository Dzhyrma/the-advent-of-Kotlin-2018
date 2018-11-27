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
	fun dequeueMin(): Node<T>? {
		return min?.also {
			deleteMin(it)
			consolidateTrees()

			size--
			it.child = null
			it.right = it
			it.left = it
			it.rank = -1
		}
	}

	private fun deleteMin(oldMin: Node<T>) {
		min = when {
			oldMin.left === min -> oldMin.child
			oldMin.child == null -> {
				oldMin.leaveNeighbours()
				oldMin.right.left
			}
			else -> {
				oldMin.child?.also { child ->
					oldMin.left.right = child
					oldMin.right.left = child.left
					child.left.right = oldMin.right
					child.left = oldMin.left
					child.left
				}
			}
		}
		min?.parent = null
	}

	private fun consolidateTrees() {
		min?.also { currentMin ->
			val visited = arrayOfNulls<Node<T>>(32 - Integer.numberOfLeadingZeros(size))
			var candidate = currentMin.right
			visited[currentMin.rank] = currentMin
			while (visited[candidate.rank] !== candidate) {
				candidate.parent = null
				while (visited[candidate.rank] != null) {
					visited[candidate.rank]?.also { temp ->
						visited[candidate.rank] = null
						candidate = compareAndPlaceNodes(temp, candidate)
					}
				}
				visited[candidate.rank] = candidate
				if (candidate.priority < min!!.priority) {
					min = candidate
				}
				candidate = candidate.right
			}
		}
	}

	private fun compareAndPlaceNodes(temp: Node<T>, candidate: Node<T>): Node<T> {
		temp.leaveNeighbours()

		if (temp.priority < candidate.priority) {
			if (candidate.left !== candidate) {
				temp.left = candidate.left
				temp.right = candidate.right
				candidate.right.left = temp
				candidate.left.right = temp
			} else {
				temp.right = temp
				temp.left = temp.right
			}
			placeNodes(candidate, temp)
			return temp
		} else {
			placeNodes(temp, candidate)
			return candidate
		}
	}

	private fun placeNodes(temp: Node<T>, candidate: Node<T>) {
		if (candidate.rank == 0) {
			candidate.child = temp
			temp.left = temp
			temp.right = temp.left
		} else {
			candidate.child?.also { child ->
				temp.right = child.right
				temp.left = child
				child.right = temp
				child.right.left = temp
				if (child.priority > temp.priority) {
					candidate.child = temp
				}
			}
		}
		temp.parent = candidate
		candidate.rank++
	}

	private fun cut(node: Node<T>) {
		node.parent?.also { parent ->
			if (parent.child === node) {
				parent.child = if (node.left === node) null else node.right
			}
			parent.rank--
			node.leaveNeighbours()
			node.isMarked = false
			insert(node)
			if (parent.parent != null) {
				when {
					parent.isMarked -> cut(parent)
					else -> parent.isMarked = true
				}
			}
		}
		node.parent = null
	}

	private fun decreaseKeyUnchecked(node: Node<T>, newPriority: Double) {
		node.priority = newPriority
		node.parent?.also { parent ->
			if (node.priority > parent.priority) {
				return
			}
			cut(node)
		}
		if (node.parent == null) {
			min?.also { currentMin ->
				if (node.priority <= currentMin.priority) {
					min = node
				}
			}
			return
		}
	}

	private fun insert(node: Node<T>) {
		min?.also { currentMin ->
			node.left = currentMin
			node.right = currentMin.right
			currentMin.right.left = node
			currentMin.right = node
			if (currentMin.priority >= node.priority) {
				min = node
			}
		}
	}

	private fun merge(node: Node<T>) {
		min?.also { currentMin ->
			node.left.right = currentMin.right
			val temp = node.left
			node.left = currentMin
			currentMin.right.left = temp
			currentMin.right = node
			if (currentMin.priority > node.priority) {
				min = node
			}
		}
	}

	data class Node<T> constructor(
		/**
		 * Returns the value of the node.
		 */
		val value: T,
		/**
		 * Returns the priority of the node
		 */
		var priority: Double,
		internal val creator: FibonacciHeap<T>
	) {
		internal var left: Node<T> = this
		internal var right: Node<T> = this
		internal var child: Node<T>? = null
		internal var parent: Node<T>? = null
		internal var rank: Int = 0
		internal var isMarked: Boolean = false

		/**
		 * Returns `true` if this node was dequeued or deleted from a heap, `false` otherwise.
		 */
		val isDequeued: Boolean
			get() = rank < 0

		internal fun leaveNeighbours() {
			left.right = right
			right.left = left
		}
	}
}