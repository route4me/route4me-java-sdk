package com.route4me.sdk.examples.advancedconstraints;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads the CSV files of the advanced-constraint examples, {@code locations.csv} and
 * {@code schedules.csv}: one header line, then comma-separated values without quotes.
 * It replaces OpenCSV, which the SDK no longer depends on; files with quoted fields need a
 * real CSV parser.
 */
final class CsvRows {

    /** Not instantiable: static helper only. */
    private CsvRows() {
    }

    /**
     * Reads every row after the header line.
     *
     * @param filename the path of the CSV file, read as UTF-8
     * @return the rows, each split on commas; blank lines are skipped
     * @throws IOException if the file cannot be read
     */
    static List<String[]> read(String filename) throws IOException {
        List<String[]> rows = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filename), StandardCharsets.UTF_8)) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    rows.add(line.split(",", -1));
                }
            }
        }
        return rows;
    }
}
