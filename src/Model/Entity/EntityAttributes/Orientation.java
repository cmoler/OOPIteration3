package Model.Entity.EntityAttributes;

public enum Orientation {
    NORTH, NORTHWEST, NORTHEAST, SOUTHWEST, SOUTHEAST, SOUTH, NONE

    public Point3D getAdjacentPoint(Point3D startingPos, Orientation orientation) {
        return new Point3D(startingPos.x + dx(orientation), startingPos.y + dy(orientation), startingPos.z + dz(orientation));
    }

    public int dx(Orientation orientation) {
        if(orientation == NORTHEAST || orientation == SOUTHEAST) { return 1; }
        if(orientation == SOUTHWEST || orientation == NORTHWEST) { return -1; }
        return 0;
    }

    public int dy(Orientation orientation) {
        if(orientation == NORTHWEST || orientation == NORTH) { return 1; }
        if(orientation == SOUTHEAST || orientation == SOUTH) { return -1; }
        return 0;
    }

    public int dz(Orientation orientation) {
        if(orientation == SOUTH || orientation == SOUTHWEST) { return 1; }
        if(orientation == NORTH || orientation == NORTHEAST) { return -1; }
        return 0;
    }
}
