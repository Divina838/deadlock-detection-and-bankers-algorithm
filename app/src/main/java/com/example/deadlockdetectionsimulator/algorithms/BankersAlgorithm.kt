package com.example.deadlockdetectionsimulator.algorithms

import com.example.deadlockdetectionsimulator.models.BankerResult

object BankersAlgorithm {
    fun calculate(
        allocation: Array<IntArray>,
        need: Array<IntArray>,
        available: IntArray
    ): BankerResult {
        val p = allocation.size
        val r = available.size
        
        val work = available.copyOf()
        val finish = BooleanArray(p)
        val safeSequence = mutableListOf<Int>()
        val workHistory = mutableListOf<IntArray>()
        workHistory.add(work.copyOf())
        
        var count = 0
        while (count < p) {
            var found = false
            for (i in 0 until p) {
                if (!finish[i]) {
                    var canBeSatisfied = true
                    for (j in 0 until r) {
                        if (need[i][j] > work[j]) {
                            canBeSatisfied = false
                            break
                        }
                    }
                    
                    if (canBeSatisfied) {
                        for (j in 0 until r) {
                            work[j] += allocation[i][j]
                        }
                        safeSequence.add(i)
                        finish[i] = true
                        workHistory.add(work.copyOf())
                        found = true
                        count++
                    }
                }
            }
            
            if (!found) break
        }
        
        return BankerResult(
            isSafe = count == p,
            safeSequence = safeSequence,
            needMatrix = need,
            workHistory = workHistory
        )
    }
}
