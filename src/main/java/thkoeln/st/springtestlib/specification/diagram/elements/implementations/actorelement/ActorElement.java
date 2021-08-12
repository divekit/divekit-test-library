package thkoeln.st.springtestlib.specification.diagram.elements.implementations.actorelement;

import thkoeln.st.springtestlib.specification.diagram.elements.ElementType;
import thkoeln.st.springtestlib.specification.diagram.elements.Point;
import thkoeln.st.springtestlib.specification.diagram.elements.RectangularElement;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.classelement.ClassAttribute;

import java.util.ArrayList;
import java.util.List;

public class ActorElement extends RectangularElement {

    public ActorElement() {
        super(ElementType.ACTOR);
    }

    public ActorElement(Point topLeft, float width, float height) {
        super(ElementType.ACTOR, topLeft, width, height);
    }
}
