int[] referenceString = {7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2};
int frameCount = 3;


void main() {
    // TODO: run the algorithms and report their results.
}



algorithmResult fifo(){
    int hits = 0;
    int faults = 0;
    return new algorithmResult(hits, faults);
}


algorithmResult lru(){
    int hits = 0;
    int faults = 0;
    return new algorithmResult(hits, faults);
}


algorithmResult optimalReplacement(){
    int hits = 0;
    int faults = 0;
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

