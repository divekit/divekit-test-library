package thkoeln.st.springtestlib.specification.diagram.parser.umlet.parser;

import thkoeln.st.springtestlib.specification.diagram.elements.Point;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.stateelement.StateElement;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.usecaseelement.UseCaseElement;
import thkoeln.st.springtestlib.specification.diagram.parser.umlet.elements.UmletElement;
import thkoeln.st.springtestlib.specification.diagram.parser.umlet.elements.UmletMetaData;

import java.util.List;

public class UmlStateParser extends UmletParser {

    @Override
    public StateElement parseElement(UmletElement sourceElement, UmletMetaData umletMetaData) {
        StateElement stateElement = new StateElement(
                new Point(sourceElement.getUmletCoordinates().getX(),
                        sourceElement.getUmletCoordinates().getY()),
                sourceElement.getUmletCoordinates().getWidth(),
                sourceElement.getUmletCoordinates().getHeight());

        List<String> properties = getProperties(sourceElement);
        stateElement.setId(properties.get(0));

        return stateElement;
    }
}
