package thkoeln.st.springtestlib.specification.diagram.parser.umlet.parser;

import thkoeln.st.springtestlib.specification.diagram.elements.Point;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.actorelement.ActorElement;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.classelement.ClassAttribute;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.classelement.ClassElement;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.usecaseelement.UseCaseElement;
import thkoeln.st.springtestlib.specification.diagram.parser.umlet.elements.UmletElement;
import thkoeln.st.springtestlib.specification.diagram.parser.umlet.elements.UmletMetaData;

import java.util.ArrayList;
import java.util.List;

public class UmlUseCaseParser extends UmletParser {

    @Override
    public UseCaseElement parseElement(UmletElement sourceElement, UmletMetaData umletMetaData) {
        UseCaseElement useCaseElement = new UseCaseElement(
                new Point(sourceElement.getUmletCoordinates().getX(),
                        sourceElement.getUmletCoordinates().getY()),
                sourceElement.getUmletCoordinates().getWidth(),
                sourceElement.getUmletCoordinates().getHeight());

        List<String> properties = getProperties(sourceElement);
        useCaseElement.setId(properties.get(0));

        return useCaseElement;
    }
}
