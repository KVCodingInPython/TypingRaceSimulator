import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class TypistCareerStats {
    private final List<RaceRecord> raceHistory = new ArrayList<>();
    private int personalBestWpm = 0;
    private int totalPoints = 0;
    private int consecutiveWins = 0;
    private int burnoutFreeRaceStreak = 0;
    private final Set<String> badges = new LinkedHashSet<>();
    private String title = "Rookie";

    public void addRecord(RaceRecord record) {
        raceHistory.add(record);
        personalBestWpm = Math.max(personalBestWpm, record.getWpm());
        totalPoints += record.getPoints();

        if (record.getFinishingPosition() == 1) {
            consecutiveWins += 1;
        } else {
            consecutiveWins = 0;
        }

        if (record.getBurnoutEvents() == 0) {
            burnoutFreeRaceStreak += 1;
        } else {
            burnoutFreeRaceStreak = 0;
        }

        if (consecutiveWins >= 3) {
            badges.add("Speed Demon");
        }
        if (burnoutFreeRaceStreak >= 5) {
            badges.add("Iron Fingers");
        }

        if (consecutiveWins >= 3) {
            title = "Speed Demon";
        } else if (burnoutFreeRaceStreak >= 5) {
            title = "Iron Fingers";
        } else if (totalPoints >= 500) {
            title = "Veteran";
        } else {
            title = "Rookie";
        }
    }

    public int getPersonalBestWpm() {
        return personalBestWpm;
    }

    public List<RaceRecord> getRaceHistory() {
        return Collections.unmodifiableList(raceHistory);
    }

    public int getRaceCount() {
        return raceHistory.size();
    }

    public double getAverageWpm() {
        if (raceHistory.isEmpty()) {
            return 0.0;
        }
        int total = 0;
        for (RaceRecord record : raceHistory) {
            total += record.getWpm();
        }
        return total / (double) raceHistory.size();
    }

    public double getAverageAccuracyPercent() {
        if (raceHistory.isEmpty()) {
            return 0.0;
        }
        double total = 0.0;
        for (RaceRecord record : raceHistory) {
            total += record.getAccuracyPercent();
        }
        return total / raceHistory.size();
    }

    public double getAveragePosition() {
        if (raceHistory.isEmpty()) {
            return 0.0;
        }
        double total = 0.0;
        for (RaceRecord record : raceHistory) {
            total += record.getFinishingPosition();
        }
        return total / raceHistory.size();
    }

    public int getTotalBurnoutEvents() {
        int total = 0;
        for (RaceRecord record : raceHistory) {
            total += record.getBurnoutEvents();
        }
        return total;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public int getConsecutiveWins() {
        return consecutiveWins;
    }

    public int getBurnoutFreeRaceStreak() {
        return burnoutFreeRaceStreak;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getBadges() {
        return Collections.unmodifiableList(new ArrayList<>(badges));
    }
}
