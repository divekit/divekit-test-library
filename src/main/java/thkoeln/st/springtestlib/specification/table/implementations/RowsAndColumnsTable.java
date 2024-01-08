package thkoeln.st.springtestlib.specification.table.implementations;

import thkoeln.st.springtestlib.specification.table.*;

import java.util.List;

/*
 * Row and column oriented table.
 * Row matching is done by row name.
 * Column matching is done by column name.
 */
public class RowsAndColumnsTable extends Table {

    public RowsAndColumnsTable(TableConfig tableConfig) {
        super(TableType.ROWS_AND_COLUMNS, tableConfig);
    }


    @Override
    public void compareToActualTable(Table actualTable) {
        if (!(actualTable instanceof RowsAndColumnsTable)) {
            throw new IllegalArgumentException("Wrong table type passed as parameter");
        }

        RowsAndColumnsTable otherTable = (RowsAndColumnsTable)actualTable;
        for (int r = 0; r < getRowCount(); r++) {
            int otherTableRowIndex = otherTable.getRowIndex(rows.get(r));
            if (otherTableRowIndex == -1) {
                tablesMismatch(r, null, TableMismatchCause.MISSING_MISMATCH);
                continue;
            }

            for (int c = 0; c < getColumnCount(); c++) {
                int otherTableColumnIndex = otherTable.getColumnIndex(columns.get(c));
                if (otherTableColumnIndex == -1) {
                    tablesMismatch(-1, c, TableMismatchCause.COLUMN_NOT_FOUND);
                    continue;
                }

                if (isDimensionExplanation(rows.get(r)) || isDimensionExplanation(columns.get(c))) {
                    if (getCell(r, c).isEmpty() || otherTable.getCell(otherTableRowIndex, otherTableColumnIndex).isEmpty()) {
                        tablesMismatch(r, c, TableMismatchCause.MISSING_EXPLANATION);
                    }
                } else {
                    if (getCell(r, c) != null && !getCell(r, c).equals(otherTable.getCell(otherTableRowIndex, otherTableColumnIndex))) {
                        if (getCell(r, c).equalsIgnoreCase(otherTable.getCell(r, otherTableColumnIndex))) {
                            tablesMismatch(otherTableRowIndex, otherTableColumnIndex, TableMismatchCause.CAPITALIZATION_MISMATCH);
                        } else if (existsInOtherRowOfColumn(c, otherTable.getCell(otherTableRowIndex, otherTableColumnIndex))) {
                            tablesMismatch(otherTableRowIndex, otherTableColumnIndex, TableMismatchCause.WRONG_ROW_MISMATCH);
                        } else {
                            var expectedColumns = getExpectedColumns(otherTable.getCell(otherTableRowIndex, otherTableColumnIndex));
                            if (!expectedColumns.isEmpty()) {
                                tablesMismatch(otherTableRowIndex, otherTableColumnIndex, expectedColumns, TableMismatchCause.WRONG_COLUMN_MISMATCH);
                            } else {
                                tablesMismatch(otherTableRowIndex, otherTableColumnIndex, TableMismatchCause.CELL_MISMATCH);
                            }
                        }
                    }
                }
            }
        }
        if (detectedTableExceptions.isEmpty()) {
            for (int r = 0; r < otherTable.getRowCount(); r++) {
                for (int c = 0; c < otherTable.getColumnCount(); c++) {
                    if (!otherTable.getCell(r, c).isEmpty() && !getAllCellsInColumn(c).contains(otherTable.getCell(r, c))) {
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

    @Override
    public void parse(List<String> contentLines) {
        contentLines = testSyntax(filterContentLines(contentLines));

        String[] columnNames = parseElementsInContentLine(contentLines.get(0));
        for (int i = 1; i < columnNames.length; i++) {
            addColumn(columnNames[i].trim());
        }

        for (int i = 2; i < contentLines.size(); i++) {
            String[] columns = parseElementsInContentLine(contentLines.get(i));
            addRow(columns[0]);
            for (int j = 1; j < columns.length; j++) {
                String[] validCellValues = getValidCellValues(i-2, j-1);
                Cell newCell = Cell.parseCell( columns[j], validCellValues, tableConfig.isCaseSensitiveColumn( j ) );
                setCell(i-2, j-1, newCell );
            }
        }
    }
}
