package thkoeln.st.springtestlib.specification.table;


import com.fasterxml.jackson.databind.ObjectMapper;
import thkoeln.st.springtestlib.specification.table.implementations.UnorderedOnlyColumnsTable;
import thkoeln.st.springtestlib.specification.table.implementations.OrderedOnlyColumnsTable;
import thkoeln.st.springtestlib.specification.table.implementations.RowsAndColumnsTable;
import thkoeln.st.springtestlib.specification.table.implementations.SequencedOnlyColumnsTable;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GenericTableSpecificationTests {

    private ObjectMapper objectMapper = new ObjectMapper();


    private Table loadTable(String path, TableConfig tableConfig, boolean isTableHashed, boolean shouldTableBeHashed) throws Exception {
        List<String> fileLines = loadFileLines(path);
        Table table = createTable(tableConfig);
        table.parse(fileLines, isTableHashed, shouldTableBeHashed);
        return table;
    }

    private Table loadTable2(String path, TableConfig tableConfig, boolean isTableHashed, boolean shouldTableBeHashed) throws Exception {
        List<String> fileLines = loadFileLines2(path);
        Table table = createTable(tableConfig);
        table.parse(fileLines, isTableHashed, shouldTableBeHashed);
        return table;
    }

    private List<String> loadFileLines(String path) throws Exception {
        List<String> fileLines = new ArrayList<>();

        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(path);
        if (inputStream == null) {
            throw new FileNotFoundException("Table not found: " + path);
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = br.readLine()) != null) {
            fileLines.add(line);
        }

        return fileLines;
    }

    private List<String> loadFileLines2(String path) throws Exception {
        List<String> fileLines = new ArrayList<>();
        File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + path);
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            String line;
            while ((line = br.readLine()) != null) {
                fileLines.add(line);
            }
        }
        return fileLines;
    }

    private TableConfig loadTableConfig(String tableConfigPath) throws IOException {
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(tableConfigPath);
        if (inputStream == null) {
            throw new FileNotFoundException("Table Config not found: " + tableConfigPath);
        }
        return objectMapper.readValue(new InputStreamReader(inputStream), TableConfig.class);
    }

    private TableConfig loadTableConfig2(String tableConfigPath) throws IOException {
        File configFile = new File(tableConfigPath);
        if (!configFile.exists()) {
            throw new FileNotFoundException("Table Config not found: " + tableConfigPath);
        }
        try (FileInputStream inputStream = new FileInputStream(configFile)) {
            return objectMapper.readValue(new InputStreamReader(inputStream), TableConfig.class);
        }
    }

    public void testTableSpecification(String expectedPath, String actualPath, String tableConfigPath, boolean isExpectedTableHashed) throws Exception {
        TableConfig tableConfig = loadTableConfig(tableConfigPath);

        Table expectedTable = loadTable(expectedPath, tableConfig, isExpectedTableHashed, false);
        Table actualTable = loadTable(actualPath, tableConfig, false, isExpectedTableHashed);

        expectedTable.compareToActualTable(actualTable);
    }

    public void testTableSpecification(String expectedPath, String expectedHashedPath, String actualPath, String tableConfigPath) throws Exception {
        try {
            testTableSpecification(expectedPath, actualPath, tableConfigPath, false);
        } catch (FileNotFoundException e) {
            if (e.getMessage().contains(expectedPath)) {
                testTableSpecification(expectedHashedPath, actualPath, tableConfigPath, true);
            }
        }
    }

    public void testTableSpecification(String tableNamePrefix) throws Exception {
        var expectedPath = tableNamePrefix + "-solution.md";
        var expectedHashedPath = tableNamePrefix + "-hashed-solution.md";
        var actualPath = tableNamePrefix + ".md";
        var tableConfigPath = tableNamePrefix + "-config.json";
        testTableSpecification(expectedPath, expectedHashedPath, actualPath, tableConfigPath);
    }

    public void testTableSpecification(String expectedPath, String actualPath, String tableConfigPath) throws Exception {
        testTableSpecification(expectedPath, actualPath, tableConfigPath, false);
    }

    public void testTableSyntax(String actualPath, String tableConfigPath) throws Exception {
        TableConfig tableConfig = loadTableConfig(tableConfigPath);
        loadTable(actualPath, tableConfig, false, false);
    }

    public void testTableSyntax(String tableNamePrefix) throws Exception {
        var actualPath = tableNamePrefix + ".md";
        var tableConfigPath = tableNamePrefix + "-config.json";
        testTableSyntax(actualPath, tableConfigPath);
    }

    private Table createTable(TableConfig tableConfig) {
        switch (tableConfig.getTableType()) {
            case ROWS_AND_COLUMNS:
                return new RowsAndColumnsTable(tableConfig);
            case ORDERED_ONLY_COLUMNS:
                return new OrderedOnlyColumnsTable(tableConfig);
            case UNORDERED_ONLY_COLUMNS:
                return new UnorderedOnlyColumnsTable(tableConfig);
            case SEQUENCED_ONLY_COLUMNS:
                return new SequencedOnlyColumnsTable(tableConfig);
            default:
                throw new IllegalArgumentException("This table type does not exist");
        }
    }

    public void hashTable(String inputPath, String outputPath, String tableConfigPath) throws Exception {
        TableConfig tableConfig = loadTableConfig2(tableConfigPath);
        Table inputTable = loadTable2(inputPath, tableConfig, false, true);
        saveTable(outputPath, inputTable);
    }

    public void saveTable(String path, Table table) throws Exception {
        List<String> fileLines = Arrays.stream(table.toString().split("\n")).collect(Collectors.toList());
        saveFileLines(path, fileLines);
    }

    private void saveFileLines(String path, List<String> fileLines) throws Exception {
        System.out.println("Saving file to " + path);
        System.out.println("File content:");
        for (String line : fileLines) {
            System.out.println(line);
        }
        var br = new BufferedWriter(new FileWriter(path));
        for (String line : fileLines) {
            br.write(line + System.lineSeparator());
        }
        br.close();
    }
}
