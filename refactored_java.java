package com.acme.interviews;

import java.util.*;
import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;

public class refactored_java{

    private static final String[] HEADERS = {
        "permalink", "company_name", "number_employees", "category",
        "city", "state", "funded_date", "raised_amount",
        "raised_currency", "round"
    };

    // Read CSV and return list of rows excluding header
    private static List<String[]> readCSV() throws IOException {
        try (CSVReader reader = new CSVReader(new FileReader("startup_funding.csv"))) {
            List<String[]> allRows = reader.readAll();
            allRows.remove(0); // remove header
            return allRows;
        }
    }

    // Converts a row into a map using HEADERS
    private static Map<String, String> mapRowToData(String[] row) {
        Map<String, String> data = new HashMap<>();
        for (int i = 0; i < HEADERS.length && i < row.length; i++) {
            data.put(HEADERS[i], row[i]);
        }
        return data;
    }

    // Checks if a row matches all key-value filters
    private static boolean matchesOptions(String[] row, Map<String, String> options) {
        for (Map.Entry<String, String> entry : options.entrySet()) {
            int index = Arrays.asList(HEADERS).indexOf(entry.getKey());
            if (index == -1 || !row[index].equals(entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    public static List<Map<String, String>> where(Map<String, String> options) throws IOException {
        List<String[]> csvData = readCSV();
        List<Map<String, String>> result = new ArrayList<>();

        for (String[] row : csvData) {
            if (matchesOptions(row, options)) {
                result.add(mapRowToData(row));
            }
        }

        return result;
    }

    public static Map<String, String> findBy(Map<String, String> options) throws IOException, NoSuchEntryException {
        List<String[]> csvData = readCSV();

        for (String[] row : csvData) {
            if (matchesOptions(row, options)) {
                return mapRowToData(row);
            }
        }

        throw new NoSuchEntryException();
    }

    public static void main(String[] args) {
        try {
            Map<String, String> options = new HashMap<>();
            options.put("company_name", "Facebook");
            options.put("round", "a");
            System.out.println(refactored_java.where(options).size());
        } catch (IOException e) {
            System.out.println("IO Error: " + e.getMessage());
        }
    }
}

class NoSuchEntryException extends Exception {
    public NoSuchEntryException() {
        super("No matching entry found.");
    }
}
