package thkoeln.st.springtestlib.specification.table;

public class TableConfig {
    private TableType tableType;
    private String[] explanationDimensions;
    private String[] validRowValues;
    private String[] validColumnValues;
    private String[] validCellValues;
    private boolean[] caseSensitiveColumns;
    private boolean[] hashedColumns;

    private boolean showRowHints;
    private boolean showColumnHints;


    public TableType getTableType() {
        return tableType;
    }

    public void setTableType(TableType tableType) {
        this.tableType = tableType;
    }

    public String[] getExplanationDimensions() {
        return explanationDimensions;
    }

    public void setExplanationDimensions(String[] explanationDimensions) {
        this.explanationDimensions = explanationDimensions;
    }

    public String[] getValidRowValues() {
        return validRowValues;
    }

    public void setValidRowValues(String[] validRowValues) {
        this.validRowValues = validRowValues;
    }

    public String[] getValidCellValues() {
        return validCellValues;
    }

    public void setValidCellValues(String[] validCellValues) {
        this.validCellValues = validCellValues;
    }

    public String[] getValidColumnValues() {
        return validColumnValues;
    }

    public void setValidColumnValues(String[] validColumnValues) {
        this.validColumnValues = validColumnValues;
    }

    public boolean isShowRowHints() {
        return showRowHints;
    }

    public void setShowRowHints(boolean showRowHints) {
        this.showRowHints = showRowHints;
    }

    public boolean isShowColumnHints() {
        return showColumnHints;
    }

    public void setShowColumnHints(boolean showColumnHints) {
        this.showColumnHints = showColumnHints;
    }

    public boolean isCaseSensitiveColumn( int column ) {
        if ( caseSensitiveColumns == null || column < 0 || column >= caseSensitiveColumns.length ) {
            return false;
        }
        return caseSensitiveColumns[column];
    }

    public boolean[] getCaseSensitiveColumns() {
        return caseSensitiveColumns;
    }

    public void setCaseSensitiveColumns( boolean[] caseSensitiveColumns ) {
        this.caseSensitiveColumns = caseSensitiveColumns;
    }

    public boolean isHashedColumn( int column ) {
        if ( hashedColumns == null || column < 0 || column >= hashedColumns.length ) {
            return false;
        }
        return hashedColumns[column];
    }

    public boolean[] getHashedColumns() {
        return hashedColumns;
    }

    public void setHashedColumns(boolean[] hashedColumns) {
        this.hashedColumns = hashedColumns;
    }
}
