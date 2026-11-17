import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.*;
import java.util.List;

static int[] referenceString = {7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2};
static int frameCount = 3;


void main(String[] args) {
    // used for debugging.
    /*System.out.println("Page Replacement Simulation");
    System.out.println("Reference string: " + Arrays.toString(referenceString));
    System.out.println("Frame count: " + frameCount);

    printResult("FIFO", fifo());
    printResult("LRU", lru());
    printResult("Optimal", optimalReplacement());*/

    startGUI();
}

static void startGUI() {
    JFrame frame = new JFrame("Page Replacement Simulator");
    frame.setSize(800, 600);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new BorderLayout(10, 10));


    JPanel topPanel = new JPanel();
    JLabel infoLabel = new JLabel("Frames: " + frameCount + " | Ref String: " + Arrays.toString(referenceString) + "   ");


    String[] algorithms = {"FIFO", "LRU", "Optimal"};
    JComboBox<String> algoSelector = new JComboBox<>(algorithms);
    JButton runButton = new JButton("Run Algorithm");

    topPanel.add(infoLabel);
    topPanel.add(algoSelector);
    topPanel.add(runButton);


    DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Page", "Status", "Frames"}, 0);
    JTable table = new JTable(tableModel);
    table.setRowHeight(30);
    table.setEnabled(false);

    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
    for (int i = 0; i < table.getColumnCount(); i++) {
        table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
    }

    JTableHeader header = table.getTableHeader();
    header.setBackground(new Color(51, 122, 183));
    header.setForeground(Color.WHITE);
    header.setFont(new Font("SansSerif", Font.BOLD, 14));
    ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

    JScrollPane tableScroll = new JScrollPane(table);


    JTextArea summaryArea = new JTextArea(5, 20);
    summaryArea.setEditable(false);
    summaryArea.setFont(new Font("Monospaced", Font.BOLD, 14));
    summaryArea.setBorder(BorderFactory.createTitledBorder("Summary"));
    JScrollPane summaryScroll = new JScrollPane(summaryArea);

    frame.add(topPanel, BorderLayout.NORTH);
    frame.add(tableScroll, BorderLayout.CENTER);
    frame.add(summaryScroll, BorderLayout.SOUTH);


    PrintStream printStream = new PrintStream(new OutputStream() {
        StringBuilder buffer = new StringBuilder();


        @Override
        public void write(byte[] b, int off, int len) {
            String text = new String(b, off, len);
            for (char c : text.toCharArray()) {
                if (c == '\n') {
                    processLine(buffer.toString().trim());
                    buffer.setLength(0);
                } else if (c != '\r') {
                    buffer.append(c);
                }
            }
        }

        @Override
        public void write(int b) {
            char c = (char) b;
            if (c == '\n') {
                processLine(buffer.toString().trim());
                buffer.setLength(0);
            } else if (c != '\r') {
                buffer.append(c);
            }
        }

        void processLine(String line) {
            if (line.contains("|")) {
                String[] parts = line.split("\\|");
                if (parts.length >= 3) {

                    String page = parts[0].replace("Page", "").trim();
                    String status = parts[1].trim();

                    String framesStr = parts[2];
                    int start = framesStr.indexOf('[');
                    int end = framesStr.indexOf(']');
                    String frames = (start != -1 && end != -1) ? framesStr.substring(start + 1, end) : framesStr;

                    tableModel.addRow(new Object[]{page, status, frames});
                }
            } else if (line.contains("===") || line.contains("Hits:") || line.contains("faults:") || line.contains("ratio:")) {
                summaryArea.append(line + "\n");
            }
        }
    });
    System.setOut(printStream);
    System.setErr(printStream);


    runButton.addActionListener(e -> {
        tableModel.setRowCount(0);
        summaryArea.setText("");

        String selected = (String) algoSelector.getSelectedItem();

        try {
            if (selected.equals("FIFO")) {
                printResult("FIFO", fifo());
            } else if (selected.equals("LRU")) {
                printResult("LRU", lru());
            } else if (selected.equals("Optimal")) {
                printResult("Optimal", optimalReplacement());
            }
        } catch (Exception ex) {
            summaryArea.append("Error: " + ex.getMessage() + "\n");
        }
    });

    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
}

static void printResult(String algorithmName, algorithmResult result) {
    System.out.println("\n=== " + algorithmName + " ===");
    System.out.println("Hits: " + result.hits);
    System.out.println("Page faults: " + result.faults);
    System.out.printf("Fault ratio: %.2f%%%n", ((double) result.faults / referenceString.length) * 100);
}



static algorithmResult fifo() {
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


static algorithmResult lru(){
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


static algorithmResult optimalReplacement() {
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

