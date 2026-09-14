package com.example.deadlockdetectionsimulator.algorithms

import org.junit.Assert.*
import org.junit.Test

class BankersAlgorithmTest {
    @Test
    fun testSafeState() {
        val allocation = arrayOf(
            intArrayOf(0, 1, 0),
            intArrayOf(2, 0, 0),
            intArrayOf(3, 0, 2),
            intArrayOf(2, 1, 1),
            intArrayOf(0, 0, 2)
        )
        val max = arrayOf(
            intArrayOf(7, 5, 3),
            intArrayOf(3, 2, 2),
            intArrayOf(9, 0, 2),
            intArrayOf(2, 2, 2),
            intArrayOf(4, 3, 3)
        )
        val available = intArrayOf(3, 3, 2)
        val need = Array(5) { i ->
            IntArray(3) { j ->
                max[i][j] - allocation[i][j]
            }
        }
        
        val result = BankersAlgorithm.calculate(allocation, need, available)
        assertTrue(result.isSafe)
        assertEquals(listOf(1, 3, 4, 0, 2), result.safeSequence)
    }

    @Test
    fun testUnsafeState() {
        val allocation = arrayOf(
            intArrayOf(0, 1, 0),
            intArrayOf(2, 0, 0),
            intArrayOf(3, 0, 2),
            intArrayOf(2, 1, 1),
            intArrayOf(0, 0, 2)
        )
        val max = arrayOf(
            intArrayOf(7, 5, 3),
            intArrayOf(3, 2, 2),
            intArrayOf(9, 0, 2),
            intArrayOf(2, 2, 2),
            intArrayOf(4, 3, 3)
        )
        val available = intArrayOf(0, 0, 0) // No resources available
        val need = Array(5) { i ->
            IntArray(3) { j ->
                max[i][j] - allocation[i][j]
            }
        }
        
        val result = BankersAlgorithm.calculate(allocation, need, available)
        assertFalse(result.isSafe)
    }
}
