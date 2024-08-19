import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVJoin {
    public static void main(String[] args) throws IOException {
        String file1 = "file1.csv";
        String file2 = "file2.csv";
        String outputFile = "output.csv";
        String commonColumn = "id";

        List<Map<String, String>> data1 = readCSV(file1, commonColumn);
        List<Map<String, String>> data2 = readCSV(file2, commonColumn);

        List<Map<String, String>> joinedData = joinData(data1, data2, commonColumn);

        writeCSV(joinedData, outputFile);
    }

    private static List<Map<String, String>> readCSV(String filePath, String commonColumn) throws IOException {
        List<Map<String, String>> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            String[] header = br.readLine().split(","); // Assuming CSV with headers
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Map<String, String> row = new HashMap<>();
                // Assuming header and values have the same length
                for (int i = 0; i < header.length; i++) {
                    row.put(header[i], values[i]);
                }
                data.add(row);
            }
        }
        return data;
    }

    private static List<Map<String, String>> joinData(List<Map<String, String>> data1, List<Map<String, String>> data2, String commonColumn) {
        List<Map<String, String>> joinedData = new ArrayList<>();
        // Implement join logic here
        // For simplicity, assuming an inner join
        for (Map<String, String> row1 : data1) {
            for (Map<String, String> row2 : data2) {
                if (row1.get(commonColumn).equals(row2.get(commonColumn))) {
                    Map<String, String> joinedRow = new HashMap<>(row1);
                    joinedRow.putAll(row2); // Combine rows, handle duplicates as needed
                    joinedData.add(joinedRow);
                }
            }
        }
        return joinedData;
    }

    private static void writeCSV(List<Map<String, String>> data, String outputFile) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
            // Get column names from the first row (assuming consistent schema)
            List<String> columnNames = new ArrayList<>(data.get(0).keySet());
    
            // Write header
            bw.write(String.join(",", columnNames));
            bw.newLine();
    
            for (Map<String, String> row : data) {
                StringBuilder sb = new StringBuilder();
                for (String columnName : columnNames) {
                    sb.append(row.get(columnName)).append(",");
                }
                sb.deleteCharAt(sb.length() - 1); // Remove trailing comma
                bw.write(sb.toString());
                bw.newLine();
            }
        }
    }
}