package Model.Utility;

import Controller.Factories.EntityFactories.EntityFactory;
import Controller.Factories.EntityFactories.MonsterFactory;
import Controller.Factories.SkillsFactory;
import Controller.Visitor.SavingVisitor;
import Model.AI.AIController;
import Model.AI.HostileAI;
import Model.AI.PatrolPath;
import Model.AreaEffect.InfiniteAreaEffect;
import Model.Command.EntityCommand.NonSettableCommand.TeleportEntityCommand;
import Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand.ToggleHealthCommand;
import Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand.ToggleSpeedCommand;
import Model.Command.EntityCommand.SettableCommand.*;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.*;
import Model.InfluenceEffect.AngularInfluenceEffect;
import Model.InfluenceEffect.LinearInfluenceEffect;
import Model.InfluenceEffect.RadialInfluenceEffect;
import Model.Item.TakeableItem.*;
import Model.Level.*;
import View.LevelView.EntityView.SmasherView;
import View.LevelView.EntityView.SneakView;
import View.LevelView.EntityView.SummonerView;
import com.sun.javafx.geom.Vec3d;
import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapGenerator extends Application {
    private static SavingVisitor savingVisitor;
    private static Level level0;
    private static Level level1;
    private static Level level3;
    private static Level level4;

    private static Entity entity;
    private static GameModel gameModel;
    private static LevelMessenger levelMessenger;
    private static SkillsFactory skillsFactory;
    private static EntityFactory entityFactory;
    private static Map<Level, List<AIController>> aiMap = new HashMap<>();

    public MapGenerator() throws IOException {
        savingVisitor = new SavingVisitor("SNEAK.xml");
        skillsFactory = new SkillsFactory(levelMessenger);
        entityFactory = new MonsterFactory(skillsFactory);
    }

    private static void generateDemoMap() {
        ArrayList<Level> levels = new ArrayList<>();
        levelMessenger = new LevelMessenger(null, null);

        level0 = new Level();
        level1 = new Level();

        createTerrains(level0);
        createAreaEffects(level0);
        createInfluenceAreas(level0);
        createItems(level0);
        createMounts(level0);
        createTraps(level0);
        createObstacle(level0);
        createRivers(level0);
        createEnities(level0);
        makeEnemy(level0);

        createTerrainsForWorld(level1);
        levels.add(level0);
        levels.add(level1);

        gameModel = new GameModel(level0, null, levels, entity, aiMap, null, null);
        savingVisitor.visitGameModel(gameModel);
    }

    private static void makeEnemy(Level level) {
        ArrayList<AIController> aiControllers = new ArrayList<>();
        ArrayList<Vec3d> path = new ArrayList<>();
        path.add(new Vec3d(1,0,-1));
//        path.add(new Vec3d(1,0,-1));
//        path.add(new Vec3d(-1,1,0));
//        path.add(new Vec3d(-1,1,0));
//        path.add(new Vec3d(0,-1,1));
//        path.add(new Vec3d(0,-1,1));
//        path.add(new Vec3d(-1,0,1));
//        path.add(new Vec3d(-1,0,1));
//        path.add(new Vec3d(1,-1,0));
//        path.add(new Vec3d(1,-1,0));

        Entity enemy = entityFactory.buildEntity();
        entityFactory.buildEntitySprite(enemy);

        HostileAI hostileAI = new HostileAI(enemy, level.getTerrainMap(), level.getEntityMap(), level.getObstacleMap(), level.getRiverMap());
        hostileAI.setPatrolPath(new PatrolPath(path));

        AIController controller = new AIController();
        controller.setActiveState(hostileAI);

        level.addEntityTo(new Point3D(0, 3, -3), enemy);
        aiControllers.add(controller);
        aiMap.put(level, aiControllers);
    }

    private static void createTerrainsForWorld(Level levelToTeleportTo) {
        RadialInfluenceEffect radialInfluenceEffect = new RadialInfluenceEffect(new RemoveHealthCommand(15), 10, 5, Orientation.SOUTHEAST);
        radialInfluenceEffect.setOriginPoint(new Point3D(0,0,0));
        levelToTeleportTo.addTerrainTo(new Point3D(0, 0, 0), Terrain.GRASS);
        for(int i = 0; i < 4; i++) {
            ArrayList<Point3D> points = radialInfluenceEffect.nextMove(radialInfluenceEffect.getOriginPoint());
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

        WeaponItem bane = new WeaponItem("ELECTRIFY",
                new RemoveHealthCommand(10),
                skillsFactory.getRangeSkill(),
                new RadialInfluenceEffect(new RemoveHealthCommand(10), 4,10, Orientation.NORTH),
                10,10,10,10,4);

        WeaponItem boon = new WeaponItem("BOONY",
                new RemoveHealthCommand(10),
                skillsFactory.getRangeSkill(),
                new RadialInfluenceEffect(new RemoveHealthCommand(10), 4,10, Orientation.NORTH),
                10,10,10,10,4);

        WeaponItem enchant = new WeaponItem("FREEZE",
                new FreezeEntityCommand(levelMessenger),
                skillsFactory.getRangeSkill(),
                new AngularInfluenceEffect(new FreezeEntityCommand(levelMessenger), 4,10, Orientation.NORTH),
                10,10,10,10,4);

        WeaponItem staff = new WeaponItem("STAFF",
                new RemoveHealthCommand(100),
                skillsFactory.getStaffSkill(),
                new LinearInfluenceEffect(new FreezeEntityCommand(levelMessenger), 4,10, Orientation.NORTH),
                10,10,10,10,4);

        Inventory inventory = new Inventory(new ArrayList<TakeableItem>() {{
            add(helmet);
            add(ringItem);
            add(bane);
            add(boon);
            add(enchant);
            add(staff);
        }}, 15);

        Equipment equipment = new Equipment(null, null, null);

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

        entity = new Entity(null, new ItemHotBar(), new ArrayList<>(),
                new ArrayList<>(), new HashMap<>(), new Vec3d(0,0,0), new NoiseLevel(5), new SightRadius(10),
                new XPLevel(), new Health(100, 100), new Mana(100, 10, 100), new Speed(10),
                new Gold(100, 100), new Attack(100, 1), new Defense(100, 1),
                equipment, inventory, Orientation.NORTH, new ArrayList<Terrain>() {{ add(Terrain.GRASS); }}, true,
                null);

        entity.addWeaponSkills(weaponSkills.get(0), weaponSkills.get(1), weaponSkills.get(2), weaponSkills.get(3));
        entity.addWeaponSkills(nonWeaponSkills.get(0), nonWeaponSkills.get(1), nonWeaponSkills.get(2));
        entity.setObserver(new SummonerView(entity, new Point3D(0,1,-1)));
    }

    private static void createSmasher() {
        ArrayList<Terrain> entityTerrain = new ArrayList<Terrain>(){{ add(Terrain.GRASS); }};
        ArmorItem helmet = new ArmorItem("Helmet", new ToggleHealthCommand(10), 10);
        RingItem ringItem = new RingItem("Ring", new ToggleSpeedCommand(10));
        WeaponItem warHammer = new WeaponItem("War Hammer Of Destruction",
                new RemoveHealthCommand(20),
                skillsFactory.getTwoHandedSkill(),
                new LinearInfluenceEffect(new RemoveHealthCommand(20), 1,10, Orientation.NORTH),
                20,2,10,45,1);

        WeaponItem sword = new WeaponItem("Plain Ol' Sword",
                new RemoveHealthCommand(10),
                skillsFactory.getTwoHandedSkill(),
                new LinearInfluenceEffect(new RemoveHealthCommand(10), 1,5, Orientation.NORTH),
                10,5,10,15,1);

        WeaponItem knuckles = new WeaponItem("Brass Knucles",
                new RemoveHealthCommand(5),
                skillsFactory.getTwoHandedSkill(),
                new LinearInfluenceEffect(new RemoveHealthCommand(5), 1,10, Orientation.NORTH),
                5,10,10,5,1);

        Inventory inventory = new Inventory(new ArrayList<TakeableItem>() {{
            add(helmet);
            add(ringItem);
            add(warHammer);
            add(sword);
            add(knuckles);
        }}, 15);

        Equipment equipment = new Equipment(null, null, null);

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
                new XPLevel(), new Health(100, 100), new Mana(100, 100, 100), new Speed(10),
                new Gold(100, 100), new Attack(100, 1), new Defense(100, 1),
                equipment, inventory, Orientation.NORTH, new ArrayList<Terrain>() {{ add(Terrain.GRASS); }}, true,
                null);

        entity.setObserver(new SmasherView(entity, new Point3D(0,1,-1)));
        entity.addWeaponSkills(weaponSkills.get(0), weaponSkills.get(1), weaponSkills.get(2));
        entity.addNonWeaponSkills(nonWeaponSkills.get(0), nonWeaponSkills.get(1), nonWeaponSkills.get(2));
    }

    private static void createSneak() {
        WeaponItem bow = new WeaponItem("Dark Bow",
                new RemoveHealthCommand(5),
                skillsFactory.getRangeSkill(),
                new LinearInfluenceEffect(new RemoveHealthCommand(20), 4,10, Orientation.NORTH),
                5,10,10,10,4);

        ArrayList<Terrain> entityTerrain = new ArrayList<Terrain>(){{ add(Terrain.GRASS); }};
        ArmorItem helmet = new ArmorItem("Helmet", new ToggleHealthCommand(10), 10);
        RingItem ringItem = new RingItem("Ring", new ToggleSpeedCommand(10));

        Inventory inventory = new Inventory(new ArrayList<TakeableItem>() {{
            add(helmet);
            add(ringItem);
            add(bow);
        }}, 15);

        Equipment equipment = new Equipment(null, null, null);

        ArrayList<Skill> weaponSkills = new ArrayList<Skill>() {{
            add(skillsFactory.getRangeSkill());
        }};

        ArrayList<Skill> nonWeaponSkills = new ArrayList<Skill>() {{
            add(skillsFactory.getObserveSkill());
            add(skillsFactory.getBargainSkill());
            add(skillsFactory.getBindWounds());
            add(skillsFactory.DisarmTrapSkill());
            add(skillsFactory.getSneakSkill());
            add(skillsFactory.getPickpocket());
        }};

        entity = new Entity(null, new ItemHotBar(), new ArrayList<>(),
                new ArrayList<>(), new HashMap<>(), new Vec3d(0,0,0), new NoiseLevel(5), new SightRadius(10),
                new XPLevel(), new Health(100, 100), new Mana(100, 100, 100), new Speed(10),
                new Gold(100, 100), new Attack(100, 1), new Defense(100, 1),
                equipment, inventory, Orientation.NORTH, new ArrayList<Terrain>() {{ add(Terrain.GRASS); }}, true,
                null);

        entity.addWeaponSkills(weaponSkills.get(0));
        entity.addNonWeaponSkills(nonWeaponSkills.get(0),
                nonWeaponSkills.get(1),
                nonWeaponSkills.get(2),
                nonWeaponSkills.get(3),
                nonWeaponSkills.get(4),
                nonWeaponSkills.get(5));
        entity.setObserver(new SneakView(entity, new Point3D(0,1,-1)));
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
        level.addItemTo(new Point3D(5,-4,-1), new ConsumableItem("Healing Potion", new AddHealthCommand(10)));
    }

    private static void createTerrains(Level level) {
        RadialInfluenceEffect radialInfluenceEffect = new RadialInfluenceEffect(new RemoveHealthCommand(15), 8, 0, Orientation.NORTH);
        radialInfluenceEffect.setOriginPoint(new Point3D(0,0,0));

        level.addTerrainTo(new Point3D(0, 0, 0), Terrain.WATER);
        for(int i = 0; i < 8; i++) {
            ArrayList<Point3D> points = radialInfluenceEffect.nextMove(radialInfluenceEffect.getOriginPoint());
            for(int j = 0; j < points.size(); j++) {
                level.addTerrainTo(points.get(j), Terrain.GRASS);
            }
        }

}

    private static void createAreaEffects(Level level) {
        TeleportEntityCommand teleportEntityCommand = new TeleportEntityCommand(levelMessenger, level1, new Point3D(0,0,0));
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

        createSneak();
        generateDemoMap();
        System.exit(0);
    }
}
