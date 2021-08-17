package thkoeln.st.springtestlib.specification.diagram.parser.umlet.parser;

import thkoeln.st.springtestlib.specification.diagram.parser.ElementParser;
import thkoeln.st.springtestlib.specification.diagram.parser.umlet.elements.UmletElement;
import thkoeln.st.springtestlib.specification.diagram.parser.umlet.elements.UmletMetaData;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class UmletParser implements ElementParser<UmletElement, UmletMetaData> {

    protected List<String> getProperties(UmletElement sourceElement) {
        List<String> properties = Arrays.stream(sourceElement.getPanelAttributes().split("\n"))
                .filter((attribute) -> !attribute.isBlank())
                .map(String::trim)
                .collect(Collectors.toList());

        if (properties.isEmpty()) {
            properties.add("");
        }

        return properties;
    }
}
