package thkoeln.st.springtestlib.specification.diagram.elements.implementations.relationelement;

import thkoeln.st.springtestlib.specification.diagram.DiagramConfig;
import thkoeln.st.springtestlib.specification.diagram.DiagramExceptionHelper;
import thkoeln.st.springtestlib.specification.diagram.elements.ElementAsserter;

public class RelationElementAsserter extends ElementAsserter {

    public RelationElementAsserter(DiagramExceptionHelper diagramExceptionHelper) {
        super(diagramExceptionHelper);
    }

    public void assertRelationLine(RelationElement expectedRelationElement,
                                   RelationElement actualRelationElement,
                                   DiagramConfig diagramConfig) {
        actualRelationElement.switchDirectionIfNecessary(expectedRelationElement);

        if (assertRelation(expectedRelationElement, diagramConfig)
                && expectedRelationElement.getRelationLineType() != actualRelationElement.getRelationLineType()) {
            diagramExceptionHelper.throwException("Relation line type is not valid for relation between: "
                    + expectedRelationElement.getReferencedElement1().getId() + ", " + expectedRelationElement.getReferencedElement2().getId());
        }
    }

    public void assertRelationArrows(RelationElement expectedRelationElement,
                                    RelationElement actualRelationElement,
                                    DiagramConfig diagramConfig) {
        actualRelationElement.switchDirectionIfNecessary(expectedRelationElement);

        if (assertRelation(expectedRelationElement, diagramConfig)
                && (expectedRelationElement.getRelationPointer1().getRelationArrowType() != actualRelationElement.getRelationPointer1().getRelationArrowType()
                || expectedRelationElement.getRelationPointer2().getRelationArrowType() != actualRelationElement.getRelationPointer2().getRelationArrowType())) {
            diagramExceptionHelper.throwException("At least one relation arrow type is not valid for relation between: "
                    + expectedRelationElement.getReferencedElement1().getId() + ", " + expectedRelationElement.getReferencedElement2().getId());
        }
    }

    public void assertRelationCardinality(RelationElement expectedRelationElement,
                                        RelationElement actualRelationElement,
                                        DiagramConfig diagramConfig) {
        actualRelationElement.switchDirectionIfNecessary(expectedRelationElement);

        if (assertRelation(expectedRelationElement, diagramConfig)
            && (!expectedRelationElement.getRelationPointer1().getCardinality().equals(actualRelationElement.getRelationPointer1().getCardinality())
                || !expectedRelationElement.getRelationPointer2().getCardinality().equals(actualRelationElement.getRelationPointer2().getCardinality()))) {
            diagramExceptionHelper.throwException("At least one relation cardinality is not valid for relation between: "
                    + expectedRelationElement.getReferencedElement1().getId() + ", " + expectedRelationElement.getReferencedElement2().getId());
        }
    }

    public void assertRelationDescription(RelationElement expectedRelationElement,
                                          RelationElement actualRelationElement,
                                          DiagramConfig diagramConfig) {
        actualRelationElement.switchDirectionIfNecessary(expectedRelationElement);

        if (assertRelation(expectedRelationElement, diagramConfig)
            && !expectedRelationElement.getDescription().equals(actualRelationElement.getDescription())) {
            diagramExceptionHelper.throwException("The description does not match for relation between: "
                    + expectedRelationElement.getReferencedElement1().getId() + ", " + expectedRelationElement.getReferencedElement2().getId());
        }
    }

    private boolean assertRelation(RelationElement relationElement, DiagramConfig diagramConfig) {
        return relationElement.getRelationLineType() != RelationLineType.DOTTED;
    }
}
