import java.util.*;
int[] referenceString = {7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2};
int frameCount = 3;


void main() {
    System.out.println("Page Replacement Simulation");
    System.out.println("Reference string: " + Arrays.toString(referenceString));
    System.out.println("Frame count: " + frameCount);

    printResult("FIFO", fifo());
    printResult("LRU", lru());
    printResult("Optimal", optimalReplacement());
}

void printResult(String algorithmName, algorithmResult result) {
    System.out.println("\n=== " + algorithmName + " ===");
    System.out.println("Hits: " + result.hits);
    System.out.println("Page faults: " + result.faults);
    System.out.printf("Fault ratio: %.2f%n", (double) result.faults / referenceString.length);
}



algorithmResult fifo() {
    // Validation
    if (referenceString == null || referenceString.length == 0) {
        throw new IllegalArgumentException("Reference string must not be empty.");
    }
    if (frameCount <= 0) {
        throw new IllegalArgumentException("Frame count must be greater than 0.");
    }

    System.out.println("\nFIFO page-by-page:");
    Queue<Integer> queue = new LinkedList<>();
    int hits = 0;
    int faults = 0;

    for (int page : referenceString) {
        if (page < 0) {
            throw new IllegalArgumentException("Page numbers cannot be negative: " + page);
        }

        boolean hit = queue.contains(page);
        if (hit) {
            hits++;
        } else {
            faults++;
            if (queue.size() == frameCount) {
                queue.poll();
            }
            queue.add(page);
        }
        System.out.printf("  Page %-3d | %-5s | Frames: %s%n", page, hit ? "HIT" : "FAULT", queue);
    }

    return new algorithmResult(hits, faults);
}


algorithmResult lru(){
    // Validation
    if (referenceString == null || referenceString.length == 0) {
        throw new IllegalArgumentException("Reference string must not be empty.");
    }
    if (frameCount <= 0) {
        throw new IllegalArgumentException("Frame count must be greater than 0.");
    }

    System.out.println("\nLRU page-by-page:");
    // frames.get(0) = least recently used, last element = most recently used
    List<Integer> frames = new ArrayList<>();
    int hits = 0, faults = 0;

    for (int page : referenceString) {
        int idx = frames.indexOf(page);

        if (idx != -1) {                     // HIT
            hits++;
            frames.remove(idx);              // move page to the "most recent" end
            frames.add(page);
        } else {                             // FAULT
            faults++;
            if (frames.size() == frameCount) {
                frames.removeFirst();            // evict the least recently used page
            }
            frames.add(page);
        }
        System.out.printf("  Page %-3d | %-5s | Frames (LRU → MRU): %s%n", page, idx != -1 ? "HIT" : "FAULT", frames);
    }
    return new algorithmResult(hits, faults);
}


algorithmResult optimalReplacement() {
    if (referenceString == null || referenceString.length == 0) {
        throw new IllegalArgumentException("Reference string must not be empty.");
    }
    if (frameCount <= 0) {
        throw new IllegalArgumentException("Frame count must be greater than 0.");
    }

    System.out.println("\nOptimal page-by-page:");
    int hits = 0;
    int faults = 0;

    java.util.List<Integer> frames = new java.util.ArrayList<>();

    for (int i = 0; i < referenceString.length; i++) {
        int page = referenceString[i];
        if (page < 0) {
            throw new IllegalArgumentException("Page numbers cannot be negative: " + page);
        }

        boolean hit = frames.contains(page);
        if (hit) {
            hits++;
        }
        else {
            faults++;

            if (frames.size() < frameCount) {
                frames.add(page);
            }
            else {
                int farthestIndex = -1;
                int pageToReplace = -1;

                for (int framePage : frames) {
                    int nextUse = Integer.MAX_VALUE;

                    for (int j = i + 1; j < referenceString.length; j++) {
                        if (referenceString[j] == framePage) {
                            nextUse = j;
                            break;
                        }
                    }

                    if (nextUse > farthestIndex) {
                        farthestIndex = nextUse;
                        pageToReplace = framePage;
                    }
                }

                int indexToReplace = frames.indexOf(pageToReplace);
                frames.set(indexToReplace, page);
            }
        }
        System.out.printf("  Page %-3d | %-5s | Frames: %s%n", page, hit ? "HIT" : "FAULT", frames);
    }

    return new algorithmResult(hits, faults);
}

static class algorithmResult {
    int hits;
    int faults;

    algorithmResult(int hits, int faults) {
        this.hits = hits;
        this.faults = faults;
    }
}

