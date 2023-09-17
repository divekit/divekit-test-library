package thkoeln.st.springtestlib.specification.diagram;

import org.junit.Test;
import thkoeln.st.springtestlib.specification.diagram.implementations.DiagramType;

public class UmletClassDiagramTests {

    @Test
    public void testClassDiagram() throws Exception {
        GenericDiagramSpecificationTests genericDiagramSpecificationTests = new GenericDiagramSpecificationTests();
        genericDiagramSpecificationTests.testDiagram( "specification/diagram/class-diagram-solution.uxf", "specification/diagram/class-diagram.uxf",
                DiagramType.CLASS_DIAGRAM, new DiagramConfig(true, false));
    }

//    @Test
    public void testWrongClassDiagram() throws Exception {
        GenericDiagramSpecificationTests genericDiagramSpecificationTests = new GenericDiagramSpecificationTests();
        genericDiagramSpecificationTests.testDiagram( "specification/diagram/class-diagram-solution.uxf", "specification/diagram/class-diagram-w.uxf",
                DiagramType.CLASS_DIAGRAM, new DiagramConfig(true, false));
    }

    @Test
    public void testPartialClassDiagram() throws Exception {
        GenericDiagramSpecificationTests genericDiagramSpecificationTests = new GenericDiagramSpecificationTests();
        genericDiagramSpecificationTests.testDiagram( "specification/diagram/class-diagram-part-solution.uxf", "specification/diagram/class-diagram.uxf",
                DiagramType.CLASS_DIAGRAM, new DiagramConfig(true, true));
    }
}
