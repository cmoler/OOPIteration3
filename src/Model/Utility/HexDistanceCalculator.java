package Model.Utility;

import javafx.geometry.Point3D;

public class HexDistanceCalculator {
    public static int getHexDistance(Point3D p1, Point3D p2){
        return (int) ((Math.abs(p1.getX() - p2.getX()) + Math.abs(p1.getY() - p2.getY()) + Math.abs(p1.getZ() - p2.getZ())) / 2);
    }
}
