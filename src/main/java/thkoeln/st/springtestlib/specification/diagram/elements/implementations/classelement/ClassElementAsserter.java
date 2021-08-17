package thkoeln.st.springtestlib.specification.diagram.elements.implementations.classelement;

import thkoeln.st.springtestlib.specification.diagram.DiagramConfig;
import thkoeln.st.springtestlib.specification.diagram.DiagramExceptionHelper;
import thkoeln.st.springtestlib.specification.diagram.elements.ElementAsserter;

import java.util.List;

public class ClassElementAsserter extends ElementAsserter {

    public ClassElementAsserter(DiagramExceptionHelper diagramExceptionHelper) {
        super(diagramExceptionHelper);
    }


    public void assertClassAttributes(ClassElement expectedClassElement, ClassElement actualClassElement, DiagramConfig diagramConfig) {
        List<ClassAttribute> expectedAttributes = expectedClassElement.getAttributes();
        List<ClassAttribute> actualAttributes = actualClassElement.getAttributes();

        for (ClassAttribute expectedAttribute : expectedAttributes) {
            assertClassAttribute(expectedClassElement, expectedAttribute, actualAttributes, diagramConfig);
        }

        if (!diagramConfig.isPartialTest() && actualAttributes.size() > expectedAttributes.size()) {
            diagramExceptionHelper.throwException("Too many attributes in class: " + expectedClassElement.getId());
        }
    }

    private void assertClassAttribute(ClassElement expectedClassElement, ClassAttribute expectedAttribute, List<ClassAttribute> actualAttributes, DiagramConfig diagramConfig) {
        for (ClassAttribute actualAttribute : actualAttributes) {
            if (expectedAttribute.equals(actualAttribute)) {
                return;
            }
        }
        diagramExceptionHelper.throwException("A certain attribute is missing in class: " + expectedClassElement.getId());
    }
}
