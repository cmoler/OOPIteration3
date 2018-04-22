package Model.Entity.EntityAttributes;

import javafx.geometry.Point3D;

import java.util.ArrayList;
import java.util.List;

public enum Orientation {
    NORTH, NORTHEAST, SOUTHEAST, SOUTH, SOUTHWEST, NORTHWEST, NONE;

    public static Point3D getAdjacentPoint(Point3D startingPos, Orientation orientation) {
        return new Point3D(startingPos.getX() + dx(orientation), startingPos.getY() + dy(orientation), startingPos.getZ() + dz(orientation));
    }

    public static List<Point3D> getAllAdjacentPoints(Point3D startingPos) {
        List<Point3D> pointsList = new ArrayList<>();

        pointsList.add(getAdjacentPoint(startingPos, NORTH));
        pointsList.add(getAdjacentPoint(startingPos, NORTHEAST));
        pointsList.add(getAdjacentPoint(startingPos, NORTHWEST));
        pointsList.add(getAdjacentPoint(startingPos, SOUTH));
        pointsList.add(getAdjacentPoint(startingPos, SOUTHEAST));
        pointsList.add(getAdjacentPoint(startingPos, SOUTHWEST));

        return pointsList;
    }

    public static int dx(Orientation orientation) {
        if(orientation == NORTHEAST || orientation == SOUTHEAST) { return 1; }
        if(orientation == SOUTHWEST || orientation == NORTHWEST) { return -1; }
        return 0;
    }

    public static int dy(Orientation orientation) {
        if(orientation == NORTHWEST || orientation == NORTH) { return 1; }
        if(orientation == SOUTHEAST || orientation == SOUTH) { return -1; }
        return 0;
    }

    public static int dz(Orientation orientation) {
        if(orientation == SOUTH || orientation == SOUTHWEST) { return 1; }
        if(orientation == NORTH || orientation == NORTHEAST) { return -1; }
        return 0;
    }

    public int getIndexOfOrientation(Orientation orientation) {
        for(int i = 0; i < values().length; i++) {
            if(orientation == values()[i]) {
                return i;
            }
        }
        return -1;
    }


    public int getDegreeOfOrientation(Orientation orientation) {
        int index = getIndexOfOrientation(orientation);
        switch(index) {
            case 0:
                return 0;
            case 1:
                return 45;
            case 2:
                return 135;
            case 3:
                return 190;
            case 4:
                return 225;
            case 5:
                return 315;
        }
        return 0;
    }

    public static String toString(Orientation orientation){
        switch (orientation){
            case NORTH:
                return "North";
            case NORTHWEST:
                return "NorthWest";
            case NORTHEAST:
                return "NorthEast";
            case SOUTHWEST:
                return "SouthWest";
            case SOUTHEAST:
                return "SouthEast";
            case SOUTH:
                return "South";

            default:
                return "None";
        }
    }

    public static Orientation toOrientation(String string) {
        switch (string.toLowerCase()) {
            case "north":
                return NORTH;

            case "south":
                return SOUTH;

            case "northeast":
                return NORTHEAST;

            case "southeast":
                return SOUTHEAST;

            case "northwest":
                return NORTHWEST;

            case "southwest":
                return SOUTHWEST;

            default:
                return NONE;
        }

    }
}
