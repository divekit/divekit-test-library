package thkoeln.st.springtestlib.specification.table.implementations;

import thkoeln.st.springtestlib.specification.table.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

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
                    tablesMismatch(null, c, TableMismatchCause.TOO_MANY_ROWS);
                }
                if (nonEmptyCells.size() > otherTableNonEmptyCells.size()) {
                    tablesMismatch(null, c, TableMismatchCause.NOT_ENOUGH_ROWS);
                }

                for (Cell nonEmptyCell : nonEmptyCells) {
                    if (!otherTableNonEmptyCells.contains(nonEmptyCell)) {
                        if (IntStream.range(0, otherTable.getColumnCount()).anyMatch(r -> otherTable.getAllNonEmptyCellsInColumn(r).contains(nonEmptyCell))) {
                            tablesMismatch(null, c, TableMismatchCause.WRONG_COLUMN_MISMATCH);
                        }
                        tablesMismatch(null, c, TableMismatchCause.CELL_MISMATCH);
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
