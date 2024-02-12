package thkoeln.st.springtestlib.specification.table.implementations;

import thkoeln.st.springtestlib.specification.table.*;

import java.util.List;

public class RowsAndColumnsTable extends Table {

    private String rowColumn;

    public RowsAndColumnsTable(TableConfig tableConfig) {
        super(tableConfig);
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
    public void parse(List<String> contentLines, boolean isHashed) {
        contentLines = testSyntax(filterContentLines(contentLines));

        String[] columnNames = parseElementsInContentLine(contentLines.get(0));
        rowColumn = columnNames[0].trim();
        for (int i = 1; i < columnNames.length; i++) {
            addColumn(columnNames[i].trim());
        }

        for (int i = 2; i < contentLines.size(); i++) {
            String[] columns = parseElementsInContentLine(contentLines.get(i));
            addRow(columns[0], isHashed, tableConfig.shouldRowBeHashed());
            for (int j = 1; j < columns.length; j++) {
                String[] validCellValues = getValidCellValues(i-2, j-1);
                Cell newCell = Cell.parseCell(columns[j], validCellValues, tableConfig.isCaseSensitiveColumn(j), isHashed, tableConfig.shouldCellBeHashed() );
                setCell(i-2, j-1, newCell );
            }
        }
    }

    @Override
    protected int getWidth() {
        return columns.size() + 1;
    }

    @Override
    protected List<String> getHeaderRow() {
        List<String> headerRow = super.getHeaderRow();
        headerRow.add(0, rowColumn);
        return headerRow;
    }

    @Override
    protected String getStringAtPosition(int row, int column) {
        if (row == 0 && column == 0)
            return rowColumn;
        if (row == 0)
            return columns.get(column-1);
        if (column == 0)
            return rows.get(row-1);
        return getCell(row-1, column-1).toString();
    }
}
