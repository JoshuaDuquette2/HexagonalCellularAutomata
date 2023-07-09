import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.HashMap;

public class Hex extends Polygon {

    private HashMap<Integer, Color> stateToColor = new HashMap<Integer, Color>(){{put(0, Color.ANTIQUEWHITE); put(1, Color.YELLOW); put(2, Color.FORESTGREEN);}};

    private final double r = 20;
    private final double n = Math.sqrt(r * r * 0.75);
    private final double TILE_HEIGHT = 2 * r;
    private final double TILE_WIDTH  = 2 * n;
    private final int HEX_X_OFFSET = -20;
    private final int HEX_Y_OFFSET = -20;

    public int state = 0;

    public Hex(double x, double y){
        x = x * TILE_WIDTH + (y % 2) * n + HEX_X_OFFSET;
        y = y * TILE_HEIGHT * 0.75 + HEX_Y_OFFSET;

        this.getPoints().addAll(
                x, y,
                x, y+r,
                x+n, y+r*1.5,
                x+TILE_WIDTH, y+r,
                x+TILE_WIDTH, y,
                x+n, y-r*0.5
        );
        setFill(Color.ANTIQUEWHITE);
        setStrokeWidth(1);
        setStroke(Color.BLACK);
    }

    public void setState(int state){
        this.state = state;
        setFill(stateToColor.get(this.state));
    }
}
