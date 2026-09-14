# OS Deadlock & Resource Allocation Simulator

An educational Android application that interactively demonstrates **Deadlock Detection** and **Banker's Algorithm**.

## Features

- **Deadlock Detection**: Input Allocation and Request matrices to detect if a system is in a deadlock state.
- **Banker's Algorithm**: Input Allocation and Max matrices to determine if the system is in a safe state and find a safe sequence.
- **Dynamic Inputs**: Adjust the number of processes and resource types dynamically.
- **Step-by-Step Visualization**: See the intermediate `Need` matrix and `Work` vector history to understand the algorithmic steps.
- **Validation**: Ensures inputs are valid (e.g., Allocation ≤ Max).

## Architecture

- **UI**: Jetpack Compose for a modern, responsive interface.
- **Algorithms**: Pure Kotlin implementation for correctness and testability.
- **Navigation**: Compose Navigation for seamless screen transitions.

## Algorithms

### Deadlock Detection
Processes whose requests can be satisfied by currently available resources are simulated to finish, releasing their allocated resources back to the available pool. Processes that cannot finish are identified as deadlocked.

### Banker's Algorithm (Safety)
Calculates the `Need` matrix (`Max - Allocation`) and finds a sequence of processes such that each process's needs can be satisfied by the available resources plus the resources released by previously finished processes.

## Development

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Test Framework**: JUnit 4
- **Min SDK**: 28
- **Target SDK**: 37

## Build & Run

1. Open the project in Android Studio.
2. Sync Project with Gradle Files.
3. Run the `:app` module on an emulator or physical device.
4. Run unit tests via `./gradlew test`.
