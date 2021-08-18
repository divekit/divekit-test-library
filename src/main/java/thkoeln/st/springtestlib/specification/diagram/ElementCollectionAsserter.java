package thkoeln.st.springtestlib.specification.diagram;

import thkoeln.st.springtestlib.specification.diagram.elements.Element;
import thkoeln.st.springtestlib.specification.diagram.elements.ElementType;

import java.util.List;

// TODO implement more abstract variant and extend this one from it
public class ElementCollectionAsserter<T extends Element> {

    public interface SingleElementComparable<T> {
        boolean compareElement(T expectedElement, T actualElement, DiagramConfig diagramConfig);
    }

    public interface SingleElementAssertable<T> {
        void assertElement(T expectedElement, T actualElement, DiagramConfig diagramConfig);
    }

    public interface DuplicateElementMessageBuildable<T> {
        String buildMessage(T duplicateElement, DiagramConfig diagramConfig);
    }


    private final ElementType elementType;

    private final String tooManyElementsMessage;
    private final String elementMissingMessage;

    private final SingleElementComparable<T> singleElementComparable;
    private final SingleElementAssertable<T> singleElementAssertable;
    private final DuplicateElementMessageBuildable<T> duplicateElementMessageBuildable;

    private final DiagramExceptionHelper diagramExceptionHelper;


    public ElementCollectionAsserter(ElementType elementType,
                                     String tooManyElementsMessage,
                                     String elementMissingMessage,
                                     SingleElementComparable<T> singleElementComparable,
                                     SingleElementAssertable<T> singleElementAssertable,
                                     DuplicateElementMessageBuildable<T> duplicateElementMessageBuildable,
                                     DiagramExceptionHelper diagramExceptionHelper) {
        this.elementType = elementType;
        this.tooManyElementsMessage = tooManyElementsMessage;
        this.elementMissingMessage = elementMissingMessage;
        this.singleElementComparable = singleElementComparable;
        this.singleElementAssertable = singleElementAssertable;
        this.duplicateElementMessageBuildable = duplicateElementMessageBuildable;
        this.diagramExceptionHelper = diagramExceptionHelper;
    }

    public void assertElements(Diagram expectedDiagram, Diagram actualDiagram, DiagramConfig diagramConfig) {
        List<T> expectedElements = expectedDiagram.getElementsByType(elementType);
        List<T> actualElements = actualDiagram.getElementsByType(elementType);

        for (T expectedElement : expectedElements) {
            searchExpectedElementInActualElementsAndAssert(expectedElement, actualElements, diagramConfig);
        }

        if (!diagramConfig.isPartialTest() && actualElements.size() > expectedElements.size()) {
            diagramExceptionHelper.throwException(tooManyElementsMessage);
        }
    }

    private void searchExpectedElementInActualElementsAndAssert(T expectedElement, List<T> actualElements, DiagramConfig diagramConfig) {
        boolean found = false;
        for (T actualElement : actualElements) {
            if (singleElementComparable.compareElement(expectedElement, actualElement, diagramConfig)) {
                if (found) {
                    String duplicateMessage = duplicateElementMessageBuildable.buildMessage(expectedElement, diagramConfig);
                    if (duplicateMessage != null) {
                        diagramExceptionHelper.throwException(duplicateMessage);
                    }
                }

                singleElementAssertable.assertElement(expectedElement, actualElement, diagramConfig);
                found = true;
            }
        }

        if (!found) {
            diagramExceptionHelper.throwException(elementMissingMessage);
        }
    }
}
