/**
 * Write a description of class Typist here.
 *
 * Starter code generously abandoned by Ty Posaurus, your predecessor,
 * who typed with two fingers and considered that "good enough".
 * He left a sticky note: "the slide-back thing is optional probably".
 * It is not optional. Good luck.
 *
 * @author Kaloyan Velikov (your name)
 * @version 1.0 (a version number or a date)
 */
public class Typist
{
    // Fields of class Typist
    // Hint: you will need six fields. Think carefully about their types.
    // One of them tracks how far along the passage the typist has reached.
    // Another tracks whether the typist is currently burnt out.
    // A third tracks HOW MANY turns of burnout remain (not just whether they are burnt out).
    // The remaining three should be fairly obvious.

    final private String typist_name;
    private char typist_position;
    private int typist_progress;
    private boolean TYPISTISBURNTOUT;
    private int burnout_remaining;
    private double typist_accuracy;
    private boolean JUSTMISTYPED;
    private int total_characters_typed;

    // GUI-related fields 
    private java.awt.Color colour; // Colour represents character correctly typed

    // Typing style index 
    // 0 = Touch Typist, 1 = Hunt & Peck, 2 = Phone Thumbs, 3 = Voice-to-Text
    private int typing_style;

    // Keyboard type index
    // 0 = Mechanical, 1 = Membrane, 2 = Touchscreen, 3 = Stenography
    private int keyboard_type;

    // Accessories
    private boolean wristSupport;
    private boolean energyDrink;
    private boolean headphones;

    // Modifier flags passed in from race configuration panel
    private boolean autoCorrectEnabled;
    private boolean caffeineModeEnabled;
    private boolean nightShiftEnabled;

    // Caffeine mode turn counter
    private int caffeine_turn_count= 0;

    // Constructor
    public Typist(TypistConfig config) {
        this.typist_name = config.getName();
        this.typist_position = config.getSymbol();
        this.typist_accuracy = config.getAccuracy();
        this.typing_style = config.getTypingStyle();
        this.keyboard_type = config.getKeyboardType();
        this.wristSupport = config.hasWristSupport();
        this.energyDrink = config.hasEnergyDrink();
        this.headphones = config.hasHeadphones();
        this.autoCorrectEnabled = config.isAutoCorrectEnabled();
        this.caffeineModeEnabled = config.isCaffeineModeEnabled();
        this.nightShiftEnabled = config.isNightShiftEnabled();

        // Base stats
        this.typist_progress = 0;
        this.TYPISTISBURNTOUT = false;
        this.burnout_remaining = 0;
        this.JUSTMISTYPED = false;
        this.total_characters_typed = 0;

        applyTypingStyle();
        applyKeyboardType();
        applyAccessories();
    }


    // Constructor of class Typist
    /**
     * Constructor for objects of class Typist.
     * Creates a new typist with a given symbol, name, and accuracy rating.
     *
     * @param typistSymbol  a single Unicode character representing this typist (e.g. '①', '②', '③')
     * @param typistName    the name of the typist (e.g. "TURBOFINGERS")
     * @param typistAccuracy the typist's accuracy rating, between 0.0 and 1.0
     */
    public Typist(char typistSymbol, String typistName, double typistAccuracy)
    {
        this.typist_position = typistSymbol;
        this.typist_name = typistName;
        this.typist_accuracy = typistAccuracy;
        this.typist_progress = 0;
        this.TYPISTISBURNTOUT = false;
        this.burnout_remaining = 0;
        this.JUSTMISTYPED = false;
        this.total_characters_typed = 0;
    }


    // Methods of class Typist

    /**
     * Sets this typist into a burnout state for a given number of turns.
     * A burnt-out typist cannot type until their burnout has worn off.
     *
     * @param turns the number of turns the burnout will last
     */
    public void burnOut(int turns)
    {
        this.TYPISTISBURNTOUT = true;
        this.burnout_remaining = turns;
        
        return;

    }

    /**
     * Reduces the remaining burnout counter by one turn.
     * When the counter reaches zero, the typist recovers automatically.
     * Has no effect if the typist is not currently burnt out.
     */
    public void recoverFromBurnout()
    {
            if (!this.TYPISTISBURNTOUT)
            {
                return;
            }

            if (this.burnout_remaining > 0)
            {
                this.burnout_remaining = this.burnout_remaining - 1;
            }

            if (this.burnout_remaining == 0)
            {
                this.TYPISTISBURNTOUT = false;
            }
        return;
    }

    /**
     * Returns the typist's accuracy rating.
     *
     * @return accuracy as a double between 0.0 and 1.0
     */
    public double getAccuracy()
    {
        return this.typist_accuracy;
    }



    /**
     * Returns the typist's current progress through the passage.
     * Progress is measured in characters typed correctly so far.
     * Note: this value can decrease if the typist mistypes.
     *
     * @return progress as a non-negative integer
     */
    public int getProgress()
    {
        return this.typist_progress;
    }

    /**
     * Returns the name of the typist.
     *
     * @return the typist's name as a String
     */
    public String getName()
    {
        return this.typist_name; // placeholder - replace with correct implementation
    }

    /**
     * Returns the character symbol used to represent this typist.
     *
     * @return the typist's symbol as a char
     */
    public char getSymbol()
    {
        return this.typist_position;
    }

    /**
     * Returns the number of turns of burnout remaining.
     * Returns 0 if the typist is not currently burnt out.
     *
     * @return burnout turns remaining as a non-negative integer
     */
    public int getBurnoutTurnsRemaining()
    {
        if (this.TYPISTISBURNTOUT == true)
        {
            return this.burnout_remaining;
        }
        return 0;
    }

    public boolean justMistyped() 
    {
        return this.JUSTMISTYPED;
    }

    public void resetMistyped() 
    {
        this.JUSTMISTYPED = false;
        return;
    }

    public int getTotalCharsTyped() 
    {
        return this.total_characters_typed;
    }

    /**
     * Resets the typist to their initial state, ready for a new race.
     * Progress returns to zero, burnout is cleared entirely.
     */
    public void resetToStart()
    {
        this.TYPISTISBURNTOUT = false;
        this.burnout_remaining = 0;
        this.typist_progress = 0;
        this.total_characters_typed = 0;

        return;

    }


    /**
     * Returns true if this typist is currently burnt out, false otherwise.
     *
     * @return true if burnt out
     */
    public boolean isBurntOut()
    {
        return this.TYPISTISBURNTOUT; // placeholder - replace with correct implementation
    }

    /**
     * Advances the typist forward by one character along the passage.
     * Should only be called when the typist is not burnt out.
     */
    public void typeCharacter()
    {
        this.JUSTMISTYPED = false;
        if (this.TYPISTISBURNTOUT == false)
        {
            this.typist_progress = this.typist_progress + 1;
            this.total_characters_typed = this.total_characters_typed + 1;
            return;
        }
        return;

    }

    /**
     * Moves the typist backwards by a given number of characters (a mistype).
     * Progress cannot go below zero — the typist cannot slide off the start.
     *
     * @param amount the number of characters to slide back (must be positive)
     */
    public void slideBack(int amount)
    {
        this.JUSTMISTYPED = true;
        if (amount > 0)
        {
            if ((this.typist_progress - amount) >= 0)
            {
                this.typist_progress = this.typist_progress - amount;
             
            }
            else
            {
                this.typist_progress = 0;
                System.out.println("Typist slides back to start");
             
            }
        }
        return;
        

    }

    /**
     * Sets the accuracy rating of the typist.
     * Values below 0.0 should be set to 0.0; values above 1.0 should be set to 1.0.
     *
     * @param newAccuracy the new accuracy rating
     */
    public void setAccuracy(double newAccuracy)
    {
        if (newAccuracy >= 0.0 && newAccuracy <= 1.0)
        {
            this.typist_accuracy = newAccuracy;
            return;
        }
        else if (newAccuracy < 0.0) {
            this.typist_accuracy = 0.0;
            return;
        }
        else if (newAccuracy > 1.0) {
            this.typist_accuracy = 1.0;
            return;
        }
        return;

    }

    /**
     * Sets the symbol used to represent this typist.
     *
     * @param newSymbol the new symbol character
     */
    public void setSymbol(char newSymbol)
    {
        this.typist_position = newSymbol;
        return;

    }






}
class Main {
    public static void main(String[] args) {
        // Create new Typist object
        Typist typer1 = new Typist('①', "TURBOFINGERS", 0.85);

        /* //'Typist Progress cannot go below 0' tests: Test Case 1
         typer1.resetToStart();
         typer1.slideBack(5);
         System.out.println(typer1.isBurntOut());
         */
        

        // 'resetToStart() method clears both progress and burnout state' tests: Test Case 3
        /* System.out.println("h");;
        typer1.TYPISTISBURNTOUT = true;
        typer1.burnOut(5);
        for (int i = 0; i <= 10; i++)
        {
            typer1.typeCharacter();
        }

        System.out.println(typer1.getBurnoutTurnsRemaining());
        System.out.println(typer1.getProgress());

        typer1.resetToStart();
        System.out.println(typer1.getBurnoutTurnsRemaining());
        System.out.println(typer1.getProgress());
        */

       /* // 'Burnout prevents typing' test: Test Case 2
        System.out.println("=== BURNOUT TEST ===");
        
        // Start fresh and type some characters
        typer1.resetToStart();
        typer1.typeCharacter();
        typer1.typeCharacter();
        System.out.println("Progress before burnout: " + typer1.getProgress());
        

        // Burn out for 3 turns
        int turns = 3;
        typer1.burnOut(turns);
        System.out.println("Is burnt out? " + typer1.isBurntOut());
        System.out.println("Burnout turns remaining: " + typer1.getBurnoutTurnsRemaining());
        
      // Try to type while burnt out (should NOT increase progress)
        typer1.typeCharacter();
        typer1.typeCharacter();
        typer1.typeCharacter();
        System.out.println("Progress while burnt out (should be 2): " + typer1.getProgress());
        
      // Recover from burnout
        System.out.println("\n--- Recovering ---");
        typer1.recoverFromBurnout();
        System.out.println("After 1st recovery - Remaining: " + typer1.getBurnoutTurnsRemaining() + ", Is burnt out? " + typer1.isBurntOut());

        typer1.recoverFromBurnout();
        System.out.println("After 2nd recovery - Remaining: " + typer1.getBurnoutTurnsRemaining() + ", Is burnt out? " + typer1.isBurntOut());

        typer1.recoverFromBurnout();
        System.out.println("After 3rd recovery - Remaining: " + typer1.getBurnoutTurnsRemaining() + ", Is burnt out? " + typer1.isBurntOut());
        
       // Try typing again after recovery
        typer1.typeCharacter();
        typer1.typeCharacter();
        System.out.println("Progress after recovery (should be 4): " + typer1.getProgress());
     */
        
                

       /* // Accuracy range Tests: Test Case 4
         System.out.println(typer1.getAccuracy());
        typer1.setAccuracy(5.0);
        System.out.println(typer1.getAccuracy());
        typer1.setAccuracy(0.0);
        System.out.println(typer1.getAccuracy());
        typer1.setAccuracy(0.8);
        System.out.println(typer1.getAccuracy());
        typer1.setAccuracy(1.0);
        System.out.println(typer1.getAccuracy());
        */

       /*// Normal forward movement via typeCharacter() : Test Case 4

       typer1.resetToStart();
       System.out.println(typer1.getProgress());
       typer1.typeCharacter();
       System.out.println(typer1.getProgress());
       typer1.typeCharacter();
       typer1.typeCharacter();
       System.out.println(typer1.getProgress());
       
       for (int i = 1; i <= 20; i++)
       {
           typer1.typeCharacter();
       }
       System.out.println(typer1.getProgress());
       typer1.typeCharacter();
       System.out.println(typer1.getProgress());

       return; 
       */
       

    }
}