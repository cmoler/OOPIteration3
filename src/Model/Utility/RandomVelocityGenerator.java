package Model.Utility;

import com.sun.javafx.geom.Vec3d;

import java.util.ArrayList;
import java.util.Random;

public class RandomVelocityGenerator {
    public static Vec3d generateRandomVelocity() {
        Random rand = new Random();
        ArrayList<Vec3d> moves = new ArrayList<>();
        moves.add(new Vec3d(0, 1, -1));
        moves.add(new Vec3d(0, -1, 1));
        moves.add(new Vec3d(1, 0, -1));
        moves.add(new Vec3d(-1, 0, 1));
        moves.add(new Vec3d(-1, 1, 0));
        moves.add(new Vec3d(1, -1, 0));
        return moves.get(rand.nextInt(moves.size()));
    }
}
