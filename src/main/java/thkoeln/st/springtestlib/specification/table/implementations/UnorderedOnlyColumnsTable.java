package thkoeln.st.springtestlib.specification.table.implementations;

import thkoeln.st.springtestlib.specification.table.*;
import thkoeln.st.springtestlib.specification.table.exceptions.DivekitMissingMismatchException;

import java.util.ArrayList;
import java.util.List;

/*
 * Column oriented table.
 * Column matching is done by column name.
 * Order of cells in columns must not match.
 */
public class UnorderedOnlyColumnsTable extends Table {


    public UnorderedOnlyColumnsTable(TableConfig tableConfig) {
        super(TableType.UNORDERED_ONLY_COLUMNS, tableConfig);
    }

    @Override
    public void compareToActualTable(Table actualTable) {
        if (!(actualTable instanceof UnorderedOnlyColumnsTable)) {
            throw new IllegalArgumentException("Wrong table type passed as parameter");
        }

        UnorderedOnlyColumnsTable otherTable = (UnorderedOnlyColumnsTable)actualTable;
        for (int c = 0; c < getColumnCount(); c++) {
            int otherTableColumnIndex = otherTable.getColumnIndex(columns.get(c));
            if (otherTableColumnIndex == -1) {
                tablesMismatch(TableMismatchCause.COLUMN_NOT_FOUND);
                continue;
            }

            List<Cell> nonEmptyCells = getAllNonEmptyCellsInColumn(c);
            List<Cell> otherTableNonEmptyCells = otherTable.getAllNonEmptyCellsInColumn(otherTableColumnIndex);
            if (isDimensionExplanation(columns.get(c))) {
                if (nonEmptyCells.size() < getRowCount() || otherTableNonEmptyCells.size() < otherTable.getRowCount()) {
                    tablesMismatch(null, c, TableMismatchCause.MISSING_EXPLANATION);
                }
            } else {
                if (nonEmptyCells.size() < otherTableNonEmptyCells.size()) {
                    tablesMismatch(null, otherTableColumnIndex, TableMismatchCause.TOO_MANY_MISMATCH);
                }
                if (nonEmptyCells.size() > otherTableNonEmptyCells.size()) {
                    tablesMismatch(null, otherTableColumnIndex, TableMismatchCause.MISSING_MISMATCH);
                }

                for (int r = 0; r < getRowCount(); r++) {
                    if (getCell(r, c) != null && !getCell(r, c).isEmpty() && !otherTableNonEmptyCells.contains(getCell(r, c))) {
                        int finalR = r;
                        int finalC = c;
                        if (otherTableNonEmptyCells.stream().anyMatch(cell -> cell.equalsIgnoreCase(getCell(finalR, finalC)))) {
                            tablesMismatch(r, otherTableColumnIndex, TableMismatchCause.CAPITALIZATION_MISMATCH);
                        } else {
                            var expectedColumns = getExpectedColumns(otherTable.getCell(r, otherTableColumnIndex));
                            if (!expectedColumns.isEmpty()) {
                                tablesMismatch(r, otherTableColumnIndex, expectedColumns, TableMismatchCause.WRONG_COLUMN_MISMATCH);
                            } else if (detectedTableExceptions.stream().noneMatch(e -> e instanceof DivekitMissingMismatchException)) {
                                tablesMismatch(r, otherTableColumnIndex, TableMismatchCause.CELL_MISMATCH);
                            }
                        }
                    }
                }
            }
        }
        if (detectedTableExceptions.isEmpty()) {
            for (int r = 0; r < otherTable.getRowCount(); r++) {
                for (int c = 0; c < otherTable.getColumnCount(); c++) {
                    if (!getAllCellsInColumn(c).contains(otherTable.getCell(r, c))) {
                        tablesMismatch(r, c, TableMismatchCause.TOO_MANY_MISMATCH);
                    }
                }
            }
            for (int r = 0; r < getRowCount(); r++) {
                for (int c = 0; c < getColumnCount(); c++) {
                    if (!otherTable.getAllCellsInColumn(c).contains(getCell(r, c))) {
                        tablesMismatch(r, c, TableMismatchCause.MISSING_MISMATCH);
                    }
                }
            }
        }
    }

    private List<Cell> getAllNonEmptyCellsInColumn(int column) {
        if (column < 0 || column >= getColumnCount()) {
            return new ArrayList<>();
        }

        List<Cell> allCells = new ArrayList<>();
        for (int r = 0; r < getRowCount(); r++) {
            Cell cell = getCell(r, column);
            if (cell != null && !cell.isEmpty()) {
                allCells.add(cell);
            }
        }

        return allCells;
    }
}
