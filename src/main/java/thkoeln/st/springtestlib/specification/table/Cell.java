package thkoeln.st.springtestlib.specification.table;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

public class Cell {

    private List<String> contents = new ArrayList<>();
    private String[] validCellValues;
    private boolean caseSensitive = false;


    private Cell(String[] validCellValues) {
        this.validCellValues = validCellValues;
    }

    public void addContent(String newContent) {
        if (!isContentValid(newContent)) {
            throw new InputMismatchException(newContent + " is not a valid cell value");
        }

        contents.add(newContent);
    }

    public boolean isContentValid(String newContent) {
        if (validCellValues.length == 0) {
            return true;
        }

        for (String validCellValue : validCellValues) {
            if (validCellValue.equalsIgnoreCase(newContent)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsContent(String content) {
        for (String testContent : contents) {
            if ( !caseSensitive ) {
                if (testContent.equalsIgnoreCase(content)) {
                    return true;
                }
            }
            else {
                if (testContent.equals(content)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Cell)) {
            return false;
        }

        Cell otherCell = (Cell)obj;

        if (contents.size() != otherCell.contents.size()) {
            return false;
        }

        for (String testContent : otherCell.contents) {
            if (!containsContent(testContent)) {
                return false;
            }
        }
        return true;
    }

    public static Cell parseCell(String content, String[] validCellValues, boolean caseSensitive) {
        Cell newCell = new Cell( validCellValues );
        newCell.setCaseSensitive( caseSensitive );

        String[] split = content.split(",");
        for (String s : split) {
            s = s.trim();

            if (!s.isEmpty()) {
                newCell.addContent(s);
            }
        }

        return newCell;
    }

    public static Cell parseCell(String content, String[] validCellValues) {
        return parseCell(content, validCellValues, false);
    }


    public boolean isEmpty() {
        return contents.isEmpty();
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public void setCaseSensitive( boolean caseSensitive ) {
        this.caseSensitive = caseSensitive;
    }
}
