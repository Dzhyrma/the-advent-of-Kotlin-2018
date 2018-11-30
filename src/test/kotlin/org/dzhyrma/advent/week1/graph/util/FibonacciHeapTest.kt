/*
 * (C) Copyright 2014-2016, by Dimitrios Michail
 *
 * JHeaps Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dzhyrma.advent.week1.graph.util

import org.junit.jupiter.api.*
import java.util.*
import kotlin.test.*


class FibonacciHeapTest {

	private val h = FibonacciHeap<Int>()

	@Test
	fun test() {
		for (i in 0 until SIZE) {
			h.enqueue(i, i.toDouble())
			assertEquals(0, h.min?.priority?.toInt())
			assertFalse(h.isEmpty)
			assertEquals(h.size, i + 1)
		}

		for (i in SIZE - 1 downTo 0) {
			assertEquals(h.dequeueMin()?.value, Integer.valueOf(SIZE - i - 1))
		}
	}

	@Test
	fun testOnlyInsert() {
		for (i in 0 until SIZE) {
			h.enqueue(SIZE - i, SIZE - i.toDouble())
			assertEquals(Integer.valueOf(SIZE - i), h.min?.value)
			assertFalse(h.isEmpty)
			assertEquals(h.size, i + 1)
		}
	}

	@Test
	fun testOnly4() {
		assertTrue(h.isEmpty)

		h.enqueue(780, 780.0)
		assertEquals(h.size, 1)
		assertEquals(780, h.min?.priority?.toInt())

		h.enqueue(-389, -389.0)
		assertEquals(h.size, 2)
		assertEquals(Integer.valueOf(-389), h.min?.value)

		h.enqueue(306, 306.0)
		assertEquals(h.size, 3)
		assertEquals(Integer.valueOf(-389), h.min?.value)

		h.enqueue(579, 579.0)
		assertEquals(h.size, 4)
		assertEquals(Integer.valueOf(-389), h.min?.value)

		h.dequeueMin()
		assertEquals(h.size, 3)
		assertEquals(306, h.min?.priority?.toInt())

		h.dequeueMin()
		assertEquals(h.size, 2)
		assertEquals(579, h.min?.priority?.toInt())

		h.dequeueMin()
		assertEquals(h.size, 1)
		assertEquals(780, h.min?.priority?.toInt())

		h.dequeueMin()
		assertEquals(h.size, 0)

		assertTrue(h.isEmpty)
	}

	@Test
	fun testSortRandomSeed1() {
		val generator = Random(1)

		(0 until SIZE).map { generator.nextInt() }.forEach {
			h.enqueue(it, it.toDouble())
		}

		var prev: Int? = null
		var cur: Int?
		while (!h.isEmpty) {
			cur = h.min?.value
			h.dequeueMin()
			if (prev != null) {
				assertTrue(prev <= cur!!)
			}
			prev = cur
		}
	}

	@Test
	fun testSort1RandomSeed1() {
		val generator = Random(1)

		(0 until SIZE).map { generator.nextInt() }.forEach {
			h.enqueue(it, it.toDouble())
		}

		var prev: Int? = null
		var cur: Int?
		while (!h.isEmpty) {
			cur = h.dequeueMin()?.value
			if (prev != null) {
				assertTrue(prev <= cur!!)
			}
			prev = cur
		}
	}

	@Test
	fun testSortRandomSeed2() {
		val generator = Random(2)

		(0 until SIZE).map { generator.nextInt() }.forEach {
			h.enqueue(it, it.toDouble())
		}

		var prev: Int? = null
		var cur: Int?
		while (!h.isEmpty) {
			cur = h.min?.value
			h.dequeueMin()
			if (prev != null) {
				assertTrue(prev <= cur!!)
			}
			prev = cur
		}
	}

	@Test
	fun testSort2RandomSeed2() {
		val generator = Random(2)

		(0 until SIZE).map { generator.nextInt() }.forEach {
			h.enqueue(it, it.toDouble())
		}

		var prev: Int? = null
		var cur: Int?
		while (!h.isEmpty) {
			cur = h.dequeueMin()?.value
			if (prev != null) {
				assertTrue(prev <= cur!!)
			}
			prev = cur
		}
	}

	@Test
	fun testFindMinDeleteMinSameObject() {
		val generator = Random(1)

		for (i in 0 until SIZE) {
			h.enqueue(generator.nextInt(), generator.nextInt().toDouble())
		}

		while (!h.isEmpty) {
			assertEquals(h.min, h.dequeueMin())
		}
	}

	@Test
	fun testDelete() {
		val array = (0..14).map { i -> h.enqueue(i, i.toDouble()) }

		h.delete(array[5])
		assertEquals(0, h.min?.priority?.toInt())
		h.delete(array[7])
		assertEquals(0, h.min?.priority?.toInt())
		h.delete(array[0])
		assertEquals(1, h.min?.priority?.toInt())
		h.delete(array[2])
		assertEquals(1, h.min?.priority?.toInt())
		h.delete(array[1])
		assertEquals(3, h.min?.priority?.toInt())
		h.delete(array[3])
		assertEquals(4, h.min?.priority?.toInt())
		h.delete(array[9])
		assertEquals(4, h.min?.priority?.toInt())
		h.delete(array[4])
		assertEquals(6, h.min?.priority?.toInt())
		h.delete(array[8])
		assertEquals(6, h.min?.priority?.toInt())
		h.delete(array[11])
		assertEquals(6, h.min?.priority?.toInt())
		h.delete(array[6])
		assertEquals(10, h.min?.priority?.toInt())
		h.delete(array[12])
		assertEquals(10, h.min?.priority?.toInt())
		h.delete(array[10])
		assertEquals(13, h.min?.priority?.toInt())
		h.delete(array[13])
		assertEquals(14, h.min?.priority?.toInt())
		h.delete(array[14])
		assertTrue(h.isEmpty)

	}

	@Test
	fun testDelete1() {
		val array = (0..7).map { i -> h.enqueue(i, i.toDouble()) }

		h.delete(array[5])
		assertEquals(0, h.min?.priority?.toInt())
		h.delete(array[7])
		assertEquals(0, h.min?.priority?.toInt())
		h.delete(array[0])
		assertEquals(1, h.min?.priority?.toInt())
		h.delete(array[2])
		assertEquals(1, h.min?.priority?.toInt())
		h.delete(array[1])
		assertEquals(3, h.min?.priority?.toInt())
	}

	@Test
	fun testAddDelete() {
		val array = (0 until SIZE).map { i -> h.enqueue(i, i.toDouble()) }

		for (i in SIZE - 1 downTo 0) {
			h.delete(array[i])
			if (i > 0) {
				assertEquals(0, h.min?.priority?.toInt())
			}
		}
		assertTrue(h.isEmpty)
	}

	@Test
	fun testAddDecreaseKeyDeleteMin() {
		val array = (0 until SIZE).map { i -> h.enqueue(i, i.toDouble()) }

		for (i in SIZE / 2 until SIZE / 2 + 10) {
			h.decreaseKey(array[i], i / 2.0)
		}

		h.delete(array[0])

		for (i in SIZE / 2 + 10 until SIZE / 2 + 20) {
			h.decreaseKey(array[i], 0.0)
		}

		assertEquals(0, h.dequeueMin()?.priority?.toInt())
	}

	@Test
	fun testDeleteTwice() {
		val array = (0..14).map { i -> h.enqueue(i, i.toDouble()) }

		h.delete(array[5])
		assertEquals(0, h.min?.priority?.toInt())
		h.delete(array[7])
		assertEquals(0, h.min?.priority?.toInt())
		h.delete(array[0])
		assertEquals(1, h.min?.priority?.toInt())
		h.delete(array[2])
		assertEquals(1, h.min?.priority?.toInt())
		h.delete(array[1])
		assertEquals(3, h.min?.priority?.toInt())
		h.delete(array[3])
		assertEquals(4, h.min?.priority?.toInt())
		h.delete(array[9])
		assertEquals(4, h.min?.priority?.toInt())
		h.delete(array[4])
		assertEquals(6, h.min?.priority?.toInt())

		assertThrows<IllegalStateException> {
			// again
			h.delete(array[2])
		}
	}

	@Test
	fun testDeleteMinDeleteTwice() {
		val e1 = h.enqueue(50, 50.0)
		h.enqueue(100, 100.0)
		h.dequeueMin()
		assertThrows<IllegalStateException> {
			h.delete(e1)
		}
	}

	@Test
	fun testDeleteMinDecreaseKey() {
		for (i in 100..199) {
			h.enqueue(i, i.toDouble())
		}
		assertThrows<IllegalStateException> {
			h.decreaseKey(h.dequeueMin()!!, 0.0)
		}
	}

	@Test
	fun testNoElementDeleteMin() {
		assertNull(h.dequeueMin())
	}

	@Test
	fun testDecreaseKey() {
		val array = (0..14).map { i -> h.enqueue(i + 100, i + 100.0) }

		assertEquals(100, h.min?.priority?.toInt())
		h.decreaseKey(array[5], 5.0)
		assertEquals(5, h.min?.priority?.toInt())
		h.decreaseKey(array[1], 50.0)
		assertEquals(5, h.min?.priority?.toInt())
		h.decreaseKey(array[1], 20.0)
		assertEquals(5, h.min?.priority?.toInt())
		h.delete(array[5])
		assertEquals(20, h.min?.priority?.toInt())
		h.decreaseKey(array[10], 3.0)
		assertEquals(3, h.min?.priority?.toInt())
		h.decreaseKey(array[0], 0.0)
		assertEquals(0, h.min?.priority?.toInt())
	}

	@Test
	fun testDecreaseKey1() {
		val array = (0..999).map { i -> h.enqueue(i + 2000, i + 2000.0) }

		for (i in 999 downTo 0) {
			h.decreaseKey(array[i], array[i].priority - 2000.0)
		}

		for (i in 0..999) {
			assertEquals(i, h.dequeueMin()?.priority?.toInt())
		}
	}

	@Test
	fun testIncreaseKey() {
		val array = (0..14).map { i -> h.enqueue(i + 100, i + 100.0) }

		assertEquals(100, h.min?.priority?.toInt())
		h.decreaseKey(array[5], 5.0)
		assertEquals(5, h.min?.priority?.toInt())
		assertThrows<java.lang.IllegalStateException> {
			h.decreaseKey(array[1], 102.0)
		}
	}

	@Test
	fun testSameKey() {
		assertTrue(h.isEmpty)

		val handle = h.enqueue(780, 780.0)
		h.decreaseKey(handle, 780.0)
		assertEquals(780, h.dequeueMin()?.priority?.toInt())
		assertTrue(h.isEmpty)
	}

	companion object {
		private const val SIZE = 100000
	}
}