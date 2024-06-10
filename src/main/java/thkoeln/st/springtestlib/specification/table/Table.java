package thkoeln.st.springtestlib.specification.table;

import thkoeln.st.springtestlib.specification.table.exceptions.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.List;

public abstract class Table {

    protected TableType tableType;

    protected List<String> rows = new ArrayList<>();
    protected List<String> columns = new ArrayList<>();
    protected List<List<Cell>> cells = new ArrayList<>();

    protected TableConfig tableConfig;

    protected List<DivekitTableException> detectedTableExceptions = new ArrayList<>();

    public Table(TableType tableType, TableConfig tableConfig) {
        this.tableType = tableType;

        this.tableConfig = tableConfig;
    }

    public void addRow(String rowName) {
        if (!isRowValid(rowName)) {
            throw new InputMismatchException("\"" + rowName + "\" is not a valid row name");
        }

        if (isRowInUse(rowName)) {
            throw new InputMismatchException("\"" + rowName + "\" is a row name which is already in use");
        }

        List<Cell> newRowArray = new ArrayList<>();
        for (int i = 0; i < columns.size(); i++) {
            newRowArray.add(null);
        }
        cells.add(newRowArray);
        rows.add(rowName);
    }

    public void deleteRow(int index) {
        cells.remove(index);
        rows.remove(index);
    }

    public void addColumn(String columnName) {
        if (!isColumnValid(columnName)) {
            throw new InputMismatchException("\"" + columnName + "\" is not a valid column name");
        }

        if (isColumnInUse(columnName)) {
            throw new InputMismatchException("\"" + columnName + "\" is a column name which is already in use");
        }

        for (List<Cell> row : cells) {
            row.add(null);
        }
        columns.add(columnName);
    }

    public boolean isRowValid(String rowName) {
        if (tableConfig.getValidRowValues().length == 0) {
            return true;
        }

        for (String s : tableConfig.getValidRowValues()) {
            if (s.equalsIgnoreCase(rowName)) {
                return true;
            }
        }
        return false;
    }

    public boolean isRowInUse(String rowName) {
        for (String foundRow : rows) {
            if (foundRow != null && foundRow.equalsIgnoreCase(rowName)) {
                return true;
            }
        }
        return false;
    }

    public boolean isColumnValid(String columnName) {
        if (tableConfig.getValidColumnValues().length == 0) {
            return true;
        }

        for (String s : tableConfig.getValidColumnValues()) {
            if (s.equalsIgnoreCase(columnName)) {
                return true;
            }
        }
        return false;
    }

    public boolean isColumnInUse(String columnName) {
        for (String foundColumn : columns) {
            if (foundColumn != null && foundColumn.equalsIgnoreCase(columnName)) {
                return true;
            }
        }
        return false;
    }

    public boolean isDimensionExplanation(String dimensionName) {
        for (String s : tableConfig.getExplanationDimensions()) {
            if (s.equalsIgnoreCase(dimensionName)) {
                return true;
            }
        }
        return false;
    }

    protected int getRowIndex(String rowName) {
        for (int i = 0; i < rows.size(); i++) {
            if (rowName.equalsIgnoreCase(rows.get(i))) {
                return i;
            }
        }
        return -1;
    }

    protected int getColumnIndex(String columnName) {
        for (int i = 0; i < columns.size(); i++) {
            if (columnName.equalsIgnoreCase(columns.get(i))) {
                return i;
            }
        }
        return -1;
    }

    public void setCell(int row, int column, Cell cell) {
        cells.get(row).set(column, cell);
    }

    public Cell getCell(int row, int column) {
        if (row < 0 || column < 0 || row >= cells.size() || column >= cells.get(row).size()) {
            return null;
        }

        return cells.get(row).get(column);
    }

    public Cell getCell(int row, String columnName) {
        int column = getColumnIndex(columnName);
        return getCell(row, column);
    }

    public Cell getCell(String rowName, String columnName) {
        int row = getRowIndex(rowName);
        int column = getColumnIndex(columnName);
        return getCell(row, column);
    }

    protected String[] parseElementsInContentLine(String contentLine) {
        // This line used to be "contentLine.trim().split("\\|")" - but this did not work for strings
        // like "| some content ||" (i.e. ending with a pipe symbol)
        String[] elements = (contentLine + " ").split("\\|");

        int columnMarks = (int)contentLine.chars().filter(ch -> ch == '|').count();

        String[] filteredElements = new String[columnMarks - 1];
        for (int i = 1; i < columnMarks; i++) {
            filteredElements[i-1] = elements[i].trim();
        }
        return filteredElements;
    }

    public int getRowCount() {
        return rows.size();
    }

    public int getColumnCount() {
        return columns.size();
    }

    protected List<String> filterContentLines(List<String> contentLines) {
        List<String> filteredContentLines = new ArrayList<>();

        for (String contentLine : contentLines) {
            if (contentLine.contains("|")) {
                filteredContentLines.add(contentLine);
            }
        }

        return filteredContentLines;
    }

    protected List<String> testSyntax(List<String> contentLines) {
        if (contentLines.size() < 3) {
            throw new InputMismatchException("A table consists of at least 3 lines");
        }

        long expectedColumnMarks = contentLines.get(0).chars().filter(ch -> ch == '|').count();
        if (expectedColumnMarks < 2) {
            throw new InputMismatchException("A table line needs at least two \"|\" chars");
        }

        for (String contentLine : contentLines) {
            long columnMarks = contentLine.chars().filter(ch -> ch == '|').count();
            if (expectedColumnMarks != columnMarks) {
                throw new InputMismatchException("Each table line needs the same number of \"|\" chars");
            }
        }

        String[] split = parseElementsInContentLine(contentLines.get(1));
        for (int i = 1; i < split.length; i++) {
            long strokes = split[i].chars().filter(ch -> ch == '-').count();
            if (strokes < 3) {
                throw new InputMismatchException("Each column in the second line needs at least 3 \"-\" chars");
            }
        }

        return contentLines;
    }

    protected String[] getValidCellValues(int row, int column) {
        return isDimensionExplanation(rows.get(row)) || isDimensionExplanation(columns.get(column)) ? new String[]{} : tableConfig.getValidCellValues();
    }

    public abstract void compareToActualTable(Table actualTable);

    protected void tablesMismatch(Integer row, Integer column, List<Integer> expected, TableMismatchCause tableMismatchCause) {
        DivekitTableException detectedTableException = null;
        switch (tableMismatchCause) {
            case ROW_NOT_FOUND:
                detectedTableException = new DivekitTableException("Expected row identifier not found.");
                break;
            case COLUMN_NOT_FOUND:
                detectedTableException = new DivekitTableException("Expected column identifier not found.");
                break;
            case NOT_ENOUGH_ROWS:
                detectedTableException = new DivekitTableException("Not enough rows.");
                break;
            case TOO_MANY_ROWS:
                detectedTableException = new DivekitTableException("Too many rows.");
                break;
            case MISSING_EXPLANATION:
                detectedTableException = new DivekitTableException("Explanation is missing. " + getRowAndColumnDescriptor(row, column));
                break;
            case CELL_MISMATCH:
                detectedTableException = new DivekitCellMismatchException("Cell content is not matching. " + getRowAndColumnDescriptor(row, column), row, column);
                break;
            case WRONG_COLUMN_MISMATCH:
                detectedTableException = new DivekitColumnMismatchException("Cell content is not matching but content was found in other column of same row. " + getRowAndColumnDescriptor(row, column), row, column, expected);
                break;
            case WRONG_ROW_MISMATCH:
                detectedTableException = new DivekitRowMismatchException("Cell content is not matching but content was found in other row of same column. " + getRowAndColumnDescriptor(row, column), row, column, expected);
                break;
            case TOO_MANY_MISMATCH:
                detectedTableException = new DivekitTooManyMismatchException("Too many cells in column. " + getRowAndColumnDescriptor(row, column), row, column);
                break;
            case MISSING_MISMATCH:
                detectedTableException = new DivekitMissingMismatchException("Missing cell in column. " + getRowAndColumnDescriptor(row, column), row, column);
                break;
            case CAPITALIZATION_MISMATCH:
                detectedTableException = new DivekitCapitalizationMismatchException("Capitalization of cell content is not matching. " + getRowAndColumnDescriptor(row, column), row, column);
                break;
        }
        detectedTableExceptions.add(detectedTableException);
    }

    protected void tablesMismatch(Integer row, Integer column, TableMismatchCause tableMismatchCause) {
        tablesMismatch(row, column, null, tableMismatchCause);
    }

    protected void tablesMismatch(TableMismatchCause tableMismatchCause) {
        tablesMismatch(null, null, tableMismatchCause);
    }

    private String getRowAndColumnDescriptor(Integer row, Integer column) {
        String message = "";
        if (row != null) {
            message += "Row: " + row;
            if (column != null) {
                message += ", ";
            }
        }

        if (column != null) {
            message += "Column: " + column;
        }

        return message;
    }

    public void parse(List<String> contentLines) {
        contentLines = testSyntax(filterContentLines(contentLines));

        String[] columnNames = parseElementsInContentLine(contentLines.get(0));
        for (String columnName : columnNames) {
            addColumn(columnName.trim());
        }

        for (int i = 2; i < contentLines.size(); i++) {
            addRow(null);
            String[] columns = parseElementsInContentLine(contentLines.get(i));
            for (int j = 0; j < columns.length; j++) {
                String[] validCellValues = getValidCellValues(i-2, j);
                Cell newCell = Cell.parseCell( columns[j], validCellValues, tableConfig.isCaseSensitiveColumn( j ) );
                setCell(i-2, j, newCell );
            }
        }
    }

    protected boolean existsInOtherRowOfColumn(int c, Cell cell) {
        for (int r = 0; r < getRowCount(); r++) {
            if (getCell(r, c) != null && getCell(r, c).equals(cell)) {
                return true;
            }
        }
        return false;
    }

    protected List<Integer> getExpectedColumns(Cell cell) {
        List<Integer> expectedColumns = new ArrayList<>();
        for (int c = 0; c < getColumnCount(); c++) {
            for (int r = 0; r < getRowCount(); r++) {
                if (!isDimensionExplanation(columns.get(c)) && getCell(r, c) != null && getCell(r, c).equals(cell)) {
                    expectedColumns.add(c);
                }
            }
        }
        return new ArrayList<>(new HashSet<>(expectedColumns));
    }

    protected List<Cell> getAllCellsInColumn(int column) {
        if (column < 0 || column >= getColumnCount()) {
            return new ArrayList<>();
        }

        List<Cell> allCells = new ArrayList<>();
        for (int r = 0; r < getRowCount(); r++) {
            Cell cell = getCell(r, column);
            allCells.add(cell);
        }

        return allCells;
    }
}
