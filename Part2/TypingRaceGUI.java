import java.awt.*;
import java.util.MissingFormatArgumentException;
import javax.swing.*;
import javax.swing.border.Border;

class RaceConfigGUI extends JPanel {
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

    // Start Race button
    private JButton startRace;

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

        add(new JScrollPane(content),BorderLayout.CENTER);
        add(buildStartBar(), BorderLayout.SOUTH);


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
        startRace = new JButton("Start Race ->");
        startRace.setFont(new Font("Arial", Font.BOLD, 14));
        startRace.addActionListener(e -> handleStart());

        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
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



