package Model.InfluenceEffect;

import Model.Command.Command;
import Model.Entity.EntityAttributes.Orientation;
import javafx.geometry.Point3D;

import java.util.ArrayList;

public class LinearInfluenceEffect extends InfluenceEffect{


    public LinearInfluenceEffect(Command command, int range, long speed, Orientation orientation) {
        super(command, range, speed, orientation);
    }

    //Defines logic for moving in a straight line in its orientation
    //TODO: restrict movement based on movement speed
    public ArrayList<Point3D> nextMove(Point3D point) {
        //Out of moves, return empty list
        if(getMovesRemaining() <= 0) { return new ArrayList<>(); }

        ArrayList<Point3D> newPos = new ArrayList<>();
        newPos.add(getOrientation().getAdjacentPoint(point, getOrientation()));

        decrementMovesRemaining();
        return newPos;
    }
}
