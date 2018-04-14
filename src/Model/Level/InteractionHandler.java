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
        // TODO: item - entity interactions
        // TODO: trap - entity interactions
        // TODO: mount - entity interactions
        // TODO: influenceEffect - entity interactions

        processAreaEffects();
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
}
