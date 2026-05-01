import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class TypistStatsStore {
    private static final Map<String, TypistCareerStats> STATS_BY_NAME = new LinkedHashMap<>();
    private static final Set<String> ACTIVE_RACE_PARTICIPANTS = new HashSet<>();
    private static final Path CSV_PATH = Paths.get("typist_stats.csv");
    private static final DateTimeFormatter TS_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static boolean restrictToActiveRace = false;

    static {
        // attempt to load persisted stats on class load
        try {
            loadFromCsv();
        } catch (Exception e) {
            // non-fatal: log to stderr and continue with empty store
            System.err.println("Warning: could not load typist stats CSV: " + e.getMessage());
        }
    }

    private TypistStatsStore() {
    }

    public static synchronized void beginRaceSession(List<String> participantNames) {
        ACTIVE_RACE_PARTICIPANTS.clear();
        ACTIVE_RACE_PARTICIPANTS.addAll(participantNames);
        restrictToActiveRace = true;
    }

    public static synchronized void endRaceSession() {
        ACTIVE_RACE_PARTICIPANTS.clear();
        restrictToActiveRace = false;
    }

    public static synchronized void recordRaceResult(String typistName,
                                                     int finishingPosition,
                                                     int wpm,
                                                     double accuracyPercent,
                                                     int burnoutTurns,
                                                     int burnoutEvents,
                                                     int points,
                                                     int earnings) {
        if (restrictToActiveRace && !ACTIVE_RACE_PARTICIPANTS.contains(typistName)) {
            return;
        }
        TypistCareerStats stats = STATS_BY_NAME.computeIfAbsent(typistName, k -> new TypistCareerStats());
        stats.addRecord(new RaceRecord(LocalDateTime.now(),
                                       finishingPosition,
                                       wpm,
                                       accuracyPercent,
                                       burnoutTurns,
                                       burnoutEvents,
                                       points,
                                       earnings));
        try {
            saveToCsv();
        } catch (Exception e) {
            System.err.println("Warning: could not persist typist stats: " + e.getMessage());
        }
    }

    public static synchronized void recordEarningsDeduction(String typistName, int amount, String reason) {
        if (amount <= 0) {
            return;
        }
        if (restrictToActiveRace && !ACTIVE_RACE_PARTICIPANTS.contains(typistName)) {
            return;
        }

        TypistCareerStats stats = STATS_BY_NAME.computeIfAbsent(typistName, k -> new TypistCareerStats());
        stats.deductEarnings(amount, LocalDateTime.now(), reason == null ? "Upgrade purchase" : reason);
        try {
            saveToCsv();
        } catch (Exception e) {
            System.err.println("Warning: could not persist typist stats: " + e.getMessage());
        }
    }

    public static synchronized TypistCareerStats getStats(String typistName) {
        return STATS_BY_NAME.get(typistName);
    }

    public static synchronized Map<String, TypistCareerStats> getAllStats() {
        return Collections.unmodifiableMap(new LinkedHashMap<>(STATS_BY_NAME));
    }

    public static synchronized List<String> getKnownTypistNames() {
        return new ArrayList<>(STATS_BY_NAME.keySet());
    }

    public static synchronized double getRankAdjustedStartingAccuracy(String typistName, double baseAccuracy) {
        TypistCareerStats stats = STATS_BY_NAME.get(typistName);
        if (stats == null) {
            return clampAccuracy(baseAccuracy);
        }

        int rank = getRankForTypist(typistName);
        double adjustment = getRankAccuracyAdjustment(rank);
        return clampAccuracy(baseAccuracy + adjustment);
    }

    public static synchronized String getTitle(String typistName) {
        TypistCareerStats stats = STATS_BY_NAME.get(typistName);
        return stats == null ? "Rookie" : stats.getTitle();
    }

    public static synchronized List<String> getBadges(String typistName) {
        TypistCareerStats stats = STATS_BY_NAME.get(typistName);
        return stats == null ? List.of() : stats.getBadges();
    }

    public static synchronized int getRankForTypist(String typistName) {
        List<Map.Entry<String, TypistCareerStats>> ranked = new ArrayList<>(STATS_BY_NAME.entrySet());
        ranked.sort((left, right) -> {
            int pointsComparison = Integer.compare(right.getValue().getTotalPoints(), left.getValue().getTotalPoints());
            if (pointsComparison != 0) {
                return pointsComparison;
            }
            return left.getKey().compareToIgnoreCase(right.getKey());
        });

        for (int i = 0; i < ranked.size(); i++) {
            if (ranked.get(i).getKey().equals(typistName)) {
                return i + 1;
            }
        }
        return ranked.size() + 1;
    }

    private static synchronized void saveToCsv() throws Exception {
        List<String> lines = new ArrayList<>();
        // header
        lines.add("entryType,typistName,timestamp,finishingPosition,wpm,accuracyPercent,burnoutTurns,burnoutEvents,points,earnings,adjustmentAmount,reason");

        for (Map.Entry<String, TypistCareerStats> entry : STATS_BY_NAME.entrySet()) {
            String name = entry.getKey();
            TypistCareerStats stats = entry.getValue();
            for (RaceRecord r : stats.getRaceHistory()) {
                String ln = String.format(
                    "%s,%s,%s,%d,%d,%.4f,%d,%d,%d,%d,%d,%s",
                    escapeCsv("RACE"),
                    escapeCsv(name),
                    r.getTimestamp().format(TS_FORMAT),
                    r.getFinishingPosition(),
                    r.getWpm(),
                    r.getAccuracyPercent(),
                    r.getBurnoutTurns(),
                    r.getBurnoutEvents(),
                    r.getPoints(),
                    r.getEarnings(),
                    0,
                    escapeCsv(""));
                lines.add(ln);
            }
            for (TypistCareerStats.EarningsAdjustment adjustment : stats.getEarningsAdjustments()) {
                String ln = String.format(
                    "%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%d,%s",
                    escapeCsv("ADJUSTMENT"),
                    escapeCsv(name),
                    adjustment.getTimestamp().format(TS_FORMAT),
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    adjustment.getAmount(),
                    escapeCsv(adjustment.getReason()));
                lines.add(ln);
            }
        }

        Files.write(CSV_PATH, lines, StandardCharsets.UTF_8);
    }

    private static synchronized void loadFromCsv() throws Exception {
        if (!Files.exists(CSV_PATH)) return;

        try (Stream<String> s = Files.lines(CSV_PATH, StandardCharsets.UTF_8)) {
            s.skip(1).forEach(line -> {
                try {
                    // simple CSV parse (name may contain commas escaped as double quotes)
                    List<String> parts = parseCsvLine(line);
                    if (parts.isEmpty()) return;

                    boolean hasEntryType = "RACE".equals(parts.get(0)) || "ADJUSTMENT".equals(parts.get(0));
                    if (hasEntryType) {
                        String entryType = parts.get(0);
                        String name = unescapeCsv(parts.get(1));
                        LocalDateTime ts = LocalDateTime.parse(parts.get(2), TS_FORMAT);

                        TypistCareerStats stats = STATS_BY_NAME.computeIfAbsent(name, k -> new TypistCareerStats());
                        if ("ADJUSTMENT".equals(entryType)) {
                            int adjustmentAmount = Integer.parseInt(parts.get(10));
                            String reason = parts.size() > 11 ? unescapeCsv(parts.get(11)) : "Upgrade purchase";
                            stats.deductEarnings(adjustmentAmount, ts, reason);
                            return;
                        }

                        int finishingPosition = Integer.parseInt(parts.get(3));
                        int wpm = Integer.parseInt(parts.get(4));
                        double accuracyPercent = Double.parseDouble(parts.get(5));
                        int burnoutTurns = Integer.parseInt(parts.get(6));
                        int burnoutEvents = Integer.parseInt(parts.get(7));
                        int points = parts.size() >= 9
                            ? Integer.parseInt(parts.get(8))
                            : TypingRace.computeRacePoints(finishingPosition, wpm, burnoutEvents);
                        int earnings = parts.size() >= 10
                            ? Integer.parseInt(parts.get(9))
                            : TypingRace.calculateEarnings(finishingPosition, wpm, burnoutEvents);

                        stats.addRecord(new RaceRecord(ts, finishingPosition, wpm, accuracyPercent, burnoutTurns, burnoutEvents, points, earnings));
                        return;
                    }

                    if (parts.size() < 8) return;
                    String name = unescapeCsv(parts.get(0));
                    LocalDateTime ts = LocalDateTime.parse(parts.get(1), TS_FORMAT);
                    int finishingPosition = Integer.parseInt(parts.get(2));
                    int wpm = Integer.parseInt(parts.get(3));
                    double accuracyPercent = Double.parseDouble(parts.get(4));
                    int burnoutTurns = Integer.parseInt(parts.get(5));
                    int burnoutEvents = Integer.parseInt(parts.get(6));
                    int points = parts.size() >= 8
                        ? Integer.parseInt(parts.get(7))
                        : TypingRace.computeRacePoints(finishingPosition, wpm, burnoutEvents);
                    int earnings = parts.size() >= 9
                        ? Integer.parseInt(parts.get(8))
                        : TypingRace.calculateEarnings(finishingPosition, wpm, burnoutEvents);

                    TypistCareerStats stats = STATS_BY_NAME.computeIfAbsent(name, k -> new TypistCareerStats());
                    stats.addRecord(new RaceRecord(ts, finishingPosition, wpm, accuracyPercent, burnoutTurns, burnoutEvents, points, earnings));
                } catch (NumberFormatException | java.time.format.DateTimeParseException ex) {
                    System.err.println("Skipping malformed CSV row: " + line + " (" + ex.getMessage() + ")");
                }
            });
        }
    }

    private static String escapeCsv(String s) {
        if (s.contains(",") || s.contains("\"") || s.contains("\n")) {
            return '"' + s.replace("\"", "\"\"") + '"';
        }
        return s;
    }

    private static String unescapeCsv(String s) {
        if (s.startsWith("\"") && s.endsWith("\"")) {
            String inner = s.substring(1, s.length() - 1);
            return inner.replace("\"\"", "\"");
        }
        return s;
    }

    private static List<String> parseCsvLine(String line) {
        List<String> out = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (inQuotes) {
                if (c == '"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        cur.append('"');
                        i++; // skip escaped quote
                    } else {
                        inQuotes = false;
                    }
                } else {
                    cur.append(c);
                }
            } else {
                if (c == ',') {
                    out.add(cur.toString());
                    cur.setLength(0);
                } else if (c == '"') {
                    inQuotes = true;
                } else {
                    cur.append(c);
                }
            }
        }
        out.add(cur.toString());
        return out;
    }

    private static double getRankAccuracyAdjustment(int rank) {
        if (rank <= 1) {
            return -0.03;
        }
        if (rank == 2) {
            return -0.02;
        }
        if (rank == 3) {
            return -0.01;
        }
        if (rank >= 6) {
            return 0.02;
        }
        return 0.01;
    }

    private static double clampAccuracy(double accuracy) {
        if (accuracy < 0.0) {
            return 0.0;
        }
        if (accuracy > 1.0) {
            return 1.0;
        }
        return accuracy;
    }
}
