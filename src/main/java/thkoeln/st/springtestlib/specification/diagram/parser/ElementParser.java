package thkoeln.st.springtestlib.specification.diagram.parser;

import thkoeln.st.springtestlib.specification.diagram.elements.Element;

public interface ElementParser<Source extends SourceElement, MetaData> {
    Element parseElement(Source sourceElement, MetaData metaData);
}
