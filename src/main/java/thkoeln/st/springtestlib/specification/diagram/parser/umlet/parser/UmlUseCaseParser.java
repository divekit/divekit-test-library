package thkoeln.st.springtestlib.specification.diagram.parser.umlet.parser;

import thkoeln.st.springtestlib.specification.diagram.elements.Point;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.actorelement.ActorElement;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.classelement.ClassAttribute;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.classelement.ClassElement;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.usecaseelement.UseCaseElement;
import thkoeln.st.springtestlib.specification.diagram.parser.umlet.elements.UmletElement;

import java.util.ArrayList;
import java.util.List;

public class UmlUseCaseParser extends UmletParser {

    @Override
    public UseCaseElement parseElement(UmletElement sourceElement) {
        UseCaseElement useCaseElement = new UseCaseElement(
                new Point(sourceElement.getUmletCoordinates().getX(),
                        sourceElement.getUmletCoordinates().getY()),
                sourceElement.getUmletCoordinates().getWidth(),
                sourceElement.getUmletCoordinates().getHeight());

        String[] properties = getSplittedProperties(sourceElement);
        useCaseElement.setId(properties[0].trim());

        return useCaseElement;
    }
}
