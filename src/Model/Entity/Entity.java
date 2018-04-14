package Model.Entity;

import Model.Entity.EntityAttributes.*;
import Model.Level.Terrain;
import Model.Level.Mount;
import View.LevelView.LevelViewElement;
import com.sun.javafx.geom.Vec3d;
import java.util.HashMap;
import java.util.List;

public class Entity {

    private List<LevelViewElement> observers;
    private List<Skill> weaponSkills;
    private List<Skill> nonWeaponSkills;
    private HashMap<Skill, SkillLevel> skillLevelsMap;
    private Vec3d velocity;
    private NoiseLevel noiseLevel;
    private SightRadius sightRadius;
    private XPLevel xpLevel;
    private Health health;
    private Mana mana;
    private Speed speed;
    private Gold gold;
    private Attack attack;
    private Defense defense;
    private Equipment equipment;
    private Inventory inventory;
    private Orientation orientation;
    private List<Terrain> compatableTerrain;
    private boolean sneaking;
    private boolean moveable;
    private Mount mount;

    public Entity() {
        this.xpLevel = new XPLevel();
        this.health = new Health(100, 100);
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public int getCurrentHealth() {
        return health.getCurrentHealth();
    }

    public int getMaxHealth() {
        return health.getMaxHealth();
    }

    public boolean isDead() {
        return health.getCurrentHealth() == 0;
    }

    public void increaseHealth(int amt) {
        health.increaseCurrentHealth(amt);
    }

    public void decreaseHealth(int amt) {
        health.decreaseCurrentHealth(amt);
    }

    public void levelUp() {
        xpLevel.increaseLevel();
    }

    public int getLevel() {
        return xpLevel.getLevel();
    }

    public void kill() {
        health.decreaseCurrentHealth(health.getMaxHealth());
    }

    public void mountVehicle(Mount mount){
        this.mount = mount;
        compatableTerrain.addAll(mount.getPassableTerrain());
        mount.setOrientation(getOrientation());
        speed.increaseSpeed(mount.getMovementSpeed());
    }
}
