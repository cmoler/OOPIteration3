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

    public int getDistance(Point3D p1, Point3D p2) {
        return (int)((Math.abs(p1.getX()-p2.getX()) + Math.abs(p1.getY()-p2.getY()) + Math.abs(p1.getZ()-p2.getZ()))/2);
    }
}
