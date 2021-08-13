package thkoeln.st.springtestlib.specification.diagram.implementations;

import thkoeln.st.springtestlib.specification.diagram.Diagram;
import thkoeln.st.springtestlib.specification.diagram.DiagramConfig;
import thkoeln.st.springtestlib.specification.diagram.ElementAsserter;
import thkoeln.st.springtestlib.specification.diagram.elements.ElementType;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.actorelement.ActorElement;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.relationelement.RelationElement;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.usecaseelement.UseCaseElement;

import java.util.List;

public class UseCaseDiagram extends Diagram {

    private final ElementAsserter<ActorElement> actorAsserter = new ElementAsserter<>(ElementType.ACTOR,
            "Too many actors",
            "A certain actor is missing",
            (expectedElement, actualElement, diagramConfig) -> { },
            duplicateElement -> "There are multiple actors with name: " + duplicateElement.getId());

    private final ElementAsserter<UseCaseElement> useCaseAsserter = new ElementAsserter<>(ElementType.USE_CASE,
            "Too many use cases",
            "A certain use case is missing",
            (expectedElement, actualElement, diagramConfig) -> { },
            duplicateElement -> "There are multiple use cases with name: " + duplicateElement.getId());

    private final ElementAsserter<RelationElement> relationAsserter = new ElementAsserter<>(ElementType.RELATION,
            "Too many relations",
            "A certain relation is missing",
            (expectedElement, actualElement, diagramConfig) -> { },
            duplicateElement -> "There are multiple relations connecting the same use cases or actors: "
                    + duplicateElement.getReferencedElement1().getId() + ", "
                    + duplicateElement.getReferencedElement2().getId());

    @Override
    public void assertActualDiagram(Diagram actualDiagram, DiagramConfig diagramConfig) {
        initializeDiagramExceptionHelper(diagramConfig);

        actorAsserter.assertElements(this, actualDiagram, diagramConfig, diagramExceptionHelper);
        useCaseAsserter.assertElements(this, actualDiagram, diagramConfig, diagramExceptionHelper);
        relationAsserter.assertElements(this, actualDiagram, diagramConfig, diagramExceptionHelper);

        diagramExceptionHelper.throwSummarizedException();
    }
}
