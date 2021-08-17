package thkoeln.st.springtestlib.specification.diagram;

import org.junit.Test;
import thkoeln.st.springtestlib.specification.diagram.implementations.DiagramType;

public class UmletDiagramParserTests {

    // Class Diagram
    @Test
    public void testClassDiagram() throws Exception {
        GenericDiagramSpecificationTests genericDiagramSpecificationTests = new GenericDiagramSpecificationTests();
        genericDiagramSpecificationTests.testDiagram("class-diagram-solution.uxf", "class-diagram.uxf",
                DiagramType.CLASS_DIAGRAM, new DiagramConfig(true, false));
    }

    @Test
    public void testWrongClassDiagram() throws Exception {
        GenericDiagramSpecificationTests genericDiagramSpecificationTests = new GenericDiagramSpecificationTests();
        genericDiagramSpecificationTests.testDiagram("class-diagram-solution.uxf", "class-diagram-w.uxf",
                DiagramType.CLASS_DIAGRAM, new DiagramConfig(true, false));
    }

    @Test
    public void testPartialClassDiagram() throws Exception {
        GenericDiagramSpecificationTests genericDiagramSpecificationTests = new GenericDiagramSpecificationTests();
        genericDiagramSpecificationTests.testDiagram("class-diagram-part-solution.uxf", "class-diagram.uxf",
                DiagramType.CLASS_DIAGRAM, new DiagramConfig(true, true));
    }

    // Use Case Diagram
    @Test
    public void testUseCaseDiagram() throws Exception {
        GenericDiagramSpecificationTests genericDiagramSpecificationTests = new GenericDiagramSpecificationTests();
        genericDiagramSpecificationTests.testDiagram("uc-diagram-solution.uxf", "uc-diagram.uxf",
                DiagramType.USE_CASE_DIAGRAM, new DiagramConfig(true, false));
    }

    @Test
    public void testWrongUseCaseDiagram() throws Exception {
        GenericDiagramSpecificationTests genericDiagramSpecificationTests = new GenericDiagramSpecificationTests();
        genericDiagramSpecificationTests.testDiagram("uc-diagram-solution.uxf", "uc-diagram-w.uxf",
                DiagramType.USE_CASE_DIAGRAM, new DiagramConfig(true, false));
    }

    @Test
    public void testPartialUseCaseDiagram() throws Exception {
        GenericDiagramSpecificationTests genericDiagramSpecificationTests = new GenericDiagramSpecificationTests();
        genericDiagramSpecificationTests.testDiagram("uc-diagram-part-solution.uxf", "uc-diagram.uxf",
                DiagramType.USE_CASE_DIAGRAM, new DiagramConfig(true, true));
    }

    // State Diagram
    @Test
    public void testStateDiagram() throws Exception {
        GenericDiagramSpecificationTests genericDiagramSpecificationTests = new GenericDiagramSpecificationTests();
        genericDiagramSpecificationTests.testDiagram("state-diagram-solution.uxf", "state-diagram.uxf",
                DiagramType.STATE_DIAGRAM, new DiagramConfig(true, false));
    }
}
