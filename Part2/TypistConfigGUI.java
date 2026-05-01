import java.awt.*;
import javax.swing.*;

public class TypistConfigGUI extends JPanel {
    // Typist details
    private JTextField nameField;
    private JTextField symbolField;
    private JButton cursorColourButton;
    private JButton progressBarColourButton;
    private JLabel cursorColour;
    private JLabel progressBarColour;

    // Style and keyboard
    private JComboBox<String> typingStyleBox;
    private JComboBox<String> keyboardTypeBox;

    // Accessories
    private JCheckBox wristSupportBox;
    private JCheckBox energyDrinkBox;
    private JCheckBox headphonesBox;
    private JButton sponsorButton;
    private JLabel earningsBalanceLabel;
    private JLabel sponsorLabel;

    // Default colours for progress bar and cursor
    private Color chosenCursorColour = Color.YELLOW;
    private Color chosenProgressBarColour = Color.BLUE;
    private SponsorDeal sponsorDeal = SponsorDeal.none();
    private boolean accessorySelectionLocked = false;
    private boolean keyboardSelectionLocked = false;
    private int availableEarnings = 0;
    private int keyboardCostPaid = 0;

    private static final int MECHANICAL_KEYBOARD_COST = 0;
    private static final int MEMBRANE_KEYBOARD_COST = 10;
    private static final int TOUCHSCREEN_KEYBOARD_COST = 15;
    private static final int STENOGRAPHY_KEYBOARD_COST = 20; 
    private static final int WRIST_SUPPORT_COST = 25;



    public TypistConfigGUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create components
        createComponents();

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(buildIdentityBox());
        content.add(Box.createVerticalStrut(10));
        content.add(buildStyleBox());
        content.add(Box.createVerticalStrut(10));
        content.add(buildAccessoriesBox());

        add(new JScrollPane(content), BorderLayout.CENTER);

    }

    // Create and initialize components
    private void createComponents() {
        // typist details components
        nameField = new JTextField("Enter name of typist", 15);
        nameField.addActionListener(e -> syncEarningsFromTypistName());
        symbolField = new JTextField("Enter Symbol (a character or an emoji)", 3);

        // Highlight colour for cursor
        cursorColour = new JLabel(" ");
        cursorColour.setOpaque(true);
        cursorColour.setBackground(chosenCursorColour);
        cursorColour.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        cursorColour.setPreferredSize(new Dimension(60, 25));

        cursorColourButton = new JButton("Choose Cursor Colour");
        cursorColourButton.addActionListener(e -> {
            Color selected = JColorChooser.showDialog(this, "Choose Cursor Colour", chosenCursorColour);
            if (selected != null) {
                chosenCursorColour = selected;
                cursorColour.setBackground(chosenCursorColour);
            }
        });

        // Set progress bar colour
        progressBarColour = new JLabel(" ");
        progressBarColour.setOpaque(true);
        progressBarColour.setBackground(chosenProgressBarColour);
        progressBarColour.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        progressBarColour.setPreferredSize(new Dimension(60, 25));



        progressBarColourButton = new JButton("Choose Progress Bar Colour");
        progressBarColourButton.addActionListener(e -> {
            Color selected = JColorChooser.showDialog(this, "Choose Progress Bar Colour", chosenProgressBarColour);
            if (selected != null) {
                chosenProgressBarColour = selected;
                progressBarColour.setBackground(selected);
            }
        });



        progressBarColour = new JLabel("  ");
        cursorColour.setOpaque(true);
        progressBarColour.setOpaque(true);
        cursorColour.setBackground(Color.LIGHT_GRAY);
        progressBarColour.setBackground(Color.LIGHT_GRAY);

        // typing style components
        String[] typingStyles = {"Touch Typist", "Hunt & Peck", "Phone Thumbs", "Voice-to-Text"};
        typingStyleBox = new JComboBox<>(typingStyles);
        typingStyleBox.setSelectedIndex(0);
        // keyboard type components
        String[] keyboardTypes = {"Mechanical", "Membrane", "Touchscreen", "Stenography"};
        keyboardTypeBox = new JComboBox<>(keyboardTypes);
        keyboardTypeBox.setSelectedIndex(0);
        keyboardTypeBox.addActionListener(e -> handleKeyboardSelection());

        // Accessory components
        wristSupportBox = new JCheckBox("Wrist Support Upgrade (25 coins)");
        energyDrinkBox = new JCheckBox("Energy Drink");
        headphonesBox = new JCheckBox("Headphones");

        wristSupportBox.addActionListener(e -> handleUpgradeToggle(wristSupportBox, WRIST_SUPPORT_COST, "wrist support"));

        sponsorButton = new JButton("Choose Sponsor Deal");
        sponsorButton.addActionListener(e -> chooseSponsorDeal());

        earningsBalanceLabel = new JLabel();
        sponsorLabel = new JLabel();
        refreshStatusLabels();

    }


    // Build panels for different sections of the GUI
    private JPanel buildIdentityBox() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Typist Identity"));

        panel.add(new JLabel("Name:"));
        panel.add(nameField);

        panel.add(new JLabel("Symbol:"));
        panel.add(symbolField);

        panel.add(cursorColourButton);
        panel.add(cursorColour);

        panel.add(progressBarColourButton);
        panel.add(progressBarColour);

        return panel;
    }

    private JPanel buildStyleBox() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Typing Style & Keyboard"));
        panel.add(new JLabel("Typing Style:"));
        panel.add(typingStyleBox);
        panel.add(new JLabel("Keyboard Type:"));
        panel.add(keyboardTypeBox);

        return panel;
    }

    private JPanel buildAccessoriesBox() {
        JPanel panel = new JPanel(new GridLayout(7, 1, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Accessories"));
        panel.add(wristSupportBox);
        panel.add(energyDrinkBox);
        panel.add(headphonesBox);
        panel.add(sponsorButton);
        panel.add(earningsBalanceLabel);
        panel.add(sponsorLabel);

        return panel;
    }

        public String get_Name() {
        return nameField.getText().trim();
    }

    @Override
    public String getName() {
        return get_Name();
    }

    public char getSymbol() {
        return symbolField.getText().trim().charAt(0);
    }

    public Color getCursorColour() {
        return chosenCursorColour;
    }

    public Color getProgressBarColour() {
        return chosenProgressBarColour;
    }

    public int getTypingStyle() {
        return typingStyleBox.getSelectedIndex();
    }

    public int getKeyboardType() {
        return keyboardTypeBox.getSelectedIndex();
    }

    public boolean isWristSupportEnabled() {
        return wristSupportBox.isSelected();
    }

    public boolean isEnergyDrinkEnabled() {
        return energyDrinkBox.isSelected();
    }

    public boolean isHeadphonesEnabled() {
        return headphonesBox.isSelected();
    }

    public SponsorDeal getSponsorDeal() {
        return sponsorDeal;
    }

    public void addCoins(int amount) {
        if (amount <= 0) {
            return;
        }
        availableEarnings += amount;
        refreshStatusLabels();
    }

    private void syncEarningsFromTypistName() {
        String typistName = get_Name();
        TypistCareerStats stats = TypistStatsStore.getStats(typistName);
        availableEarnings = stats == null ? 0 : stats.getTotalEarnings();
        keyboardCostPaid = 0;
        refreshStatusLabels();
    }

    private boolean spendEarnings(int amount, String reason) {
        if (amount <= 0) {
            return true;
        }
        if (availableEarnings < amount) {
            return false;
        }

        String typistName = get_Name();
        TypistCareerStats stats = typistName.isEmpty() ? null : TypistStatsStore.getStats(typistName);
        if (stats != null) {
            TypistStatsStore.recordEarningsDeduction(typistName, amount, reason);
            // refresh local view from persistent store
            TypistCareerStats updated = TypistStatsStore.getStats(typistName);
            availableEarnings = updated == null ? availableEarnings - amount : updated.getTotalEarnings();
        } else {
            // no persistent record - update local balance
            availableEarnings -= amount;
        }
        refreshStatusLabels();
        return true;
    }

    private void handleKeyboardSelection() {
        if (keyboardSelectionLocked) {
            return;
        }
        String typistName = get_Name();
        int selectedIndex = keyboardTypeBox.getSelectedIndex();
        int selectedCost = getKeyboardSelectionCost(selectedIndex);
        int additionalCost = Math.max(0, selectedCost - keyboardCostPaid);
        if (additionalCost == 0) {
            return;
        }

        if (!spendEarnings(additionalCost, "Keyboard upgrade: " + keyboardTypeBox.getSelectedItem())) {
            keyboardSelectionLocked = true;
            keyboardTypeBox.setSelectedIndex(getKeyboardSelectionForPaidCost(keyboardCostPaid));
            keyboardSelectionLocked = false;
            JOptionPane.showMessageDialog(
                this,
                "Not enough earnings for this keyboard. Run more races to earn at least " + additionalCost + " more earnings.",
                "Insufficient Earnings",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        keyboardCostPaid = selectedCost;
        refreshStatusLabels();
    }

    private int getKeyboardSelectionCost(int selectedIndex) {
        if (selectedIndex == 1) {
            return MEMBRANE_KEYBOARD_COST;
        }
        if (selectedIndex == 2) {
            return TOUCHSCREEN_KEYBOARD_COST;
        }
        if (selectedIndex == 3) {
            return STENOGRAPHY_KEYBOARD_COST;
        }
        return MECHANICAL_KEYBOARD_COST;
    }

    private int getKeyboardSelectionForPaidCost(int paidCost) {
        if (paidCost >= STENOGRAPHY_KEYBOARD_COST) {
            return 3;
        }
        if (paidCost >= TOUCHSCREEN_KEYBOARD_COST) {
            return 2;
        }
        if (paidCost >= MEMBRANE_KEYBOARD_COST) {
            return 1;
        }
        return 0;
    }

    private void chooseSponsorDeal() {
        SponsorDeal[] choices = SponsorDeal.choices();
        SponsorDeal selected = (SponsorDeal) JOptionPane.showInputDialog(
            this,
            "Pick a sponsor deal for this typist:",
            "Sponsor Deals",
            JOptionPane.PLAIN_MESSAGE,
            null,
            choices,
            sponsorDeal);

        if (selected != null) {
            sponsorDeal = selected;
            refreshStatusLabels();
        }
    }

    private void handleUpgradeToggle(JCheckBox box, int cost, String upgradeName) {
        if (accessorySelectionLocked) {
            return;
        }

        if (!box.isSelected()) {
            accessorySelectionLocked = true;
            box.setSelected(true);
            accessorySelectionLocked = false;
            return;
        }

        if (!spendEarnings(cost, upgradeName + " upgrade")) {
            accessorySelectionLocked = true;
            box.setSelected(false);
            accessorySelectionLocked = false;
            JOptionPane.showMessageDialog(
                this,
                "Not enough earnings for " + upgradeName + ". Run more races to earn at least " + cost + " earnings.",
                "Insufficient Earnings",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // `spendEarnings` already updates `availableEarnings` (persisted or local), so just refresh
        refreshStatusLabels();
    }

    private void refreshStatusLabels() {
        earningsBalanceLabel.setText("Earnings available: " + availableEarnings);
        sponsorLabel.setText("Sponsor: " + sponsorDeal.toString());
    }

}

// Removed local test Main to avoid duplicate main class across files.












        


    


