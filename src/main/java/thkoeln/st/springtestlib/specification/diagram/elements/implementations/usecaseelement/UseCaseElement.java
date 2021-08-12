package thkoeln.st.springtestlib.specification.diagram.elements.implementations.usecaseelement;

import thkoeln.st.springtestlib.specification.diagram.elements.ElementType;
import thkoeln.st.springtestlib.specification.diagram.elements.Point;
import thkoeln.st.springtestlib.specification.diagram.elements.RectangularElement;

public class UseCaseElement extends RectangularElement {

    public UseCaseElement() {
        super(ElementType.USE_CASE);
    }

    public UseCaseElement(Point topLeft, float width, float height) {
        super(ElementType.USE_CASE, topLeft, width, height);
    }
}
