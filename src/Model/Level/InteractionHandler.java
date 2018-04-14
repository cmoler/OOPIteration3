package Model.Level;

import Model.AreaEffect.AreaEffect;
import Model.Entity.Entity;
import Model.InfluenceEffect.InfluenceEffect;
import Model.Item.Item;
import View.LevelView.LevelViewElement;
import javafx.geometry.Point3D;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InteractionHandler {

    private Map<Point3D, Item> itemLocations;
    private Map<Point3D, Entity> entityLocations;
    private Map<Point3D, AreaEffect> areaEffectLocations;
    private Map<Point3D, Trap> trapLocations;
    private Map<Point3D, Mount> mountLocations;
    private Map<Point3D, InfluenceEffect> influenceEffectLocations;

    private List<LevelViewElement> observers;

    public  InteractionHandler(Map<Point3D, Item> itemLocations,
                               Map<Point3D, Entity> entityLocations,
                               Map<Point3D, AreaEffect> areaEffectLocations,
                               Map<Point3D, Trap> trapLocations,
                               Map<Point3D, Mount> mountLocations,
                               Map<Point3D, InfluenceEffect> influenceEffectLocations,
                               List<LevelViewElement> observers) {

        this.itemLocations = itemLocations;
        this.entityLocations = entityLocations;
        this.areaEffectLocations = areaEffectLocations;
        this.trapLocations = trapLocations;
        this.mountLocations = mountLocations;
        this.influenceEffectLocations = influenceEffectLocations;

        this.observers = observers;
    }

    // TODO: add logic for notifying observers

    public void processInteractions() {
        // TODO: mount - entity interactions
        // TODO: influenceEffect - entity interactions

        processItems();
        processAreaEffects();
        processTraps();

        deleteItems();
    }

    private void processItems() {
        List<Point3D> entityPoints = new ArrayList<>(entityLocations.keySet());

        for(Point3D point : entityPoints) {
            if(itemLocations.containsKey(point)) {
                Item item = itemLocations.get(point);
                Entity entity = entityLocations.get(point);

                item.onTouch(entity);
            }
        }
    }

    private void processAreaEffects() {
        List<Point3D> entityPoints = new ArrayList<>(entityLocations.keySet());

        for(Point3D point : entityPoints) {
            if(areaEffectLocations.containsKey(point)) {
                AreaEffect effect = areaEffectLocations.get(point);
                Entity entity = entityLocations.get(point);

                effect.trigger(entity);
            }
        }
    }

    private void processTraps() {
        List<Point3D> entityPoints = new ArrayList<>(entityLocations.keySet());

        for(Point3D point : entityPoints) {
            if(trapLocations.containsKey(point)) {
                Trap trap = trapLocations.get(point);
                Entity entity = entityLocations.get(point);

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
}
