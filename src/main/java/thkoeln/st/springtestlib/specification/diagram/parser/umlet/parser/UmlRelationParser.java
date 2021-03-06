package thkoeln.st.springtestlib.specification.diagram.parser.umlet.parser;

import thkoeln.st.springtestlib.specification.diagram.elements.Point;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.relationelement.RelationArrowType;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.relationelement.RelationElement;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.relationelement.RelationLineType;
import thkoeln.st.springtestlib.specification.diagram.parser.umlet.elements.UmletElement;
import thkoeln.st.springtestlib.specification.diagram.parser.umlet.elements.UmletMetaData;

import java.util.List;


public class UmlRelationParser extends UmletParser {

    @Override
    public RelationElement parseElement(UmletElement sourceElement, UmletMetaData umletMetaData) {
        String[] split = sourceElement.getAdditionalAttributes().split(";");
        Point start = new Point(Float.parseFloat(split[0]), Float.parseFloat(split[1]))
                .scale(umletMetaData.getScale());
        Point end = new Point(Float.parseFloat(split[split.length-2]), Float.parseFloat(split[split.length-1]))
                .scale(umletMetaData.getScale());

        Point origin = new Point(sourceElement.getUmletCoordinates().getX(), sourceElement.getUmletCoordinates().getY());
        start.add(origin);
        end.add(origin);

        RelationElement relationElement = new RelationElement(start, end);
        setRelationType(relationElement, sourceElement);

        return relationElement;
    }

    private void setRelationType(RelationElement relationElement, UmletElement sourceElement) {
        List<String> properties = getProperties(sourceElement);
        setRelationLineType(relationElement, properties.get(0));
        setRelationArrow1(relationElement, properties);
        setRelationArrow2(relationElement, properties);
        setRelationDescription(relationElement, properties.get(properties.size() - 1));
    }

    private void setRelationLineType(RelationElement relationElement, String content) {
        if (content.contains("-") || content.isBlank()) {
            relationElement.setRelationLineType(RelationLineType.CONTINUOUS);
        } else {
            int numberOfDots = countOccurrences(content, ".");
            if (numberOfDots == 1) {
                relationElement.setRelationLineType(RelationLineType.DASHED);
            } else if (numberOfDots == 2) {
                relationElement.setRelationLineType(RelationLineType.DOTTED);
            }
        }
    }

    private void setRelationArrow1(RelationElement relationElement, List<String> properties) {
        relationElement.getRelationPointer1().setRelationArrowType(
            getRelationArrowType(properties.get(0), "<"));

        relationElement.getRelationPointer1().setCardinality(
            getCardinality(properties, "m1"));
    }

    private void setRelationArrow2(RelationElement relationElement, List<String> properties) {
        relationElement.getRelationPointer2().setRelationArrowType(
            getRelationArrowType(properties.get(0), ">"));

        relationElement.getRelationPointer2().setCardinality(
            getCardinality(properties, "m2"));
    }

    private RelationArrowType getRelationArrowType(String content, String arrowDescriptor) {
        int numberOfArrows = countOccurrences(content, arrowDescriptor);

        switch (numberOfArrows) {
            case 5:
                return RelationArrowType.DIAMOND_FULL;
            case 4:
                return RelationArrowType.DIAMONG_HOLLOW;
            case 3:
                return RelationArrowType.THREE_LINES_FULL;
            case 2:
                return RelationArrowType.THREE_LINES_HOLLOW;
            case 1:
                return RelationArrowType.TWO_LINES;
            default:
                return RelationArrowType.NONE;
        }
    }

    private String getCardinality(List<String> properties, String cardinalityPrefix) {
        for (String property : properties) {
            if (property.startsWith(cardinalityPrefix)) {
                String[] split = property.split("=");
                if (split.length >= 2) {
                    String cardinality = split[1].trim();
                    cardinality = cardinality.replace('m', 'n');
                    cardinality = cardinality.replace('*', 'n');
                    cardinality = cardinality.replace("0..n", "n");
                    return cardinality;
                }
            }
        }
        return "";
    }

    private void setRelationDescription(RelationElement relationElement, String content) {
        if (!content.contains("lt=") // TODO parse all description lines not only the last one
            && !content.contains("m1=")
            && !content.contains("m2=")) {
            relationElement.setDescription(content);
        }
    }

    private int countOccurrences(String content, String find) {
        int lastIndex = 0;
        int count = 0;

        while(lastIndex != -1){

            lastIndex = content.indexOf(find, lastIndex);

            if(lastIndex != -1){
                count ++;
                lastIndex += find.length();
            }
        }
        return count;
    }
}
