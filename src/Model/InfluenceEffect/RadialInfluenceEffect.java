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

        ArrayList<Point3D> newPoints = new ArrayList<>();

        int ringDistance = getRange()-getMovesRemaining();
        Point3D currentPoint = point;
        for(int i = 0; i < ringDistance; i++) {//Find starting point
            currentPoint = getOrientation().getAdjacentPoint(currentPoint, Orientation.NORTH);
        }

        //Generates ring of radius ringDistance
        for(int i = 0; i < Orientation.values().length-1; i++) {
            for(int j = 0; j < ringDistance; j++) {
                newPoints.add(currentPoint);
                currentPoint = getOrientation().getAdjacentPoint(currentPoint, Orientation.values()[(i+2)%6]);
            }
        }


        decrementMovesRemaining();

        return newPoints;
    }
}
