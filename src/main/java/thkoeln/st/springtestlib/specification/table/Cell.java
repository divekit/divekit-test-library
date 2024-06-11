package thkoeln.st.springtestlib.specification.table;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

public class Cell {

    private List<String> contents = new ArrayList<>();
    private String hashedContent;
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

        if (hashedContent != null && otherCell.hashedContent != null) {
            return hashedContent.equals(otherCell.hashedContent);
        }

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

    @Override
    public String toString() {
        if (hashedContent != null) {
            return hashedContent;
        }
        return String.join(", ", contents);
    }

    public static Cell parseCell(String content, String[] validCellValues, boolean caseSensitive, boolean isCellHashed, boolean shouldCellBeHashed) {
        Cell newCell = new Cell( validCellValues );
        newCell.setCaseSensitive( caseSensitive );

        if (isCellHashed) {
            newCell.hashedContent = content;
            return newCell;
        }

        String[] split = content.split(",");
        for (String s : split) {
            s = s.trim();

            if (!s.isEmpty()) {
                newCell.addContent(s);
            }
        }

        if (shouldCellBeHashed) {
            newCell.contents.sort(String::compareToIgnoreCase);
            String joinedContent = String.join(",", newCell.contents);
            try {
                if (caseSensitive) {
                    newCell.hashedContent = Hashing.hashString(joinedContent);
                } else {
                    newCell.hashedContent = Hashing.hashString(joinedContent.toLowerCase());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return newCell;
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

    public String getHashedContent() {
        return hashedContent;
    }
}
