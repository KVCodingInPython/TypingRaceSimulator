import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;

    // Names of the three screens (cards)
    private static final String MAIN_MENU = "MENU";
    private static final String RACE_CONFIG = "RACE_CONFIG";
    private static final String TYPIST_CONFIG = "TYPIST_CONFIG";

    public MainFrame() {
        setTitle("Typing Race Simulator");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        cardPanel.add(new MainMenuPanel(this), MAIN_MENU);
        cardPanel.add(new RaceConfigGUI(this), RACE_CONFIG);
        
        add(cardPanel);
        cardLayout.show(cardPanel, MAIN_MENU);
    }

    public void displayMenu() {
        cardLayout.show(cardPanel, MAIN_MENU);
    }

    public void displayRaceConfig() {
        cardLayout.show(cardPanel, RACE_CONFIG);
    }

    public void displayTypistConfig() {
        TypistConfigPanel typistConfigPanel = new TypistConfigPanel(this, seatCount);
        cardPanel.add(typistPanel, TYPIST_CONFIG);
        cardLayout.show(cardPanel, TYPIST_CONFIG);
    }

    public static void main (String[] a) {
        SwingUtilities.invokeLater(() -> 
        new MainFrame().setVisible(true));

    }
}