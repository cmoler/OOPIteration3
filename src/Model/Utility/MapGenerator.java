package Model.Utility;

import Controller.Factories.EntityFactories.*;
import Controller.Factories.ItemFactory;
import Controller.Factories.PetAIFactory;
import Controller.Factories.SkillsFactory;
import Controller.Visitor.SavingVisitor;
import Model.AI.AIController;
import Model.AI.HostileAI;
import Model.AI.PatrolPath;
import Model.AI.PetAI.PetStates.GeneralPetState;
import Model.AreaEffect.InfiniteAreaEffect;
import Model.AreaEffect.OneShotAreaEffect;
import Model.Command.EntityCommand.NonSettableCommand.InstaDeathCommand;
import Model.Command.EntityCommand.NonSettableCommand.LevelUpCommand;
import Model.Command.EntityCommand.NonSettableCommand.TeleportEntityCommand;
import Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand.ToggleHealthCommand;
import Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand.ToggleSpeedCommand;
import Model.Command.EntityCommand.SettableCommand.*;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.*;
import Model.InfluenceEffect.AngularInfluenceEffect;
import Model.InfluenceEffect.LinearInfluenceEffect;
import Model.InfluenceEffect.RadialInfluenceEffect;
import Model.Item.OneShotItem;
import Model.Item.TakeableItem.*;
import Model.Level.*;
import View.LevelView.EntityView.SmasherView;
import View.LevelView.EntityView.SneakView;
import View.LevelView.EntityView.SummonerView;
import View.LevelView.ItemView;
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
    private static SavingVisitor sneakSavingVisitor;
    private static SavingVisitor smasherSavingVisitor;
    private static SavingVisitor summonerSavingVisitor;
    private static Level level0;
    private static Level level1;
    private static Level level2;
    private static Level level3;
    private static Level level4;

    private static Entity player;
    private static GameModel gameModel;
    private static LevelMessenger levelMessenger;
    private static SkillsFactory skillsFactory;
    private static EntityFactory entityFactory;
    private static ItemFactory itemFactory;
    private static Map<Level, List<AIController>> aiMap;
    private static List<Entity> petList;
    private static List<Entity> monsterList;
    private static List<Entity> shopkeeperList;

    public MapGenerator() throws IOException {
        levelMessenger = new LevelMessenger(null, null);
        sneakSavingVisitor = new SavingVisitor("SNEAK.xml");
        smasherSavingVisitor = new SavingVisitor("SMASHER.xml");
        summonerSavingVisitor = new SavingVisitor("SUMMONER.xml");
        skillsFactory = new SkillsFactory(levelMessenger);
        entityFactory = new MonsterFactory(skillsFactory);
        itemFactory = new ItemFactory(skillsFactory, levelMessenger);
        aiMap = new HashMap<>();
        petList = new ArrayList<>();
        monsterList = new ArrayList<>();
        shopkeeperList = new ArrayList<>();
    }

    public void reinit() {
        levelMessenger = new LevelMessenger(null, null);
        skillsFactory = new SkillsFactory(levelMessenger);
        entityFactory = new MonsterFactory(skillsFactory);
        itemFactory = new ItemFactory(skillsFactory, levelMessenger);
        aiMap = new HashMap<>();
        petList = new ArrayList<>();
        monsterList = new ArrayList<>();
        shopkeeperList = new ArrayList<>();
    }

    private static void generateDemoMap() {
        makeGame();
    }

    private static void makeEnemy(Level level) {
        ArrayList<AIController> aiControllers = new ArrayList<>();
        ArrayList<Vec3d> path = new ArrayList<>();
        path.add(new Vec3d(1,0,-1));

        entityFactory = new MonsterFactory(skillsFactory);
        Entity enemy = entityFactory.buildEntity();
        enemy.setSpeed(2000000000L);
        enemy.setSightRadius(new SightRadius(3));
        enemy.setTargetingList(new ArrayList<Entity>(){{add(player);}});

        Entity other = entityFactory.buildEntity();
        other.setSpeed(1500000000L);
        other.setSightRadius(new SightRadius(3));
        other.setTargetingList(new ArrayList<Entity>(){{add(player);}});

        WeaponItem longsword = itemFactory.getTwoHandedSword();
        enemy.addItemToInventory(longsword);
        enemy.equipWeapon(longsword);

        entityFactory.buildEntitySprite(enemy);
        entityFactory.buildEntitySprite(other);

        HostileAI hostileAI = new HostileAI(enemy, level.getTerrainMap(), level.getEntityMap(), level.getObstacleMap(), level.getRiverMap());
        hostileAI.setPatrolPath(new PatrolPath(path));

        ArrayList<Vec3d> path2 = new ArrayList<>();
        path2.add(new Vec3d(1,1,0));

        HostileAI hostileAI2 = new HostileAI(other, level.getTerrainMap(), level.getEntityMap(), level.getObstacleMap(), level.getRiverMap());
        hostileAI2.setPatrolPath(new PatrolPath(path2));

        AIController controller = new AIController();
        controller.setActiveState(hostileAI);

        AIController controller2 = new AIController();
        controller2.setActiveState(hostileAI2);

        level.addEntityTo(Orientation.getAdjacentPoint(Orientation.getAdjacentPoint(Orientation.getAdjacentPoint(Orientation.getAdjacentPoint(Orientation.getAdjacentPoint(Orientation.getAdjacentPoint(new Point3D(0,1,-1),Orientation.NORTH), Orientation.NORTH), Orientation.NORTH), Orientation.NORTH), Orientation.NORTH), Orientation.NORTH), enemy);
        level.addEntityTo(Orientation.getAdjacentPoint(Orientation.getAdjacentPoint(Orientation.getAdjacentPoint(Orientation.getAdjacentPoint(Orientation.getAdjacentPoint(Orientation.getAdjacentPoint(new Point3D(0,1,-1),Orientation.SOUTH), Orientation.SOUTH), Orientation.SOUTH), Orientation.SOUTH), Orientation.SOUTH), Orientation.SOUTH), other);

        entityFactory = new PetFactory(skillsFactory);
        Entity pet = entityFactory.buildEntity();
        PetAIFactory petAIFactory = new PetAIFactory(level.getTerrainMap(),
                level.getObstacleMap(),
                level.getItemMap(),
                skillsFactory.getPickpocket(),
                level.getRiverMap(),
                level.getEntityMap(),
                player,
                pet);

        entityFactory.buildEntitySprite(pet);
        pet.setTargetingList(new ArrayList<Entity>() {{ add(enemy); add(other); }});
        pet.setMoveable(true);
        pet.setNoise(5);
        pet.setName("McNugget");
        pet.setSpeed(1000000000l);
        pet.setSightRadius(new SightRadius(3));
        pet.increaseMaxHealth(50);
        pet.increaseHealth(50);
        WeaponItem sword = itemFactory.getOneHandedSword();
        pet.addItemToInventory(sword);
        pet.equipWeapon(sword);
        pet.addItemToInventory(itemFactory.getPotion());
        pet.addItemToInventory(itemFactory.getHealthRing());
        pet.addItemToInventory(itemFactory.getFreezeBow());

        level.addEntityTo(Orientation.getAdjacentPoint(new Point3D(0,1,-1),Orientation.SOUTH), pet);

        GeneralPetState passivePetState = petAIFactory.getGeneralPetState();

        AIController controller3 = new AIController();
        controller3.setActiveState(passivePetState);

        aiControllers.add(controller);
        aiControllers.add(controller2);
        aiControllers.add(controller3);

        player.addFriendly(pet);
        enemy.addTarget(pet);
        other.addTarget(pet);
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
        level.addEntityTo(new Point3D(0,1,-1), player);
    }

    private static void createSummoner() {
        entityFactory = new SummonerFactory(skillsFactory);
        player = entityFactory.buildEntity();
        entityFactory.buildEntitySprite(player);
        player.addItemToInventory(itemFactory.getMediumArmor());
        player.addItemToInventory(itemFactory.getManaPotion());
        player.addItemToInventory(itemFactory.getManaPotion());
        player.addItemToInventory(itemFactory.getManaPotion());
        player.addItemToInventory(itemFactory.getPotion());
        player.addItemToInventory(itemFactory.getPotion());
        player.addItemToInventory(itemFactory.getStaff());
        player.addItemToInventory(itemFactory.getSpeedRing());

        level0.addEntityTo(new Point3D(0,0,0), player);
    }

    private static void createSmasher() {
        entityFactory = new SmasherFactory(skillsFactory);
        player = entityFactory.buildEntity();
        entityFactory.buildEntitySprite(player);
        player.addItemToInventory(itemFactory.getHeavyArmor());
        player.addItemToInventory(itemFactory.getOneHandedSword());
        player.addItemToInventory(itemFactory.getTwoHandedSword());
        player.addItemToInventory(itemFactory.getBrawlerWeapon());

        level0.addEntityTo(new Point3D(0,0,0), player);
    }

    private static void createSneak() {
        entityFactory = new SneakFactory(skillsFactory);
        player = entityFactory.buildEntity();
        entityFactory.buildEntitySprite(player);
        player.addItemToInventory(itemFactory.getRangedWeapon());
        player.addItemToInventory(itemFactory.getLightArmor());
        player.addItemToInventory(itemFactory.getFreezeBow());

        level0.addEntityTo(new Point3D(0,0,0), player);
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

        radialInfluenceEffect = new RadialInfluenceEffect(new RemoveHealthCommand(15), 10, 5, Orientation.SOUTHEAST);
        radialInfluenceEffect.setOriginPoint(new Point3D(0,0,0));
        level2.addTerrainTo(new Point3D(0, 0, 0), Terrain.GRASS);
        for(int i = 0; i < 10; i++) {
            ArrayList<Point3D> points = radialInfluenceEffect.nextMove(radialInfluenceEffect.getOriginPoint());
            for(int j = 0; j < points.size(); j++) {
                level2.addTerrainTo(points.get(j), Terrain.GRASS);
            }
        }

        // LEVEL 2 AREA EFFECTS
        OneShotAreaEffect levelUpEffect = new OneShotAreaEffect(new LevelUpCommand());
        OneShotAreaEffect instantDeathCommand = new OneShotAreaEffect(new InstaDeathCommand());
        InfiniteAreaEffect healthPool = new InfiniteAreaEffect(new AddHealthCommand(1));
        InfiniteAreaEffect damagePool = new InfiniteAreaEffect(new RemoveHealthCommand(1));
        ItemFactory itemFactory = new ItemFactory(skillsFactory, levelMessenger);

        level2.addAreaEffectTo(new Point3D(-2,1,1), levelUpEffect);
        level2.addAreaEffectTo(new Point3D(2,-1,-1), instantDeathCommand);

        level2.addAreaEffectTo(new Point3D(0,-3,3), healthPool);
        level2.addAreaEffectTo(new Point3D(0,3,-3), damagePool);

        // LEVEL 2 ITEMS
        OneShotItem hurts = new OneShotItem("YEAH", new RemoveHealthCommand(100));
        ConsumableItem healingPotion = new ConsumableItem("Healing Potion", new AddHealthCommand(100));
        WeaponItem brawler = itemFactory.getBrawlerWeapon();
        level2.addItemTo(new Point3D(2,0,-2), healingPotion);
        level2.addItemTo(new Point3D(-2,0,2), brawler);
        level2.addItemTo(new Point3D(-4,0,4), hurts);

        // Level 2 Traps
        Trap trap = new Trap(new RemoveHealthCommand(100), 10);
        level2.addTrapTo(new Point3D(-6, 0, 6), trap);

        // END OF LEVEL 2

        radialInfluenceEffect = new RadialInfluenceEffect(new RemoveHealthCommand(15), 10, 5, Orientation.SOUTHEAST);
        radialInfluenceEffect.setOriginPoint(new Point3D(0,0,0));
        level3.addTerrainTo(new Point3D(0, 0, 0), Terrain.GRASS);
        for(int i = 0; i < 9; i++) {
            ArrayList<Point3D> points = radialInfluenceEffect.nextMove(radialInfluenceEffect.getOriginPoint());
            for(int j = 0; j < points.size(); j++) {
                level3.addTerrainTo(points.get(j), Terrain.GRASS);
            }
        }

        makeEnemy(level3);

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
        level0.addAreaEffectTo(new Point3D(5, -5, 0), homeTeleport);

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

        gameModel = new GameModel(level0, null, levels, player, aiMap, null, null);
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
        createSmasher();
        generateDemoMap();
        smasherSavingVisitor.visitGameModel(gameModel);

        reinit();

        level0 = new Level();
        level1 = new Level();
        level2 = new Level();
        level3 = new Level();
        level4 = new Level();
        createSummoner();
        generateDemoMap();
        summonerSavingVisitor.visitGameModel(gameModel);

        reinit();

        level0 = new Level();
        level1 = new Level();
        level2 = new Level();
        level3 = new Level();
        level4 = new Level();
        createSneak();
        generateDemoMap();
        sneakSavingVisitor.visitGameModel(gameModel);

        System.exit(0);
    }
}
