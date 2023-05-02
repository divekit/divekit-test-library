package thkoeln.st.springtestlib.specification.diagram;

import org.junit.Test;
import thkoeln.st.springtestlib.specification.diagram.implementations.DiagramType;

public class UmletStateDiagramTests {

    @Test
    public void testStateDiagram() throws Exception {
        GenericDiagramSpecificationTests genericDiagramSpecificationTests = new GenericDiagramSpecificationTests();
        genericDiagramSpecificationTests.testDiagram("state-diagram-solution.uxf", "state-diagram.uxf",
                DiagramType.STATE_DIAGRAM, new DiagramConfig(true, false));
    }

    @Test
    public void testPartialStateDiagram() throws Exception {
        GenericDiagramSpecificationTests genericDiagramSpecificationTests = new GenericDiagramSpecificationTests();
        genericDiagramSpecificationTests.testDiagram("state-diagram2-part-solution.uxf", "state-diagram2.uxf",
                DiagramType.STATE_DIAGRAM, new DiagramConfig(true, true));
    }
}
