package custom_shapes;


import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public interface IShape{
    /**
     * <p>When shapes are moved, they need to perform some calculations to adjust properly.</p>
     * <p>This method is used by EventListener of shapes.</p>
     * @param ox Origin point x
     * @param oy Origin point y
     */
    void move(double ox, double oy);

    /**
     * <p>When shapes are adjusted, they need to perform various calculations to construct properly</p>
     * <p>This method is used while user is creating the shape (-> preview)</p>
     * @param mx Mouse coordinate x
     * @param my Mouse coordinate y
     */
    void adjust(double mx, double my);

    /**
     * <p>Returns valid svg format of shape</p>
     */
    // Svg reference
    // https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute
    // https://developer.mozilla.org/en-US/docs/Web/SVG/Element
    String toSvg();
    String toJvgf();
    // Needed for snapping -> Every shape has different center coords
    double getCenterX();
    double getCenterY();
    // Needed for moving -> Every shape has different start coords
    double getStartX();
    double getStartY();
    void setStartX(double x);
    void setStartY(double y);
    // Needed for adjusting -> Every shape has different size parameters
    double getAdjustX();
    double getAdjustY();
    // These are needed by InfoPane
    int getLayer();
    void setLayer(int i);
    void setOpacity(double o);
    double getOpacity();
    void setStrokeWidth(double w);
    double getStrokeWidth();
    void setStroke(Paint p);
    void setFill(Paint p);
    Paint getStroke();
    Paint getFill();
}
