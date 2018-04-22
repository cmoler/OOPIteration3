package Model.InfluenceEffect;

import Controller.Visitor.Visitor;
import Model.Command.EntityCommand.SettableCommand.SettableCommand;
import Model.Entity.EntityAttributes.Orientation;
import javafx.geometry.Point3D;

import java.util.ArrayList;

public class AngularInfluenceEffect extends InfluenceEffect {

    public AngularInfluenceEffect(SettableCommand command, int range, long speed, Orientation orientation) {
        super(command, range, speed, orientation);
    }

    public AngularInfluenceEffect(SettableCommand command, int range, long speed, Orientation orientation, int movesRemaining) {
        super(command, range, speed, orientation, movesRemaining);
    }

    public ArrayList<Point3D> nextMove(Point3D point) {
        if(noMovesRemaining()) { return new ArrayList<>(); }

        ArrayList<Point3D> newPoints = new ArrayList<>();

        if(rangeIsZero() || !canMove()) {
            newPoints.add(point);
            return newPoints;
        }

        int distance = getRange()-getMovesRemaining()+1;
        Point3D currentPoint = point;
        for(int i = 0; i < distance; i++) {//Find starting point based on distance
            currentPoint = Orientation.getAdjacentPoint(currentPoint, getOrientation());
        }
        newPoints.add(currentPoint);

        //Get orientation and points of triangular angles
        int orientationIndex = getOrientation().getIndexOfOrientation(getOrientation());
        Orientation adj1Orientation = Orientation.values()[(orientationIndex+2)%6];
        Orientation adj2Orientation = Orientation.values()[(orientationIndex+4)%6];
        Point3D adj1 = Orientation.getAdjacentPoint(currentPoint, adj1Orientation);
        Point3D adj2 = Orientation.getAdjacentPoint(currentPoint, adj2Orientation);
        for(int i = 0; i < distance/2; i++) {
            newPoints.add(adj1);
            newPoints.add(adj2);
            adj1 = Orientation.getAdjacentPoint(adj1, adj1Orientation);
            adj2 = Orientation.getAdjacentPoint(adj2, adj2Orientation);
        }

        decrementMovesRemaining();

        return newPoints;
    }

    @Override
    public InfluenceEffect cloneInfluenceEffect() {
        return new AngularInfluenceEffect(getCommand(), getRange(), getSpeed(), getOrientation(), getMovesRemaining());
    }

    public void accept(Visitor visitor) {
        visitor.visitInfluenceEffect(this);
    }
}
