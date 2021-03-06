package thkoeln.st.springtestlib.specification.diagram.implementations;

import thkoeln.st.springtestlib.specification.diagram.Diagram;
import thkoeln.st.springtestlib.specification.diagram.DiagramConfig;
import thkoeln.st.springtestlib.specification.diagram.ElementCollectionAsserter;
import thkoeln.st.springtestlib.specification.diagram.elements.ElementType;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.actorelement.ActorElement;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.nodeelement.NodeElement;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.relationelement.RelationElement;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.relationelement.RelationElementAsserter;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.stateelement.StateElement;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.usecaseelement.UseCaseElement;

public class StateDiagram extends Diagram {

    private final ElementCollectionAsserter<StateElement> stateAsserter = new ElementCollectionAsserter<>(ElementType.STATE,
            "Too many states",
            "A certain state is missing",
            (expectedElement, actualElement, diagramConfig) -> expectedElement.equals(actualElement),
            (expectedElement, actualElement, diagramConfig) -> { },
            (duplicateElement, diagramConfig) -> "There are multiple states with name: " + duplicateElement.getId(),
            diagramExceptionHelper);

    private final ElementCollectionAsserter<NodeElement> nodeAsserter = new ElementCollectionAsserter<>(ElementType.NODE,
            "Too many initial or final nodes",
            "An initial or final node is missing",
            (expectedElement, actualElement, diagramConfig) -> expectedElement.equals(actualElement),
            (expectedElement, actualElement, diagramConfig) -> { },
            (duplicateElement, diagramConfig) -> "There are multiple initial or final nodes with type: " + duplicateElement.getType(),
            diagramExceptionHelper);

    private final ElementCollectionAsserter<RelationElement> relationAsserter = new ElementCollectionAsserter<>(ElementType.RELATION,
            "Too many relations",
            "A certain relation is missing",
            (expectedElement, actualElement, diagramConfig) -> {
                if (diagramConfig.isPartialTest()) {
                    return RelationElementAsserter.compareRelationByReferencedElements(expectedElement, actualElement)
                            && RelationElementAsserter.compareRelationByArrows(expectedElement, actualElement);
                } else {
                    return RelationElementAsserter.compareRelationByReferencedElements(expectedElement, actualElement)
                            && RelationElementAsserter.compareRelationByArrows(expectedElement, actualElement)
                            && RelationElementAsserter.compareRelationByDescription(expectedElement, actualElement);
                }
            },
            (expectedElement, actualElement, diagramConfig) -> {
                RelationElementAsserter.assertRelationLine(expectedElement, actualElement, diagramConfig, diagramExceptionHelper);
                RelationElementAsserter.assertRelationCardinality(expectedElement, actualElement, diagramConfig, diagramExceptionHelper); // TODO replace with preemptive tests for existing cardinalities
            },
            (duplicateElement, diagramConfig) -> diagramConfig.isPartialTest() ? null :
                    "There are multiple relations connecting the same states: "
                    + duplicateElement.getReferencedElement1().getId() + ", "
                    + duplicateElement.getReferencedElement2().getId(),
            diagramExceptionHelper);

    @Override
    public void assertActualDiagram(Diagram actualDiagram, DiagramConfig diagramConfig) {
        diagramExceptionHelper.setSumExceptions(diagramConfig.isSummarizeExceptions());

        stateAsserter.assertElements(this, actualDiagram, diagramConfig);
        nodeAsserter.assertElements(this, actualDiagram, diagramConfig);
        relationAsserter.assertElements(this, actualDiagram, diagramConfig);

        diagramExceptionHelper.throwSummarizedException();
    }
}
