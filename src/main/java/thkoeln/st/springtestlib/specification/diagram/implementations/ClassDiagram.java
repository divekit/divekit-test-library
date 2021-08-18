package thkoeln.st.springtestlib.specification.diagram.implementations;

import thkoeln.st.springtestlib.specification.diagram.Diagram;
import thkoeln.st.springtestlib.specification.diagram.DiagramConfig;
import thkoeln.st.springtestlib.specification.diagram.ElementCollectionAsserter;
import thkoeln.st.springtestlib.specification.diagram.elements.ElementType;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.classelement.ClassElement;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.classelement.ClassElementAsserter;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.relationelement.RelationElement;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.relationelement.RelationElementAsserter;


public class ClassDiagram extends Diagram {

    private final ElementCollectionAsserter<ClassElement> classAsserter = new ElementCollectionAsserter<>(ElementType.CLASS,
            "Too many classes",
            "A certain class is missing",
            (expectedElement, actualElement, diagramConfig) -> expectedElement.equals(actualElement),
            (expectedElement, actualElement, diagramConfig) ->
                    ClassElementAsserter.assertClassAttributes(expectedElement, actualElement, diagramConfig, diagramExceptionHelper),
            (duplicateElement, diagramConfig) -> "There are multiple classes with name: " + duplicateElement.getId(),
            diagramExceptionHelper);

    private final ElementCollectionAsserter<RelationElement> relationAsserter = new ElementCollectionAsserter<>(ElementType.RELATION,
            "Too many relations",
            "A certain relation is missing",
            (expectedElement, actualElement, diagramConfig) -> RelationElementAsserter.compareRelationByReferencedElements(expectedElement, actualElement),
            (expectedElement, actualElement, diagramConfig) -> {
                RelationElementAsserter.assertRelationLine(expectedElement, actualElement, diagramConfig, diagramExceptionHelper);
                RelationElementAsserter.assertRelationArrows(expectedElement, actualElement, diagramConfig, diagramExceptionHelper);
                RelationElementAsserter.assertRelationCardinality(expectedElement, actualElement, diagramConfig, diagramExceptionHelper);
            },
            (duplicateElement, diagramConfig) -> "There are multiple relations connecting the same classes: "
                    + duplicateElement.getReferencedElement1().getId() + ", "
                    + duplicateElement.getReferencedElement2().getId(),
            diagramExceptionHelper);

    @Override
    public void assertActualDiagram(Diagram actualDiagram, DiagramConfig diagramConfig) {
        diagramExceptionHelper.setSumExceptions(diagramConfig.isSummarizeExceptions());

        classAsserter.assertElements(this, actualDiagram, diagramConfig);
        relationAsserter.assertElements(this, actualDiagram, diagramConfig);

        diagramExceptionHelper.throwSummarizedException();
    }
}
