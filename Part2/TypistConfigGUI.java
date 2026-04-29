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

    // Default colours for progress bar and cursor
    private Color chosenCursorColour = Color.YELLOW;
    private Color chosenProgressBarColour = Color.BLUE;



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

        // Accessory components
        wristSupportBox = new JCheckBox("Wrist Support");
        energyDrinkBox = new JCheckBox("Energy Drink");
        headphonesBox = new JCheckBox("Headphones");

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
        JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Accessories"));
        panel.add(wristSupportBox);
        panel.add(energyDrinkBox);
        panel.add(headphonesBox);

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

}

// Removed local test Main to avoid duplicate main class across files.












        


    


