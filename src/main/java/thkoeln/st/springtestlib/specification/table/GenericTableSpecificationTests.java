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


    private Table loadTable(String path, TableConfig tableConfig, boolean isHashed, boolean shouldBeHashed) throws Exception {
        List<String> fileLines = loadFileLines(path);
        Table table = createTable(tableConfig);
        table.parse(fileLines, isHashed, shouldBeHashed);
        return table;
    }

    private List<String> loadFileLines(String path) throws Exception {
        List<String> fileLines = new ArrayList<>();

        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = br.readLine()) != null) {
            fileLines.add(line);
        }

        return fileLines;
    }

    private TableConfig loadTableConfig(String tableConfigPath) throws IOException {
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(tableConfigPath);

        return objectMapper.readValue(new InputStreamReader(inputStream), TableConfig.class);
    }

    public void testTableSpecification(String expectedPath, String actualPath, String tableConfigPath, boolean isExpectedTableHashed) throws Exception {
        TableConfig tableConfig = loadTableConfig(tableConfigPath);

        Table expectedTable = loadTable(expectedPath, tableConfig, isExpectedTableHashed, false);
        Table actualTable = loadTable(actualPath, tableConfig, false, isExpectedTableHashed);

        expectedTable.compareToActualTable(actualTable);
    }

    public void testTableSyntax(String actualPath, String tableConfigPath) throws Exception {
        TableConfig tableConfig = loadTableConfig(tableConfigPath);
        loadTable(actualPath, tableConfig, false, false);
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
        TableConfig tableConfig = loadTableConfig(tableConfigPath);
        Table inputTable = loadTable(inputPath, tableConfig, false, true);
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
