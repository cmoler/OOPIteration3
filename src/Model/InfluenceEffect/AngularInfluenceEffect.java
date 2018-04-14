package Model.InfluenceEffect;

import Model.Command.Command;
import Model.Entity.EntityAttributes.Orientation;
import javafx.geometry.Point3D;

import java.util.ArrayList;

public class AngularInfluenceEffect extends InfluenceEffect {

    public AngularInfluenceEffect(Command command, int range, long speed, Orientation orientation) {
        super(command, range, speed, orientation);
    }

    public ArrayList<Point3D> nextMove(Point3D point) {
        if(getMovesRemaining() <= 0) { return new ArrayList<>(); }

        ArrayList<Point3D> newPoints = new ArrayList<>();
        int distance = getRange()-getMovesRemaining()+1;
        Point3D currentPoint = point;
        for(int i = 0; i < distance; i++) {//Find starting point based on distance
            currentPoint = getOrientation().getAdjacentPoint(currentPoint, getOrientation());

        }
        newPoints.add(currentPoint);

        //Get orientation and points of triangular angles
        int orientationIndex = getOrientation().getIndexOfOrientation(getOrientation());
        Orientation adj1Orientation = Orientation.values()[(orientationIndex+2)%6];
        Orientation adj2Orientation = Orientation.values()[(orientationIndex+4)%6];
        Point3D adj1 = getOrientation().getAdjacentPoint(currentPoint, adj1Orientation);
        Point3D adj2 = getOrientation().getAdjacentPoint(currentPoint, adj2Orientation);
        for(int i = 0; i < distance/2; i++) {
            newPoints.add(adj1);
            newPoints.add(adj2);
            adj1 = getOrientation().getAdjacentPoint(adj1, adj1Orientation);
            adj2 = getOrientation().getAdjacentPoint(adj2, adj2Orientation);
        }

        decrementMovesRemaining();
        return newPoints;
    }
}
