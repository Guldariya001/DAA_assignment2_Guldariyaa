# DAA Assignment 2 - Data Structures Benchmark

**Author:** Guldariya Urazaliyeva  
**Course:** Data Structures and Algorithms  
**Language:** Java 

## Overview
This project contains custom implementations of dynamic data structures and an empirical performance analysis (benchmarking) across 4 workloads.

Implemented Data Structures:
* `DynamicArray` - Resizable array implementation.
* `MyLinkedList` - Doubly linked list implementation.
* `MinHeap` - Binary min-heap for priority processing.

## Project Structure
* `src/main/java/structures/` - Core data structure classes.
* `src/main/java/metrics/` - `OperationMetrics` counter class.
* `src/main/java/benchmark/` - Benchmark setup and CSV export logic.
* `src/test/java/` - Comprehensive JUnit 5 unit tests (16/16 passed).
* `results/` - Output `results.csv` and generated charts in `results/plots/`.

## How to Build and Run

### Run Benchmark
To execute the benchmark and generate `results.csv`, run the main class:
```bash
mvn compile
java -cp target/classes org.example.Main