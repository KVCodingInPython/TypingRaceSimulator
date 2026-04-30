import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class TypistStatsStore {
    private static final Map<String, TypistCareerStats> STATS_BY_NAME = new LinkedHashMap<>();
    private static final Path CSV_PATH = Paths.get("typist_stats.csv");
    private static final DateTimeFormatter TS_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

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

    public static synchronized void recordRaceResult(String typistName,
                                                     int finishingPosition,
                                                     int wpm,
                                                     double accuracyPercent,
                                                     int burnoutTurns,
                                                     int burnoutEvents) {
        TypistCareerStats stats = STATS_BY_NAME.computeIfAbsent(typistName, k -> new TypistCareerStats());
        stats.addRecord(new RaceRecord(LocalDateTime.now(),
                                       finishingPosition,
                                       wpm,
                                       accuracyPercent,
                                       burnoutTurns,
                                       burnoutEvents));
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

    private static synchronized void saveToCsv() throws Exception {
        List<String> lines = new ArrayList<>();
        // header
        lines.add("typistName,timestamp,finishingPosition,wpm,accuracyPercent,burnoutTurns,burnoutEvents");

        for (Map.Entry<String, TypistCareerStats> entry : STATS_BY_NAME.entrySet()) {
            String name = entry.getKey();
            TypistCareerStats stats = entry.getValue();
            for (RaceRecord r : stats.getRaceHistory()) {
                String ln = String.format(
                    "%s,%s,%d,%d,%.4f,%d,%d",
                    escapeCsv(name),
                    r.getTimestamp().format(TS_FORMAT),
                    r.getFinishingPosition(),
                    r.getWpm(),
                    r.getAccuracyPercent(),
                    r.getBurnoutTurns(),
                    r.getBurnoutEvents());
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
                    if (parts.size() < 7) return;
                    String name = unescapeCsv(parts.get(0));
                    LocalDateTime ts = LocalDateTime.parse(parts.get(1), TS_FORMAT);
                    int finishingPosition = Integer.parseInt(parts.get(2));
                    int wpm = Integer.parseInt(parts.get(3));
                    double accuracyPercent = Double.parseDouble(parts.get(4));
                    int burnoutTurns = Integer.parseInt(parts.get(5));
                    int burnoutEvents = Integer.parseInt(parts.get(6));

                    TypistCareerStats stats = STATS_BY_NAME.computeIfAbsent(name, k -> new TypistCareerStats());
                    stats.addRecord(new RaceRecord(ts, finishingPosition, wpm, accuracyPercent, burnoutTurns, burnoutEvents));
                } catch (Exception ex) {
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
}
