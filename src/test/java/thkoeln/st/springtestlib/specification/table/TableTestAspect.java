package thkoeln.st.springtestlib.specification.table;

public class TableTestAspect {
    private String aspectName;
    private Class expectedException;
    private String expectedExceptionMessage;

    public TableTestAspect( String aspectName, Class expectedException, String expectedExceptionMessage ) {
        this.aspectName = aspectName;
        this.expectedException = expectedException;
        this.expectedExceptionMessage = expectedExceptionMessage;
    }

    public String getAspectName() {
        return aspectName;
    }

     public Class getExpectedException() {
        return expectedException;
    }

    public String getExpectedExceptionMessage() {
        return expectedExceptionMessage;
    }
}
