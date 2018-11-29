package org.dzhyrma.advent.week1.graph.util

/**
 * This class represents a priority queue backed by a Fibonacci heap, as
 * described by Fredman and Tarjan in 1984. It is used because of the
 * find-minimum, insert, decrease key and merge in O(1) amortized time. Only
 * delete and delete minimum work in O(log n) amortized time.
 */
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
	 * Returns `true` if the heap is empty (contains no elements), `false` otherwise.
	 */
	val isEmpty: Boolean
		get() = min == null

	/**
	 * Returns `true` if the heap contains node, `false` otherwise.
	 */
	fun contains(node: Node<T>) = node.creator === this

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
		insert(newNode)

		return newNode
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
		check(newPriority <= node.priority) { "New priority ($newPriority) cannot exceed old (${node.priority})." }

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
			it.creator = null
		}
	}

	private fun deleteMin(oldMin: Node<T>) {
		min = when {
			oldMin.left === min -> oldMin.child
			oldMin.child == null -> {
				oldMin.leaveNeighbours()
				oldMin.right
			}
			else -> {
				oldMin.child?.also { child ->
					oldMin.left.right = child
					oldMin.right.left = child.left
					child.left.right = oldMin.right
					child.left = oldMin.left
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
						candidate = mergeNodeWithSameRank(temp, candidate)
					}
				}
				visited[candidate.rank] = candidate
				if (candidate.priority < currentMin.priority) {
					min = candidate
				}
				candidate = candidate.right
			}
		}
	}

	private fun mergeNodeWithSameRank(temp: Node<T>, candidate: Node<T>): Node<T> {
		temp.leaveNeighbours()

		return if (temp.priority < candidate.priority) {
			if (candidate.left !== candidate) {
				temp.left = candidate.left
				temp.right = candidate.right
				candidate.right.left = temp
				candidate.left.right = temp
			} else {
				temp.right = temp
				temp.left = temp
			}
			if (min === candidate) {
				min = temp
			}
			temp.apply { addChild(candidate) }
		} else {
			if (min === temp) {
				min = candidate
			}
			candidate.apply { addChild(temp) }
		}
	}

	private fun decreaseKeyUnchecked(node: Node<T>, newPriority: Double) {
		node.priority = newPriority
		node.parent.also { parent ->
			if (parent != null && parent.rank > 0) {
				if (node.priority < parent.priority) {
					cut(node, parent)
				}
			} else {
				min?.also { currentMin ->
					if (node.priority < currentMin.priority) {
						min = node
					}
				}
				return
			}
		}
	}

	private fun cut(node: Node<T>, parent: Node<T>) {
		if (parent.rank > 0) {
			if (parent.child === node) {
				parent.child = if (node.left === node) null else node.right
			}
			parent.rank--
			node.leaveNeighbours()
			node.isMarked = false
			insert(node)
			parent.parent.also { grandParent ->
				if (grandParent != null && grandParent.rank > 0) {
					when {
						parent.isMarked -> cut(parent, grandParent)
						else -> parent.isMarked = true
					}
				}
			}
		}
		node.parent = null
	}

	private fun insert(node: Node<T>) {
		min?.also { currentMin ->
			node.left = currentMin
			node.right = currentMin.right
			currentMin.right = node
			node.right.left = node
			if (currentMin.priority >= node.priority) {
				min = node
			}
		} ?: run {
			min = node
		}
	}

	override fun toString(): String {
		return min?.let { currentMin ->
			val stringBuilder = StringBuilder().apply {
				fun appendNodeString(node: Node<T>, prefix: String) {
					var pc = "│ "
					var temp = node
					while (true) {
						if (temp.right != node) {
							append("$prefix├─")
						} else {
							append("$prefix└─")
							pc = "  "
						}
						temp.child.also { child ->
							if (child == null) {
								appendln("─ ${temp.priority}, ${temp.value}")
							} else {
								appendln("┐ ${temp.priority}, ${temp.value}")
								appendNodeString(child, prefix + pc)
							}
						}
						if (temp.right == node) break
						temp = temp.right
					}
				}
				appendNodeString(currentMin, "")
			}
			stringBuilder.toString()
		} ?: "<empty>"
	}

	class Node<T>(
		/**
		 * Returns the value of the node.
		 */
		val value: T,
		/**
		 * Returns the priority of the node
		 **/
		internal var priority: Double,
		internal var creator: FibonacciHeap<T>? = null
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

		internal fun addChild(newChild: Node<T>) {
			if (rank == 0) {
				child = newChild
				newChild.left = newChild
				newChild.right = newChild
			} else {
				child?.also { oldChild ->
					newChild.right = oldChild.right
					newChild.left = oldChild
					oldChild.right = newChild
					newChild.right.left = newChild

					if (oldChild.priority > newChild.priority) {
						child = newChild
					}
				}
			}
			newChild.parent = this
			rank++
		}
	}
}