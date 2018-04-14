package Model.InfluenceEffect;

public class InfluenceEffect {
    private Command command;
    private int movesRemaining;
    private int distanceTravelled;
    private long nextMoveTime;
    private long speed;
    private Orientation orientation;
    private Vector3D vector;

    public InfluenceEffect(Command command, int range, long speed, Orientation orientation) {
        this.command = command;
        this.movesRemaining = range;
        this.distanceTravelled = 0;
        this.speed = speed;
        //TODO: make nextMoveTime based on speed
        this.orientation = orientation;



    }

    public void nextMove(Map<Point3D, InfluenceEffect> effectMap) {

    }

    public void hitEntity(Entity entity) {
        command.execute(entity);
    }


    public Orientation getOrientation() {
        return orientation;
    }

    public int getMovesRemaining() {
        return movesRemaining;
    }

    public int getDistanceTravelled() {
        return distanceTravelled;
    }

    public void decrementDistanceTravelled() {
        distanceTravelled--;
    }
}
