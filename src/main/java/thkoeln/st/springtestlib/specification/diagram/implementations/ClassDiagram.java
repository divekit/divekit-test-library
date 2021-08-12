package thkoeln.st.springtestlib.specification.diagram.implementations;

import thkoeln.st.springtestlib.specification.diagram.Diagram;
import thkoeln.st.springtestlib.specification.diagram.DiagramConfig;
import thkoeln.st.springtestlib.specification.diagram.elements.ElementType;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.classelement.ClassAttribute;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.classelement.ClassElement;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.relationelement.RelationElement;

import java.util.List;

public class ClassDiagram extends Diagram {

    @Override
    public void compareToActualDiagram(Diagram actualDiagram, DiagramConfig diagramConfig) {
        initializeDiagramExceptionHelper(diagramConfig);

        assertClasses(actualDiagram, diagramConfig);
        assertRelations(actualDiagram, diagramConfig);

        diagramExceptionHelper.throwSummarizedException();
    }

    // Assert Classes
    private void assertClasses(Diagram actualDiagram, DiagramConfig diagramConfig) {
        List<ClassElement> expectedClassElements = getElementsByType(ElementType.CLASS);
        List<ClassElement> actualClassElements = actualDiagram.getElementsByType(ElementType.CLASS);

        for (ClassElement expectedClassElement : expectedClassElements) {
            compareExpectedClassWithActualClasses(expectedClassElement, actualClassElements, diagramConfig);
        }

        if (!diagramConfig.isPartialTest() && actualClassElements.size() > expectedClassElements.size()) {
            diagramExceptionHelper.throwException("Too many classes");
        }
    }

    private void compareExpectedClassWithActualClasses(ClassElement expectedClassElement, List<ClassElement> actualClassElements, DiagramConfig diagramConfig) {
        boolean found = false;
        for (ClassElement actualClassElement : actualClassElements) {
            if (expectedClassElement.equals(actualClassElement)) {
                if (found) {
                    diagramExceptionHelper.throwException("There are multiple classes with name: " + expectedClassElement.getId());
                }

                compareExpectedClassWithActualClass(expectedClassElement, actualClassElement, diagramConfig);
                found = true;
            }
        }

        if (!found) {
            diagramExceptionHelper.throwException("A certain class is missing");
        }
    }

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
    private void assertRelations(Diagram actualDiagram, DiagramConfig diagramConfig) {
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

                if (!diagramConfig.isPartialTest()) {
                    compareExpectedRelationWithActualRelation(expectedRelationElement, actualRelationElement, diagramConfig);
                }
                found = true;
            }
        }

        if (!found) {
            diagramExceptionHelper.throwException("A certain relation is missing");
        }
    }

    private void compareExpectedRelationWithActualRelation(RelationElement expectedRelationElement, RelationElement actualRelationElement, DiagramConfig diagramConfig) {
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
