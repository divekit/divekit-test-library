package thkoeln.st.springtestlib.specification.diagram.elements.implementations.classelement;

import thkoeln.st.springtestlib.specification.diagram.DiagramConfig;
import thkoeln.st.springtestlib.specification.diagram.DiagramExceptionHelper;

import java.util.List;

public class ClassElementAsserter {

    public static void assertClassAttributes(ClassElement expectedClassElement,
                                             ClassElement actualClassElement,
                                             DiagramConfig diagramConfig,
                                             DiagramExceptionHelper diagramExceptionHelper) {
        List<ClassAttribute> expectedAttributes = expectedClassElement.getAttributes();
        List<ClassAttribute> actualAttributes = actualClassElement.getAttributes();

        for (ClassAttribute expectedAttribute : expectedAttributes) {
            assertClassAttribute(expectedClassElement, expectedAttribute, actualAttributes, diagramConfig, diagramExceptionHelper);
        }

        if (!diagramConfig.isPartialTest() && actualAttributes.size() > expectedAttributes.size()) {
            diagramExceptionHelper.throwException("Too many attributes in class: " + expectedClassElement.getId());
        }
    }

    private static void assertClassAttribute(ClassElement expectedClassElement,
                                             ClassAttribute expectedAttribute,
                                             List<ClassAttribute> actualAttributes,
                                             DiagramConfig diagramConfig,
                                             DiagramExceptionHelper diagramExceptionHelper) {
        for (ClassAttribute actualAttribute : actualAttributes) {
            if (expectedAttribute.equals(actualAttribute)) {
                return;
            }
        }
        diagramExceptionHelper.throwException("A certain attribute is missing in class: " + expectedClassElement.getId());
    }
}
