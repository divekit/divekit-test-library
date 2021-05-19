package thkoeln.st.springtestlib.specification.table.implementations;

import thkoeln.st.springtestlib.specification.table.*;

import java.util.List;

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
                tablesMismatch(TableMismatchCause.ROW_NOT_FOUND);
            }

            for (int c = 0; c < getColumnCount(); c++) {
                int otherTableColumnIndex = otherTable.getColumnIndex(columns.get(c));
                if (otherTableColumnIndex == -1) {
                    tablesMismatch(TableMismatchCause.COLUMN_NOT_FOUND);
                }

                if (isDimensionExplanation(rows.get(r)) || isDimensionExplanation(columns.get(c))) {
                    if (getCell(r, c).isEmpty() || otherTable.getCell(otherTableRowIndex, otherTableColumnIndex).isEmpty()) {
                        tablesMismatch(rows.get(r), columns.get(c), TableMismatchCause.MISSING_EXPLANATION);
                    }
                } else {
                    if (getCell(r, c) != null && !getCell(r, c).equals(otherTable.getCell(otherTableRowIndex, otherTableColumnIndex))) {
                        tablesMismatch(rows.get(r), columns.get(c), TableMismatchCause.CELL_MISMATCH);
                    }
                }
            }
        }

        checkRowCountMatch(otherTable);
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
                setCell(i-2, j-1, Cell.parseCell(columns[j], getValidCellValues(i-2, j-1)));
            }
        }
    }
}
