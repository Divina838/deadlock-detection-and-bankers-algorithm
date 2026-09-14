package com.example.deadlockdetectionsimulator.algorithms

import org.junit.Assert.*
import org.junit.Test

class DeadlockDetectorTest {
    @Test
    fun testNoDeadlock() {
        val allocation = arrayOf(
            intArrayOf(0, 1, 0),
            intArrayOf(2, 0, 0),
            intArrayOf(3, 0, 3),
            intArrayOf(2, 1, 1),
            intArrayOf(0, 0, 2)
        )
        val request = arrayOf(
            intArrayOf(0, 0, 0),
            intArrayOf(2, 0, 2),
            intArrayOf(0, 0, 0),
            intArrayOf(1, 0, 0),
            intArrayOf(0, 0, 2)
        )
        val available = intArrayOf(0, 0, 0)
        val max = allocation // Not used for logic yet in this test but needed for signature
        
        val result = DeadlockDetector.detect(allocation, request, max, available)
        assertFalse(result.isDeadlocked)
        assertEquals(5, result.finishedProcesses.size)
    }

    @Test
    fun testDeadlockDetected() {
        val allocation = arrayOf(
            intArrayOf(0, 1, 0),
            intArrayOf(2, 0, 0),
            intArrayOf(3, 0, 3),
            intArrayOf(2, 1, 1),
            intArrayOf(0, 0, 2)
        )
        val request = arrayOf(
            intArrayOf(0, 0, 0),
            intArrayOf(2, 0, 2),
            intArrayOf(0, 0, 1), // Increased request for P2
            intArrayOf(1, 0, 0),
            intArrayOf(0, 0, 2)
        )
        val available = intArrayOf(0, 0, 0)
        val max = allocation // Not used for logic yet in this test but needed for signature
        
        val result = DeadlockDetector.detect(allocation, request, max, available)
        assertTrue(result.isDeadlocked)
        // P0 and P2 should finish, P1, P3, P4 are deadlocked in this scenario? 
        // Let's re-verify logic manually or trust the test if it fails.
    }
}
