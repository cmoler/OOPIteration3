package Model.AI;

import Model.Entity.Entity;
import Model.Level.Obstacle;
import Model.Level.Terrain;
import com.sun.javafx.geom.Vec3d;
import javafx.geometry.Point3D;

import java.util.*;

public class PathingAlgorithm {
    private Map<Point3D, Terrain> terrainMap;
    private Map<Point3D, Obstacle> obstacleMap;

    public PathingAlgorithm(Map<Point3D, Terrain> terrainMap, Map<Point3D, Obstacle> obstacleMap) {
        this.terrainMap = terrainMap;
        this.obstacleMap = obstacleMap;
    }

    public ArrayList<Point3D> getPath(Point3D start, Point3D goal, Entity actor) {
        ArrayList<Point3D> path = new ArrayList<>();

        Queue<Point3D> queue = new LinkedList<>();
        ArrayList<Point3D> visited = new ArrayList<>();
        Queue<Point3D> adj;

        HashMap<Point3D, Point3D> nodeList = new HashMap<>();

        queue.add(start);
        Point3D vert;
        Boolean found = false;

        while (!queue.isEmpty()) {
            vert = queue.remove();
            if (listContainsPoint(visited, vert)) {
                continue;
            }
            visited.add(vert);
            if (vert.equals(goal)) {
                break;
            }

            adj = getAdjacent(vert);
            Point3D next;

            while (!adj.isEmpty()) {
                next = adj.remove();
                if (next.equals(goal)) {
                    nodeList.put(next, vert);
                    queue.add(next);
                    found = true;
                    break;
                }
                if (!actor.canMoveOnTerrain(terrainMap.get(next)) || obstacleMap.containsKey(next)) {//Tile not passable
                    continue;
                }
                if (!listContainsPoint(visited, next)) {//Not Already visited
                    nodeList.put(next, vert);
                    queue.add(next);
                }
            }
            if(found) {
                break;
            }
        }

        Point3D current = goal;
        if(nodeList.size() == 0) {
            return path;
        }

        while(!start.equals(current)) {
            path.add(current);
            current = nodeList.get(current);
            if(current == null) { break; }
        }

        return path;
    }

    private boolean listContainsPoint(ArrayList<Point3D> pointList, Point3D point) {
        for(int i = 0; i < pointList.size(); i++) {
            if(pointList.get(i).equals(point)) {
                return true;
            }
        }
        return false;
    }

    private Queue<Point3D> getAdjacent(Point3D p) {
        Queue<Point3D> adj = new LinkedList<>();
        for (int dx = -1; dx <= 1; ++dx) {
            for (int dy = -1; dy <= 1; ++dy) {
                for (int dz = -1; dz <= 1; ++dz){
                    if (dx != 0 || dy != 0 || dz !=0) {
                        adj.add(new Point3D((p.getX()+ dx),(p.getY() + dy), (p.getZ()+dz)));
                    }
                }
            }
        }
        return adj;
    }
}
