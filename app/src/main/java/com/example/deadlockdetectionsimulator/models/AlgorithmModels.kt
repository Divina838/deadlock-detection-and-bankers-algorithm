package com.example.deadlockdetectionsimulator.models

/**
 * Common data model for matrix inputs.
 */
data class MatrixData(
    val rows: Int,
    val cols: Int,
    val data: Array<IntArray>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as MatrixData
        if (rows != other.rows) return false
        if (cols != other.cols) return false
        return data.contentDeepEquals(other.data)
    }

    override fun hashCode(): Int {
        var result = rows
        result = 31 * result + cols
        result = 31 * result + data.contentDeepHashCode()
        return result
    }
}

/**
 * Result of the Deadlock Detection algorithm.
 */
data class DeadlockResult(
    val isDeadlocked: Boolean,
    val deadlockedProcesses: List<Int>,
    val finishedProcesses: List<Int>,
    val workHistory: List<IntArray>
)

/**
 * Result of the Banker's Algorithm.
 */
data class BankerResult(
    val isSafe: Boolean,
    val safeSequence: List<Int>,
    val needMatrix: Array<IntArray>,
    val workHistory: List<IntArray>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as BankerResult
        if (isSafe != other.isSafe) return false
        if (safeSequence != other.safeSequence) return false
        if (!needMatrix.contentDeepEquals(other.needMatrix)) return false
        
        // Manual deep equality check for workHistory since it's a list of IntArrays
        if (workHistory.size != other.workHistory.size) return false
        for (i in workHistory.indices) {
            if (!workHistory[i].contentEquals(other.workHistory[i])) return false
        }
        
        return true
    }

    override fun hashCode(): Int {
        var result = isSafe.hashCode()
        result = 31 * result + safeSequence.hashCode()
        result = 31 * result + needMatrix.contentDeepHashCode()
        // Simple hash for workHistory
        result = 31 * result + workHistory.size
        return result
    }
}
