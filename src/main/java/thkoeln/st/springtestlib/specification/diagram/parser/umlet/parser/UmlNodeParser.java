package thkoeln.st.springtestlib.specification.diagram.parser.umlet.parser;

import thkoeln.st.springtestlib.specification.diagram.elements.Point;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.nodeelement.NodeElement;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.nodeelement.NodeElementType;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.relationelement.RelationArrowType;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.relationelement.RelationElement;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.relationelement.RelationLineType;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.stateelement.StateElement;
import thkoeln.st.springtestlib.specification.diagram.parser.umlet.elements.UmletElement;
import thkoeln.st.springtestlib.specification.diagram.parser.umlet.elements.UmletMetaData;

import java.util.List;


public class UmlNodeParser extends UmletParser {

    private static final String TYPE_PREFIX = "type";
    private static final String TYPE_INITIAL = "initial";
    private static final String TYPE_FINAL = "final";

    @Override
    public NodeElement parseElement(UmletElement sourceElement, UmletMetaData umletMetaData) {
        NodeElement nodeElement = new NodeElement(
                new Point(sourceElement.getUmletCoordinates().getX(),
                        sourceElement.getUmletCoordinates().getY()),
                sourceElement.getUmletCoordinates().getWidth(),
                sourceElement.getUmletCoordinates().getHeight());

        setNodeType(nodeElement, sourceElement);

        return nodeElement;
    }

    private void setNodeType(NodeElement nodeElement, UmletElement sourceElement) {
        List<String> properties = getProperties(sourceElement);
        switch (searchType(properties)) {
            case TYPE_FINAL:
                nodeElement.setType(NodeElementType.FINAL);
                break;
            case TYPE_INITIAL:
            default:
                nodeElement.setType(NodeElementType.INITIAL);
        }
    }

    private String searchType(List<String> properties) {
        for (String property : properties) {
            if (property.startsWith(TYPE_PREFIX)) {
                String[] split = property.split("=");
                if (split.length >= 2) {
                    return split[1].trim();
                }
            }
        }
        return ""; // TODO throw exception here?
    }
}
