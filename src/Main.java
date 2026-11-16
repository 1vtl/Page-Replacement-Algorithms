import java.util.*;
int[] referenceString = {7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2};
int frameCount = 3;


void main() {
    // LRU testing
    algorithmResult r = lru();
    algorithmResult q = fifo();
    algorithmResult o = optimalReplacement();
    System.out.println("LRU \n");
    System.out.println("Hits: " + r.hits);
    System.out.println("Faults: " + r.faults);
    System.out.printf("Fault ratio: %.2f%n", (double) r.faults / referenceString.length);
    // TODO: run the algorithms and report their results.
    System.out.println("FIFO \n");
    System.out.println("Hits: " + q.hits);
    System.out.println("Faults: " + q.faults);
    System.out.printf("Fault ratio: %.2f%n", (double) q.faults / referenceString.length);
    System.out.println("Optimal \n");
    System.out.println("Hits: " + o.hits);
    System.out.println("Faults: " + o.faults);
    System.out.printf("Fault ratio: %.2f%n", (double) o.faults / referenceString.length);
}



algorithmResult fifo() {
    // Validation
    if (referenceString == null || referenceString.length == 0) {
        throw new IllegalArgumentException("Reference string must not be empty.");
    }
    if (frameCount <= 0) {
        throw new IllegalArgumentException("Frame count must be greater than 0.");
    }

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
        System.out.println("Page " + page + (hit ? " HIT   " : " FAULT ") + queue);
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
        System.out.println("Page " + page + (idx != -1 ? " HIT   " : " FAULT ") + frames);
    }
    return new algorithmResult(hits, faults);
}


algorithmResult optimalReplacement() {
    int hits = 0;
    int faults = 0;

    java.util.List<Integer> frames = new java.util.ArrayList<>();

    for (int i = 0; i < referenceString.length; i++) {
        int page = referenceString[i];

        if (frames.contains(page)) {
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

