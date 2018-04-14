package Model.InfluenceEffect;

public class LinearInfluenceEffect {


    public LinearInfluenceEffect(Command command, int range, long speed, Orientation orientation) {
        super(command, range, speed, orientation);
    }

    //Defines logic for moving in a straight line in its orientation
    public ArrayList<Point3D> nextMove(Point3D point) {
        ArrayList<Point3D> newPos = new ArrayList<>();
        newPos.add(orientation.getAdjacentPoint(point, getOrientation));

        decrementDistanceTravelled();
        return newPos;
    }
}
