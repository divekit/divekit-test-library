package thkoeln.st.springtestlib.specification.diagram.implementations;

import thkoeln.st.springtestlib.specification.diagram.Diagram;
import thkoeln.st.springtestlib.specification.diagram.DiagramConfig;
import thkoeln.st.springtestlib.specification.diagram.elements.ElementType;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.actorelement.ActorElement;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.relationelement.RelationElement;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.usecaseelement.UseCaseElement;

import java.util.List;

public class UseCaseDiagram extends Diagram {

    @Override
    public void compareToActualDiagram(Diagram actualDiagram, DiagramConfig diagramConfig) {
        initializeDiagramExceptionHelper(diagramConfig);

        assertActors(actualDiagram, diagramConfig);
        assertUseCases(actualDiagram, diagramConfig);
        assertRelations(actualDiagram, diagramConfig);

        diagramExceptionHelper.throwSummarizedException();
    }

    // Assert Actors
    private void assertActors(Diagram actualDiagram, DiagramConfig diagramConfig) { // TODO generische methode die function entgegennimmt, um expected mit actual abzugleichen?
        List<ActorElement> expectedActorElements = getElementsByType(ElementType.ACTOR);
        List<ActorElement> actualActorElements = actualDiagram.getElementsByType(ElementType.ACTOR);

        for (ActorElement expectedActorElement : expectedActorElements) {
            compareExpectedActorWithActualActors(expectedActorElement, actualActorElements, diagramConfig);
        }

        if (!diagramConfig.isPartialTest() && actualActorElements.size() > expectedActorElements.size()) {
            diagramExceptionHelper.throwException("Too many actors");
        }
    }

    private void compareExpectedActorWithActualActors(ActorElement expectedActorElement, List<ActorElement> actualActorElements, DiagramConfig diagramConfig) {
        boolean found = false;
        for (ActorElement actualActorElement : actualActorElements) {
            if (expectedActorElement.equals(actualActorElement)) {
                if (found) {
                    diagramExceptionHelper.throwException("There are multiple actors with name: " + expectedActorElement.getId());
                }

                found = true;
            }
        }

        if (!found) {
            diagramExceptionHelper.throwException("A certain actor is missing");
        }
    }

    // Assert Use Cases
    private void assertUseCases(Diagram actualDiagram, DiagramConfig diagramConfig) { // TODO generische methode die function entgegennimmt, um expected mit actual abzugleichen?
        List<UseCaseElement> expectedUseCaseElements = getElementsByType(ElementType.USE_CASE);
        List<UseCaseElement> actualUseCaseElements = actualDiagram.getElementsByType(ElementType.USE_CASE);

        for (UseCaseElement expectedUseCaseElement : expectedUseCaseElements) {
            compareExpectedUseCaseWithActualUseCases(expectedUseCaseElement, actualUseCaseElements, diagramConfig);
        }

        if (!diagramConfig.isPartialTest() && actualUseCaseElements.size() > expectedUseCaseElements.size()) {
            diagramExceptionHelper.throwException("Too many use cases");
        }
    }

    private void compareExpectedUseCaseWithActualUseCases(UseCaseElement expectedUseCaseElement, List<UseCaseElement> actualUseCaseElements, DiagramConfig diagramConfig) {
        boolean found = false;
        for (UseCaseElement actualUseCaseElement : actualUseCaseElements) {
            if (expectedUseCaseElement.equals(actualUseCaseElement)) {
                if (found) {
                    diagramExceptionHelper.throwException("There are multiple use cases with name: " + expectedUseCaseElement.getId());
                }

                found = true;
            }
        }

        if (!found) {
            diagramExceptionHelper.throwException("A certain use case is missing");
        }
    }

    // Assert Relations
    private void assertRelations(Diagram actualDiagram, DiagramConfig diagramConfig) { // TODO generische methode die function entgegennimmt, um expected mit actual abzugleichen?
        List<RelationElement> expectedRelationElements = getElementsByType(ElementType.RELATION);
        List<RelationElement> actualRelationElements = actualDiagram.getElementsByType(ElementType.RELATION);

        for (RelationElement expectedRelationElement : expectedRelationElements) {
            compareExpectedRelationWithActualRelations(expectedRelationElement, actualRelationElements, diagramConfig);
        }

        if (!diagramConfig.isPartialTest() && actualRelationElements.size() > expectedRelationElements.size()) {
            diagramExceptionHelper.throwException("Too many relations");
        }
    }

    private void compareExpectedRelationWithActualRelations(RelationElement expectedRelationElement, List<RelationElement> actualRelationElements, DiagramConfig diagramConfig) {
        boolean found = false;
        for (RelationElement actualRelationElement : actualRelationElements) {
            if (expectedRelationElement.compareToRelationAndSwitchDirectionIfNecessary(actualRelationElement)) {
                if (found) {
                    diagramExceptionHelper.throwException("There are multiple relations connecting classes: "
                        + expectedRelationElement.getReferencedElement1().getId() + ", " + expectedRelationElement.getReferencedElement2().getId());
                }
                found = true;
            }
        }

        if (!found) {
            diagramExceptionHelper.throwException("A certain relation is missing");
        }
    }
}
