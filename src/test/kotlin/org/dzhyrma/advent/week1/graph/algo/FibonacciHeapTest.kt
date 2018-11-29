package org.dzhyrma.advent.week1.graph.algo

import org.dzhyrma.advent.week1.graph.util.FibonacciHeap
import org.junit.jupiter.api.Test
import kotlin.test.*


class FibonacciHeapTest {

	@Test
	fun `verify initial heap size`() {
		val heap = FibonacciHeap<Int>()
		assertEquals(0, heap.size)
		assertTrue(heap.isEmpty)
	}

	@Test
	fun `verify correct min`() {
		val items = listOf(2, 1, 0, 2, 1)
		val heap = FibonacciHeap<Int>()
		val nodes = items.map { heap.enqueue(it, it.toDouble()) }

		assertEquals(items.size, heap.size)
		nodes.forEach { heap.contains(it) }
		assertEquals(nodes[2], heap.min)
	}

	@Test
	fun `verify min dequeue process`() {
		val items = listOf(3.14, 2.718, 1.0, 7.0)
		val heap = FibonacciHeap<Double>()
		val nodes = items.map { heap.enqueue(it, it) }

		assertEquals(items.size, heap.size)
		val extraItem = 1.0
		val extraNode = heap.enqueue(extraItem, extraItem)
		assertEquals(items.size + 1, heap.size)


		val result = (0 until items.size + 1).mapNotNull {
			assertEquals(items.size +1 - it, heap.size)
			heap.dequeueMin()?.value
		}
		val sortedItems = (items + extraItem).sorted()
		assertEquals(result, sortedItems)
		(nodes + extraNode).forEach {
			assertFalse(heap.contains(it))
		}
	}
}