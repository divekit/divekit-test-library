package thkoeln.st.springtestlib.specification.table.exceptions;

import java.util.List;

public class DivekitRowMismatchException extends DivekitTableException {

  private final Integer row;
  private final Integer column;
  private final List<Integer> expectedRows;

  public DivekitRowMismatchException(String message, Integer row, Integer column, List<Integer> expectedRows) {
    super(message);
    this.row = row;
    this.column = column;
    this.expectedRows = expectedRows;
  }

  public Integer getRow() {
    return row;
  }

  public Integer getColumn() {
    return column;
  }

  public List<Integer> getExpectedRows() {
    return expectedRows;
  }
}