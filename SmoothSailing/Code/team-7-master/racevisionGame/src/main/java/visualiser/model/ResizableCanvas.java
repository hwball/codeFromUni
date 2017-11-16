package visualiser.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 * The abstract class for the resizable race canvases.
 */
public abstract class ResizableCanvas extends Canvas {

    /**
     * The {@link GraphicsContext} to draw to.
     */
    protected final GraphicsContext gc;

    /**
     * Ctor.
     */
    public ResizableCanvas(){
        this.gc = this.getGraphicsContext2D();

        // Redraw canvas when size changes.
        widthProperty().addListener(evt -> draw());
        heightProperty().addListener(evt -> draw());
    }


    /**
     * Draws desired contents onto the canvas.
     * Subclasses implement this to decide what to draw.
     */
    public abstract void draw();


    @Override
    public boolean isResizable() {
        return true;
    }


    @Override
    public double prefWidth(double width) {
        return getWidth();
    }

    @Override
    public double prefHeight(double height) {
        return getHeight();
    }

}
