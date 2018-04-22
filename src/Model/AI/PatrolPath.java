package Model.AI;

import Controller.Visitor.SavingVisitor;
import com.sun.javafx.geom.Vec3d;

import java.util.ArrayList;

public class PatrolPath {
    private ArrayList<Vec3d> patrolPath;
    private int index;

    public PatrolPath(ArrayList<Vec3d> patrolPath) {
        this.patrolPath = patrolPath;
        this.index = 0;
    }

    public PatrolPath() {
        ArrayList<Vec3d> path = new ArrayList<>();
        path.add(new Vec3d(1,0,-1));
        path.add(new Vec3d(1,0,-1));
        path.add(new Vec3d(-1,1,0));
        path.add(new Vec3d(-1,1,0));
        path.add(new Vec3d(0,-1,1));
        path.add(new Vec3d(0,-1,1));
        path.add(new Vec3d(-1,0,1));
        path.add(new Vec3d(-1,0,1));
        path.add(new Vec3d(1,-1,0));
        path.add(new Vec3d(1,-1,0));
        this.patrolPath = path;
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


    public void accept(SavingVisitor visitor) {
        visitor.visitPatrolPath(this);
    }

    public ArrayList<Vec3d> getVectors() {
        return patrolPath;
    }
}
