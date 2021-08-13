package thkoeln.st.springtestlib.specification.diagram.parser.umlet.parser;

import com.thoughtworks.xstream.XStream;
import thkoeln.st.springtestlib.specification.diagram.Diagram;
import thkoeln.st.springtestlib.specification.diagram.implementations.DiagramType;
import thkoeln.st.springtestlib.specification.diagram.parser.DiagramParser;
import thkoeln.st.springtestlib.specification.diagram.parser.umlet.elements.UmletCoordinates;
import thkoeln.st.springtestlib.specification.diagram.parser.umlet.elements.UmletDiagram;
import thkoeln.st.springtestlib.specification.diagram.parser.umlet.elements.UmletElement;
import thkoeln.st.springtestlib.specification.diagram.parser.umlet.elements.UmletElementTypes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;


public class UmletDiagramParser extends DiagramParser {

    public Diagram parseDiagram(String path, DiagramType diagramType) throws IOException {
        UmletDiagram umletDiagram = parseUmletDiagram(path);
        Diagram diagram = createDiagram(diagramType);

        for (UmletElement umletElement : umletDiagram.getUmletElements()) {
            parseElement(diagram, umletElement);
        }

        diagram.initElements();
        return diagram;
    }

    private void parseElement(Diagram diagram, UmletElement umletElement) {
        for (UmletElementTypes umletElementType : UmletElementTypes.values()) {
            if (umletElementType.getElementName().equalsIgnoreCase(umletElement.getId())) {
                diagram.addElement(umletElementType.getElementType(), umletElementType.getElementParser().parseElement(umletElement));
                break;
            }
        }
    }

    private UmletDiagram parseUmletDiagram(String path) throws IOException {
        String diagramXml = loadDiagramString(path);

        XStream xStream = new XStream();
        xStream.processAnnotations(UmletDiagram.class);
        xStream.processAnnotations(UmletElement.class);
        xStream.processAnnotations(UmletCoordinates.class);
        xStream.ignoreUnknownElements();
        return (UmletDiagram)xStream.fromXML(diagramXml);
    }

    private String loadDiagramString(String path) throws IOException {
        URL fileUrl = getClass().getClassLoader().getResource(path);
        if (fileUrl == null) {
            throw new FileNotFoundException(path);
        }

        try {
            return Files.readString(Paths.get(fileUrl.toURI()));
        } catch (URISyntaxException e) {
            throw new IOException(e.getMessage());
        }
    }
}
