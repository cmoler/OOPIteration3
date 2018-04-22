package Model.Utility;

import Controller.Factories.EntityFactories.EntityFactory;
import Controller.Factories.SkillsFactory;
import Controller.Visitor.SavingVisitor;
import Model.AreaEffect.InfiniteAreaEffect;
import Model.Command.EntityCommand.NonSettableCommand.TeleportEntityCommand;
import Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand.ToggleHealthCommand;
import Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand.ToggleSpeedCommand;
import Model.Command.EntityCommand.SettableCommand.*;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.*;
import Model.InfluenceEffect.RadialInfluenceEffect;
import Model.Item.TakeableItem.*;
import Model.Level.*;
import View.LevelView.EntityView.SmasherView;
import com.sun.javafx.geom.Vec3d;
import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MapGenerator extends Application {
    private static SavingVisitor savingVisitor;
    private static Level level;
    private static Level levelToTeleportTo;
    private static Entity entity;
    private static GameModel gameModel;
    private static LevelMessenger levelMessenger;
    private static SkillsFactory skillsFactory;
    private static EntityFactory entityFactory;

    public MapGenerator() throws IOException {
        savingVisitor = new SavingVisitor("SMASHER.xml");
        skillsFactory = new SkillsFactory(levelMessenger);
    }

    private static void generateDemoMap() {
        ArrayList<Level> levels = new ArrayList<>();
        levelMessenger = new LevelMessenger(null, null);

        level = new Level();
        levelToTeleportTo = new Level();

        createTerrains(level);
        createAreaEffects(level);
        createInfluenceAreas(level);
        createItems(level);
        createMounts(level);
        createTraps(level);
        createObstacle(level);
        createRivers(level);
        createEnities(level);

        createTerrainsForWorld(levelToTeleportTo);
        levels.add(level);
        levels.add(levelToTeleportTo);

        gameModel = new GameModel(level, null, levels, entity, null, null, null);
        savingVisitor.visitGameModel(gameModel);
    }

    private static void createTerrainsForWorld(Level levelToTeleportTo) {
        RadialInfluenceEffect radialInfluenceEffect = new RadialInfluenceEffect(new RemoveHealthCommand(15), 10, 5, Orientation.SOUTHEAST);

        levelToTeleportTo.addTerrainTo(new Point3D(0, 0, 0), Terrain.GRASS);
        for(int i = 0; i < 4; i++) {
            ArrayList<Point3D> points = radialInfluenceEffect.nextMove(new Point3D(0, 0, 0));
            for(int j = 0; j < points.size(); j++) {
                levelToTeleportTo.addTerrainTo(points.get(j), Terrain.GRASS);
            }
        }
    }

    private static void createEnities(Level level) {
        level.addEntityTo(new Point3D(0,1,-1), entity);
    }

    private static void createSummoner() {
        ArrayList<Terrain> entityTerrain = new ArrayList<Terrain>(){{ add(Terrain.GRASS); }};
        ArmorItem helmet = new ArmorItem("Helmet", new ToggleHealthCommand(10), 10);
        RingItem ringItem = new RingItem("Ring", new ToggleSpeedCommand(10));

        Inventory inventory = new Inventory(new ArrayList<TakeableItem>() {{
            add(helmet);
            add(ringItem);
        }}, 15);

        Equipment equipment = new Equipment(null, helmet, ringItem);

        ArrayList<Skill> weaponSkills = new ArrayList<Skill>() {{
            add(skillsFactory.getBaneSkill());
            add(skillsFactory.getBoonSkill());
            add(skillsFactory.getEnchantSkill());
            add(skillsFactory.getStaffSkill());
        }};

        ArrayList<Skill> nonWeaponSkills = new ArrayList<Skill>() {{
            add(skillsFactory.getObserveSkill());
            add(skillsFactory.getBargainSkill());
            add(skillsFactory.getBindWounds());
        }};

        entity = new Entity(null, new ItemHotBar(), weaponSkills,
                nonWeaponSkills, new HashMap<>(), new Vec3d(0,0,0), new NoiseLevel(5), new SightRadius(10),
                new XPLevel(), new Health(100, 100), new Mana(100, 100), new Speed(10),
                new Gold(100, 100), new Attack(100, 1), new Defense(100, 1),
                equipment, inventory, Orientation.NORTH, new ArrayList<Terrain>() {{ add(Terrain.GRASS); }}, false,
                null);

        entity.addWeaponSkills(weaponSkills.get(0), weaponSkills.get(1), weaponSkills.get(2));
        entity.setObserver(new SmasherView(entity, new Point3D(0,1,-1)));
    }

    private static void createSmasher() {
        ArrayList<Terrain> entityTerrain = new ArrayList<Terrain>(){{ add(Terrain.GRASS); }};
        ArmorItem helmet = new ArmorItem("Helmet", new ToggleHealthCommand(10), 10);
        RingItem ringItem = new RingItem("Ring", new ToggleSpeedCommand(10));

        Inventory inventory = new Inventory(new ArrayList<TakeableItem>() {{
            add(helmet);
            add(ringItem);
        }}, 15);

        Equipment equipment = new Equipment(null, helmet, ringItem);

        ArrayList<Skill> weaponSkills = new ArrayList<Skill>() {{
            add(skillsFactory.getOneHandedSkill());
            add(skillsFactory.getTwoHandedSkill());
            add(skillsFactory.getBrawlerSkill());
        }};

        ArrayList<Skill> nonWeaponSkills = new ArrayList<Skill>() {{
            add(skillsFactory.getObserveSkill());
            add(skillsFactory.getBargainSkill());
            add(skillsFactory.getBindWounds());
        }};
        entity = new Entity(null, new ItemHotBar(), weaponSkills,
                nonWeaponSkills, new HashMap<>(), new Vec3d(0,0,0), new NoiseLevel(5), new SightRadius(10),
                new XPLevel(), new Health(100, 100), new Mana(100, 100), new Speed(10),
                new Gold(100, 100), new Attack(100, 1), new Defense(100, 1),
                equipment, inventory, Orientation.NORTH, new ArrayList<Terrain>() {{ add(Terrain.GRASS); }}, false,
                null);

        entity.setObserver(new SmasherView(entity, new Point3D(0,1,-1)));
        entity.addWeaponSkills(weaponSkills.get(0), weaponSkills.get(1), weaponSkills.get(2));
    }

    private static void createSneak() {
        ArrayList<Terrain> entityTerrain = new ArrayList<Terrain>(){{ add(Terrain.GRASS); }};
        ArmorItem helmet = new ArmorItem("Helmet", new ToggleHealthCommand(10), 10);
        RingItem ringItem = new RingItem("Ring", new ToggleSpeedCommand(10));

        Inventory inventory = new Inventory(new ArrayList<TakeableItem>() {{
            add(helmet);
            add(ringItem);
        }}, 15);

        Equipment equipment = new Equipment(null, helmet, ringItem);

        ArrayList<Skill> weaponSkills = new ArrayList<Skill>() {{
            add(skillsFactory.getRangeSkill());
        }};

        ArrayList<Skill> nonWeaponSkills = new ArrayList<Skill>() {{
            add(skillsFactory.getObserveSkill());
            add(skillsFactory.getBargainSkill());
            add(skillsFactory.getBindWounds());
            add(skillsFactory.DisarmTrapSkill());
            add(skillsFactory.getSneakSkill());
        }};

        entity = new Entity(null, new ItemHotBar(), weaponSkills,
                nonWeaponSkills, new HashMap<>(), new Vec3d(0,0,0), new NoiseLevel(5), new SightRadius(10),
                new XPLevel(), new Health(100, 100), new Mana(100, 100), new Speed(10),
                new Gold(100, 100), new Attack(100, 1), new Defense(100, 1),
                equipment, inventory, Orientation.NORTH, new ArrayList<Terrain>() {{ add(Terrain.GRASS); }}, false,
                new Mount(Orientation.NORTH, new Speed(10), entityTerrain, new ArrayList<>()));

        entity.addWeaponSkills(weaponSkills.get(0), weaponSkills.get(1), weaponSkills.get(2));
        entity.setObserver(new SmasherView(entity, new Point3D(0,1,-1)));
    }

    private static void createRivers(Level level) {
        level.addRiverTo(new Point3D(-1,-1,2), new River(new Vec3d(1,0,0)));
    }

    private static void createObstacle(Level level) {
        level.addObstacleTo(new Point3D(-5,0,5), new Obstacle());
    }

    private static void createTraps(Level level) {
        level.addTrapTo(new Point3D(0,-2,2), new Trap(new RemoveHealthCommand(10), false, false, 10));
    }

    private static void createMounts(Level level) {
        ArrayList<Terrain> mountTerrain = new ArrayList<Terrain>(){{ add(Terrain.GRASS); add(Terrain.WATER); }};
        level.addMountTo(new Point3D(0,2,-2), new Mount(Orientation.NORTH, new Speed(10), mountTerrain, new ArrayList<>()));
    }

    private static void createItems(Level level) {
//        level.addItemnTo(new Point3D(0,0,0), new InteractiveItem("Door", new ToggleManaCommand(10)));
//        level.addItemnTo(new Point3D(4,-4,0), new OneShotItem("The Bomb", new RemoveHealthCommand(20)));
//        level.addItemnTo(new Point3D(4,-3,1), new ArmorItem("Plain Helmet", new ToggleHealthCommand(10), 1));
//        level.addItemnTo(new Point3D(4,-5,1), new RingItem("The Flash's Ring", new ToggleSpeedCommand(10)));
        level.addItemnTo(new Point3D(5,-4,-1), new ConsumableItem("Healing Potion", new AddHealthCommand(10)));
    }

    private static void createTerrains(Level level) {
        RadialInfluenceEffect radialInfluenceEffect = new RadialInfluenceEffect(new RemoveHealthCommand(15), 10, 5, Orientation.SOUTHEAST);

        level.addTerrainTo(new Point3D(0, 0, 0), Terrain.WATER);
        for(int i = 0; i < 8; i++) {
            ArrayList<Point3D> points = radialInfluenceEffect.nextMove(new Point3D(0, 0, 0));
            for(int j = 0; j < points.size(); j++) {
                level.addTerrainTo(points.get(j), Terrain.GRASS);
            }
        }

}

    private static void createAreaEffects(Level level) {
        TeleportEntityCommand teleportEntityCommand = new TeleportEntityCommand(levelMessenger, levelToTeleportTo, new Point3D(0,0,0));
        InfiniteAreaEffect damage = new InfiniteAreaEffect(teleportEntityCommand);
        RadialInfluenceEffect radialInfluenceEffect = new RadialInfluenceEffect(new RemoveHealthCommand(15), 10, 5, Orientation.SOUTHEAST);

        level.addAreaEffectTo(new Point3D(-4, 4, 0), damage);
        for(int i = 0; i < 2; i++) {
            ArrayList<Point3D> points = radialInfluenceEffect.nextMove(new Point3D(-4, 4, 0));
            for(int j = 0; j < points.size(); j++) {
                level.addAreaEffectTo(points.get(j), damage);
                level.addTerrainTo(points.get(j), Terrain.WATER);
            }
        }

//        level.addAreaEffectTo(new Point3D(-4,4,0), damage);
//        level.addAreaEffectTo(new Point3D(-4,3,1), damage);
//        level.addAreaEffectTo(new Point3D(-4,5,-1), damage);
//        level.addAreaEffectTo(new Point3D(-5,4,1), damage);
//        level.addAreaEffectTo(new Point3D(-3,4,-1), damage);
//        level.addAreaEffectTo(new Point3D(-5,5,0), damage);
//        level.addAreaEffectTo(new Point3D(-3,3,0), damage);
    }

    private static void createInfluenceAreas(Level level) {
    }

    public static void main(String... args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            new MapGenerator();
        } catch (IOException e) {
            e.printStackTrace();
        }

        createSmasher();
        generateDemoMap();
        System.exit(0);
    }
}
