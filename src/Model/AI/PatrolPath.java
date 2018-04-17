package Model.AI;

import com.sun.javafx.geom.Vec3d;

import java.util.ArrayList;

public class PatrolPath {
    private ArrayList<Vec3d> patrolPath;
    private int index;

    public PatrolPath(ArrayList<Vec3d> patrolPath) {
        this.patrolPath = patrolPath;
        this.index = 0;
    }

    public Vec3d getNextMove(){
        Vec3d next = patrolPath.get(index);
        if (index+1 >= patrolPath.size()){
            index = 0;
        }
        else {
            ++index;
        }
        return next;
    }


}
