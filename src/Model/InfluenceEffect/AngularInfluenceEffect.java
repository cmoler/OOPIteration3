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

    public AngularInfluenceEffect(SettableCommand command, int range, long speed, Orientation orientation, int movesRemaining, long nextMoveTime, Point3D originPoint) {
        super(command, range, speed, orientation, movesRemaining, nextMoveTime, originPoint);
    }

    // Defines logic for a "shotgun-blast"-like cone of fire for an influence effect
    public ArrayList<Point3D> nextMove(Point3D point) {
        if(canMove()) {
            setNextMoveTime();

            ArrayList<Point3D> newPoints = new ArrayList<>();

            if (rangeIsZero()) {
                newPoints.add(point);
                return newPoints;
            }

            int projectileIteration = getRange() - getMovesRemaining();

            Point3D newOriginPoint = Orientation.getAdjacentPoint(point, getOrientation());

            setOriginPoint(newOriginPoint);
            newPoints.add(newOriginPoint);

            int leftFlankIndex = (int) Math.ceil((double) projectileIteration / (double) 2);
            int rightFlankIndex = (int) Math.ceil((double) projectileIteration / (double) 2);

            Point3D flankPoint = newOriginPoint;
            while(leftFlankIndex > 0) {
                int orientationIndex = getOrientation().getIndexOfOrientation(getOrientation());
                Orientation leftOrientation = Orientation.values()[(orientationIndex + 4) % 6];

                flankPoint = Orientation.getAdjacentPoint(flankPoint, leftOrientation);

                newPoints.add(flankPoint);

                leftFlankIndex--;
            }

            flankPoint = newOriginPoint;
            while(rightFlankIndex > 0) {
                int orientationIndex = getOrientation().getIndexOfOrientation(getOrientation());
                Orientation rightOrientation = Orientation.values()[(orientationIndex + 2) % 6];

                flankPoint = Orientation.getAdjacentPoint(flankPoint, rightOrientation);

                newPoints.add(flankPoint);

                rightFlankIndex--;
            }

            decrementMovesRemaining();

            return newPoints;
        }
        return new ArrayList<>();
    }

    public InfluenceEffect cloneInfluenceEffect() {
        return new AngularInfluenceEffect(getCommand(), getRange(), getSpeed(), getOrientation());
    }

    public void accept(Visitor visitor) {
        visitor.visitInfluenceEffect(this);
    }
}