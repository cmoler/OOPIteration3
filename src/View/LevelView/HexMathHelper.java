package View.LevelView;

import javafx.geometry.Point3D;

public class HexMathHelper {
    public HexMathHelper() {

    }

    public int getXCoord(Point3D point) {
        return -(int)(point.getX() + point.getZ());

    }

    public int getYCoord(Point3D point) {
        return (int) (2*point.getX() + point.getY());
    }
}
