package thkoeln.st.springtestlib.specification.diagram.implementations;

import thkoeln.st.springtestlib.specification.diagram.Diagram;
import thkoeln.st.springtestlib.specification.diagram.DiagramConfig;
import thkoeln.st.springtestlib.specification.diagram.ElementCollectionAsserter;
import thkoeln.st.springtestlib.specification.diagram.elements.ElementType;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.actorelement.ActorElement;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.relationelement.RelationElement;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.relationelement.RelationElementAsserter;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.usecaseelement.UseCaseElement;

public class UseCaseDiagram extends Diagram {

    private final ElementCollectionAsserter<ActorElement> actorAsserter = new ElementCollectionAsserter<>(ElementType.ACTOR,
            "Too many actors",
            "A certain actor is missing",
            (expectedElement, actualElement, diagramConfig) -> expectedElement.equals(actualElement),
            (expectedElement, actualElement, diagramConfig) -> { },
            (duplicateElement, diagramConfig) -> "There are multiple actors with name: " + duplicateElement.getId(),
            diagramExceptionHelper);

    private final ElementCollectionAsserter<UseCaseElement> useCaseAsserter = new ElementCollectionAsserter<>(ElementType.USE_CASE,
            "Too many use cases",
            "A certain use case is missing",
            (expectedElement, actualElement, diagramConfig) -> expectedElement.equals(actualElement),
            (expectedElement, actualElement, diagramConfig) -> { },
            (duplicateElement, diagramConfig) -> "There are multiple use cases with name: " + duplicateElement.getId(),
            diagramExceptionHelper);

    private final ElementCollectionAsserter<RelationElement> relationAsserter = new ElementCollectionAsserter<>(ElementType.RELATION,
            "Too many relations",
            "A certain relation is missing",
            (expectedElement, actualElement, diagramConfig) -> RelationElementAsserter.compareRelationByReferencedElements(expectedElement, actualElement),
            (expectedElement, actualElement, diagramConfig) -> {
                RelationElementAsserter.assertRelationLine(expectedElement, actualElement, diagramConfig, diagramExceptionHelper);
                RelationElementAsserter.assertRelationArrows(expectedElement, actualElement, diagramConfig, diagramExceptionHelper);
                RelationElementAsserter.assertRelationCardinality(expectedElement, actualElement, diagramConfig, diagramExceptionHelper); // TODO replace with preemptive tests for existing cardinalities
                RelationElementAsserter.assertRelationDescription(expectedElement, actualElement, diagramConfig, diagramExceptionHelper);
            },
            (duplicateElement, diagramConfig) -> "There are multiple relations connecting the same use cases or actors: "
                    + duplicateElement.getReferencedElement1().getId() + ", "
                    + duplicateElement.getReferencedElement2().getId(),
            diagramExceptionHelper);

    @Override
    public void assertActualDiagram(Diagram actualDiagram, DiagramConfig diagramConfig) {
        diagramExceptionHelper.setSumExceptions(diagramConfig.isSummarizeExceptions());

        actorAsserter.assertElements(this, actualDiagram, diagramConfig);
        useCaseAsserter.assertElements(this, actualDiagram, diagramConfig);
        relationAsserter.assertElements(this, actualDiagram, diagramConfig);

        diagramExceptionHelper.throwSummarizedException();
    }
}
