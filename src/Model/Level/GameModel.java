package Model.Level;

import Controller.Factories.EntityFactories.*;
import Controller.Factories.ItemFactory;
import Controller.Factories.SkillsFactory;
import Controller.Visitor.Visitable;
import Controller.Visitor.Visitor;
import Model.AI.AIController;
import Model.AI.FriendlyAI;
import Model.AI.HostileAI;
import Model.AI.PatrolPath;
import Model.AI.PetAI.PetStates.PassivePetState;
import Model.AreaEffect.InfiniteAreaEffect;
import Model.Command.EntityCommand.NonSettableCommand.SendInfluenceEffectCommand;
import Model.Command.EntityCommand.NonSettableCommand.TeleportEntityCommand;
import Model.Command.EntityCommand.SettableCommand.AddHealthCommand;
import Model.Command.EntityCommand.SettableCommand.RemoveHealthCommand;
import Model.Command.EntityCommand.SettableCommand.SettableCommand;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Orientation;
import Model.Entity.EntityAttributes.SightRadius;
import Model.Entity.EntityAttributes.Skill;
import Model.InfluenceEffect.AngularInfluenceEffect;
import Model.InfluenceEffect.LinearInfluenceEffect;
import Model.InfluenceEffect.RadialInfluenceEffect;
import Model.Item.TakeableItem.ConsumableItem;
import Model.Item.TakeableItem.WeaponItem;
import Model.Utility.BidiMap;
import View.LevelView.LevelViewElement;
import com.sun.javafx.geom.Vec3d;
import javafx.geometry.Point3D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GameModel implements Visitable {

    private GameModelMessenger gameModelMessenger;

    private List<Level> levels;
    private Level currentLevel;
    private LevelMessenger currentLevelMessenger;

    private Entity player;

    private Map<Level, List<AIController>> aiMap;

    private Queue<TeleportTuple> teleportQueue;
    private Queue<TeleportTuple> failedTeleportQueue;

    private SkillsFactory skillsFactory;
    private EntityFactory entityFactory;

    public GameModel(GameLoopMessenger gameLoopMessenger) {
        gameModelMessenger = new GameModelMessenger(gameLoopMessenger, this);
        currentLevelMessenger = new LevelMessenger(gameModelMessenger, currentLevel);

        levels = new ArrayList<>();
        levels.add(currentLevel);

        aiMap = new ConcurrentHashMap<>();
        teleportQueue = new LinkedList<>();
        failedTeleportQueue = new LinkedList<>();
    }

    public GameModel(Level currentLevel, LevelMessenger currentLevelMessenger, List<Level> levels, Entity player,
                     Map<Level, List<AIController>> aiMap, LinkedList<TeleportTuple> teleportQueue, LinkedList<TeleportTuple> failedTeleportQueue) {
        this.currentLevel = currentLevel;
        this.currentLevelMessenger = currentLevelMessenger;
        this.levels = levels;
        this.player = player;
        this.aiMap = aiMap;
        this.currentLevel.setMovementHandlerDialogCommand(this.currentLevelMessenger);
        this.teleportQueue = teleportQueue;
        this.failedTeleportQueue = failedTeleportQueue;
    }

    public void init() {
        currentLevel = new Level();
        currentLevelMessenger.setLevel(currentLevel);
        currentLevel.setMovementHandlerDialogCommand(currentLevelMessenger);

        levels.add(currentLevel);

        skillsFactory = new SkillsFactory(currentLevelMessenger);

        entityFactory = new SummonerFactory(skillsFactory);

        player = entityFactory.buildEntity();

        ConsumableItem potion = new ConsumableItem("potion", new AddHealthCommand(20));
        potion.setCurrentLevelMessenger(currentLevelMessenger);
        potion.setPrice(2);
        potion.onTouch(player);

        ConsumableItem manapotion = new ConsumableItem("manapotion", new AddHealthCommand(20));
        manapotion.setCurrentLevelMessenger(currentLevelMessenger);
        manapotion.setPrice(2);
        manapotion.onTouch(player);

        ConsumableItem healthpotion = new ConsumableItem("healthpotion", new AddHealthCommand(20));
        healthpotion.setCurrentLevelMessenger(currentLevelMessenger);
        healthpotion.setPrice(2);
        healthpotion.onTouch(player);

        ConsumableItem beer = new ConsumableItem("beer", new AddHealthCommand(20));
        beer.setCurrentLevelMessenger(currentLevelMessenger);
        beer.setPrice(2);
        beer.onTouch(player);

        ConsumableItem wine = new ConsumableItem("wine", new AddHealthCommand(20));
        wine.setCurrentLevelMessenger(currentLevelMessenger);
        wine.setPrice(2);
        wine.onTouch(player);

        ConsumableItem gin = new ConsumableItem("gin", new AddHealthCommand(20));
        gin.setCurrentLevelMessenger(currentLevelMessenger);
        gin.setPrice(2);
        gin.onTouch(player);

        entityFactory.buildEntitySprite(player);

        player.setMoveable(true);
        player.setNoise(5);
        player.levelUp();
        Skill attack = new Skill();
        player.addWeaponSkills(attack);
        player.setSpeed(0500000000l);

        RemoveHealthCommand removeHealthCommand = new RemoveHealthCommand(15);
        LinearInfluenceEffect linearInfluenceEffect = new LinearInfluenceEffect(removeHealthCommand, 1, 1, Orientation.NORTH);
        SendInfluenceEffectCommand sendInfluenceEffectCommand = new SendInfluenceEffectCommand(currentLevelMessenger);
        Skill oneHandedSkill = skillsFactory.getOneHandedSkill();
        player.addWeaponSkills(oneHandedSkill);
        attack.setSendInfluenceEffectCommand(new SendInfluenceEffectCommand(currentLevelMessenger));
        SettableCommand og = new RemoveHealthCommand(1000);
        WeaponItem mace = new WeaponItem("Sword of Light", og, attack, new RadialInfluenceEffect(og,3,10, Orientation.NORTH), 1000, 1,100,100,2);
        player.addItemToInventory(mace);
        player.equipWeapon(mace);
        player.setSightRadius(new SightRadius(7));
        currentLevel.addEntityTo(new Point3D(0, -5, 5), player);

        RadialInfluenceEffect radialInfluenceEffect = new RadialInfluenceEffect(new RemoveHealthCommand(15), 10, 5, Orientation.SOUTHEAST);

        currentLevel.addTerrainTo(new Point3D(0, 0, 0), Terrain.GRASS);
        for(int i = 0; i < 8; i++) {
            ArrayList<Point3D> points = radialInfluenceEffect.nextMove(new Point3D(0, 0, 0));
            for(int j = 0; j < points.size(); j++) {
                currentLevel.addTerrainTo(points.get(j), Terrain.GRASS);
            }
        }

        currentLevel.addRiverTo(new Point3D(1, 0, -1), new River(new Vec3d(0, 1, -1)));
        currentLevel.addAreaEffectTo(new Point3D(-2, 1, 1), new InfiniteAreaEffect(new RemoveHealthCommand(10)));
        currentLevel.addObstacleTo(new Point3D(-2, 2, 0), new Obstacle());
        //currentLevel.addMountTo(new Point3D(0, 1, -1), new Mount());

        entityFactory = new MonsterFactory(skillsFactory);
        Entity enemy = entityFactory.buildEntity();
        entityFactory.buildEntitySprite(enemy);

        enemy.setMoveable(true);
        enemy.setNoise(5);
        Skill skill = new Skill();
        enemy.addWeaponSkills(skill);
        enemy.setSpeed(1500000000l);
        skill.setSendInfluenceEffectCommand(new SendInfluenceEffectCommand(currentLevelMessenger));
        SettableCommand bleh = new RemoveHealthCommand(5);
        WeaponItem sword = new WeaponItem("Sword of Darkness", bleh, skill, new LinearInfluenceEffect(bleh,2,10,Orientation.NORTH), 5, 1,1,450,2);
        enemy.addItemToInventory(sword);
        enemy.equipWeapon(sword);

        enemy.setSightRadius(new SightRadius(3));
        ArrayList<Vec3d> path = new ArrayList<>();
        path.add(new Vec3d(1,0,-1));
        path.add(new Vec3d(1,0,-1));
        path.add(new Vec3d(-1,1,0));
        path.add(new Vec3d(-1,1,0));
        path.add(new Vec3d(0,-1,1));
        path.add(new Vec3d(0,-1,1));
        path.add(new Vec3d(-1,0,1));
        path.add(new Vec3d(-1,0,1));
        path.add(new Vec3d(1,-1,0));
        path.add(new Vec3d(1,-1,0));//*/
        currentLevel.addEntityTo(new Point3D(0, 3, -3),enemy);
        List<Entity> list = new ArrayList<>();
        list.add(player);
        enemy.setTargetingList(list);
        HostileAI hostileAI = new HostileAI(enemy,currentLevel.getTerrainMap(),currentLevel.getEntityMap(),currentLevel.getObstacleMap(), currentLevel.getRiverMap());
        hostileAI.setPatrolPath(new PatrolPath(path));
        AIController controller = new AIController();
        controller.setActiveState(hostileAI);
        List<AIController> AIList = new ArrayList<>();
        aiMap.put(currentLevel,AIList);


        entityFactory = new PetFactory(skillsFactory);
        Entity pet = entityFactory.buildEntity();
        entityFactory.buildEntitySprite(pet);
        pet.setMoveable(true);
        pet.setNoise(5);
        pet.setSpeed(1000000000l);
        Skill skill1 = new Skill();
        enemy.addWeaponSkills(skill1);
        skill1.setSendInfluenceEffectCommand(new SendInfluenceEffectCommand(currentLevelMessenger));
        SettableCommand rawr = new RemoveHealthCommand(5);
        WeaponItem claw = new WeaponItem("Sharp Claw", rawr, skill1, new LinearInfluenceEffect(rawr,1,10,Orientation.NORTH), 5, 10000000l,100,450,1);
        enemy.addItemToInventory(claw);
        enemy.equipWeapon(claw);
        pet.setSightRadius(new SightRadius(2));
        list.add(pet);
        currentLevel.addEntityTo(new Point3D(5, -5, 0), pet);
        AIController test = new AIController();

        //AIList.add(test);

        //currentLevel.addInfluenceEffectTo(new Point3D(-2, -1, 3), new RadialInfluenceEffect(new RemoveHealthCommand(100), 5, 5, Orientation.NORTH));
        ItemFactory itemFactory = new ItemFactory(skillsFactory, currentLevelMessenger);
        WeaponItem weaponItem = itemFactory.getStaff();
        weaponItem.notifyObserver(new Point3D(1, -1, 0));
        currentLevel.addItemTo(new Point3D(1, -1, 0), weaponItem);

        // Passive Pet AI
        PassivePetState PPS = new PassivePetState(pet,currentLevel.getTerrainMap(),currentLevel.getEntityMap(),currentLevel.getObstacleMap(),player, currentLevel.getRiverMap());
        test.setActiveState(PPS);

        // Combat Pet AI
        /*CombatPetState CPS = new CombatPetState(pet,currentLevel.getTerrainMap(),currentLevel.getEntityMap(),currentLevel.getObstacleMap(),player, currentLevel.getRiverMap());
        test.setActiveState(CPS);*/

       // Item Pet AI
        /*Skill pickpock = skillsFactory.getPickpocket();
        pet.setSkillLevel(pickpock,1000);
        ItemPetState IPS = new ItemPetState(pet,currentLevel.getTerrainMap(),currentLevel.getEntityMap(),currentLevel.getObstacleMap(),currentLevel.getItemMap(),player, pickpock, currentLevel.getRiverMap());
        test.setActiveState(IPS);
*/
        ShopKeeperFactory friendlyFactory = new ShopKeeperFactory(skillsFactory);
        Entity friendly = friendlyFactory.buildEntity();
        friendlyFactory.buildEntitySprite(friendly);

        ConsumableItem sin = new ConsumableItem("sin", new AddHealthCommand(20));
        sin.setCurrentLevelMessenger(currentLevelMessenger);
        sin.setPrice(2);
        friendly.addItemToInventory(sin);
        friendly.setMoveable(false);

        currentLevel.addEntityTo(new Point3D(0,-1,1),friendly);

        FriendlyAI friendlyAI = new FriendlyAI(friendly,currentLevel.getTerrainMap(),currentLevel.getEntityMap(),currentLevel.getObstacleMap(), currentLevel.getRiverMap());
        AIController best = new AIController();
        best.setActiveState(friendlyAI);


        AIList.add(best);
        AIList.add(test);
        AIList.add(controller);
        ConsumableItem potion1 = itemFactory.getManaPotion();
        potion1.notifyObserver(new Point3D(3, -3, 0));

        currentLevel.addItemTo(new Point3D(3, -3, 0), potion1);
        aiMap.put(currentLevel,AIList);

        levels.add(currentLevel);
    }


    public Level getCurrentLevel(){
        return currentLevel;
    }

    public List<Level> getLevels() {
        return levels;
    }

    public AIController getAIForEntity(Entity entity) {
        ArrayList<AIController> ais = (ArrayList)aiMap.get(currentLevel);
        for(int i = 0; i < ais.size(); ++i){
            if(ais.get(i).getEntity() == entity){
                return ais.get(i);
            }
        }
        try {
            throw new Exception("couldnt find ai for that entity");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addToTeleportQueue(TeleportEntityCommand teleportEntityCommand) {
        TeleportTuple tuple = new TeleportTuple(teleportEntityCommand.getEntity(),
                teleportEntityCommand.getDestinationLevel(), teleportEntityCommand.getDestinationPoint());

        teleportQueue.add(tuple);
    }

    public boolean entityIsPlayer(Entity entity) {
        return entity.equals(player);
    }

    public boolean hasLevels() {
        return !levels.isEmpty();
    }

    public boolean hasPlayer() {
        return player != null;
    }

    public boolean hasCurrentLevel() {
        return currentLevel != null;
    }

    public LevelMessenger getLevelMessenger() {
        return currentLevelMessenger;
    }

    public boolean isTeleportQueueEmpty() {
        return teleportQueue == null || teleportQueue.isEmpty();
    }

    public Queue<TeleportTuple> getTeleportQueue() {
        return teleportQueue;
    }

    public boolean isFailedQueueEmpty() {
        return failedTeleportQueue == null || failedTeleportQueue.isEmpty();
    }

    public Queue<TeleportTuple> getFailedQueue() {
        return failedTeleportQueue;
    }

    public void resetPlayer() {
        player.reset();
    }

    public class TeleportTuple {
        private Entity entity;
        private Level destLevel;
        private Point3D destinationPoint;

        public TeleportTuple(Entity entity, Level destLevel, Point3D destinationPoint) {
            this.entity = entity;
            this.destLevel = destLevel;
            this.destinationPoint = destinationPoint;
        }

        public Entity getEntity() {
            return entity;
        }

        public Level getDestLevel() {
            return destLevel;
        }

        public Point3D getDestinationPoint() {
            return destinationPoint;
        }

        public String entityToString() {
            return entity.toString();
        }

        public String levelToString() {
            return destLevel.toString();
        }
    }

    public void processTeleportQueue() {
        for(TeleportTuple tuple : teleportQueue) {
            changeLevels(tuple.getEntity(), tuple.getDestLevel(), tuple.getDestinationPoint());
        }

        teleportQueue.clear();

        teleportQueue.addAll(failedTeleportQueue);
    }

    private void changeLevels(Entity entity, Level destinationLevel, Point3D destinationPoint) {
        if(!destinationLevel.hasEntityAtPoint(destinationPoint)) {
            moveEntityToLevel(entity, destinationLevel, destinationPoint);

            if (entity.equals(player)) {
                changeCurrentLevelToDestination(destinationLevel);
            }
        } else {
            if(entity.equals(player)) {
                Point3D point3D = destinationLevel.findNearestOpenPointForEntity(destinationPoint);

                destinationLevel.moveEntityFromFirstPointToSecondPoint(destinationPoint, point3D, destinationLevel.getEntityAtPoint(destinationPoint));

                if(point3D != null) {
                    moveEntityToLevel(entity, destinationLevel, destinationPoint);
                    changeCurrentLevelToDestination(destinationLevel);
                }
            } else {
                failedTeleportQueue.add(new TeleportTuple(entity, destinationLevel, destinationPoint));
            }
        }
    }

    private void clearDeadAI(ArrayList<Entity> deadpool, List<AIController> currentAiMap) {
        if (currentAiMap != null) {
            Iterator<AIController> it = currentAiMap.iterator();
            while (it.hasNext()) {
                AIController ai = it.next();
                if (deadpool.contains(ai.getEntity())) {
                    it.remove();
                    aiMap.get(currentLevel).remove(ai);
                }
            }
        }
    }

    private void processDeadEntities(){
        ArrayList<Entity> deadpool = currentLevel.clearDeadEntities(player);
        List<AIController> AIMAP = aiMap.get(currentLevel);
        untargetDeadTargets(deadpool, currentLevel.getEntityMap());
        clearDeadAI(deadpool,AIMAP);
    }

    private void untargetDeadTargets(ArrayList<Entity> deadpool, BidiMap<Point3D, Entity> entityLocations) {
        Set<Entity> enitySet = entityLocations.getValueList();
        for (Entity deadEnt: deadpool){
            for (Entity entity: enitySet){
                if (entity.targets(deadEnt)){
                    entity.removeTarget(deadEnt);
                }
            }
        }
    }

    private void printEntHealth() {
        System.out.println();
        for (Entity e : currentLevel.getEntityMap().getValueList()) {
            System.out.println(e + " health is :\t" + e.getCurrentHealth());
        }
        System.out.println();
    }

    private void moveEntityToLevel(Entity entity, Level destinationLevel, Point3D destinationPoint) {
        List<LevelViewElement> observers = new ArrayList<>();

        observers.add(entity.getObserver());
        entity.notifyObservers(destinationPoint);

        currentLevel.removeEntityFrom(entity);
        currentLevel.removeObservers(observers);

        destinationLevel.addEntityTo(destinationPoint, entity);
        destinationLevel.addObservers(observers);

        if(entity.isMounted()) {
            Mount mount = entity.getMount();
            currentLevel.removeMount(mount);
            destinationLevel.addMountTo(destinationPoint, mount);
            mount.notifyObservers(destinationPoint);
        }
    }

    private void changeCurrentLevelToDestination(Level destinationLevel) {
        currentLevel = destinationLevel;
        currentLevelMessenger.setLevel(currentLevel);
        currentLevel.setMovementHandlerDialogCommand(currentLevelMessenger);
        // TODO: notify pets when player teleports, so we can teleport them as well
    }

    public void advance() {
        //printEntHealth();
        processDeadEntities();
        processAIMoves();
        currentLevel.advance();
        processTeleportQueue();

        if(hasPlayer()) {
            currentLevel.updateRenderLocations(getPlayerPosition(), player.getSight());
        }
    }

    private void processAIMoves(){

        if(aiMap.containsKey(currentLevel)) {
            List<AIController> aiControllers = aiMap.get(currentLevel);

            for (AIController AI : aiControllers) {
                AI.processMove();
            }
        }
    }

    public boolean playerIsDead() {
        return player.isDead();
    }

    public Point3D getPlayerPosition() {
        return currentLevel.getEntityPoint(player);
    }

    public void setPlayer(Entity player) {
        this.player = player;
    }

    public Entity getPlayer() {
        return player;
    }

    public void addLevel(Level level) {
        levels.add(level);
    }

    public void setCurrentLevel(Level level) {
        currentLevel = level;

        if(currentLevelMessenger != null) {
            currentLevelMessenger.setLevel(currentLevel);

        } else {
            if(gameModelMessenger == null) {
                throw new RuntimeException("GameModel's messenger not set!");
            }
            currentLevelMessenger = new LevelMessenger(gameModelMessenger, currentLevel);
        }

        currentLevel.setMovementHandlerDialogCommand(currentLevelMessenger);
    }

    public void accept(Visitor visitor) {
        visitor.visitGameModel(this);
    }
}
