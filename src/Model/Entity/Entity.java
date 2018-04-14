package Model.Entity;

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
    private boolean sneaking;
    private boolean moveable;
}
