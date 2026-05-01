import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * A typing race simulation. Three typists race to complete a passage of text,
 * advancing character by character — or sliding backwards when they mistype.
 *
 * Originally written by Ty Posaurus, who left this project to "focus on his
 * two-finger technique". He assured us the code was "basically done".
 * We have found evidence to the contrary.
 *
 * @author TyPosaurus
 * @version 0.7 (the other 0.3 is left as an exercise for the reader)
 */
public class TypingRace
{
    private int passageLength;   // Total characters in the passage to type
    private List<Typist> typists;
    private String passage;
    private boolean raceFinished;
    private RaceConfigGUI raceConfig;
    // Modifier selection components
    private int turnCount = 0;
    private boolean autoCorrectEnabled;
    private boolean caffeineModeEnabled;
    private boolean nightShiftEnabled;

    // Constructor for the GUI version
    public TypingRace(RaceConfigGUI config, List<TypistConfigGUI> typistConfigs, String passage) {
        this.passage = passage;
        this.passageLength = passage.length();
        this.raceFinished = false;
        this.typists = new ArrayList<Typist>();
        this.autoCorrectEnabled = config.isAutoCorrectEnabled();
        this.caffeineModeEnabled = config.isCaffeineModeEnabled();
        this.nightShiftEnabled = config.isNightShiftEnabled();
        this.raceConfig = config;

        // Dynamically builds one seat in the race for each typist provided in race config
        for (TypistConfigGUI tc : typistConfigs) {
            Typist t = new Typist(tc, config);
            typists.add(t);
        }
    }

    /**
     * Advance the race by a single turn. Returns true if the race finished
     * as a result of this turn.
     */
    public boolean advanceTurn() {
        if (raceFinished) return true;
        turnCount++;
        for (Typist t : typists) {
            advanceTypist(t, SLIDE_BACK_AMOUNT, BURNOUT_DURATION, this.raceConfig);
            if (raceFinishedBy(t)) {
                raceFinished = true;
            }
        }
        return raceFinished;
    }

    public boolean advanceTurn(RaceConfigGUI config) {
        if (raceFinished) {
            return true;
        }
        turnCount++;
        for (Typist t : typists) {
            advanceTypist(t, SLIDE_BACK_AMOUNT, BURNOUT_DURATION, config);

            if (turnCount <= 10 && this.caffeineModeEnabled == true) {
                t.typeCharacter();  
            }
            if (raceFinishedBy(t)) {
                raceFinished = true;
            }
        }
        return raceFinished;
    }
    
    public List<Typist> getTypists() {
        return typists;
    }

    public String getPassage() {
        return passage;
    }

    public int getPassageLength() {
        return passageLength;
    }

    public boolean isRaceFinished() {
        return raceFinished;
    }





    // Accuracy thresholds for mistype and burnout events
    // (Ty tuned these values "by feel". They may need adjustment.)
    private static final double MISTYPE_BASE_CHANCE = 0.3;
    private static final int    SLIDE_BACK_AMOUNT   = 2;
    private static final int    BURNOUT_DURATION     = 3;

    /**
     * Constructor for objects of class TypingRace.
     * Sets up the race with a passage of the given length.
     * Initially there are no typists seated.
     *
     * @param passageLength the number of characters in the passage to type
     */
    public TypingRace(int passageLength)
    {
        this.passageLength = passageLength;
    }

    /**
     * Seats a typist at the given seat number (1, 2, or 3).
     *
     * @param theTypist  the typist to seat
     * @param seatNumber the seat to place them in (1–3)
     */
    public void addTypist(Typist theTypist, int seatNumber)
    {
        for (Typist t : typists) {
            for (int i = 0; i < typists.size(); i++) {
                if (typists.get(i) == null) {
                    typists.set(i, theTypist);
                    return;
                }
            }
        }
    }

    /**
     * Starts the typing race.
     * All typists are reset to the beginning, then the simulation runs
     * turn by turn until one typist completes the full passage.
     *
     * Note from Ty: "I didn't bother printing the winner at the end,
     * you can probably figure that out yourself."
     */
    public void startRace(RaceConfigGUI config)
    {
        long startNanos = System.nanoTime();
        boolean finished = false;

        // Reset all typists to the start of the passage
        // (Ty was in a hurry here)
        for (Typist t : typists) {
            t.resetToStart();
        }

        while (!finished)
        {
            // advance global turn counter (used by caffeine mode etc.)
            this.turnCount++;
            for (Typist t : typists) {
            // Advance each typist by one turn
                advanceTypist(t, SLIDE_BACK_AMOUNT, BURNOUT_DURATION, config);

            // Print the current state of the race
            printRace(passageLength, startNanos);

            // Check if any typist has finished the passage
                if ( raceFinishedBy(t) )
            {
                finished = true;
                    double oldAccuracy = t.getAccuracy();
                    double finalAccuracyPercent = this.calculateFinalAccuracyPercentage(t);
                    double newAccuracy = finalAccuracyPercent / 100.0;
                    t.setAccuracy(newAccuracy);
                    System.out.println("And the winner is... " + t.getName() + "!");
                    System.out.println("Final summary: accuracy " + finalAccuracyPercent + "%"
                        + ", WPM " + getWPM(t, startNanos)
                        + ", burnout turns " + t.getTotalBurnoutTurns());
                    if (oldAccuracy < newAccuracy)
                    {
                        System.out.println("Final accuracy improved from " + oldAccuracy);
                    }
                    else
                    {
                        System.out.println("Final accuracy worsened from " + oldAccuracy);
                    }
                }
            }

            // Wait 200ms between turns so the animation is visible
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (Exception e) {}
        }


        // TODO (Task 2a): Print the winner's name here
        // At the end of the simulation, print a summary for every typist
        System.out.println("\nFinal summaries:");
        for (Typist t : typists) {
            double finalAccuracyPercent = this.calculateFinalAccuracyPercentage(t);
            t.setAccuracy(finalAccuracyPercent / 100.0);
            int wpm = getWPM(t, startNanos);
            System.out.println(t.getName() + " — WPM: " + wpm + ", Accuracy: " + finalAccuracyPercent + "%, Burnout turns: " + t.getTotalBurnoutTurns());
            }
         
     
    }

    /**
     * Simulates one turn for a typist.
     *
     * If the typist is burnt out, they recover one turn's worth and skip typing.
     * Otherwise:
     *   - They may type a character (advancing progress) based on their accuracy.
     *   - They may mistype (sliding back) — the chance of a mistype should decrease
     *     for more accurate typists.
     *   - They may burn out — more likely for very high-accuracy typists
     *     who are pushing themselves too hard.
     *
     * @param theTypist the typist to advance
     */
    private void advanceTypist(Typist theTypist, int SLIDE_BACK_AMOUNT, int BURNOUT_DURATION, RaceConfigGUI config)
    {
        theTypist.resetMistyped(); // Clear mistyped state at the start of the turn
        
        if (theTypist.isBurntOut())
        {
            // Recovering from burnout — skip this turn
            theTypist.recoverFromBurnout();
            return;
        }

        // Attempt to type a character
        else if (Math.random() < theTypist.getAccuracy())
        {
            theTypist.typeCharacter();
            
        }

        // Mistype check — the probability should reflect the typist's accuracy
        double mistypeChance = MISTYPE_BASE_CHANCE * (1 - theTypist.getAccuracy());
        if (this.nightShiftEnabled == true) {
            mistypeChance *= 1.2; // 20% higher chance of mistyping under night shift
        }

        // Keyboard type modifiers to mistype chance
        if (theTypist.getHeadphones()) {
            mistypeChance = mistypeChance - (mistypeChance * 0.1); // Headphones reduce mistype chance by 10% ( +0.1x accuracy)
        }
        if (theTypist.hasBetterKeyboardUpgrade()) {
            mistypeChance = mistypeChance - (mistypeChance * 0.25); // Better keyboard reduces mistype chance by 25%
        }

        if (!theTypist.getHeadphones()) {
            if (theTypist.getKeyboardType() == 0) { // Mechanical
                mistypeChance = mistypeChance - (mistypeChance * 0.2); // -0.2x mistype chance ( +0.2x accuracy)
            }
            else if (theTypist.getKeyboardType() == 2) { // Touchscreen
                mistypeChance = mistypeChance + (mistypeChance * 1.8); // +1.8x mistype chance ( -1.8x accuracy)
            }
            else if (theTypist.getKeyboardType() == 3) { // Stenography
                mistypeChance = mistypeChance - (mistypeChance * 0.4); // -0.4x mistype chance ( +0.4x accuracy)
            }
        }
        if (Math.random() < mistypeChance)
        {
            // Auto-correct reduces slide back by half
            if (this.autoCorrectEnabled == true) {
                SLIDE_BACK_AMOUNT = (int) Math.floor(SLIDE_BACK_AMOUNT / 2);
            }
            theTypist.slideBack(SLIDE_BACK_AMOUNT, config);
            
        }

        // Burnout check — pushing too hard increases burnout risk
        // (probability scales with accuracy squared, capped at ~0.05)
            else
            {
                double burnoutProb = 0.05 * theTypist.getAccuracy() * theTypist.getAccuracy();

                // If caffeine mode is enabled in the race config, increase burnout risk
                if (this.caffeineModeEnabled == true) {
                    burnoutProb *= 1.5; // 50% higher risk under caffeine
                }

                // Typing style modifiers to burnout risk
                if (theTypist.getTypingStyle() == 0) { // Touch Typist
                    burnoutProb = burnoutProb - (burnoutProb * 0.2); // -0.2x burnout risk
        
                } else if (theTypist.getTypingStyle() == 1) { // Hunt & Peck
                    burnoutProb = burnoutProb + (burnoutProb * 0.1); // +0.1x burnout risk
                    
                } else if (theTypist.getTypingStyle() == 2) { // Phone Thumbs
                    burnoutProb = burnoutProb + (burnoutProb * 0.3); // +0.3x burnout risk

                } else if (theTypist.getTypingStyle() == 3) { // Voice-to-Text
                    burnoutProb = burnoutProb + (burnoutProb * 0.25); // +0.25x burnout risk
                }
                if (Math.random() < burnoutProb)
        {
                    if (theTypist.getWristSupport()) {
                        BURNOUT_DURATION = (int) Math.floor(BURNOUT_DURATION * 0.75); // Wrist support reduces burnout duration by 25%, rounds down to int
                    }
            theTypist.burnOut(BURNOUT_DURATION);
                }
        }
    }

    /**
     * Returns true if the given typist has completed the full passage.
     *
     * @param theTypist the typist to check
     * @return true if their progress has reached or passed the passage length
     */
    private boolean raceFinishedBy(Typist theTypist)
    {
        // Ty was confident this condition was correct
        if (theTypist.getProgress() >= passageLength)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Prints the current state of the race to the terminal.
     * Shows each typist's position along the passage, burnout state,
     * and a WPM estimate based on current progress.
     */
    private void printRace(int passageLength, long startNanos)
    {
        System.out.print('\u000C'); // Clear terminal

        System.out.println("  TYPING RACE — passage length: " + passageLength + " chars");
        multiplePrint('=', passageLength + 3);
        System.out.println();

        for (Typist t: typists) {
            printSeat(t, startNanos);
        System.out.println();
        }

        multiplePrint('=', passageLength + 3);
        System.out.println();
        System.out.println("  [~] = burnt out    [<] = just mistyped");
        System.out.println();
    }

    /**
     * Prints a single typist's lane.
     *
     * Examples:
     *   |          ⌨           | TURBOFINGERS (Accuracy: 0.85)
     *   |    [zz]              | HUNT_N_PECK  (Accuracy: 0.40) BURNT OUT (2 turns)
     *
     * Note: Ty forgot to show when a typist has just mistyped. That would
     * be a nice improvement — perhaps a [<] marker after their symbol.
     *
     * @param theTypist the typist whose lane to print
     */
    private void printSeat(Typist theTypist, long startNanos)
    {
        int spacesBefore = theTypist.getProgress();
        int spacesAfter  = passageLength - theTypist.getProgress();

        System.out.print('|');
        multiplePrint(' ', spacesBefore);

        // Always show the typist's symbol so they can be identified on screen.
        // Append ~ when burnt out so the state is visible without hiding identity.
        System.out.print(theTypist.getSymbol());
        if (theTypist.isBurntOut())
        {
            System.out.print("~");
            spacesAfter--; // symbol + ~ together take two characters
        }
        else if (theTypist.justMistyped())
        {
            System.out.print(" [<]");
            spacesAfter-=4; // symbol + [<] together take two characters
        }

        multiplePrint(' ', spacesAfter);
        System.out.print('|');
        System.out.print(' ');

        // Print name and accuracy
        if (theTypist.isBurntOut())
        {
            System.out.print(theTypist.getName()
                + " (Accuracy: " + theTypist.getAccuracy() + ")"
                 + " BURNT OUT (" + theTypist.getBurnoutTurnsRemaining() + " turns) " + " (WPM: " + getWPM(theTypist, startNanos) + ")");
        }
        
        else if (theTypist.justMistyped())
        {
            System.out.print(theTypist.getName()
                + " (Accuracy: " + theTypist.getAccuracy() + ")" + " <- just mistyped " + " (WPM: " + getWPM(theTypist, startNanos) + ")");
        }
        else
        {
            System.out.print(theTypist.getName()
                + " (Accuracy: " + theTypist.getAccuracy() + ")" + " (WPM: " + getWPM(theTypist, startNanos) + ")");
        }
    }
     public static int calculateWPM(int passageLength, int progress, long startNanos)
    {
        int WPM;

        double progressRatio = progress / (double) passageLength;

        if (progressRatio == 0.0)
        {
            return 0;
        }

        long elapsedNanos = System.nanoTime() - startNanos;
        double elapsedSeconds = elapsedNanos / 1_000_000_000.0;
    
        double estimatedTotalSeconds = elapsedSeconds / progressRatio;
        double estimatedMinutes = estimatedTotalSeconds / 60.0;

        WPM =  (int) (passageLength / 5.0 / estimatedMinutes);
        WPM = (int) Math.round(WPM);
        return WPM;
        
    }

    /**
     * Calculate the final accuracy for a typist based on progress and total
     * characters typed. Returns a value rounded to two decimal places in the
     * range [0.0, 1.0]. This is provided as a shared helper for GUI and
     * simulation code.
     */
    public double calculateFinalAccuracy(Typist typist)
    {
        return calculateFinalAccuracyPercentage(typist) / 100.0;
    }

    /**
     * Final accuracy percentage computed as proportion of the passage
     * correctly completed (progress / passageLength * 100). Mistypes are
     * intentionally not counted in this metric per spec.
     */
    public double calculateFinalAccuracyPercentage(Typist typist)
    {
        if (this.passageLength <= 0) {
            return Math.round(typist.getAccuracy() * 10000.0) / 100.0;
        }
        double accuracyPercent = (typist.getProgress() * 100.0) / this.passageLength;
        return Math.round(accuracyPercent * 100.0) / 100.0;
    }

    

     private int getWPM(Typist theTypist, long startNanos)
    {
        return calculateWPM(passageLength, theTypist.getProgress(), startNanos);
    }

    public static int computeRacePoints(int position, int wpm, int burnoutEvents) {
        int points = 0;
        // Points for finishing position
        if (position == 1) {
            points += 3;
        }
        else if (position == 2) {
            points += 2;
        }
        else if (position == 3) {
            points += 1;
        }

        // Bonus points for WPM thresholds
        if (wpm >= 20) {
            points += 3;
        }
        else if (wpm >= 15) {
            points += 2;
        }
        else if (wpm >= 10) {
            points += 1;
        }

        // Penalty points for burnout events
        points -= burnoutEvents; // 1 point penalty per burnout event

        return points;
    }

    public static int calculateEarnings(int position, int wpm, int burnoutEvents) {
        int baseEarnings = 0;
        // Base earnings for finishing positions 
        if (position == 1) {
            baseEarnings += 20;
        }
        else if (position == 2) {
            baseEarnings += 10;
        }
        else if (position == 3) {
            baseEarnings += 5;
        }

        // Bonus earnings for WPM thresholds
        if (wpm >= 20) {
            baseEarnings += 20;
        }
        else if (wpm >= 15) {
            baseEarnings += 10;
        }
        else if (wpm >= 10) {
            baseEarnings += 5;
        }

        // Penalty for burnout events
        baseEarnings -= burnoutEvents * 5; // £5 penalty per burnout event
        return Math.max(baseEarnings, 0); // Earnings can't be negative
    }

    

    /**
     * Prints a character a given number of times.
     *
     * @param aChar the character to print
     * @param times how many times to print it
     */
    private void multiplePrint(char aChar, int times)
    {
        int i = 0;
        while (i < times)
        {
            System.out.print(aChar);
            i = i + 1;
        }
    }

    public static void main(String[] args) {
    boolean Mistyped = false;
    TypingRace race = new TypingRace(40);
    race.addTypist(new Typist('①', "TURBOFINGERS", 0.85), 1);
    race.addTypist(new Typist('②', "QWERTY_QUEEN",  0.60), 2);
    race.addTypist(new Typist('③', "HUNT_N_PECK",   0.30), 3);
    race.startRace(new RaceConfigGUI());
    
}
}
