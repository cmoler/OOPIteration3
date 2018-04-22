package Model.Level;

import Model.AreaEffect.AreaEffect;
import Model.Entity.Entity;
import Model.InfluenceEffect.InfluenceEffect;
import Model.Item.Item;
import Model.Utility.BidiMap;
import View.LevelView.LevelViewElement;
import javafx.geometry.Point3D;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InteractionHandler {

    private Map<Point3D, Item> itemLocations;
    private BidiMap<Point3D, Entity> entityLocations;
    private Map<Point3D, AreaEffect> areaEffectLocations;
    private Map<Point3D, Trap> trapLocations;
    private Map<Point3D, Mount> mountLocations;
    private Map<Point3D, InfluenceEffect> influenceEffectLocations;
    private Map<Point3D, River> riverLocations;
    private List<LevelViewElement> observers;

    public  InteractionHandler(Map<Point3D, Item> itemLocations,
                               BidiMap<Point3D, Entity> entityLocations,
                               Map<Point3D, AreaEffect> areaEffectLocations,
                               Map<Point3D, Trap> trapLocations,
                               Map<Point3D, Mount> mountLocations,
                               Map<Point3D, InfluenceEffect> influenceEffectLocations,
                               Map<Point3D, River> riverLocations, List<LevelViewElement> observers) {

        this.itemLocations = itemLocations;
        this.entityLocations = entityLocations;
        this.areaEffectLocations = areaEffectLocations;
        this.trapLocations = trapLocations;
        this.mountLocations = mountLocations;
        this.influenceEffectLocations = influenceEffectLocations;
        this.riverLocations = riverLocations;

        this.observers = observers;
    }

    // TODO: add logic for notifying observers

    public void processInteractions() {
        processItems();
        processAreaEffects();
        processTraps();
        processInfluenceEffects();
        processRivers();
        deleteItems();
    }

    private void processRivers() {
        List<Point3D> entityPoints = new ArrayList<>(entityLocations.getKeyList());

        for(Point3D point : entityPoints) {
            if(riverLocations.containsKey(point)) {
                entityLocations.getValueFromKey(point).addVelocity(riverLocations.get(point).getFlowrate());
            }
        }
    }

    private void processItems() {
        List<Point3D> entityPoints = new ArrayList<>(entityLocations.getKeyList());

        for(Point3D point : entityPoints) {
            if(itemLocations.containsKey(point)) {
                Item item = itemLocations.get(point);
                Entity entity = entityLocations.getValueFromKey(point);

                item.onTouch(entity);
            }
        }
    }

    private void processAreaEffects() {
        List<Point3D> entityPoints = new ArrayList<>(entityLocations.getKeyList());

        for(Point3D point : entityPoints) {
            if(areaEffectLocations.containsKey(point)) {
                AreaEffect effect = areaEffectLocations.get(point);
                Entity entity = entityLocations.getValueFromKey(point);

                effect.trigger(entity);
            }
        }
    }

    private void processTraps() {
        List<Point3D> entityPoints = new ArrayList<>(entityLocations.getKeyList());

        for(Point3D point : entityPoints) {
            if(trapLocations.containsKey(point)) {
                Trap trap = trapLocations.get(point);
                Entity entity = entityLocations.getValueFromKey(point);

                trap.fire(entity);
            }
        }
    }

    private void deleteItems() {
        List<Point3D> itemPoints = new ArrayList<>(itemLocations.keySet());

        for(Point3D point : itemPoints) {
            Item item = itemLocations.get(point);

            if(item.isToBeDeleted()) {
                itemLocations.remove(point, item);
            }
        }
    }

    private void processInfluenceEffects() {
        List<Point3D> influenceEffectPoints = new ArrayList<>(influenceEffectLocations.keySet());

        for(Point3D point : influenceEffectPoints) {

            InfluenceEffect influenceEffect = influenceEffectLocations.get(point); //Get current influence effect

            if(entityLocations.hasKey(point)) {//Check if there is an entity on that location
                Entity entity = entityLocations.getValueFromKey(point); //Get entity
                influenceEffect.hitEntity(entity); //Trigger command
                System.out.println("Removing influence effect");
                influenceEffectLocations.remove(point, influenceEffect); // remove influence effect if it hit the entity
            } else if(influenceEffect.noMovesRemaining()) { // remove influence effect if it has no moves left and didn't hit an entity
                influenceEffectLocations.remove(point, influenceEffect);
            }
        }
    }
}
