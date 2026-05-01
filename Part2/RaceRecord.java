import java.time.LocalDateTime;

public class RaceRecord {
    private final LocalDateTime timestamp;
    private final int finishingPosition;
    private final int wpm;
    private final double accuracyPercent;
    private final int burnoutTurns;
    private final int burnoutEvents;
    private final int points;
    private final int earnings;
    public RaceRecord(LocalDateTime timestamp,
                      int finishingPosition,
                      int wpm,
                      double accuracyPercent,
                      int burnoutTurns,
                      int burnoutEvents,
                      int points,
                      int earnings) {
        this.timestamp = timestamp;
        this.finishingPosition = finishingPosition;
        this.wpm = wpm;
        this.accuracyPercent = accuracyPercent;
        this.burnoutTurns = burnoutTurns;
        this.burnoutEvents = burnoutEvents;
        this.points = points;
        this.earnings = earnings;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getFinishingPosition() {
        return finishingPosition;
    }

    public int getWpm() {
        return wpm;
    }

    public double getAccuracyPercent() {
        return accuracyPercent;
    }

    public int getBurnoutTurns() {
        return burnoutTurns;
    }

    public int getBurnoutEvents() {
        return burnoutEvents;
    }

    public int getPoints() {
        return points;
    }

    public int getEarnings() {
        return earnings;
    }
}
