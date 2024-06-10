package thkoeln.st.springtestlib.specification.table.implementations;

import thkoeln.st.springtestlib.specification.table.Table;
import thkoeln.st.springtestlib.specification.table.TableConfig;
import thkoeln.st.springtestlib.specification.table.TableMismatchCause;
import thkoeln.st.springtestlib.specification.table.TableType;


/*
 * Column oriented table.
 * Column matching is done by column name.
 * Order of cells in columns must match.
 */
public class OrderedOnlyColumnsTable extends Table {


    public OrderedOnlyColumnsTable(TableConfig tableConfig) {
        super(TableType.ORDERED_ONLY_COLUMNS, tableConfig);
    }

    @Override
    public void compareToActualTable(Table actualTable) {
        if (!(actualTable instanceof OrderedOnlyColumnsTable)) {
            throw new IllegalArgumentException("Wrong table type passed as parameter");
        }

        OrderedOnlyColumnsTable otherTable = (OrderedOnlyColumnsTable)actualTable;
        for (int r = 0; r < getRowCount(); r++) {
            for (int c = 0; c < getColumnCount(); c++) {
                int otherTableColumnIndex = otherTable.getColumnIndex(columns.get(c));
                if (otherTableColumnIndex == -1) {
                    tablesMismatch(TableMismatchCause.COLUMN_NOT_FOUND);
                    continue;
                }

                if (isDimensionExplanation(columns.get(c))) {
                    if (getCell(r, c).isEmpty() || otherTable.getCell(r, otherTableColumnIndex).isEmpty()) {
                        tablesMismatch(r, c, TableMismatchCause.MISSING_EXPLANATION);
                    }
                } else {
                    if (!getCell(r, c).equals(otherTable.getCell(r, otherTableColumnIndex))) {
                        if (getCell(r, c).equalsIgnoreCase(otherTable.getCell(r, otherTableColumnIndex))) {
                            tablesMismatch(r, c, TableMismatchCause.CAPITALIZATION_MISMATCH);
                        } else if (existsInOtherRowOfColumn(c, otherTable.getCell(r, otherTableColumnIndex))) {
                            tablesMismatch(r, c, TableMismatchCause.WRONG_ROW_MISMATCH);
                        } else {
                            var expectedColumns = getExpectedColumns(otherTable.getCell(r, otherTableColumnIndex));
                            if (!expectedColumns.isEmpty()) {
                                tablesMismatch(r, otherTableColumnIndex, expectedColumns, TableMismatchCause.WRONG_COLUMN_MISMATCH);
                            } else {
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
}
