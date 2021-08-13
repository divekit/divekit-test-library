package thkoeln.st.springtestlib.specification.diagram.elements.implementations.relationelement;

import thkoeln.st.springtestlib.specification.diagram.elements.*;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

public class RelationElement extends LineElement {

    private static final float CONNECTION_ACCURACY = 10;

    private RelationPointer relationPointer1 = new RelationPointer();
    private RelationPointer relationPointer2 = new RelationPointer();

    private RelationLineType relationLineType;


    public RelationElement() {
        super(ElementType.RELATION);
    }

    public RelationElement(Point start, Point end) {
        super(ElementType.RELATION, start, end);
    }

    @Override
    public void init(List<Element> elements) {
        relationPointer1.setConnectedElement(getElementAtPos(elements, start));
        relationPointer2.setConnectedElement(getElementAtPos(elements, end));

        if (relationPointer1.getConnectedElement() == null || relationPointer2.getConnectedElement() == null) {
            throw new InputMismatchException("Could not connect relation");
        }
    }

    private void switchDirection() {
        RelationPointer tmp = relationPointer1;
        relationPointer1 = relationPointer2;
        relationPointer2 = tmp;
    }

    private RectangularElement getElementAtPos(List<Element> elements, Point pos) {
        List<RectangularElement> possibleElements = new ArrayList<>();

        for (Element element : elements) {
            if (element instanceof RectangularElement) {
                RectangularElement rectangularElement = (RectangularElement)element;
                if (checkOuterBorder(rectangularElement, pos)) {
                    possibleElements.add(rectangularElement);
                }
            }
        }

        possibleElements.sort((o1, o2) -> (int)((o1.getWidth() * o1.getHeight())
                - (o2.getWidth() * o2.getHeight())));

        return possibleElements.isEmpty() ? null : possibleElements.get(0);
    }

    private boolean checkOuterBorder(RectangularElement checkElement, Point pos) {
        return pos.getX() >= checkElement.getTopLeft().getX() - CONNECTION_ACCURACY
                && pos.getX() <= checkElement.getBottomRight().getX() + CONNECTION_ACCURACY
                && pos.getY() >= checkElement.getTopLeft().getY() - CONNECTION_ACCURACY
                && pos.getY() <= checkElement.getBottomRight().getY() + CONNECTION_ACCURACY;
    }

    private boolean checkInnerBorder(RectangularElement checkElement, Point pos) {
        return pos.getX() >= checkElement.getTopLeft().getX() + CONNECTION_ACCURACY
                && pos.getX() <= checkElement.getBottomRight().getX() - CONNECTION_ACCURACY
                && pos.getY() >= checkElement.getTopLeft().getY() + CONNECTION_ACCURACY
                && pos.getY() <= checkElement.getBottomRight().getY() - CONNECTION_ACCURACY;
    }

    public void switchDirectionIfNecessary(RelationElement relationElement) {
        if (getReferencedElement1().equals(relationElement.getReferencedElement2())
            && getReferencedElement2().equals(relationElement.getReferencedElement1())) {
            switchDirection();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RelationElement)) {
            return false;
        }

        RelationElement that = (RelationElement) o;

        return  (getReferencedElement1().equals(that.getReferencedElement1())
                && getReferencedElement2().equals(that.getReferencedElement2()))
                || (getReferencedElement1().equals(that.getReferencedElement2())
                && getReferencedElement2().equals(that.getReferencedElement1()));
    }

    public RelationPointer getRelationPointer1() {
        return relationPointer1;
    }

    public RelationPointer getRelationPointer2() {
        return relationPointer2;
    }

    public RectangularElement getReferencedElement1() {
        return relationPointer1.getConnectedElement();
    }

    public RectangularElement getReferencedElement2() {
        return relationPointer2.getConnectedElement();
    }

    public RelationLineType getRelationLineType() {
        return relationLineType;
    }

    public void setRelationLineType(RelationLineType relationLineType) {
        this.relationLineType = relationLineType;
    }
}
