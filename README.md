# Page Replacement Algorithms

A small Java 25 project for CPIT260 Topic 7. The project compares FIFO, LRU, and Optimal page replacement for the same page reference string and frame count.

## Shared inputs

The current example in `src/Main.java` uses the reference string `7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2` and 3 frames. These values can be changed when testing.

## Team implementation

Each algorithm has a method with the same return type: `algorithmResult`. Implement the replacement logic in `fifo()`, `lru()`, or `optimalReplacement()` while keeping that method’s frame state and hit/fault counters local to its run. Return the counts in a new `algorithmResult` object.

`algorithmResult` stores the hit and fault counts. The fault ratio is the number of faults divided by the number of references.

## Status

The repository contains the method and result structure, but the replacement logic, input validation, and final reporting are still to be implemented. The current methods return zero counts as placeholders.

## Compile and run

From the project root with JDK 25:

```text
javac src/Main.java
java -cp src Main
```
