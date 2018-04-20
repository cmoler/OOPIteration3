package Model.AI;

import Model.Entity.Entity;
import Model.Level.Obstacle;
import Model.Level.Terrain;
import Model.Utility.VectorToPointCalculator;
import com.sun.javafx.geom.Vec3d;
import javafx.geometry.Point3D;
import javafx.util.Pair;

import java.util.*;

public class PathingAlgorithm {
    private Map<Point3D, Terrain> terrainMap;
    private Map<Point3D, Obstacle> obstacleMap;

    public PathingAlgorithm(Map<Point3D, Terrain> terrainMap, Map<Point3D, Obstacle> obstacleMap) {
        this.terrainMap = terrainMap;
        this.obstacleMap = obstacleMap;
    }

    public Point3D getAStarPoint(Point3D start, Point3D goal, Entity mover){
        Point3D next = start;
        Pair<Point3D,Integer> cost = new Pair<>(start,Integer.MAX_VALUE);

        for (Point3D p: getAdjacentList(start)){
            int new_cost = cost.getValue() + getCost(p,goal);
            if (new_cost < cost.getValue() && isValidPoint(p,mover)){
                next = p;
                cost = new Pair<>(p,new_cost);
            }
        }
        System.out.println("Next is :\t"+next);
        return next;
    }

    private int getCost(Point3D point, Point3D goal){
        return (int) goal.distance(point);
    }

    public ArrayList<Point3D> getPath(Point3D start, Point3D goal, Entity actor) {
        ArrayList<Point3D> path = new ArrayList<>();

        Queue<Point3D> queue = new LinkedList<>();
        ArrayList<Point3D> reachable = new ArrayList<>();
        Queue<Point3D> adj;

        HashMap<Point3D, Point3D> nodeList = new HashMap<>();

        queue.add(start);
        Point3D vert;
        Boolean found = false;

        while (!queue.isEmpty()) {
            vert = queue.remove();
            if (listContainsPoint(reachable, vert)) {
                continue;
            }
            reachable.add(vert);
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
                if (!listContainsPoint(reachable, next)) {//Not Already reachable
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


    public ArrayList<Point3D> getReachablePoints(Point3D start, int rangeLimit, Entity actor){
        ArrayList<Point3D> visited = new ArrayList<>();
        visited.add(start);
        ArrayList<ArrayList<Point3D>> fringe = new ArrayList<>();
        ArrayList<Point3D> zeroith = new ArrayList<Point3D>();
        zeroith.add(start);
        fringe.add(zeroith);

        for (int i = 1; i <= rangeLimit; ++i){
            ArrayList<Point3D> temp = new ArrayList<>();
            fringe.add(temp);
            for (Point3D point: fringe.get(i-1)){
                ArrayList<Point3D> adj = (ArrayList<Point3D>) getAdjacentList(point);
                for (Point3D p: adj){
                    if (isValidPointToMoveTo(p,visited, actor)){
                        visited.add(p);
                        fringe.get(i).add(p);
                    }
                }
            }

        }
        return visited;
    }

    private boolean isValidPoint(Point3D p, Entity e){
        return (e.canMoveOnTerrain(terrainMap.get(p)) && !obstacleMap.containsKey(p));
    }

    private boolean isValidPointToMoveTo(Point3D p, ArrayList<Point3D> visited, Entity e){
        return (e.canMoveOnTerrain(terrainMap.get(p)) && !obstacleMap.containsKey(p) || !visited.contains(p));
    }

    private boolean listContainsPoint(ArrayList<Point3D> pointList, Point3D point) {
        for(int i = 0; i < pointList.size(); i++) {
            if(pointList.get(i).equals(point)) {
                return true;
            }
        }
        return false;
    }

    public List<Point3D> getAdjacentList(Point3D p) {
        List<Point3D> adj = new ArrayList<>();
        adj.add(VectorToPointCalculator.calculateNewPoint(p,new Vec3d(0, 1, -1)));
        adj.add(VectorToPointCalculator.calculateNewPoint(p,new Vec3d(0, -1, 1)));
        adj.add(VectorToPointCalculator.calculateNewPoint(p,new Vec3d(1, 0, -1)));
        adj.add(VectorToPointCalculator.calculateNewPoint(p,new Vec3d(-1, 0, 1)));
        adj.add(VectorToPointCalculator.calculateNewPoint(p,new Vec3d(-1, 1, 0)));
        adj.add(VectorToPointCalculator.calculateNewPoint(p,new Vec3d(1, -1, 0)));
        return adj;
    }

    public Queue<Point3D> getAdjacent(Point3D p) {
        Queue<Point3D> adj = new LinkedList<>();
        adj.add(VectorToPointCalculator.calculateNewPoint(p,new Vec3d(0, 1, -1)));
        adj.add(VectorToPointCalculator.calculateNewPoint(p,new Vec3d(0, -1, 1)));
        adj.add(VectorToPointCalculator.calculateNewPoint(p,new Vec3d(1, 0, -1)));
        adj.add(VectorToPointCalculator.calculateNewPoint(p,new Vec3d(-1, 0, 1)));
        adj.add(VectorToPointCalculator.calculateNewPoint(p,new Vec3d(-1, 1, 0)));
        adj.add(VectorToPointCalculator.calculateNewPoint(p,new Vec3d(1, -1, 0)));
        return adj;
    }
}
