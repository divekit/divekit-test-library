package thkoeln.st.springtestlib.specification.table.implementations;

import thkoeln.st.springtestlib.specification.table.Table;
import thkoeln.st.springtestlib.specification.table.TableConfig;
import thkoeln.st.springtestlib.specification.table.TableMismatchCause;
import thkoeln.st.springtestlib.specification.table.TableType;
import thkoeln.st.springtestlib.specification.table.exceptions.DivekitCapitalizationMismatchException;
import thkoeln.st.springtestlib.specification.table.exceptions.DivekitColumnMismatchException;

import java.util.InputMismatchException;
import java.util.List;



/*
 * Row oriented table.
 * Row matching is done by row content.
 * Cell matching in rows is done by column name.
 */
public class SequencedOnlyColumnsTable extends Table {


    public SequencedOnlyColumnsTable(TableConfig tableConfig) {
        super(TableType.SEQUENCED_ONLY_COLUMNS, tableConfig);
    }

    @Override
    public void compareToActualTable(Table actualTable) {
        if (!(actualTable instanceof SequencedOnlyColumnsTable)) {
            throw new IllegalArgumentException("Wrong table type passed as parameter");
        }

        SequencedOnlyColumnsTable otherTable = (SequencedOnlyColumnsTable)actualTable;

        for (int otherTableR = 0; otherTableR < otherTable.getRowCount(); otherTableR++) {
            boolean foundMatchingRow = false;
            boolean foundMatchingRowIgnoreCase = false;
            for (int r = 0; r < getRowCount(); r++) {
                if (compareSequenceRows(r, otherTableR, otherTable)) {
                    foundMatchingRow = true;
                    break;
                }
                if (compareSequenceRowsIgnoreCase(r, otherTableR, otherTable)) {
                    foundMatchingRowIgnoreCase = true;
                }
            }
            if (!foundMatchingRow) {
                for (int c = 0; c < getColumnCount(); c++) {
                    if (foundMatchingRowIgnoreCase && !getCell(otherTableR, c).equals(otherTable.getCell(otherTableR, c)) && getCell(otherTableR, c).equalsIgnoreCase(otherTable.getCell(otherTableR, c))) {
                        tablesMismatch(otherTableR, c, TableMismatchCause.CAPITALIZATION_MISMATCH);
                    } else if (!existsInOtherRowOfColumn(c, otherTable.getCell(otherTableR, c))) {
                        var expectedColumns = getExpectedColumns(otherTable.getCell(otherTableR, c));
                        if (!expectedColumns.isEmpty()) {
                            tablesMismatch(otherTableR, c, expectedColumns, TableMismatchCause.WRONG_COLUMN_MISMATCH);
                        }
                    }
                }
                int finalOtherTableR = otherTableR;
                if (detectedTableExceptions.stream().noneMatch(e ->
                        e instanceof DivekitCapitalizationMismatchException && ((DivekitCapitalizationMismatchException) e).getRow() == finalOtherTableR ||
                        e instanceof DivekitColumnMismatchException         && ((DivekitColumnMismatchException)         e).getRow() == finalOtherTableR
                    )) {
                    tablesMismatch(otherTableR, null, TableMismatchCause.CELL_MISMATCH);
                }
            }
        }
        if (detectedTableExceptions.isEmpty()) {
            for (int r = 0; r < otherTable.getRowCount(); r++) {
                for (int c = 0; c < otherTable.getColumnCount(); c++) {
                    if (!getAllCellsInColumn(c).contains(otherTable.getCell(r, c))) {
                        tablesMismatch(r, c, TableMismatchCause.TOO_MANY_MISMATCH);
                        break;
                    }
                }
            }
            for (int r = 0; r < getRowCount(); r++) {
                for (int c = 0; c < getColumnCount(); c++) {
                    if (!otherTable.getAllCellsInColumn(c).contains(getCell(r, c))) {
                        tablesMismatch(r, c, TableMismatchCause.MISSING_MISMATCH);
                        break;
                    }
                }
            }
        }
    }

    private void findDuplicateEntries() {
        for (int r = 0; r < getRowCount(); r++) {
            for (int i = 0; i < getRowCount(); i++) {
                if (r != i && compareSequenceRows(r, i, this)) {
                    throw new InputMismatchException("Duplicate entries were found");
                }
            }
        }
    }

    private boolean compareSequenceRows(int row, int otherTableRow, SequencedOnlyColumnsTable otherTable) {
        for (int c = 0; c < getColumnCount(); c++) {
            int otherTableColumnIndex = otherTable.getColumnIndex(columns.get(c));
            if (otherTableColumnIndex == -1) {
                tablesMismatch(TableMismatchCause.COLUMN_NOT_FOUND);
            }

            if (isDimensionExplanation(columns.get(c))) {
                if (getCell(row, c).isEmpty() || otherTable.getCell(otherTableRow, otherTableColumnIndex).isEmpty()) {
                    tablesMismatch(null, c, TableMismatchCause.MISSING_EXPLANATION);
                }
            } else {
                if (!getCell(row, c).equals(otherTable.getCell(otherTableRow, otherTableColumnIndex))) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean compareSequenceRowsIgnoreCase(int row, int otherTableRow, SequencedOnlyColumnsTable otherTable) {
        for (int c = 0; c < getColumnCount(); c++) {
            int otherTableColumnIndex = otherTable.getColumnIndex(columns.get(c));
            if (otherTableColumnIndex == -1) {
                tablesMismatch(TableMismatchCause.COLUMN_NOT_FOUND);
            }

            if (isDimensionExplanation(columns.get(c))) {
                if (getCell(row, c).isEmpty() || otherTable.getCell(otherTableRow, otherTableColumnIndex).isEmpty()) {
                    tablesMismatch(null, c, TableMismatchCause.MISSING_EXPLANATION);
                }
            } else {
                if (!getCell(row, c).equalsIgnoreCase(otherTable.getCell(otherTableRow, otherTableColumnIndex))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void parse(List<String> contentLines) {
        super.parse(contentLines);

        findDuplicateEntries();
    }
}
