package thkoeln.st.springtestlib.specification.diagram.elements.implementations.nodeelement;

import thkoeln.st.springtestlib.specification.diagram.elements.Element;
import thkoeln.st.springtestlib.specification.diagram.elements.ElementType;
import thkoeln.st.springtestlib.specification.diagram.elements.Point;
import thkoeln.st.springtestlib.specification.diagram.elements.RectangularElement;

public class NodeElement extends RectangularElement {

    private NodeElementType type;

    public NodeElement() {
        super(ElementType.NODE);
    }

    public NodeElement(Point topLeft, float width, float height) {
        super(ElementType.NODE, topLeft, width, height);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof NodeElement)) {
            return false;
        }

        return type.equals(((NodeElement)obj).getType());
    }

    public void setType(NodeElementType type) {
        this.type = type;
        setId(type.toString());
    }

    public NodeElementType getType() {
        return type;
    }
}
