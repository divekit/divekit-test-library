package thkoeln.st.springtestlib.specification.diagram.elements.implementations.stateelement;

import thkoeln.st.springtestlib.specification.diagram.elements.ElementType;
import thkoeln.st.springtestlib.specification.diagram.elements.Point;
import thkoeln.st.springtestlib.specification.diagram.elements.RectangularElement;

public class StateElement extends RectangularElement {

    public StateElement() {
        super(ElementType.STATE);
    }

    public StateElement(Point topLeft, float width, float height) {
        super(ElementType.STATE, topLeft, width, height);
    }
}
