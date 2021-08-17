package thkoeln.st.springtestlib.specification.diagram.parser.umlet.parser;

import thkoeln.st.springtestlib.specification.diagram.elements.Point;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.actorelement.ActorElement;
import thkoeln.st.springtestlib.specification.diagram.parser.umlet.elements.UmletElement;
import thkoeln.st.springtestlib.specification.diagram.parser.umlet.elements.UmletMetaData;

import java.util.List;

public class UmlActorParser extends UmletParser {

    @Override
    public ActorElement parseElement(UmletElement sourceElement, UmletMetaData umletMetaData) {
        ActorElement actorElement = new ActorElement(
                new Point(sourceElement.getUmletCoordinates().getX(),
                sourceElement.getUmletCoordinates().getY()),
                sourceElement.getUmletCoordinates().getWidth(),
                sourceElement.getUmletCoordinates().getHeight());

        List<String> properties = getProperties(sourceElement);
        actorElement.setId(properties.get(0));

        return actorElement;
    }
}
