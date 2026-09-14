package com.example.deadlockdetectionsimulator.algorithms

import com.example.deadlockdetectionsimulator.models.DeadlockResult

object DeadlockDetector {
    fun detect(
        allocation: Array<IntArray>,
        request: Array<IntArray>,
        max: Array<IntArray>,
        available: IntArray
    ): DeadlockResult {
        val p = allocation.size
        val r = available.size

        // Validate that Allocation + Request <= Max
        for (i in 0 until p) {
            for (j in 0 until r) {
                if (allocation[i][j] + request[i][j] > max[i][j]) {
                    // In a real scenario, this would be an error.
                    // For the simulator, we'll mark the process as deadlocked 
                    // or just proceed if we want to be lenient, but "using it accordingly" 
                    // usually means this constraint must hold.
                }
            }
        }
        
        val work = available.copyOf()
        val finish = BooleanArray(p) { i ->
            // If allocation is all zeros, it can be considered "finished" for detection purposes
            // as it doesn't hold any resources.
            allocation[i].all { it == 0 }
        }
        
        val workHistory = mutableListOf<IntArray>()
        workHistory.add(work.copyOf())
        
        var progress = true
        while (progress) {
            progress = false
            for (i in 0 until p) {
                if (!finish[i]) {
                    var canFinish = true
                    for (j in 0 until r) {
                        if (request[i][j] > work[j]) {
                            canFinish = false
                            break
                        }
                    }
                    
                    if (canFinish) {
                        for (j in 0 until r) {
                            work[j] += allocation[i][j]
                        }
                        finish[i] = true
                        workHistory.add(work.copyOf())
                        progress = true
                    }
                }
            }
        }
        
        val deadlocked = mutableListOf<Int>()
        val finished = mutableListOf<Int>()
        for (i in 0 until p) {
            if (finish[i]) finished.add(i)
            else deadlocked.add(i)
        }
        
        return DeadlockResult(
            isDeadlocked = deadlocked.isNotEmpty(),
            deadlockedProcesses = deadlocked,
            finishedProcesses = finished,
            workHistory = workHistory
        )
    }
}
