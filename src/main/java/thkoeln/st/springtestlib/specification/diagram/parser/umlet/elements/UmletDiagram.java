package thkoeln.st.springtestlib.specification.diagram.parser.umlet.elements;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

@XStreamAlias("diagram")
public class UmletDiagram {

    @XStreamAlias("zoom_level")
    private int zoomLevel;

    @XStreamImplicit(itemFieldName = "element")
    private List<UmletElement> umletElements;


    public List<UmletElement> getUmletElements() {
        return umletElements;
    }

    public int getZoomLevel() {
        return zoomLevel;
    }
}
