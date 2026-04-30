import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class RaceConfigGUI extends JPanel {
    // Passage selection components
    private JRadioButton preDefinedRadio;
    private JRadioButton customRadio;
    private JComboBox<String> passageDropdown;
    private JTextArea passagePreview;
    private JTextArea customInput;

    // Seat selection component
    private JSpinner seatSpinner;

    // Modifier selection components
    private JCheckBox autoCorrect;
    private JCheckBox caffeineMode;
    private JCheckBox nightShift;

    // Typist configuration components (tabs)
    private JTabbedPane typistTabs;
    private final java.util.List<TypistConfigGUI> typistConfigs = new java.util.ArrayList<>();

    // Start Race button
    private JButton startRace;
    private JButton historyButton;
    private JButton compareButton;

    public RaceConfigGUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        createComponents();

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(buildPassage());
        content.add(Box.createVerticalStrut(10));
        content.add(buildSeatSelection());
        content.add(Box.createVerticalStrut(10));
        content.add(buildModifiers());

        JPanel left = content;
        JPanel right = buildTypistPanel();
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                                          new JScrollPane(left), right);
        split.setResizeWeight(0.65);
        split.setContinuousLayout(true);
        add(split, BorderLayout.CENTER);
        add(buildStartBar(), BorderLayout.SOUTH);


    }

   

// helper: build the right-hand panel with tabs
private JPanel buildTypistPanel() {
    JPanel panel = new JPanel(new BorderLayout(6,6));
    panel.setBorder(BorderFactory.createTitledBorder("Typist Configuration"));
    typistTabs = new JTabbedPane();
    panel.add(typistTabs, BorderLayout.CENTER);
    panel.setPreferredSize(new Dimension(320, 400));
    refreshTypistTabs(); // create initial tabs
    return panel;
}

// helper: refresh tabs to match seatSpinner value
private void refreshTypistTabs() {
    if (typistTabs == null) return;
    int seats = (Integer) seatSpinner.getValue(); // spinner controls number of tabs
    typistTabs.removeAll();
    typistConfigs.clear();
    for (int i = 1; i <= seats; i++) {
        TypistConfigGUI cfg = new TypistConfigGUI();
        typistConfigs.add(cfg);
        typistTabs.addTab("Typist " + i, cfg);
    }
    typistTabs.revalidate();
    typistTabs.repaint();
}



    private void createComponents() {
       preDefinedRadio = new JRadioButton("Pre-defined Passage");
        customRadio = new JRadioButton("Custom Passage");
        ButtonGroup passageGroup = new ButtonGroup();
        passageGroup.add(preDefinedRadio);
        passageGroup.add(customRadio); 

        passageDropdown = new JComboBox<>(new String[] {"Short passage (~50 words)", "Medium passage (~100 words)", "Long passage (~200 words)"});

        passagePreview = new JTextArea(4, 40);
        passagePreview.setLineWrap(true);
        passagePreview.setWrapStyleWord(true);
        passagePreview.setEditable(false);
        passagePreview.setFont(new Font("Monospaced", Font.PLAIN, 13));
        passagePreview.setBackground(new Color(240, 240, 240));

        customInput = new JTextArea(4, 40);
        customInput.setLineWrap(true);
        customInput.setWrapStyleWord(true);
        customInput.setFont(new Font("Monospaced", Font.PLAIN, 13));
        customInput.setVisible(false);

        // Seat Count
        seatSpinner = new JSpinner(new SpinnerNumberModel(2, 2, 6, 1));
        seatSpinner.setPreferredSize(new Dimension(60, 30));

        // Modifiers
        autoCorrect = new JCheckBox("Autocorrect - slideBack amount halved");
        caffeineMode = new JCheckBox("Caffeine Mode - speed boost for first 10 turns, higher burnout risk ");
        nightShift = new JCheckBox("Night Shift - reduced visibility, accuracy reduced for all typists" );

        // Passage toggle listeners
        preDefinedRadio.addActionListener(e -> {
            passageDropdown.setVisible(true);
            passagePreview.setVisible(true);
            customInput.setVisible(false);
        });

        customRadio.addActionListener(e -> {
            passageDropdown.setVisible(false);
            passagePreview.setVisible(false);
            customInput.setVisible(true);
        });

        // Update preview when passage dropdown changes
        passageDropdown.addActionListener(e -> updatePreview());
        updatePreview();
        

    }

    public boolean isAutoCorrectEnabled() {
        return autoCorrect.isSelected();
    }

    public boolean isCaffeineModeEnabled() {
        return caffeineMode.isSelected();
    }

    public boolean isNightShiftEnabled() {
        return nightShift.isSelected();
    }

    // Box 1 - Passage selection
    private JPanel buildPassage() {
        JPanel panel = new JPanel(new BorderLayout(5,5));
        panel.setBorder(BorderFactory.createTitledBorder("Passage Selection"));

        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topRow.add(preDefinedRadio);
        topRow.add(customRadio);
        topRow.add(passageDropdown);

        panel.add(topRow, BorderLayout.NORTH);
        panel.add(new JScrollPane(passagePreview), BorderLayout.CENTER);
        panel.add(new JScrollPane(customInput), BorderLayout.SOUTH);

        return panel;
    }

    // Box 2 - Seat selection
    private JPanel buildSeatSelection() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Number of Typists"));
        panel.add(new JLabel("Number of typists:"));
        panel.add(new JLabel("Typists (2-6):"));
        panel.add(seatSpinner);
        seatSpinner.addChangeListener(e -> refreshTypistTabs());
        return panel;

    }

    // Box 3 - Modifiers
    private JPanel buildModifiers() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5));
        panel.add(autoCorrect);
        panel.add(caffeineMode);
        panel.add(nightShift);
        return panel;
    }

    // Start Button Bar
    private JPanel buildStartBar() {
        historyButton = new JButton("Historical Data");
        historyButton.addActionListener(e -> showHistoricalDataDialog());

        compareButton = new JButton("Compare Typists");
        compareButton.addActionListener(e -> showComparisonDialog(TypistStatsStore.getKnownTypistNames()));

        startRace = new JButton("Start Race ->");
        startRace.setFont(new Font("Arial", Font.BOLD, 14));
        startRace.addActionListener(e -> handleStart());

        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bar.add(historyButton);
        bar.add(compareButton);
        bar.add(startRace);
        return bar;
    
    }

    // Passage library for dropdown selection
    private void updatePreview() {
        String[] passages = {
            "The quick brown fox jumps over the lazy dog.",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
            "In a hole in the ground there lived a hobbit. Not a nasty, dirty, wet hole, filled with the ends of worms and an oozy smell, nor yet a dry, bare, sandy hole with nothing in it to sit down on or to eat: it was a hobbit-hole, and that means comfort."
        };
        passagePreview.setText(passages[passageDropdown.getSelectedIndex()]);
    }

    // Handles start button click
    private void handleStart() {
        String passage = customRadio.isSelected() ? customInput.getText().trim() : passagePreview.getText().trim();

        if (passage.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select or enter a passage.", "Missing Passage", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Typist> typists = new ArrayList<>();
        List<JTextPane> passagePanes = new ArrayList<>();
        List<JProgressBar> progressBars = new ArrayList<>();
        List<Color> typedColors = new ArrayList<>();
        List<Color> cursorColors = new ArrayList<>();

        for (TypistConfigGUI cfg : typistConfigs) {
            Typist typist = new Typist(cfg, this);
            typists.add(typist);
            typedColors.add(lighten(cfg.getCursorColour()));
            cursorColors.add(cfg.getCursorColour());
        }

        JFrame raceFrame = new JFrame("Typing Race");
        raceFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        raceFrame.setLayout(new BorderLayout(10, 10));

        JPanel lanes = new JPanel();
        lanes.setLayout(new BoxLayout(lanes, BoxLayout.Y_AXIS));
        lanes.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (int i = 0; i < typists.size(); i++) {
            Typist typist = typists.get(i);

            JPanel lane = new JPanel();
            lane.setLayout(new BorderLayout(6, 6));
            lane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));

            JLabel header = new JLabel(typist.getName() + "  (" + typist.getSymbol() + ")");
            header.setFont(header.getFont().deriveFont(Font.BOLD));
            lane.add(header, BorderLayout.NORTH);

            JTextPane passagePane = new JTextPane();
            passagePane.setEditable(false);
            passagePane.setFont(new Font("Monospaced", Font.PLAIN, 13));
            passagePane.setBackground(Color.WHITE);
            passagePane.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
            passagePane.setText(passage);
            updateLaneText(passagePane, passage, 0, typedColors.get(i), cursorColors.get(i));
            passagePanes.add(passagePane);

            JScrollPane passageScroll = new JScrollPane(passagePane,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            lane.add(passageScroll, BorderLayout.CENTER);

            JProgressBar progressBar = new JProgressBar(0, passage.length());
            progressBar.setValue(0);
            progressBar.setStringPainted(true);
            progressBar.setString("0/" + passage.length());
            progressBar.setForeground(cursorColors.get(i));
            progressBars.add(progressBar);
            lane.add(progressBar, BorderLayout.SOUTH);

            lanes.add(lane);
            lanes.add(Box.createVerticalStrut(10));
        }

        raceFrame.add(new JScrollPane(lanes), BorderLayout.CENTER);
        raceFrame.setSize(900, 700);
        raceFrame.setLocationRelativeTo(this);
        raceFrame.setVisible(true);

        // Build the model-driven race and use it to advance turns from the GUI
        TypingRace race = new TypingRace(this, typistConfigs, passage);
        Map<String, Double> preRaceAccuracy = new HashMap<>();
        for (Typist t : race.getTypists()) {
            preRaceAccuracy.put(t.getName(), t.getAccuracy());
        }
        final long startNanos = System.nanoTime();
        final boolean[] raceOver = {false};

        Timer timer = new Timer(200, e -> {
            if (raceOver[0]) {
                ((Timer) e.getSource()).stop();
                return;
            }

            // advance the simulation one turn via the central model
            boolean finished = race.advanceTurn(this);

            List<Typist> ts = race.getTypists();
            for (int i = 0; i < ts.size(); i++) {
                Typist t = ts.get(i);
                progressBars.get(i).setValue(t.getProgress());
                progressBars.get(i).setString(t.getProgress() + "/" + passage.length());
                updateLaneText(passagePanes.get(i), passage, t.getProgress(), typedColors.get(i), cursorColors.get(i));
            }

            if (finished) {
                raceOver[0] = true;
                ((Timer) e.getSource()).stop();
                List<Typist> ranked = new ArrayList<>(ts);
                ranked.sort((a, b) -> {
                    int progressCmp = Integer.compare(b.getProgress(), a.getProgress());
                    if (progressCmp != 0) {
                        return progressCmp;
                    }
                    int wpmA = TypingRace.calculateWPM(passage.length(), a.getProgress(), startNanos);
                    int wpmB = TypingRace.calculateWPM(passage.length(), b.getProgress(), startNanos);
                    return Integer.compare(wpmB, wpmA);
                });

                Map<Typist, Integer> finishingPositions = new HashMap<>();
                for (int i = 0; i < ranked.size(); i++) {
                    finishingPositions.put(ranked.get(i), i + 1);
                }

                Typist winner = ranked.get(0);
                StringBuilder summary = new StringBuilder();
                summary.append("And the winner is... ").append(winner.getName()).append("!\n\n");
                summary.append("Race Results\n");
                for (Typist t : ts) {
                    int position = finishingPositions.getOrDefault(t, ranked.size());
                    double oldAccuracyPercent = preRaceAccuracy.getOrDefault(t.getName(), t.getAccuracy()) * 100.0;
                    double finalAccuracyPercent = race.calculateFinalAccuracyPercentage(t);
                    t.setAccuracy(finalAccuracyPercent / 100.0);
                    int finalWpm = TypingRace.calculateWPM(passage.length(), t.getProgress(), startNanos);
                    double accuracyDelta = finalAccuracyPercent - oldAccuracyPercent;

                    TypistStatsStore.recordRaceResult(
                        t.getName(),
                        position,
                        finalWpm,
                        finalAccuracyPercent,
                        t.getTotalBurnoutTurns(),
                        t.getBurnoutEventCount());

                    TypistCareerStats career = TypistStatsStore.getStats(t.getName());
                    int personalBest = career == null ? finalWpm : career.getPersonalBestWpm();

                    summary.append(t.getName())
                        .append(" — Position: ").append(position)
                        .append(" — WPM: ").append(finalWpm)
                        .append(", Accuracy: ").append(finalAccuracyPercent).append("%")
                        .append(" (\u0394 ").append(String.format("%+.2f", accuracyDelta)).append("%)")
                        .append(", Burnout turns: ").append(t.getTotalBurnoutTurns())
                        .append(", Burnout events: ").append(t.getBurnoutEventCount())
                        .append(", Personal Best WPM: ").append(personalBest)
                        .append("\n");
                }

                summary.append("\nTip: Use 'Historical Data' and 'Compare Typists' for trends and side-by-side metrics.");

                JOptionPane.showMessageDialog(this,
                    summary.toString(),
                    "Race Finished",
                    JOptionPane.INFORMATION_MESSAGE);

                showHistoricalDataDialog();
            }
        });
        timer.start();
    }

    private void showHistoricalDataDialog() {
        Map<String, TypistCareerStats> statsByName = TypistStatsStore.getAllStats();
        if (statsByName.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No race history yet. Run at least one race first.",
                "Historical Data",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder report = new StringBuilder();
        report.append("Historical Data\n\n");

        for (Map.Entry<String, TypistCareerStats> entry : statsByName.entrySet()) {
            String name = entry.getKey();
            TypistCareerStats stats = entry.getValue();
            List<RaceRecord> history = stats.getRaceHistory();

            report.append(name)
                .append("\n")
                .append("  Races: ").append(stats.getRaceCount())
                .append(" | PB WPM: ").append(stats.getPersonalBestWpm())
                .append(" | Avg WPM: ").append(String.format("%.1f", stats.getAverageWpm()))
                .append(" | Avg Accuracy: ").append(String.format("%.2f", stats.getAverageAccuracyPercent())).append("%")
                .append(" | Avg Position: ").append(String.format("%.2f", stats.getAveragePosition()))
                .append(" | Total Burnout Events: ").append(stats.getTotalBurnoutEvents())
                .append("\n");

            report.append("  WPM Trend: ").append(buildTrend(history, "WPM")).append("\n");
            report.append("  Accuracy Trend: ").append(buildTrend(history, "Accuracy")).append("\n");
            report.append("  Position Trend: ").append(buildTrend(history, "Position")).append("\n");

            report.append("  Race Log:\n");
            for (int i = 0; i < history.size(); i++) {
                RaceRecord r = history.get(i);
                report.append("    #").append(i + 1)
                    .append(" Pos ").append(r.getFinishingPosition())
                    .append(" | WPM ").append(r.getWpm())
                    .append(" | Acc ").append(String.format("%.2f", r.getAccuracyPercent())).append("%")
                    .append(" | Burnout Turns ").append(r.getBurnoutTurns())
                    .append(" | Burnout Events ").append(r.getBurnoutEvents())
                    .append("\n");
            }
            report.append("\n");
        }

        JTextArea area = new JTextArea(report.toString(), 26, 100);
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        area.setCaretPosition(0);
        JScrollPane scroll = new JScrollPane(area);

        JOptionPane.showMessageDialog(this,
            scroll,
            "Historical Data",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void showComparisonDialog(List<String> candidateNames) {
        if (candidateNames == null || candidateNames.size() < 2) {
            JOptionPane.showMessageDialog(this,
                "Need at least two typists with recorded history to compare.",
                "Comparison View",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        java.util.List<JCheckBox> checkBoxes = new java.util.ArrayList<>();
        for (String n : candidateNames) {
            JCheckBox cb = new JCheckBox(n);
            checkBoxes.add(cb);
            listPanel.add(cb);
        }

        JButton selectAllButton = new JButton("Select All");
        selectAllButton.addActionListener(e -> checkBoxes.forEach(cb -> cb.setSelected(true)));

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> checkBoxes.forEach(cb -> cb.setSelected(false)));

        JComboBox<String> metricBox = new JComboBox<>(new String[] {
            "WPM", "Accuracy", "Finishing Position", "Burnout Events"
        });

        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.add(new JLabel("Select 2 or more typists and a metric (check boxes):"), BorderLayout.NORTH);
        panel.add(new JScrollPane(listPanel), BorderLayout.CENTER);

        JPanel metricPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        metricPanel.add(new JLabel("Metric:"));
        metricPanel.add(metricBox);
        metricPanel.add(selectAllButton);
        metricPanel.add(clearButton);
        panel.add(metricPanel, BorderLayout.SOUTH);

        int result = JOptionPane.showConfirmDialog(this,
            panel,
            "Comparison View",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        List<String> selected = new ArrayList<>();
        for (JCheckBox cb : checkBoxes) {
            if (cb.isSelected()) selected.add(cb.getText());
        }
        if (selected.size() < 2) {
            JOptionPane.showMessageDialog(this,
                "Please select at least two typists.",
                "Comparison View",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String metric = (String) metricBox.getSelectedItem();
        StringBuilder comparison = new StringBuilder();
        comparison.append("Comparison Metric: ").append(metric).append("\n\n");

        String bestName = "";
        double bestValue = Double.NEGATIVE_INFINITY;

        for (String name : selected) {
            TypistCareerStats stats = TypistStatsStore.getStats(name);
            if (stats == null || stats.getRaceCount() == 0) {
                comparison.append(name).append(" — no race history\n");
                continue;
            }

            double value;
            String valueLabel;
            if ("WPM".equals(metric)) {
                value = stats.getAverageWpm();
                valueLabel = String.format("Avg WPM %.2f", value);
            } else if ("Accuracy".equals(metric)) {
                value = stats.getAverageAccuracyPercent();
                valueLabel = String.format("Avg Accuracy %.2f%%", value);
            } else if ("Finishing Position".equals(metric)) {
                value = -stats.getAveragePosition();
                valueLabel = String.format("Avg Position %.2f", -value);
            } else {
                value = -stats.getTotalBurnoutEvents();
                valueLabel = String.format("Total Burnout Events %d", (int) -value);
            }

            comparison.append(name)
                .append(" — ")
                .append(valueLabel)
                .append(" | PB WPM ").append(stats.getPersonalBestWpm())
                .append(" | Races ").append(stats.getRaceCount())
                .append("\n");

            if (value > bestValue) {
                bestValue = value;
                bestName = name;
            }
        }

        if (!bestName.isEmpty()) {
            comparison.append("\nTop performer on selected metric: ").append(bestName);
        }

        JOptionPane.showMessageDialog(this,
            comparison.toString(),
            "Comparison Results",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private String buildTrend(List<RaceRecord> history, String metric) {
        if (history.isEmpty()) {
            return "No data";
        }

        StringBuilder trend = new StringBuilder();
        for (int i = 0; i < history.size(); i++) {
            RaceRecord record = history.get(i);
            if ("WPM".equals(metric)) {
                trend.append(record.getWpm());
            } else if ("Accuracy".equals(metric)) {
                trend.append(String.format("%.1f", record.getAccuracyPercent())).append("%");
            } else {
                trend.append(record.getFinishingPosition());
            }

            if (i < history.size() - 1) {
                trend.append(" -> ");
            }
        }
        return trend.toString();
    }

    private void updateLaneText(JTextPane pane, String passage, int progress, Color typedColor, Color cursorColor) {
        int visibleProgress = Math.min(progress, passage.length());
        int visibleCursor = Math.min(visibleProgress, passage.length() - 1);

        StyledDocument document = pane.getStyledDocument();
        try {
            document.remove(0, document.getLength());
        } catch (BadLocationException ignored) {
        }

        for (int i = 0; i < passage.length(); i++) {
            SimpleAttributeSet attrs = new SimpleAttributeSet();
            StyleConstants.setFontFamily(attrs, "Monospaced");
            StyleConstants.setFontSize(attrs, 13);
            StyleConstants.setForeground(attrs, Color.DARK_GRAY);

            if (i == visibleProgress && visibleProgress < passage.length()) {
                try {
                    SimpleAttributeSet cursorAttrs = new SimpleAttributeSet();
                    StyleConstants.setFontFamily(cursorAttrs, "Monospaced");
                    StyleConstants.setFontSize(cursorAttrs, 13);
                    StyleConstants.setForeground(cursorAttrs, cursorColor);
                    StyleConstants.setBold(cursorAttrs, true);
                    document.insertString(document.getLength(), "|", cursorAttrs);
                } catch (BadLocationException ignored) {
                }
            }

            if (i < visibleProgress) {
                StyleConstants.setBackground(attrs, typedColor);
                StyleConstants.setBold(attrs, true);
            } else if (i == visibleCursor) {
                StyleConstants.setBackground(attrs, cursorColor);
                StyleConstants.setBold(attrs, true);
            }

            try {
                document.insertString(document.getLength(), String.valueOf(passage.charAt(i)), attrs);
            } catch (BadLocationException ignored) {
            }
        }

        pane.setCaretPosition(0);
    }

    private Color lighten(Color color) {
        int red = Math.min(255, (color.getRed() + 255) / 2);
        int green = Math.min(255, (color.getGreen() + 255) / 2);
        int blue = Math.min(255, (color.getBlue() + 255) / 2);
        return new Color(red, green, blue);
    }

}



class Main {
        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> {
                JFrame frame = new JFrame("Race Configuration");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(600, 550);
                frame.setLocationRelativeTo(null);
                frame.add(new RaceConfigGUI());
                frame.setVisible(true);
            });
        }
    }


