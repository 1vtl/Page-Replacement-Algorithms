# Page Replacement Simulator

A Java Swing application that demonstrates three operating-system page replacement algorithms: FIFO, LRU, and Optimal. Choose an algorithm to step through a reference string and see whether each page access is a hit or fault, how the frames change, and the final hit, page fault, and fault ratio totals.

## Features

- **FIFO (First-In, First-Out):** evicts the page that has been in memory the longest.
- **LRU (Least Recently Used):** evicts the page that has not been accessed for the longest time. Frames are shown from least recently used to most recently used.
- **Optimal:** evicts the page whose next use is farthest in the future, or that will not be used again.
- Displays a page-by-page results table and a summary for the selected algorithm.
- Checks that the reference string is present and nonempty, the frame count is positive, and page numbers are nonnegative.

## Requirements

- JDK 25 or later. The program uses Java's compact source-file form with a `void main()` method.
- A graphical desktop environment for the Swing interface.

## Run

From the project root, compile and launch the application:

```sh
javac src/Main.java
java -cp src Main
```

The simulator opens with the example input configured in `src/Main.java`:

- Reference string: `7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2`
- Frame count: `3`

Select an algorithm from the dropdown and click **Run Algorithm**. The table shows each referenced page, its `HIT` or `FAULT` status, and the frame contents after processing it. The summary reports the hit count, page fault count, and fault ratio as a percentage of all references.

To change the simulation input, edit the `referenceString` and `frameCount` values near the top of `src/Main.java`, then compile and launch again.
