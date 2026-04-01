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

    String typist_name;
    char typist_position;
    int typist_progress;
    boolean TYPISTISBURNTOUT;
    int burnout_remaining;
    double typist_accuracy;




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
        while (this.TYPISTISBURNTOUT == true)
        {
            this.burnout_remaining = turns;

        }
        return;

    }

    /**
     * Reduces the remaining burnout counter by one turn.
     * When the counter reaches zero, the typist recovers automatically.
     * Has no effect if the typist is not currently burnt out.
     */
    public void recoverFromBurnout()
    {
        while (this.TYPISTISBURNTOUT == true) {
            if (this.burnout_remaining != 0)
            {
                this.burnout_remaining = this.burnout_remaining - 1;
                System.out.println(this.burnout_remaining);
                return;
            }
            else {
                this.TYPISTISBURNTOUT = false;
                System.out.println(this.TYPISTISBURNTOUT);
                System.out.println(this.burnout_remaining);
            }
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
        return this.typist_accuracy; // placeholder - replace with correct implementation
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
        return this.typist_progress; // placeholder - replace with correct implementation
    }

    /**
     * Returns the name of the typist.
     *
     * @return the typist's name as a String
     */
    public String getName()
    {
        return (String) this.typist_name; // placeholder - replace with correct implementation
    }

    /**
     * Returns the character symbol used to represent this typist.
     *
     * @return the typist's symbol as a char
     */
    public char getSymbol()
    {
        return this.typist_position; // placeholder - replace with correct implementation
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
        return 0; // placeholder - replace with correct implementation
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
        return;

    }

    /**
     * Returns true if this typist is currently burnt out, false otherwise.
     *
     * @return true if burnt out
     */
    public boolean isBurntOut()
    {
        if (this.TYPISTISBURNTOUT == true)
        {
            return true;
        }
        return false; // placeholder - replace with correct implementation
    }

    /**
     * Advances the typist forward by one character along the passage.
     * Should only be called when the typist is not burnt out.
     */
    public void typeCharacter()
    {
        if (this.TYPISTISBURNTOUT == false)
        {
            this.typist_progress = this.typist_progress + 1;
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
        while (amount > 0)
        {
            if ((this.typist_progress - amount) >= 0)
            {
                this.typist_progress = this.typist_progress - amount;
                return;
            }
            else {
                this.typist_progress = 0;
                System.out.println("Typist slides back to start");
                return;
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
    public void main(String[] args) {
        // Create new Typist object
        Typist typer1 = new Typist('①', "TURBOFINGERS", 0.85);
        /*
         'Typist Progress cannot go below 0' tests: Test Case 1
         typer1.resetToStart();
         typer1.slideBack(5);
         System.out.println(typer1.isBurntOut());
        */

        // 'Burnout correctly decrements turn by turn and clears at zero' test: Test Case 2
        /*
        typer1.TYPISTISBURNTOUT = true;
        typer1.recoverFromBurnout();
        typer1.burnout_remaining = 5;
        typer1.TYPISTISBURNTOUT = true;
        typer1.recoverFromBurnout();
        typer1.recoverFromBurnout();
        typer1.recoverFromBurnout();
        typer1.recoverFromBurnout();
        typer1.recoverFromBurnout();
        typer1.recoverFromBurnout();
        */

        // 'resetToStart() method clears both progress and burnout state' tests: Test Case 3

        /* typer1.TYPISTISBURNTOUT = true;
        typer1.burnout_remaining = 5;
        typer1.typist_progress = 10;

        System.out.println(typer1.TYPISTISBURNTOUT);
        System.out.println(typer1.burnout_remaining);
        System.out.println(typer1.typist_progress);

        typer1.resetToStart();
        System.out.println(typer1.TYPISTISBURNTOUT);
        System.out.println(typer1.burnout_remaining);
        System.out.println(typer1.typist_progress);
        */

       // Accuracy range Tests: Test Case 4
       /*
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

       // Normal forward movement via typeCharacter() : Test Case 4
       typer1.resetToStart();
       System.out.println(typer1.typist_progress);
       typer1.typeCharacter();
       System.out.println(typer1.typist_progress);
       typer1.typeCharacter();
       typer1.typeCharacter();
       System.out.println(typer1.typist_progress);
       typer1.typist_progress = 20;
       typer1.typeCharacter();
       System.out.println(typer1.typist_progress);

       return;
       

    }
}