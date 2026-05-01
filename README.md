# TypingRaceSimulator

Object Oriented Programming Project — ECS414U

## Project Structure

```
TypingRaceSimulator/
├── Part1/    # Textual simulation (Java, command-line)
    - Typist.java
    - TypingRace.java
└── Part2/    # GUI simulation (to be completed)
    - RaceConfigGUI.java
    - RaceRecord.java
    - SponsorDeal.java
    - TypingRace.java
    - typist_stats.csv
    - Typist.java
    - TypistCareerStats.java
    - TypistConfigGUI.java
    - TypistStatsStore.java
```

## Part 1 — Textual Simulation

### How to compile

```bash
cd Part1
javac Typist.java TypingRace.java
```

### How to run

The race is started by calling `startRace()` on a `TypingRace` object.
A simple way to test this is to add a `main` method to `TypingRace`, for example:

```java
public static void main(String[] args) {
    TypingRace race = new TypingRace(40);
    race.addTypist(new Typist('①', "TURBOFINGERS", 0.85), 1);
    race.addTypist(new Typist('②', "QWERTY_QUEEN",  0.60), 2);
    race.addTypist(new Typist('③', "HUNT_N_PECK",   0.30), 3);
    race.startRace();
}
```

Then run:

```bash
java TypingRace
```

## Part 2 — GUI Simulation
Description regarding handling the GUI version:

There are two panels allowing you to customise both the race and typist settings:
Race config:
- Passage Selection: pre-defined (short, medium or long) or custom which can be previewed.
- Number of Typists (2 - 6) (Adds an additional panel for each new typist added in typist config)
- Autocorrect - slideBack amount halved
- caffeineMode - speed boost for first 10 turns, higher burnout risk
- Night Shift - reduced visibility, accuracy reduced for all typists.
- Global leaderboard ranking typists by cumulative points gained in races accounting for positions, WPM, and burnout events.

- Typist config:
- Name, symbol (A character or an emoji). (Press 'Enter' key when entering name to update earnings available label).
- Cursor Colour (Highlighting the characters correctly typed) Default is yellow.
- Progress Bar Colour, which can be seen below the typist's name in their track. Default is blue.
- Typing Styles: Touch Typist, Hunt & Peck, Phone Thumbs, and Voice-to-Text.
- Keyboard types: Mechanical (default), Membrane (costs 10 coins), Touchscreen (costs 15 coins), and Stenography (20 coins).
Note: Keyboard costs are cumulative, so if membrane is purchased (10), then if switched to stenography you pay another 10 coins, not 20.
- Accessories: Wrist support (25 coins), Energy Drink (free) and Noise-cancelling Headphones (free).
- Sponsor Deals: No Sponsor (Default), KeyCorp (+50 coins if you finish with single burnout), SpeedForge(+35 coins if finish >= 55 WPM), and SteadyHands(+40 coins if finish with >= 90% accuracy).
- Earnings available for the typist is also listed below sponsor deals.
- Global financial leaderboard displayed below ranking typist's earnings.

- Historical Data: Paragraph for each typist detailing the number of races they have participated in, PB WPM, Avg WPM, Accuracy and Position.
It also displays trends for accuracy and position and shows race logs for each race, highlighting number of burnout events and number of burnout turns.

- Compare Typists: Need to select at least two or more typists to compare based on a given metric (WPM, Accuracy, Finishing position, burnout events). Can also select all typists for comparison.

- Start Race button in the bottom right of the panel, which when pressed, calls TypingRace, which then calls Typist, and creates a number of tracks given number of typists and the chosen passage for each typist, displaying a block cursor (|) advancing through the passage. 
- If the race ends, summaries for each typist is displayed, then historical data is updated and displayed, which can then be both closed, and both leaderboard and financial leaderboard will update.



To be implemented as part of the coursework. Place all GUI-related source files in this folder. The graphical version is started by calling `startRaceGUI()`.

## How to Run GUI
First compile each of the following files in Part2 using a terminal in VSCode:
javac RaceConfigGUI.java
javac RaceRecord.java
javac SponsorDeal.java
javac TypingRace.java
javac Typist.java
javac TypistCareerStats.java
javac TypistConfigGUI.java
javac TypistStatsStore.java

Then, to run the race:
'java Main', which calls the Main class which is to only be used in 'RaceConfigGUI.java'. An example of a Main class would be:
```java

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
```




## Dependencies

- Java Development Kit (JDK) 11 or higher
- No external libraries required for Part 1
- Part 2 uses Java Swing, Java AWT (for GUI components), and Java FX.

## Notes

- All code should compile and run using standard command-line tools without any IDE-specific configuration.
- The starter code in Part1 was originally written by Ty Posaurus. It contains known issues — finding and fixing them is part of the coursework.
