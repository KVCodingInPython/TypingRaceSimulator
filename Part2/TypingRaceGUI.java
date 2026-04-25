import java.awt.*;
import javax.swing.*;

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

    public String passageLibrary(JComboBox<String> presetPassage) {
        if (presetPassage.getSelectedItem().equals("Short passage")) {
            return "The quick brown fox jumps over the lazy dog.";
        } else if (presetPassage.getSelectedItem().equals("Medium passage")) {
            return "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
        } else if (presetPassage.getSelectedItem().equals("Long passage")) {
            return "In a hole in the ground there lived a hobbit. Not a nasty, dirty, wet hole, filled with the ends of worms and an oozy smell, nor yet a dry, bare, sandy hole with nothing in it to sit down on or to eat: it was a hobbit-hole, and that means comfort.";
        } else {
            return "";
        }
    } 

    


    public JPanel buildPassageDisplay() {
        JPanel panel = new JPanel(new BorderLayout(8,8));
        panel.setBorder(BorderFactory.createTitledBorder("Passage Selection"));
        JCheckBox customPassageCheckBox = new JCheckBox("Use custom passage");
        panel.add(customPassageCheckBox, BorderLayout.NORTH);
        JLabel presetPasssageLabel = new JLabel("Select a preset passage:");
        panel.add(presetPasssageLabel, BorderLayout.EAST);
        JComboBox <String> presetPassage = new JComboBox<>(new String[] {"", "Short passage", "Medium passage", "Long passage"});
        panel.add(presetPassage, BorderLayout.NORTH);
        return panel;
    }

}


class Main {
        public static void main(String[] args) {
            JFrame parentFrame = new JFrame("Race Configuration");
            RaceConfigGUI raceConfigGUI = new RaceConfigGUI();
            parentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            parentFrame.setLayout(new BorderLayout(10,10));

            parentFrame.add(raceConfigGUI.buildPassageDisplay(), BorderLayout.NORTH);
            parentFrame.setSize(400, 300);
            parentFrame.setVisible(true);
        }
}


