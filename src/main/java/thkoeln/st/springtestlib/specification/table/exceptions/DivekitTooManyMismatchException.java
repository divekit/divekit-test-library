package thkoeln.st.springtestlib.specification.table.exceptions;

public class DivekitTooManyMismatchException extends DivekitTableException {

  private final Integer row;
  private final Integer column;

  public DivekitTooManyMismatchException(String message, Integer row, Integer column) {
    super(message);
    this.row = row;
    this.column = column;
  }

  public Integer getRow() {
    return row;
  }

  public Integer getColumn() {
    return column;
  }
}