package thkoeln.st.springtestlib.specification.diagram.elements.implementations.relationelement;

import thkoeln.st.springtestlib.specification.diagram.DiagramConfig;
import thkoeln.st.springtestlib.specification.diagram.DiagramExceptionHelper;

public class RelationElementAsserter {


    public static boolean compareRelationByReferencedElements(RelationElement expectedRelation, RelationElement actualRelation) {
        return  (expectedRelation.getReferencedElement1().equals(actualRelation.getReferencedElement1())
                && expectedRelation.getReferencedElement2().equals(actualRelation.getReferencedElement2()))
                || (expectedRelation.getReferencedElement1().equals(actualRelation.getReferencedElement2())
                && expectedRelation.getReferencedElement2().equals(actualRelation.getReferencedElement1()));
    }

    public static boolean compareRelationByArrows(RelationElement expectedRelation, RelationElement actualRelation) {
        actualRelation.switchDirectionIfNecessary(expectedRelation);

        boolean equalArrows = expectedRelation.getRelationPointer1().getRelationArrowType() == actualRelation.getRelationPointer1().getRelationArrowType()
                && expectedRelation.getRelationPointer2().getRelationArrowType() == actualRelation.getRelationPointer2().getRelationArrowType();

        boolean selfReferencing = expectedRelation.getReferencedElement1().equals(expectedRelation.getReferencedElement2());

        boolean equalArrowsReverse = expectedRelation.getRelationPointer1().getRelationArrowType() == actualRelation.getRelationPointer2().getRelationArrowType()
                && expectedRelation.getRelationPointer2().getRelationArrowType() == actualRelation.getRelationPointer1().getRelationArrowType();

        return equalArrows || (selfReferencing && equalArrowsReverse);
    }

    public static boolean compareRelationByCardinality(RelationElement expectedRelation, RelationElement actualRelation) {
        actualRelation.switchDirectionIfNecessary(expectedRelation);

        return expectedRelation.getRelationPointer1().getCardinality().equals(actualRelation.getRelationPointer1().getCardinality())
                && expectedRelation.getRelationPointer2().getCardinality().equals(actualRelation.getRelationPointer2().getCardinality());
    }

    public static boolean compareRelationByLineType(RelationElement expectedRelation, RelationElement actualRelation) {
        return expectedRelation.getRelationLineType() == actualRelation.getRelationLineType();
    }

    public static boolean compareRelationByDescription(RelationElement expectedRelation, RelationElement actualRelation) {
        return expectedRelation.getDescription().equals(actualRelation.getDescription());
    }

    public static void assertRelationLine(RelationElement expectedRelationElement,
                                   RelationElement actualRelationElement,
                                   DiagramConfig diagramConfig,
                                   DiagramExceptionHelper diagramExceptionHelper) {
        actualRelationElement.switchDirectionIfNecessary(expectedRelationElement);

        if (assertRelation(expectedRelationElement, diagramConfig)
                && !compareRelationByLineType(expectedRelationElement, actualRelationElement)) {
            diagramExceptionHelper.throwException("Relation line type is not valid for relation between: "
                    + expectedRelationElement.getReferencedElement1().getId() + ", " + expectedRelationElement.getReferencedElement2().getId());
        }
    }

    public static void assertRelationArrows(RelationElement expectedRelationElement,
                                    RelationElement actualRelationElement,
                                    DiagramConfig diagramConfig,
                                    DiagramExceptionHelper diagramExceptionHelper) {
        actualRelationElement.switchDirectionIfNecessary(expectedRelationElement);

        if (assertRelation(expectedRelationElement, diagramConfig)
                && !compareRelationByArrows(expectedRelationElement, actualRelationElement)) {
            diagramExceptionHelper.throwException("At least one relation arrow type is not valid for relation between: "
                    + expectedRelationElement.getReferencedElement1().getId() + ", " + expectedRelationElement.getReferencedElement2().getId());
        }
    }

    public static void assertRelationCardinality(RelationElement expectedRelationElement,
                                        RelationElement actualRelationElement,
                                        DiagramConfig diagramConfig,
                                        DiagramExceptionHelper diagramExceptionHelper) {
        actualRelationElement.switchDirectionIfNecessary(expectedRelationElement);

        if (assertRelation(expectedRelationElement, diagramConfig)
            && !compareRelationByCardinality(expectedRelationElement, actualRelationElement)) {
            diagramExceptionHelper.throwException("At least one relation cardinality is not valid for relation between: "
                    + expectedRelationElement.getReferencedElement1().getId() + ", " + expectedRelationElement.getReferencedElement2().getId());
        }
    }

    public static void assertRelationDescription(RelationElement expectedRelationElement,
                                          RelationElement actualRelationElement,
                                          DiagramConfig diagramConfig,
                                          DiagramExceptionHelper diagramExceptionHelper) {
        actualRelationElement.switchDirectionIfNecessary(expectedRelationElement);

        if (assertRelation(expectedRelationElement, diagramConfig)
            && !compareRelationByDescription(expectedRelationElement, actualRelationElement)) {
            diagramExceptionHelper.throwException("The description does not match for relation between: "
                    + expectedRelationElement.getReferencedElement1().getId() + ", " + expectedRelationElement.getReferencedElement2().getId());
        }
    }

    private static boolean assertRelation(RelationElement relationElement, DiagramConfig diagramConfig) {
        return relationElement.getRelationLineType() != RelationLineType.DOTTED;
    }
}
