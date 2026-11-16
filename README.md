# Page Replacement Algorithms

A small Java project for simulating three operating-system page replacement algorithms: FIFO, LRU, and Optimal. Each algorithm runs against the same reference string and frame count, prints the frame contents after every page reference, and reports its total hits, page faults, and fault ratio.

## Current example

The values in `src/Main.java` are:

- Reference string: `7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2`
- Frame count: `3`

Change these values in `Main.java` to run another simulation.

## Algorithms

- **FIFO** replaces the page that has been in memory the longest.
- **LRU** replaces the page that has not been used for the longest time. Its displayed frame list is ordered from least recently used to most recently used.
- **Optimal** replaces the page whose next use is farthest in the future, or that is never used again.

The program validates that the reference string is not empty, the frame count is greater than zero, and page numbers are nonnegative. Results are returned in an `algorithmResult` object containing hit and fault counts. The fault ratio is `page faults / number of references`.

## Compile and run

Requires JDK 25, which supports the compact source-file `void main()` used by this project. From the project root:

```text
javac src/Main.java
java -cp src Main
```
