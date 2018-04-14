package Model.InfluenceEffect;

import Model.Command.Command;
import Model.Entity.EntityAttributes.Orientation;
import javafx.geometry.Point3D;

import java.util.ArrayList;

public class RadialInfluenceEffect extends InfluenceEffect{
    public RadialInfluenceEffect(Command command, int range, long speed, Orientation orientation) {
        super(command, range, speed, orientation);
    }

    //Defines logic for moving in every direction
    //TODO: restrict movement based on movement speed
    public ArrayList<Point3D> nextMove(Point3D point) {
        //Out of moves, return empty list
        if(getMovesRemaining() <= 0) { return new ArrayList<>(); }

        ArrayList<Point3D> newPos = new ArrayList<>();
        for (Orientation orientation : Orientation.values()) {
            newPos.add(orientation.getAdjacentPoint(point, orientation));
        }
        decrementMovesRemaining();

        return newPos;
    }
}
