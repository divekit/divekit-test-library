package thkoeln.st.springtestlib.specification.diagram.parser.umlet.parser;

import thkoeln.st.springtestlib.specification.diagram.parser.ElementParser;
import thkoeln.st.springtestlib.specification.diagram.parser.umlet.elements.UmletElement;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class UmletParser implements ElementParser<UmletElement> {

    protected List<String> getProperties(UmletElement sourceElement) {
        return Arrays.stream(sourceElement.getPanelAttributes().split("\n"))
                .filter((attribute) -> !attribute.isBlank())
                .map(String::trim)
                .collect(Collectors.toList());
    }
}
