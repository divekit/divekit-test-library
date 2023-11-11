package thkoeln.st.springtestlib.specification.table.exceptions;

import java.util.List;

public class DivekitColumnMismatchException extends DivekitTableException {

  private final Integer row;
  private final Integer column;
  private final List<Integer> expectedColumns;

  public DivekitColumnMismatchException(String message, Integer row, Integer column, List<Integer> expectedColumns) {
    super(message);
    this.row = row;
    this.column = column;
    this.expectedColumns = expectedColumns;
    this.expectedColumns.remove(column);
  }

  public Integer getRow() {
    return row;
  }

  public Integer getColumn() {
    return column;
  }

  public List<Integer> getExpectedColumns() {
    return expectedColumns;
  }
}