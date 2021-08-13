package thkoeln.st.springtestlib.specification.diagram.implementations;

import thkoeln.st.springtestlib.specification.diagram.Diagram;
import thkoeln.st.springtestlib.specification.diagram.DiagramConfig;
import thkoeln.st.springtestlib.specification.diagram.ElementAsserter;
import thkoeln.st.springtestlib.specification.diagram.elements.ElementType;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.actorelement.ActorElement;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.classelement.ClassAttribute;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.classelement.ClassElement;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.relationelement.RelationElement;

import java.util.List;

public class ClassDiagram extends Diagram {

    private final ElementAsserter<ClassElement> classAsserter = new ElementAsserter<>(ElementType.CLASS,
            "Too many classes",
            "A certain class is missing",
            this::compareExpectedClassWithActualClass,
            duplicateElement -> "There are multiple classes with name: " + duplicateElement.getId());

    private final ElementAsserter<RelationElement> relationAsserter = new ElementAsserter<>(ElementType.RELATION,
            "Too many relations",
            "A certain relation is missing",
            (expectedElement, actualElement, diagramConfig) -> {
                if (!diagramConfig.isPartialTest()) {
                    compareExpectedRelationWithActualRelation(expectedElement, actualElement, diagramConfig);
                }
            },
            duplicateElement -> "There are multiple relations connecting the same classes: "
                    + duplicateElement.getReferencedElement1().getId() + ", "
                    + duplicateElement.getReferencedElement2().getId());

    @Override
    public void assertActualDiagram(Diagram actualDiagram, DiagramConfig diagramConfig) {
        initializeDiagramExceptionHelper(diagramConfig);

        classAsserter.assertElements(this, actualDiagram, diagramConfig, diagramExceptionHelper);
        relationAsserter.assertElements(this, actualDiagram, diagramConfig, diagramExceptionHelper);

        diagramExceptionHelper.throwSummarizedException();
    }

    // Assert Classes
    private void compareExpectedClassWithActualClass(ClassElement expectedClassElement, ClassElement actualClassElement, DiagramConfig diagramConfig) {
        compareExpectedClassAttributesWithActualClassAttributes(expectedClassElement, actualClassElement, diagramConfig);
    }

    private void compareExpectedClassAttributesWithActualClassAttributes(ClassElement expectedClassElement, ClassElement actualClassElement, DiagramConfig diagramConfig) {
        List<ClassAttribute> expectedAttributes = expectedClassElement.getAttributes();
        List<ClassAttribute> actualAttributes = actualClassElement.getAttributes();

        for (ClassAttribute expectedAttribute : expectedAttributes) {
            compareExpectedClassAttributeWithActualClassAttributes(expectedClassElement, expectedAttribute, actualAttributes, diagramConfig);
        }

        if (!diagramConfig.isPartialTest() && actualAttributes.size() > expectedAttributes.size()) {
            diagramExceptionHelper.throwException("Too many attributes in class: " + expectedClassElement.getId());
        }
    }

    private void compareExpectedClassAttributeWithActualClassAttributes(ClassElement expectedClassElement, ClassAttribute expectedAttribute, List<ClassAttribute> actualAttributes, DiagramConfig diagramConfig) {
        for (ClassAttribute actualAttribute : actualAttributes) {
            if (expectedAttribute.equals(actualAttribute)) {
                return;
            }
        }
        diagramExceptionHelper.throwException("A certain attribute is missing in class: " + expectedClassElement.getId());
    }

    // Assert Relations
    private void compareExpectedRelationWithActualRelation(RelationElement expectedRelationElement, RelationElement actualRelationElement, DiagramConfig diagramConfig) {
        actualRelationElement.switchDirectionIfNecessary(expectedRelationElement);

        if (expectedRelationElement.getRelationLineType() != actualRelationElement.getRelationLineType()) {
            diagramExceptionHelper.throwException("Relation line type is not valid for relation between classes: "
                + expectedRelationElement.getReferencedElement1().getId() + ", " + expectedRelationElement.getReferencedElement2().getId());
        }

        if (expectedRelationElement.getRelationPointer1().getRelationArrowType() != actualRelationElement.getRelationPointer1().getRelationArrowType()
            || expectedRelationElement.getRelationPointer2().getRelationArrowType() != actualRelationElement.getRelationPointer2().getRelationArrowType()) {
            diagramExceptionHelper.throwException("At least one relation arrow type is not valid for relation between classes: "
                    + expectedRelationElement.getReferencedElement1().getId() + ", " + expectedRelationElement.getReferencedElement2().getId());
        }

        if (!expectedRelationElement.getRelationPointer1().getCardinality().equals(actualRelationElement.getRelationPointer1().getCardinality())
                || !expectedRelationElement.getRelationPointer2().getCardinality().equals(actualRelationElement.getRelationPointer2().getCardinality())) {
            diagramExceptionHelper.throwException("At least one relation cardinality is not valid for relation between classes: "
                    + expectedRelationElement.getReferencedElement1().getId() + ", " + expectedRelationElement.getReferencedElement2().getId());
        }
    }
}
