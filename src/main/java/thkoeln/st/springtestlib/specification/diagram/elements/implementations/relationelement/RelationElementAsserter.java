package thkoeln.st.springtestlib.specification.diagram.elements.implementations.relationelement;

import thkoeln.st.springtestlib.specification.diagram.DiagramConfig;
import thkoeln.st.springtestlib.specification.diagram.DiagramExceptionHelper;
import thkoeln.st.springtestlib.specification.diagram.elements.ElementAsserter;

public class RelationElementAsserter extends ElementAsserter {

    public RelationElementAsserter(DiagramExceptionHelper diagramExceptionHelper) {
        super(diagramExceptionHelper);
    }

    public void compareExpectedRelationWithActualRelation(RelationElement expectedRelationElement,
                                                          RelationElement actualRelationElement,
                                                          DiagramConfig diagramConfig) {
        actualRelationElement.switchDirectionIfNecessary(expectedRelationElement);

        if (expectedRelationElement.getRelationLineType() != actualRelationElement.getRelationLineType()) {
            diagramExceptionHelper.throwException("Relation line type is not valid for relation between: "
                    + expectedRelationElement.getReferencedElement1().getId() + ", " + expectedRelationElement.getReferencedElement2().getId());
        }

        if (expectedRelationElement.getRelationPointer1().getRelationArrowType() != actualRelationElement.getRelationPointer1().getRelationArrowType()
                || expectedRelationElement.getRelationPointer2().getRelationArrowType() != actualRelationElement.getRelationPointer2().getRelationArrowType()) {
            diagramExceptionHelper.throwException("At least one relation arrow type is not valid for relation between: "
                    + expectedRelationElement.getReferencedElement1().getId() + ", " + expectedRelationElement.getReferencedElement2().getId());
        }

        if (!expectedRelationElement.getRelationPointer1().getCardinality().equals(actualRelationElement.getRelationPointer1().getCardinality())
                || !expectedRelationElement.getRelationPointer2().getCardinality().equals(actualRelationElement.getRelationPointer2().getCardinality())) {
            diagramExceptionHelper.throwException("At least one relation cardinality is not valid for relation between: "
                    + expectedRelationElement.getReferencedElement1().getId() + ", " + expectedRelationElement.getReferencedElement2().getId());
        }

        if (!expectedRelationElement.getDescription().equals(actualRelationElement.getDescription())) {
            diagramExceptionHelper.throwException("The description does not match for relation between: "
                    + expectedRelationElement.getReferencedElement1().getId() + ", " + expectedRelationElement.getReferencedElement2().getId());
        }
    }
}
