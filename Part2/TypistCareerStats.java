import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TypistCareerStats {
    private final List<RaceRecord> raceHistory = new ArrayList<>();
    private int personalBestWpm = 0;

    public void addRecord(RaceRecord record) {
        raceHistory.add(record);
        personalBestWpm = Math.max(personalBestWpm, record.getWpm());
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
}
