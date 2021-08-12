package thkoeln.st.springtestlib.specification.diagram.parser.umlet.elements;

import thkoeln.st.springtestlib.specification.diagram.elements.ElementType;
import thkoeln.st.springtestlib.specification.diagram.parser.ElementParser;
import thkoeln.st.springtestlib.specification.diagram.parser.umlet.parser.UmlActorParser;
import thkoeln.st.springtestlib.specification.diagram.parser.umlet.parser.UmlClassParser;
import thkoeln.st.springtestlib.specification.diagram.parser.umlet.parser.UmlRelationParser;
import thkoeln.st.springtestlib.specification.diagram.parser.umlet.parser.UmlUseCaseParser;

public enum UmletElementTypes {
    UML_CLASS("UMLClass", ElementType.CLASS, new UmlClassParser()),
    UML_RELATION("Relation", ElementType.RELATION, new UmlRelationParser()),
    UML_USE_CASE("UMLUseCase", ElementType.USE_CASE, new UmlUseCaseParser()),
    UML_ACTOR("UMLActor", ElementType.ACTOR, new UmlActorParser());

    private String elementName;
    private ElementType elementType;
    private ElementParser<UmletElement> elementParser;


    UmletElementTypes(String elementName, ElementType elementType, ElementParser<UmletElement> elementParser) {
        this.elementName = elementName;
        this.elementType = elementType;
        this.elementParser = elementParser;
    }

    public String getElementName() {
        return elementName;
    }

    public ElementType getElementType() {
        return elementType;
    }

    public ElementParser<UmletElement> getElementParser() {
        return elementParser;
    }
}
