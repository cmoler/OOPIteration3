//TODO: Have enemies also attack pets

package Model.AI;

import Model.Entity.Entity;
import Model.Level.Obstacle;
import Model.Level.Terrain;
import com.sun.javafx.geom.Vec3d;
import javafx.geometry.Point3D;
import java.util.*;


public class HostileAI extends AIState{
    private Map<Point3D, Terrain> terrainMap;
    private Map<Point3D, Entity> entityMap;
    private Map<Point3D, Obstacle> obstacleMap;
    private Entity player;

    public HostileAI(Entity ent, Map<Point3D, Terrain> terrainMap, Map<Point3D, Entity> entityMap, Map<Point3D, Obstacle> obstacleMap, Entity player) {
        super(ent);
        this.terrainMap = terrainMap;
        this.entityMap = entityMap;
        this.obstacleMap = obstacleMap;
        this.player = player;
    }

    @Override
    public void nextMove() {
        Point3D position = getEntityPoint(super.getEntity(), entityMap);
        Point3D goal = getEntityPoint(player, entityMap);

        if (position.distance(goal) >= player.getNoise()){
            moveAlongPatrol();
        }
        else {
            moveToGoal(position, goal);
        }
    }

    private void moveAlongPatrol() {
        //@TODO: Make AI initialized by a path
        //Right now they just move randomly
        Random rand = new Random();
        super.getEntity().addVelocity(super.generateRandomVelcity());
    }

    private void moveToGoal(Point3D start, Point3D goal){
        ArrayList<Point3D> path = getPath(start, goal);
        Point3D firstStep = path.get(0);
        super.getEntity().addVelocity(new Vec3d(firstStep.getX()-start.getX(),firstStep.getY()-start.getY(),firstStep.getZ()-start.getZ()));
    }

    private Point3D getEntityPoint(Entity entity, Map<Point3D, Entity> entityLocations) {
        if(entityLocations.containsValue(entity)) {
            for(Point3D point: entityLocations.keySet()) {
                if(entityLocations.get(point) == entity) {
                    return point;
                }
            }
        }
        return new Point3D(0,0,0);
    }

    private ArrayList<Point3D> getPath(Point3D start, Point3D goal) {
        ArrayList<Point3D> path = new ArrayList<>();

        Queue<Point3D> queue = new LinkedList<>();
        ArrayList<Point3D> visited = new ArrayList<>();
        Queue<Point3D> adj;

        ArrayList<Vec3d> actions = new ArrayList<>();
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
                if (!super.getEntity().canMoveOnTerrain(terrainMap.get(next)) || obstacleMap.containsKey(next)) {//Tile not passable
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

    private  boolean listContainsPoint(ArrayList<Point3D> pointList, Point3D point) {
        for(int i = 0; i < pointList.size(); i++) {
            if(pointList.get(i).equals(point)) {
                return true;
            }
        }
        return false;
    }

}
