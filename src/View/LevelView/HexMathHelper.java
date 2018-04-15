package View.LevelView;

import javafx.geometry.Point3D;

public class HexMathHelper {
    public HexMathHelper() {

    }

    public int getXCoord(Point3D point) {
        return -(int)(point.getY() + point.getZ());

    }

    public int getYCoord(Point3D point) {
        return -(int) (2*point.getY() + point.getX());
    }
}
