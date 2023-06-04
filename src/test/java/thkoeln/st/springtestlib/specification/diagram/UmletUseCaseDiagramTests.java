package thkoeln.st.springtestlib.specification.diagram;

import org.junit.Test;
import thkoeln.st.springtestlib.specification.diagram.implementations.DiagramType;

public class UmletUseCaseDiagramTests {

    @Test
    public void testUseCaseDiagram() throws Exception {
        GenericDiagramSpecificationTests genericDiagramSpecificationTests = new GenericDiagramSpecificationTests();
        genericDiagramSpecificationTests.testDiagram( "specification/diagram/uc-diagram-solution.uxf", "specification/diagram/uc-diagram.uxf",
                DiagramType.USE_CASE_DIAGRAM, new DiagramConfig(true, false));
    }

    @Test
    public void testWrongUseCaseDiagram() throws Exception {
        GenericDiagramSpecificationTests genericDiagramSpecificationTests = new GenericDiagramSpecificationTests();
        genericDiagramSpecificationTests.testDiagram( "specification/diagram/uc-diagram-solution.uxf", "specification/diagram/uc-diagram-w.uxf",
                DiagramType.USE_CASE_DIAGRAM, new DiagramConfig(true, false));
    }

    @Test
    public void testPartialUseCaseDiagram() throws Exception {
        GenericDiagramSpecificationTests genericDiagramSpecificationTests = new GenericDiagramSpecificationTests();
        genericDiagramSpecificationTests.testDiagram( "specification/diagram/uc-diagram-part-solution.uxf", "specification/diagram/uc-diagram.uxf",
                DiagramType.USE_CASE_DIAGRAM, new DiagramConfig(true, true));
    }
}
