package Model.Utility;

import com.sun.javafx.geom.Vec3d;
import javafx.geometry.Point3D;

public class VectorToPointCalculator {
    public static Point3D calculateNewPoint(Point3D origin, Vec3d velocity){
        return new Point3D(origin.getX()+velocity.x,origin.getY()+velocity.y,origin.getZ()+velocity.z);
    }
}
