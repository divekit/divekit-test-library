package thkoeln.st.springtestlib.specification.diagram;

public class DiagramConfig {

    private boolean summarizeExceptions;
    private boolean partialTest;


    public DiagramConfig(boolean summarizeExceptions, boolean partialTest) {
        this.summarizeExceptions = summarizeExceptions;
        this.partialTest = partialTest;
    }

    public boolean isSummarizeExceptions() {
        return summarizeExceptions;
    }

    public void setSummarizeExceptions(boolean summarizeExceptions) {
        this.summarizeExceptions = summarizeExceptions;
    }

    public boolean isPartialTest() {
        return partialTest;
    }

    public void setPartialTest(boolean partialTest) {
        this.partialTest = partialTest;
    }
}
