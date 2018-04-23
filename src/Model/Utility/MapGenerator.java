package Model.Utility;

import Controller.Factories.EntityFactories.EntityFactory;
import Controller.Factories.EntityFactories.MonsterFactory;
import Controller.Factories.EntityFactories.PetFactory;
import Controller.Factories.PetAIFactory;
import Controller.Factories.SkillsFactory;
import Controller.Visitor.SavingVisitor;
import Model.AI.AIController;
import Model.AI.HostileAI;
import Model.AI.PatrolPath;
import Model.AI.PetAI.PetStates.GeneralPetState;
import Model.AI.PetAI.PetStates.PassivePetState;
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
    private static Level level2;
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
        makeGame();

//        createTerrains(level0);
//        createAreaEffects(level0);
//        createInfluenceAreas(level0);
//        createItems(level0);
//        createMounts(level0);
//        createTraps(level0);
//        createObstacle(level0);
//        createRivers(level0);
//        createEnities(level0);
//        makeEnemy(level0);
//
//        createTerrainsForWorld(level1);
//        levels.add(level0);
//        levels.add(level1);
    }

    private static void makeEnemy(Level level) {
        ArrayList<AIController> aiControllers = new ArrayList<>();
        ArrayList<Vec3d> path = new ArrayList<>();
        path.add(new Vec3d(1,0,-1));

        Entity other = entityFactory.buildEntity();
        Entity enemy = entityFactory.buildEntity();
        enemy.setTargetingList(new ArrayList<Entity>() {{ add(level.getEntityAtPoint(new Point3D(0,1,-1))); }});
        entityFactory.buildEntitySprite(enemy);

        HostileAI hostileAI = new HostileAI(enemy, level.getTerrainMap(), level.getEntityMap(), level.getObstacleMap(), level.getRiverMap());
        hostileAI.setPatrolPath(new PatrolPath(path));

        AIController controller = new AIController();
        controller.setActiveState(hostileAI);

        aiControllers.add(controller);

        hostileAI = new HostileAI(other, level.getTerrainMap(), level.getEntityMap(), level.getObstacleMap(), level.getRiverMap());
        hostileAI.setPatrolPath(new PatrolPath(path));

        controller = new AIController();
        controller.setActiveState(hostileAI);

        level.addEntityTo(new Point3D(0, 3, -3), enemy);
        level.addEntityTo(new Point3D(0, -3, 3), other);

        aiControllers.add(controller);

        entityFactory = new PetFactory(skillsFactory);
        Entity pet = entityFactory.buildEntity();
        PetAIFactory petAIFactory = new PetAIFactory(level.getTerrainMap(),
                level.getObstacleMap(),
                level.getItemMap(),
                skillsFactory.getPickpocket(),
                level.getRiverMap(),
                level.getEntityMap(),
                level.getEntityAtPoint(new Point3D(0,1,-1)), pet);

        entityFactory.buildEntitySprite(pet);
        pet.setTargetingList(new ArrayList<Entity>() {{ add(enemy); }});
        pet.setMoveable(true);
        pet.setNoise(5);
        pet.setName("McNugget");
        pet.setSpeed(1000000000l);
        pet.setSightRadius(new SightRadius(5));
        level.addEntityTo(new Point3D(-3, 5, -2), pet);

        GeneralPetState passivePetState = petAIFactory.getGeneralPetState();
        controller.setActiveState(passivePetState);

        level.getEntityAtPoint(new Point3D(0,1,-1)).addFriendly(pet);
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
                null, new ArrayList<>(), new ArrayList<>());

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
                null, new ArrayList<>(), new ArrayList<>());

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
                new ArrayList<>(), new HashMap<>(), new Vec3d(0,0,0), new NoiseLevel(5), new SightRadius(6),
                new XPLevel(), new Health(100, 100), new Mana(100, 100, 100), new Speed(10),
                new Gold(100, 100), new Attack(100, 1), new Defense(100, 1),
                equipment, inventory, Orientation.NORTH, new ArrayList<Terrain>() {{ add(Terrain.GRASS); }}, true,
                null, new ArrayList<>(), new ArrayList<>());

        entity.addWeaponSkills(weaponSkills.get(0));
        entity.addNonWeaponSkills(nonWeaponSkills.get(0),
                nonWeaponSkills.get(1),
                nonWeaponSkills.get(2),
                nonWeaponSkills.get(3),
                nonWeaponSkills.get(4),
                nonWeaponSkills.get(5));
        entity.setObserver(new SneakView(entity, new Point3D(0,1,-1)));
        level0.addEntityTo(new Point3D(0,0,0), entity);
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
        level.addMountTo(new Point3D(0,2,-2), new Mount(Orientation.NORTH, new Speed(10), mountTerrain));
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

    public static void makeGame() {
        ArrayList<Level> levels = new ArrayList<>();
        levelMessenger = new LevelMessenger(null, null);

        /** TERRAINS **/

        RadialInfluenceEffect radialInfluenceEffect = new RadialInfluenceEffect(new RemoveHealthCommand(15), 10, 5, Orientation.SOUTHEAST);
        radialInfluenceEffect.setOriginPoint(new Point3D(0,0,0));
        level0.addTerrainTo(new Point3D(0, 0, 0), Terrain.GRASS);
        //MAIN WORLD
        for(int i = 0; i < 10; i++) {
            ArrayList<Point3D> points = radialInfluenceEffect.nextMove(radialInfluenceEffect.getOriginPoint());
            for(int j = 0; j < points.size(); j++) {
                level0.addTerrainTo(points.get(j), Terrain.GRASS);
            }
        }

        radialInfluenceEffect = new RadialInfluenceEffect(new RemoveHealthCommand(15), 10, 5, Orientation.SOUTHEAST);
        radialInfluenceEffect.setOriginPoint(new Point3D(0,0,0));
        level1.addTerrainTo(new Point3D(0, 0, 0), Terrain.GRASS);
        for(int i = 0; i < 10; i++) {
            ArrayList<Point3D> points = radialInfluenceEffect.nextMove(radialInfluenceEffect.getOriginPoint());
            for(int j = 0; j < points.size(); j++) {
                level1.addTerrainTo(points.get(j), Terrain.GRASS);
            }
        }

        level1.addTerrainTo(new Point3D(5, 0, -5), Terrain.MOUNTAINS);
        level1.addTerrainTo(new Point3D(5, -1, -4), Terrain.WATER);
        level1.addObstacleTo(new Point3D( 5, -2, -3), new Obstacle());

        ArrayList passableTerrains = new ArrayList();
        passableTerrains.add(Terrain.GRASS);
        passableTerrains.add(Terrain.MOUNTAINS);
        passableTerrains.add(Terrain.WATER);
        Mount mount = new Mount(Orientation.NORTH, new Speed(-0425000000l), passableTerrains);
        level1.addMountTo(new Point3D(-2, 0, 2), mount);

        level1.addRiverTo(new Point3D(-3, 3, 0), new River(new Vec3d(1, 0 , -1)));
        level1.addRiverTo(new Point3D(-2, 3, -1), new River(new Vec3d(1, 0 , -1)));
        level1.addRiverTo(new Point3D(-1, 3, -2), new River(new Vec3d(1, 0 , -1)));
        level1.addRiverTo(new Point3D(0, 3, -3), new River(new Vec3d(1, -1 , 0)));
        level1.addRiverTo(new Point3D(1, 2, -3), new River(new Vec3d(1, -1 , 0)));
        level1.addRiverTo(new Point3D(2, 1, -3), new River(new Vec3d(1, -1 , 0)));
        level1.addRiverTo(new Point3D(3, 0, -3), new River(new Vec3d(0, -1 , 1)));
        level1.addRiverTo(new Point3D(3, -1, -2), new River(new Vec3d(0, -1 , 1)));
        level1.addRiverTo(new Point3D(3, -2, -1), new River(new Vec3d(0, -1 , 1)));
        level1.addRiverTo(new Point3D(3, -3, 0), new River(new Vec3d(-1, 0 , 1)));
        level1.addRiverTo(new Point3D(2, -3, 1), new River(new Vec3d(-1, 0 , 1)));
        level1.addRiverTo(new Point3D(1, -3, 2), new River(new Vec3d(-1, 0 , 1)));
        level1.addRiverTo(new Point3D(0, -3, 3), new River(new Vec3d(-1, 1 , 0)));
        level1.addRiverTo(new Point3D(-1, -2, 3), new River(new Vec3d(-1, 1 , 0)));
        level1.addRiverTo(new Point3D(-2, -1, 3), new River(new Vec3d(-1, 1 , 0)));
        level1.addRiverTo(new Point3D(-3, 0, 3), new River(new Vec3d(0, 1 , -1)));
        level1.addRiverTo(new Point3D(-3, 1, 2), new River(new Vec3d(0, 1 , -1)));
        level1.addRiverTo(new Point3D(-3, 2, 1), new River(new Vec3d(0, 1 , -1)));


        radialInfluenceEffect = new RadialInfluenceEffect(new RemoveHealthCommand(15), 10, 5, Orientation.SOUTHEAST);
        radialInfluenceEffect.setOriginPoint(new Point3D(0,0,0));
        level2.addTerrainTo(new Point3D(0, 0, 0), Terrain.GRASS);
        for(int i = 0; i < 10; i++) {
            ArrayList<Point3D> points = radialInfluenceEffect.nextMove(radialInfluenceEffect.getOriginPoint());
            for(int j = 0; j < points.size(); j++) {
                level2.addTerrainTo(points.get(j), Terrain.GRASS);
            }
        }

        radialInfluenceEffect = new RadialInfluenceEffect(new RemoveHealthCommand(15), 10, 5, Orientation.SOUTHEAST);
        radialInfluenceEffect.setOriginPoint(new Point3D(0,0,0));
        level3.addTerrainTo(new Point3D(0, 0, 0), Terrain.GRASS);
        for(int i = 0; i < 9; i++) {
            ArrayList<Point3D> points = radialInfluenceEffect.nextMove(radialInfluenceEffect.getOriginPoint());
            for(int j = 0; j < points.size(); j++) {
                level3.addTerrainTo(points.get(j), Terrain.GRASS);
            }
        }

        radialInfluenceEffect = new RadialInfluenceEffect(new RemoveHealthCommand(15), 10, 5, Orientation.SOUTHEAST);
        radialInfluenceEffect.setOriginPoint(new Point3D(0,0,0));
        level4.addTerrainTo(new Point3D(0, 0, 0), Terrain.GRASS);
        for(int i = 0; i < 8; i++) {
            ArrayList<Point3D> points = radialInfluenceEffect.nextMove(radialInfluenceEffect.getOriginPoint());
            for(int j = 0; j < points.size(); j++) {
                level4.addTerrainTo(points.get(j), Terrain.GRASS);
            }
        }


        /** TELEPORTS **/

        TeleportEntityCommand teleportEntityCommand = new TeleportEntityCommand(levelMessenger, level0, new Point3D(0,0,0));
        InfiniteAreaEffect homeTeleport = new InfiniteAreaEffect(teleportEntityCommand);
        RadialInfluenceEffect homeEffect = new RadialInfluenceEffect(new RemoveHealthCommand(15), 10, 5, Orientation.SOUTHEAST);

        for(int i = 0; i < 2; i++) {
            ArrayList<Point3D> points = homeEffect.nextMove(new Point3D(-5, 5, 0));
            for(int j = 0; j < points.size(); j++) {
                level1.addAreaEffectTo(points.get(j), homeTeleport);
            }
        }

        teleportEntityCommand = new TeleportEntityCommand(levelMessenger, level0, new Point3D(0,0,0));
        homeTeleport = new InfiniteAreaEffect(teleportEntityCommand);
        homeEffect = new RadialInfluenceEffect(new RemoveHealthCommand(15), 10, 5, Orientation.SOUTHEAST);
        for(int i = 0; i < 2; i++) {
            ArrayList<Point3D> points = homeEffect.nextMove(new Point3D(-4, 4, 0));
            for(int j = 0; j < points.size(); j++) {
                level2.addAreaEffectTo(points.get(j), homeTeleport);
            }
        }

        teleportEntityCommand = new TeleportEntityCommand(levelMessenger, level0, new Point3D(0,0,0));
        homeTeleport = new InfiniteAreaEffect(teleportEntityCommand);
        homeEffect = new RadialInfluenceEffect(new RemoveHealthCommand(15), 10, 5, Orientation.SOUTHEAST);
        for(int i = 0; i < 2; i++) {
            ArrayList<Point3D> points = homeEffect.nextMove(new Point3D(-4, 4, 0));
            for(int j = 0; j < points.size(); j++) {
                level3.addAreaEffectTo(points.get(j), homeTeleport);
            }
        }

        teleportEntityCommand = new TeleportEntityCommand(levelMessenger, level0, new Point3D(0,0,0));
        homeTeleport = new InfiniteAreaEffect(teleportEntityCommand);
        homeEffect = new RadialInfluenceEffect(new RemoveHealthCommand(15), 10, 5, Orientation.SOUTHEAST);
        for(int i = 0; i < 2; i++) {
            ArrayList<Point3D> points = homeEffect.nextMove(new Point3D(-4, 4, 0));
            for(int j = 0; j < points.size(); j++) {
                level4.addAreaEffectTo(points.get(j), homeTeleport);
            }
        }

        /** Telports From Main Word **/
        teleportEntityCommand = new TeleportEntityCommand(levelMessenger, level1, new Point3D(0,1,-1));
        homeTeleport = new InfiniteAreaEffect(teleportEntityCommand);

        level0.addAreaEffectTo(new Point3D(-5, 5, 0), homeTeleport);

        // LEVEL 3
        teleportEntityCommand = new TeleportEntityCommand(levelMessenger, level2, new Point3D(0,1,-1));
        homeTeleport = new InfiniteAreaEffect(teleportEntityCommand);
//        level0.addAreaEffectTo(new Point3D(5, -5, 0), homeTeleport);

        // LEVEL 4
        teleportEntityCommand = new TeleportEntityCommand(levelMessenger, level3, new Point3D(0,1,-1));
        homeTeleport = new InfiniteAreaEffect(teleportEntityCommand);
        level0.addAreaEffectTo(new Point3D(5, 0, -5), homeTeleport);

        // LEVEL 5
        teleportEntityCommand = new TeleportEntityCommand(levelMessenger, level4, new Point3D(0,1,-1));
        homeTeleport = new InfiniteAreaEffect(teleportEntityCommand);
        level0.addAreaEffectTo(new Point3D(-5, 0, 5), homeTeleport);

        levels.add(level0);
        levels.add(level1);
        levels.add(level2);
        levels.add(level3);
        levels.add(level4);

        gameModel = new GameModel(level0, null, levels, entity, aiMap, null, null);
        savingVisitor.visitGameModel(gameModel);
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

        level0 = new Level();
        level1 = new Level();
        level2 = new Level();
        level3 = new Level();
        level4 = new Level();

        createSneak();
        generateDemoMap();
        System.exit(0);
    }
}
