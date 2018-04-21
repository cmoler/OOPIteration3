package Model.Utility;

import com.sun.javafx.geom.Vec3d;
import javafx.geometry.Point3D;

public class VectorToPointCalculator {
    public static Point3D calculateNewPoint(Point3D origin, Vec3d velocity){
        return new Point3D(origin.getX()+velocity.x,origin.getY()+velocity.y,origin.getZ()+velocity.z);
    }

    public static Vec3d calculateNewVelocity(Point3D position, Point3D goal) {
        return new Vec3d(goal.getX()-position.getX(), goal.getY()-position.getY(),goal.getZ()-position.getZ());
    }
}
