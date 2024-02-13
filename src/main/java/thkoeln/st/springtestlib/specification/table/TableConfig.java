package thkoeln.st.springtestlib.specification.table;

public class TableConfig {
    private String[] explanationDimensions;
    private String[] validRowValues;
    private String[] validColumnValues;
    private String[] validCellValues;
    private boolean[] caseSensitiveColumns;

    private boolean showRowHints;
    private boolean showColumnHints;

    private boolean[] shouldColumnsBeHashed;

    private TableType tableType;


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

    public boolean shouldColumnBeHashed( int column ) {
        if ( shouldColumnsBeHashed == null || column < 0 || column >= shouldColumnsBeHashed.length ) {
            return false;
        }
        return shouldColumnsBeHashed[column];
    }

    public boolean[] shouldColumnsBeHashed() {
        return shouldColumnsBeHashed;
    }

    public void setShouldColumnsBeHashed( boolean[] shouldColumnsBeHashed ) {
        this.shouldColumnsBeHashed = shouldColumnsBeHashed;
    }

    public TableType getTableType() {
        return tableType;
    }

    public void setTableType(TableType tableType) {
        this.tableType = tableType;
    }
}
