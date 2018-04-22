package Model.InfluenceEffect;

import Controller.Visitor.Visitor;
import Model.Command.EntityCommand.SettableCommand.SettableCommand;
import Model.Entity.EntityAttributes.Orientation;
import javafx.geometry.Point3D;

import java.util.ArrayList;

public class LinearInfluenceEffect extends InfluenceEffect {


    public LinearInfluenceEffect(SettableCommand command, int range, long speed, Orientation orientation) {
        super(command, range, speed, orientation);
    }

    public LinearInfluenceEffect(SettableCommand command, int range, long speed, Orientation orientation, int movesRemaining) {
        super(command, range, speed, orientation, movesRemaining);
    }

    //Defines logic for moving in a straight line in the direction of its orientation
    //TODO: restrict movement based on movement speed
    public ArrayList<Point3D> nextMove(Point3D point) {

        //Out of moves, return empty list
        if(noMovesRemaining()) {
            return new ArrayList<>();
        }

        ArrayList<Point3D> newPos = new ArrayList<>();

        if(rangeIsZero()) {
            newPos.add(point);
            return newPos;
        }

        Point3D newPoint = point;

        for(int i = 0; i < getRange()-getMovesRemaining()+1; i++) {
            newPoint = Orientation.getAdjacentPoint(newPoint, getOrientation());
        }
        newPos.add(newPoint);
        decrementMovesRemaining();
        return newPos;

        /*
        Point3D newPoint = Orientation.getAdjacentPoint(point, getOrientation());

        newPos.add(newPoint);

        decrementMovesRemaining();

        return newPos;
        */
    }

    public InfluenceEffect cloneInfluenceEffect() {
        return new LinearInfluenceEffect(getCommand(), getRange(), getSpeed(), getOrientation(), getMovesRemaining());
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitInfluenceEffect(this);
    }
}
