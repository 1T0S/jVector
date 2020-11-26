package custom_shapes;


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

    int getLayer();
}
