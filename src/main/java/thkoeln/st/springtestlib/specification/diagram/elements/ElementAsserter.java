package thkoeln.st.springtestlib.specification.diagram.elements;

import thkoeln.st.springtestlib.specification.diagram.DiagramExceptionHelper;


public abstract class ElementAsserter {

    protected DiagramExceptionHelper diagramExceptionHelper;


    public ElementAsserter(DiagramExceptionHelper diagramExceptionHelper) {
        this.diagramExceptionHelper = diagramExceptionHelper;
    }
}
