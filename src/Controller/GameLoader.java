package Controller;

import Controller.Factories.PetAIFactory;
import Controller.Factories.SkillsFactory;
import Model.AI.AIController;
import Model.AI.HostileAI;
import Model.AI.PatrolPath;
import Model.AreaEffect.AreaEffect;
import Model.AreaEffect.InfiniteAreaEffect;
import Model.AreaEffect.OneShotAreaEffect;
import Model.Command.Command;
import Model.Command.EntityCommand.NonSettableCommand.InstaDeathCommand;
import Model.Command.EntityCommand.NonSettableCommand.LevelUpCommand;
import Model.Command.EntityCommand.NonSettableCommand.SendInfluenceEffectCommand;
import Model.Command.EntityCommand.NonSettableCommand.TeleportEntityCommand;
import Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand.ToggleableCommand;
import Model.Command.EntityCommand.SettableCommand.*;
import Model.Command.EntityCommand.SettableCommand.ToggleableCommand.ToggleSneaking;
import Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand.ToggleHealthCommand;
import Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand.ToggleManaCommand;
import Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand.ToggleSpeedCommand;
import Model.Condition.Condition;
import Model.Condition.HasItemCondition;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.*;
import Model.InfluenceEffect.AngularInfluenceEffect;
import Model.InfluenceEffect.InfluenceEffect;
import Model.InfluenceEffect.LinearInfluenceEffect;
import Model.InfluenceEffect.RadialInfluenceEffect;
import Model.Item.InteractiveItem;
import Model.Item.Item;
import Model.Item.OneShotItem;
import Model.Item.TakeableItem.*;
import Model.Level.*;
import View.LevelView.AreaEffectView;
import View.LevelView.EntityView.MonsterView;
import View.LevelView.EntityView.SmasherView;
import View.LevelView.EntityView.SneakView;
import View.LevelView.EntityView.SummonerView;
import View.LevelView.EntityView.*;
import View.LevelView.ItemView;
import com.sun.javafx.geom.Vec3d;
import javafx.geometry.Point3D;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class GameLoader {

    private Level currentLevel;
    private GameModel gameModel;
    private GameLoopMessenger gameLoopMessenger;
    private GameModelMessenger gameModelMessenger;
    private LevelMessenger levelMessenger;
    private List<Level> world;
    private Entity entity;
    private HashMap<String, Entity> entityRef = new HashMap<>();
    private HashMap<String, Item> itemRef = new HashMap<>();
    private HashMap<String, Level> levelRef = new HashMap<>();
    private HashMap<String, Command> commandRef = new HashMap<>();
    private HashMap<String, Skill> skillRef = new HashMap<>();
    private HashMap<String, InfluenceEffect> influenceRef = new HashMap<>();
    private LinkedList<GameModel.TeleportTuple> teleportTuples = new LinkedList<>();
    private LinkedList<GameModel.TeleportTuple> failedTuples = new LinkedList<>();
    private HashMap<Level, List<AIController>> aiMap = new HashMap<>();
    private Queue<ReferenceMap> needToSetMap = new LinkedList<ReferenceMap>();
    private PetAIFactory petAIFactory;
    private SkillsFactory skillsFactory;

    public GameLoader(GameLoop gameLoop) {
        world = new ArrayList<>();
        this.gameLoopMessenger = new GameLoopMessenger(gameLoop);
        gameModelMessenger = new GameModelMessenger(gameLoopMessenger, this.gameModel);
        levelMessenger = new LevelMessenger(gameModelMessenger, currentLevel);
        skillsFactory = new SkillsFactory(levelMessenger);
    }

    public GameLoader(GameLoopMessenger gameLoopMessenger) {
        world = new ArrayList<>();
        this.gameLoopMessenger = gameLoopMessenger;
        gameModelMessenger = new GameModelMessenger(gameLoopMessenger, this.gameModel);
        levelMessenger = new LevelMessenger(gameModelMessenger, currentLevel);
    }

    public void loadGame(String fileName) throws IOException, SAXException, ParserConfigurationException {
        File file = new File(fileName);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(file);

        loadGameModel(document);
    }

    private void loadMaps(NodeList nodeList, Level level) {
        for (int temp = 0; temp < nodeList.getLength(); temp++) {
            Node node = nodeList.item(temp);
            if(node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element)node;

                switch (element.getAttribute("id").toLowerCase()) {
                    case "terrain":
                        processTerrains(element, level);
                        break;

                    case "areaeffect":
                        processAreaEffects(element, level);
                        break;

                    case "influenceeffect":
                        processInfluenceEffects(element, level);
                        break;

                    case "item":
                        processItems(element, level);
                        break;

                    case "entity":
                        processEntities(element, level);
                        break;

                    case "trap":
                        processTraps(element, level);
                        break;

                    case "river":
                        processRivers(element, level);
                        break;

                    case "mount":
                        processMounts(element, level);
                        break;

                    case "obstacle":
                        processObstacles(element, level);
                        break;

                    case "decal":
                        processDecals(element, level);
                        break;
                }
            }
        }
    }

    private void processDecals(Element element, Level level) {
    }

    private void processObstacles(Element element, Level level) {
        List<Point3D> pointsToAdd = getKeyPoints(element);

        for(int i = 0; i < pointsToAdd.size(); i++) {
            level.addObstacleTo(pointsToAdd.get(i), new Obstacle());
        }
    }

    private void processMounts(Element element, Level level) {
        List<Point3D> pointsToAdd = getKeyPoints(element);
        List<Mount> mountsToAdd = new ArrayList<>();
        Orientation orientation;
        List<Terrain> terrains = new ArrayList<>();

        NodeList mountValues = element.getElementsByTagName("VALUE");
        for(int i = 0; i < mountValues.getLength(); i++) {
            NodeList mountNodes = mountValues.item(i).getChildNodes();

            for(int j = 0; j < mountNodes.getLength(); j++) {
                Node mountNode = mountNodes.item(j);
                if(mountNode.getNodeType() == Node.ELEMENT_NODE) {
                    Speed speed;
                    speed = new Speed(Integer.parseInt(mountNode.getAttributes().getNamedItem("speed").getTextContent()));
                    orientation = Orientation.toOrientation(mountNode.getAttributes().getNamedItem("orientation").getTextContent());
                    processTerrainList(element, terrains);

                    mountsToAdd.add(new Mount(orientation, speed, terrains));
                }
            }
        }

        for(int i = 0; i < pointsToAdd.size(); i++) {
            level.addMountTo(pointsToAdd.get(i), mountsToAdd.get(i));
        }
    }

    private void processTerrainList(Element element, List<Terrain> terrains) {
        NodeList terrainValues = element.getElementsByTagName("TERRAINLIST");
        for(int terrainIter = 0; terrainIter < terrainValues.getLength(); terrainIter++) {

            NodeList terrainNodes = terrainValues.item(terrainIter).getChildNodes();
            for(int j = 0; j < terrainNodes.getLength(); j++) {
                Node terrainNode = terrainNodes.item(j);
                if (terrainNode.getNodeType() == Node.ELEMENT_NODE) {
                    switch (terrainNode.getAttributes().getNamedItem("value").getTextContent().toLowerCase()) {
                        case "grass":
                            terrains.add(Terrain.GRASS);
                            break;

                        case "water":
                            terrains.add(Terrain.WATER);
                            break;

                        case "mountains":
                            terrains.add(Terrain.MOUNTAINS);
                            break;

                        default:
                            terrains.add(Terrain.NONE);
                    }
                }
            }
        }
    }

    private void processRivers(Element element, Level level) {
        List<Point3D> pointsToAdd = getKeyPoints(element);
        List<River> riversToAdd = new ArrayList<>();

        NodeList riverValues = element.getElementsByTagName("VALUE");
        for(int i = 0; i < riverValues.getLength(); i++) {
            NodeList riverNodes = riverValues.item(i).getChildNodes();

            for(int j = 0; j < riverNodes.getLength(); j++) {
                Node riverNode = riverNodes.item(j);
                if(riverNode.getNodeType() == Node.ELEMENT_NODE) {
                    Vec3d flow;
                    flow = toVector(riverNode.getAttributes().getNamedItem("flowRate").getTextContent());
                    riversToAdd.add(new River(flow));
                }
            }
        }

        for(int i = 0; i < pointsToAdd.size(); i++) {
            level.addRiverTo(pointsToAdd.get(i), riversToAdd.get(i));
        }
    }

    private void processTraps(Element element, Level level) {
        List<Point3D> pointsToAdd = getKeyPoints(element);
        List<Trap> traps = new ArrayList<>();
        boolean isVisible;
        boolean isDisarmed;
        int trapStrength;

        NodeList trapValues = element.getElementsByTagName("VALUE");
        for(int i = 0; i < trapValues.getLength(); i++) {
            NodeList trapNodes = trapValues.item(i).getChildNodes();

            for(int j = 0; j < trapNodes.getLength(); j++) {
                Node trapNode = trapNodes.item(j);
                if(trapNode.getNodeType() == Node.ELEMENT_NODE) {
                    Command command = processCommand(trapNode.getChildNodes());

                    if(command != null) {
                        isVisible = Boolean.parseBoolean(trapNode.getAttributes().getNamedItem("isVisible").getTextContent());
                        isDisarmed = Boolean.parseBoolean(trapNode.getAttributes().getNamedItem("isDisarmed").getTextContent());
                        trapStrength = Integer.parseInt(trapNode.getAttributes().getNamedItem("trapStrength").getTextContent());

                        traps.add(new Trap(command, isVisible, isDisarmed, trapStrength));
                    }
                }
            }
        }

        for(int i = 0; i < pointsToAdd.size(); i++) {
            level.addTrapTo(pointsToAdd.get(i), traps.get(i));
        }
    }

    private void processEntities(Element element, Level level) {
        List<Point3D> pointsToAdd = getKeyPoints(element);
        List<Entity> entitiesToAdd = new ArrayList<>();
        boolean moveable;
        int noise;
        int goldAmount;
        int maxGold;
        int attackPoints;
        int attackModifier;
        int defensePoints;
        int defenseModifier;
        long speedAmount;
        int manaPoints;
        int maxMana;
        int sight;
        int currentHealth;
        int maxHealth;
        int currentExperience;
        int levelAmount;
        int experienceToNextLevel;
        int regenRate;
        String reference;

        NodeList entityValues = element.getElementsByTagName("VALUE");
        for(int i = 0; i < entityValues.getLength(); i++) {
            NodeList entityNodes = entityValues.item(i).getChildNodes();

            for(int j = 0; j < entityNodes.getLength(); j++) {
                Node entityNode = entityNodes.item(j);
                if(entityNode.getNodeType() == Node.ELEMENT_NODE) {
                    reference = entityNode.getAttributes().getNamedItem("reference").getTextContent();

                    if(entityRef.containsKey(reference)) {
                        entitiesToAdd.add(entityRef.get(reference));
                    }

                    else {
                        ItemHotBar hotBar;
                        List<Skill> weaponSkills = new ArrayList<>();
                        List<Skill> nonWeaponSkills = new ArrayList<>();
                        HashMap<Skill, SkillLevel> skillLevelsMap = new HashMap<>();
                        Vec3d velocity;
                        NoiseLevel noiseLevel;
                        SightRadius sightRadius;
                        XPLevel xpLevel;
                        Health health;
                        Mana mana;
                        Speed speed;
                        Gold gold;
                        String name;
                        Attack attack;
                        Defense defense;
                        Equipment equipment;
                        Inventory inventory;
                        Orientation orientation;
                        List<Terrain> compatableTerrain = new ArrayList<>();
                        Mount mount;
                        ArrayList<Entity> friends = new ArrayList<>();
                        ArrayList<Entity> targets = new ArrayList<>();

                        name = entityNode.getAttributes().getNamedItem("name").getTextContent();

                        noise = Integer.parseInt(entityNode.getAttributes().getNamedItem("noiseLevel").getTextContent());
                        noiseLevel = new NoiseLevel(noise);

                        goldAmount = Integer.parseInt(entityNode.getAttributes().getNamedItem("goldAmount").getTextContent());
                        maxGold = Integer.parseInt(entityNode.getAttributes().getNamedItem("maxGold").getTextContent());
                        gold = new Gold(goldAmount, maxGold);

                        speedAmount = Integer.parseInt(entityNode.getAttributes().getNamedItem("speed").getTextContent());
                        speed = new Speed(speedAmount);

                        manaPoints = Integer.parseInt(entityNode.getAttributes().getNamedItem("manaPoints").getTextContent());
                        maxMana = Integer.parseInt(entityNode.getAttributes().getNamedItem("maxMana").getTextContent());
                        regenRate = Integer.parseInt(entityNode.getAttributes().getNamedItem("regenRate").getTextContent());
                        mana = new Mana(manaPoints, maxMana, regenRate);

                        attackPoints = Integer.parseInt(entityNode.getAttributes().getNamedItem("attackPoints").getTextContent());
                        attackModifier = Integer.parseInt(entityNode.getAttributes().getNamedItem("attackModifier").getTextContent());
                        attack = new Attack(attackPoints, attackModifier);

                        defensePoints = Integer.parseInt(entityNode.getAttributes().getNamedItem("defensePoints").getTextContent());
                        defenseModifier = Integer.parseInt(entityNode.getAttributes().getNamedItem("defenseModifier").getTextContent());
                        defense = new Defense(defensePoints, defenseModifier);

                        sight = Integer.parseInt(entityNode.getAttributes().getNamedItem("sightRadius").getTextContent());
                        sightRadius = new SightRadius(sight);

                        orientation = Orientation.toOrientation(entityNode.getAttributes().getNamedItem("orientation").getTextContent());

                        currentHealth = Integer.parseInt(entityNode.getAttributes().getNamedItem("currentHealth").getTextContent());
                        maxHealth = Integer.parseInt(entityNode.getAttributes().getNamedItem("maxHealth").getTextContent());
                        health = new Health(currentHealth, maxHealth);

                        currentExperience = Integer.parseInt(entityNode.getAttributes().getNamedItem("experience").getTextContent());
                        levelAmount = Integer.parseInt(entityNode.getAttributes().getNamedItem("level").getTextContent());
                        experienceToNextLevel = Integer.parseInt(entityNode.getAttributes().getNamedItem("experienceToNextLevel").getTextContent());
                        xpLevel = new XPLevel(levelAmount, currentExperience, experienceToNextLevel);

                        moveable = Boolean.parseBoolean(entityNode.getAttributes().getNamedItem("moveable").getTextContent());

                        velocity = toVector(entityNode.getAttributes().getNamedItem("velocity").getTextContent());

                        processTerrainList((Element) entityNode, compatableTerrain);
                        processWeaponSkillsList(entityNode.getChildNodes(), weaponSkills, skillLevelsMap);
                        processNonWeaponSkillsList(entityNode.getChildNodes(), nonWeaponSkills, skillLevelsMap);

                        mount = processEntityMount((Element) entityNode);
                        equipment = processEquipment((Element) entityNode);
                        hotBar = processHotBar((Element) entityNode);
                        inventory = processInventory((Element) entityNode);

                        Entity entity = new Entity(null, hotBar, weaponSkills, nonWeaponSkills, skillLevelsMap, velocity,
                                noiseLevel, sightRadius, xpLevel, health, mana, speed, gold, attack, defense, equipment,
                                inventory, orientation, compatableTerrain, moveable, mount, new ArrayList<>(), new ArrayList<>());

                        entity.setName(name);

                        processFriendsAndFoes(entityNode.getChildNodes(), entity);
                        inventory.setStrategies(entity, levelMessenger);
                        equipment.setStrategies(entity, levelMessenger);
                        hotBar.setStrategies(entity, levelMessenger);

                        entitiesToAdd.add(entity);
                        entityRef.put(reference, entity);
                    }
                }
            }
        }

        for(int i = 0; i < pointsToAdd.size(); i++) {
            Entity addingEnt = entitiesToAdd.get(i);

            for(Skill skill: addingEnt.getWeaponSkills()) {
                if(skill.getName().equalsIgnoreCase("one-handed")) {
                    if(addingEnt.getName().equals("McNugget")){
                        addingEnt.setObserver(new PetView(addingEnt, pointsToAdd.get(i)));
                    }
                    else addingEnt.setObserver(new SmasherView(addingEnt, pointsToAdd.get(i)));
                    break;
                }

                else if(skill.getName().equalsIgnoreCase("enchant")) {
                    addingEnt.setObserver(new SummonerView(addingEnt, pointsToAdd.get(i)));
                    break;
                }

                else if(skill.getName().equalsIgnoreCase("range")){
                    addingEnt.setObserver(new SneakView(addingEnt, pointsToAdd.get(i)));
                    break;
                }
            }

            if(addingEnt.getName() == "McNugget"){
                addingEnt.setObserver(new PetView(addingEnt, pointsToAdd.get(i)));
            }
            else if(addingEnt.getObserver() == null) {
                addingEnt.setObserver(new MonsterView(addingEnt, pointsToAdd.get(i)));
            }

            level.addEntityTo(pointsToAdd.get(i), entitiesToAdd.get(i));
        }
    }

    private void processFriendsAndFoes(NodeList nodeList, Entity entity) {
        String entityRef;
        for(int i = 0; i < nodeList.getLength(); i++) {
            NodeList listNodes = nodeList.item(i).getChildNodes();

            for(int j = 0; j < listNodes.getLength(); j++) {
                Node listNode = listNodes.item(j);

                if(listNode.getNodeType() == Node.ELEMENT_NODE) {
                    System.out.println(listNode.getNodeName());
                    switch (listNode.getNodeName()) {
                        case "FRIEND":
                            entityRef = listNode.getAttributes().getNamedItem("entityReference").getTextContent();
                            ReferenceMap referenceMap = new ReferenceMap(entityRef,"friend", entity);
                            this.needToSetMap.add(referenceMap);
                            break;

                        case "TARGET":
                            entityRef = listNode.getAttributes().getNamedItem("entityReference").getTextContent();
                            ReferenceMap targetMap = new ReferenceMap(entityRef,"target", entity);
                            this.needToSetMap.add(targetMap);
                            break;
                    }
                }
            }
        }
    }

    private Inventory processInventory(Element element) {
        List<TakeableItem> itemsToAdd = new ArrayList<>();
        String name;
        int maxSize = 0;
        String itemReference;
        int price;

        NodeList values = element.getElementsByTagName("Inventory");
        for(int i = 0; i < values.getLength(); i++) {

            NodeList nodes = values.item(i).getChildNodes();
            Node maxSizeNode = values.item(i);

            if(maxSizeNode.getNodeType() == Node.ELEMENT_NODE) {
                maxSize = Integer.parseInt(maxSizeNode.getAttributes().getNamedItem("maxSize").getTextContent());
            }

            for(int j = 0; j < nodes.getLength(); j++) {
                NodeList itemList = nodes.item(j).getChildNodes();

                for(int k = 0; k < itemList.getLength(); k++) {
                    Node itemNode = itemList.item(k);
                    if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                        Command command = processCommand(itemNode.getChildNodes());
                        name = itemNode.getAttributes().getNamedItem("name").getTextContent();
                        itemReference = itemNode.getAttributes().getNamedItem("reference").getTextContent();
                        price = Integer.parseInt(itemNode.getAttributes().getNamedItem("price").getTextContent());

                        if(itemRef.containsKey(itemReference)) {
                            itemsToAdd.add((TakeableItem) itemRef.get(itemReference));
                        }

                        else {
                            switch (itemNode.getNodeName().toLowerCase()) {
                                case "armoritem":
                                    ItemView armorItemView = new ItemView(new Point3D(0,0,0));
                                    int defense = Integer.parseInt(itemNode.getAttributes().getNamedItem("defense").getTextContent());
                                    ArmorItem armorItem = new ArmorItem(name, (ToggleableCommand) command, defense);
                                    armorItem.setCurrentLevelMessenger(levelMessenger);
                                    armorItem.setPrice(price);
                                    armorItem.setObserver(armorItemView);
                                    if(defense > 15) {
                                        armorItemView.setHeavyArmor();
                                    }

                                    else if(defense > 10) {
                                        armorItemView.setMediumArmor();
                                    }

                                    else
                                        armorItemView.setLightArmor();

                                    itemsToAdd.add(armorItem);
                                    itemRef.put(itemReference, armorItem);
                                    break;

                                case "consumableitem":
                                    ItemView consumableItemView = new ItemView(new Point3D(0,0,0));
                                    ConsumableItem consumableItem = new ConsumableItem(name, command);
                                    consumableItem.setCurrentLevelMessenger(levelMessenger);
                                    consumableItem.setPrice(price);
                                    consumableItem.setObserver(consumableItemView);
                                    itemsToAdd.add(consumableItem);
                                    consumableItemView.setPotion();
                                    itemRef.put(itemReference, consumableItem);
                                    break;

                                case "ringitem":
                                    ItemView ringItemView = new ItemView(new Point3D(0,0,0));
                                    RingItem ringItem = new RingItem(name, (ToggleableCommand) command);
                                    ringItem.setCurrentLevelMessenger(levelMessenger);
                                    ringItem.setPrice(price);
                                    ringItem.setObserver(ringItemView);
                                    ringItemView.setHealthRing();
                                    itemsToAdd.add(ringItem);
                                    itemRef.put(itemReference, ringItem);
                                    break;

                                case "weaponitem": // TODO: this needs to be changed
                                    Skill weaponSkill;
                                    InfluenceEffect influenceEffect;
                                    int damage;
                                    int speed;
                                    int accuracy;
                                    int useCost;
                                    int range;

                                    damage = Integer.parseInt(itemNode.getAttributes().getNamedItem("damage").getTextContent());
                                    speed = Integer.parseInt(itemNode.getAttributes().getNamedItem("speed").getTextContent());
                                    accuracy = Integer.parseInt(itemNode.getAttributes().getNamedItem("accuracy").getTextContent());
                                    useCost = Integer.parseInt(itemNode.getAttributes().getNamedItem("useCost").getTextContent());
                                    range = Integer.parseInt(itemNode.getAttributes().getNamedItem("range").getTextContent());

                                    influenceEffect = processWeaponInfluenceEffect(itemNode.getChildNodes());
                                    weaponSkill = processSkill(itemNode.getChildNodes());

                                    WeaponItem weaponItem = new WeaponItem(name, (SettableCommand) command, weaponSkill, influenceEffect, damage, speed, accuracy, useCost, range);
                                    weaponItem.setCurrentLevelMessenger(levelMessenger);
                                    weaponItem.setPrice(price);
                                    ItemView weaponView = new ItemView(new Point3D(0,0,0));
                                    weaponItem.setObserver(weaponView);

                                    switch (weaponSkill.getName()) {
                                        case "One-Handed":
                                            weaponView.setOneHandedSword();
                                            break;
                                        case "Two-Handed":
                                            weaponView.setTwoHandedWeapon();
                                            break;
                                        case "Brawler":
                                            weaponView.setBrawlerWeapon();
                                            break;
                                        case "Staff":
                                            weaponView.setStaff();
                                            break;
                                        case "Range":
                                            weaponView.setRangedWeapon();
                                            break;
                                    }
                                    itemsToAdd.add(weaponItem);
                                    itemRef.put(itemReference, weaponItem);
                                    break;
                            }
                        }
                    }
                }
            }
        }

        return new Inventory(itemsToAdd, maxSize);
    }

    private ItemHotBar processHotBar(Element element) {
        ItemHotBar itemHotBar = new ItemHotBar();
        HashMap<Integer, TakeableItem> itemMap = new HashMap<>();
        String name;
        int index = -1;
        String reference;
        int price;

        NodeList itemVals = element.getElementsByTagName("ItemHotBar");
        for(int i = 0; i < itemVals.getLength(); i++) {
            NodeList itemNodes = itemVals.item(i).getChildNodes();

            for(int j = 0; j < itemNodes.getLength(); j++) {
                NodeList invNodes = itemNodes.item(j).getChildNodes();

                for(int k = 0; k < invNodes.getLength(); k++) {
                    Node itemNode = invNodes.item(k);

                    if(itemNode.getNodeType() == Node.ELEMENT_NODE) {
                        if(itemNode.getNodeName().equalsIgnoreCase("INTEGERKEY")) {
                            index = Integer.parseInt(itemNode.getAttributes().getNamedItem("key").getTextContent());
                        }

                        else {
                            Command command = processCommand(itemNode.getChildNodes());
                            name = itemNode.getAttributes().getNamedItem("name").getTextContent();
                            reference = itemNode.getAttributes().getNamedItem("reference").getTextContent();
                            price = Integer.parseInt(itemNode.getAttributes().getNamedItem("price").getTextContent());

                            if(command != null && !name.isEmpty() && index != -1) {
                                switch (itemNode.getNodeName().toLowerCase()) {
                                    case "armoritem":
                                        if(itemRef.containsKey(reference)) {
                                            itemHotBar.addItem((ArmorItem)itemRef.get(reference), index);
                                        }

                                        else {
                                            int defense = Integer.parseInt(itemNode.getAttributes().getNamedItem("defense").getTextContent());
                                            ArmorItem armorItem = new ArmorItem(name, (ToggleableCommand) command, defense);
                                            armorItem.setPrice(price);
                                            ItemView armorItemView = new ItemView(new Point3D(0,0,0));
                                            armorItem.setObserver(armorItemView);

                                            if(defense > 15) {
                                                armorItemView.setHeavyArmor();
                                            }

                                            else if(defense > 10) {
                                                armorItemView.setMediumArmor();
                                            }

                                            else
                                                armorItemView.setLightArmor();

                                            itemHotBar.addItem(armorItem, index);
                                            itemRef.put(reference, armorItem);
                                        }
                                        break;

                                    case "consumableitem":
                                        if(itemRef.containsKey(reference)) {
                                            itemHotBar.addItem((ConsumableItem)itemRef.get(reference), index);
                                        }

                                        else {
                                            ConsumableItem consumableItem = new ConsumableItem(name, command);
                                            consumableItem.setPrice(price);
                                            ItemView consumableItemView = new ItemView(new Point3D(0,0,0));
                                            consumableItem.setObserver(consumableItemView);
                                            consumableItemView.setPotion();
                                            itemHotBar.addItem(consumableItem, index);
                                            itemRef.put(reference, consumableItem);
                                        }
                                        break;

                                    case "ringitem":
                                        if(itemRef.containsKey(reference)) {
                                            itemHotBar.addItem((RingItem)itemRef.get(reference), index);
                                        }

                                        else {
                                            RingItem ringItem = new RingItem(name, (ToggleableCommand) command);
                                            ringItem.setPrice(price);
                                            ItemView ringItemView = new ItemView(new Point3D(0,0,0));
                                            ringItem.setObserver(ringItemView);
                                            ringItemView.setHealthRing();
                                            itemHotBar.addItem(ringItem, index);
                                            itemRef.put(reference, ringItem);
                                        }
                                        break;

                                    case "weaponitem": //TODO: this needs to be changed
                                        if(itemRef.containsKey(reference)) {
                                            itemHotBar.addItem((WeaponItem)itemRef.get(reference), index);
                                        }

                                        else {
                                            Skill weaponSkill;
                                            InfluenceEffect influenceEffect;
                                            int damage;
                                            int speed;
                                            int accuracy;
                                            int useCost;
                                            int range;

                                            damage = Integer.parseInt(itemNode.getAttributes().getNamedItem("damage").getTextContent());
                                            speed = Integer.parseInt(itemNode.getAttributes().getNamedItem("speed").getTextContent());
                                            accuracy = Integer.parseInt(itemNode.getAttributes().getNamedItem("accuracy").getTextContent());
                                            useCost = Integer.parseInt(itemNode.getAttributes().getNamedItem("useCost").getTextContent());
                                            range = Integer.parseInt(itemNode.getAttributes().getNamedItem("range").getTextContent());

                                            influenceEffect = processWeaponInfluenceEffect(itemNode.getChildNodes());
                                            weaponSkill = processSkill(itemNode.getChildNodes());

                                            WeaponItem weaponItem = new WeaponItem(name, (SettableCommand) command, weaponSkill, influenceEffect, damage, speed, accuracy, useCost, range);
                                            weaponItem.setPrice(price);

                                            ItemView weaponView = new ItemView(new Point3D(0,0,0));

                                            switch (weaponSkill.getName()){
                                                case "One-Handed":
                                                    weaponView.setOneHandedSword();
                                                    break;
                                                case "Two-Handed":
                                                    weaponView.setTwoHandedWeapon();
                                                    break;
                                                case "Brawler":
                                                    weaponView.setBrawlerWeapon();
                                                    break;
                                                case "Staff":
                                                    weaponView.setStaff();
                                                    break;
                                                case "Range":
                                                    weaponView.setRangedWeapon();
                                                    break;
                                            }
                                            weaponItem.setObserver(weaponView);

                                            itemHotBar.addItem(weaponItem, index);
                                            itemRef.put(reference, weaponItem);
                                        }
                                        break;
                                }
                            }
                        }
                    }
                }
            }
        }

        return itemHotBar;
    }

    private Equipment processEquipment(Element element) {
        WeaponItem weaponItem = null;
        RingItem ringItem = null;
        ArmorItem armorItem = null;
        String name;
        Command command;
        String reference;
        int price;

        NodeList equipValues = element.getElementsByTagName("Equipment");
        for(int i = 0; i < equipValues.getLength(); i++) {
            NodeList equipNodes = equipValues.item(i).getChildNodes();

            for(int j = 0; j < equipNodes.getLength(); j++) {
                Node equipNode = equipNodes.item(j);

                if(equipNode.getNodeType() == Node.ELEMENT_NODE) {
                    command = processCommand(equipNode.getChildNodes());
                    name = equipNode.getAttributes().getNamedItem("name").getTextContent();
                    reference = equipNode.getAttributes().getNamedItem("reference").getTextContent();
                    price = Integer.parseInt(equipNode.getAttributes().getNamedItem("price").getTextContent());

                    switch (equipNode.getNodeName().toLowerCase()) {
                        case "armoritem":
                            if(itemRef.containsKey(reference)) {
                                armorItem = (ArmorItem)itemRef.get(reference);
                            }

                            else {
                                int defense = Integer.parseInt(equipNode.getAttributes().getNamedItem("defense").getTextContent());
                                armorItem = new ArmorItem(name, (ToggleableCommand) command, defense);

                                ItemView armorItemView = new ItemView(new Point3D(0,0,0));
                                if(defense > 15) {
                                    armorItemView.setHeavyArmor();
                                }

                                else if(defense > 10) {
                                    armorItemView.setMediumArmor();
                                }

                                else
                                    armorItemView.setLightArmor();

                                armorItem.setObserver(armorItemView);
                                armorItem.setPrice(price);
                                itemRef.put(reference, armorItem);
                            }
                            break;

                        case "ringitem":
                            if(itemRef.containsKey(reference)) {
                                ringItem = (RingItem)itemRef.get(reference);
                            }

                            else {
                                ringItem = new RingItem(name, (ToggleableCommand) command);
                                ringItem.setPrice(price);
                                ItemView ringItemView = new ItemView(new Point3D(0,0,0));
                                ringItemView.setHealthRing();
                                ringItem.setObserver(ringItemView);
                                itemRef.put(reference, ringItem);
                            }
                            break;

                        case "weaponitem":
                            if(itemRef.containsKey(reference)) {
                                weaponItem = (WeaponItem)itemRef.get(reference);
                            }

                            else {
                                Skill weaponSkill;
                                InfluenceEffect influenceEffect;
                                int damage;
                                int speed;
                                int accuracy;
                                int useCost;
                                int range;

                                damage = Integer.parseInt(equipNode.getAttributes().getNamedItem("damage").getTextContent());
                                speed = Integer.parseInt(equipNode.getAttributes().getNamedItem("speed").getTextContent());
                                accuracy = Integer.parseInt(equipNode.getAttributes().getNamedItem("accuracy").getTextContent());
                                useCost = Integer.parseInt(equipNode.getAttributes().getNamedItem("useCost").getTextContent());
                                range = Integer.parseInt(equipNode.getAttributes().getNamedItem("range").getTextContent());

                                influenceEffect = processWeaponInfluenceEffect(equipNode.getChildNodes());
                                weaponSkill = processSkill(equipNode.getChildNodes());

                                weaponItem = new WeaponItem(name, (SettableCommand) command, weaponSkill, influenceEffect, damage, speed, accuracy, useCost, range);
                                weaponItem.setPrice(price);

                                ItemView weaponView = new ItemView(new Point3D(0,0,0));

                                switch (weaponSkill.getName()){
                                    case "One-Handed":
                                        weaponView.setOneHandedSword();
                                        break;
                                    case "Two-Handed":
                                        weaponView.setTwoHandedWeapon();
                                        break;
                                    case "Brawler":
                                        weaponView.setBrawlerWeapon();
                                        break;
                                    case "Staff":
                                        weaponView.setStaff();
                                        break;
                                    case "Range":
                                        weaponView.setRangedWeapon();
                                        break;
                                }

                                weaponItem.setObserver(weaponView);
                                itemRef.put(reference, weaponItem);
                            }
                            break;
                    }
                }
            }
        }

        return new Equipment(weaponItem, armorItem, ringItem);
    }

    private Mount processEntityMount(Element element) {
        Speed speed;
        Orientation orientation;
        List<Terrain> terrains = new ArrayList<>();

        NodeList mountValues = element.getElementsByTagName("Mount");
        for(int i = 0; i < mountValues.getLength(); i++) {
            Node mountNode = mountValues.item(i);
            if(mountNode.getNodeType() == Node.ELEMENT_NODE) {
                speed = new Speed(Integer.parseInt(mountNode.getAttributes().getNamedItem("speed").getTextContent()));
                orientation = Orientation.toOrientation(mountNode.getAttributes().getNamedItem("orientation").getTextContent());
                processTerrainList((Element)mountNode, terrains);
                return new Mount(orientation, speed, terrains);
            }
        }

        return null;
    }

    private void processNonWeaponSkillsList(NodeList skillValues, List<Skill> skills, HashMap<Skill, SkillLevel> skillLevelMap) {
        Command command;
        InfluenceEffect influenceEffect = null;
        SendInfluenceEffectCommand sendInfluenceEffectCommand = null;

        String name;
        int useCost;
        int accuracy;
        int skillLevelAmount;
        String skillReference;

        for(int i = 0; i < skillValues.getLength(); i++) {
            NodeList skillNodes = skillValues.item(i).getChildNodes();
            Node weaponListNode = skillValues.item(i);

            if(weaponListNode.getNodeType() == Node.ELEMENT_NODE) {
                if (weaponListNode.getNodeName().equalsIgnoreCase("NONWEAPONSKILLS")) {
                    for (int j = 0; j < skillNodes.getLength(); j++) {
                        Node skillNode = skillNodes.item(j);
                        if (skillNode.getNodeType() == Node.ELEMENT_NODE && skillNode.getNodeName().contains("Skill")) {
                            command = processCommand(skillNode.getChildNodes());
                            influenceEffect = processInfluenceEffect(skillNode.getChildNodes());
                            skillReference = skillNode.getAttributes().getNamedItem("reference").getTextContent();

                            Skill newSkill = null;
                            SkillLevel skillLevel;

                            if (skillRef.containsKey(skillReference)) {
                                skills.add(skillRef.get(skillReference));
                            } else if (command != null) {
                                name = skillNode.getAttributes().getNamedItem("name").getTextContent();
                                switch (name){
                                    case "Observe":
                                        newSkill = skillsFactory.getObserveSkill();
                                        break;
                                    case "Bind Wounds":
                                        newSkill = skillsFactory.getBindWounds();
                                        break;
                                    case "Bargain":
                                        newSkill = skillsFactory.getBargainSkill();
                                        break;
                                    case "One-Handed":
                                        newSkill = skillsFactory.getOneHandedSkill();
                                        break;
                                    case "Two-Handed":
                                        newSkill = skillsFactory.getTwoHandedSkill();
                                        break;
                                    case "Brawler":
                                        newSkill = skillsFactory.getBrawlerSkill();
                                        break;
                                    case "Enchant":
                                        newSkill = skillsFactory.getEnchantSkill();
                                        break;
                                    case "Boon":
                                        newSkill = skillsFactory.getBoonSkill();
                                        break;
                                    case "Bane":
                                        newSkill = skillsFactory.getBaneSkill();
                                        break;
                                    case "Staff":
                                        newSkill = skillsFactory.getStaffSkill();
                                        break;
                                    case "Sneak":
                                        newSkill = skillsFactory.getSneakSkill();
                                        break;
                                    case "Detect and Remove Trap":
                                        newSkill = skillsFactory.DisarmTrapSkill();
                                        break;
                                    case "Pickpocket":
                                        newSkill = skillsFactory.getPickpocket();
                                        break;
                                    case "Range":
                                        newSkill = skillsFactory.getRangeSkill();
                                        break;
                                }

                                skills.add(newSkill);

                                skillLevelAmount = Integer.parseInt(skillNode.getAttributes().getNamedItem("level").getTextContent());
                                skillLevel = new SkillLevel(skillLevelAmount);
                                skillLevelMap.put(newSkill, skillLevel);
                                skillRef.put(skillReference, newSkill);
                            }
                        }
                    }
                }
            }
        }
    }

    private void processWeaponSkillsList(NodeList skillValues, List<Skill> skills, HashMap<Skill, SkillLevel> skillLevelMap) {
        Command command;
        InfluenceEffect influenceEffect = null;
        SendInfluenceEffectCommand sendInfluenceEffectCommand = null;
        String skillReference;

        String name;
        int useCost;
        int accuracy;
        int skillLevelAmount;

        for(int i = 0; i < skillValues.getLength(); i++) {

            NodeList skillNodes = skillValues.item(i).getChildNodes();
            Node weaponListNode = skillValues.item(i);

            if(weaponListNode.getNodeType() == Node.ELEMENT_NODE) {
                if(!weaponListNode.getNodeName().contains("NONWEAPONSKILL")) {
                    for (int j = 0; j < skillNodes.getLength(); j++) {
                        Node skillNode = skillNodes.item(j);
                        if (skillNode.getNodeType() == Node.ELEMENT_NODE && skillNode.getNodeName().contains("Skill")) {
                            Skill newSkill = null;
                            SkillLevel skillLevel;
                            command = processCommand(skillNode.getChildNodes());
                            influenceEffect = processInfluenceEffect(skillNode.getChildNodes());
                            skillReference = skillNode.getAttributes().getNamedItem("reference").getTextContent();

                            if (skillRef.containsKey(skillReference)) {
                                skills.add(skillRef.get(skillReference));
                            } else if (command != null) {
                                name = skillNode.getAttributes().getNamedItem("name").getTextContent();
                                switch (name){
                                    case "Observe":
                                        newSkill = skillsFactory.getObserveSkill();
                                        break;
                                    case "Bind Wounds":
                                        newSkill = skillsFactory.getBindWounds();
                                        break;
                                    case "Bargain":
                                        newSkill = skillsFactory.getBargainSkill();
                                        break;
                                    case "One-Handed":
                                        newSkill = skillsFactory.getOneHandedSkill();
                                        break;
                                    case "Two-Handed":
                                        newSkill = skillsFactory.getTwoHandedSkill();
                                        break;
                                    case "Brawler":
                                        newSkill = skillsFactory.getBrawlerSkill();
                                        break;
                                    case "Enchant":
                                        newSkill = skillsFactory.getEnchantSkill();
                                        break;
                                    case "Boon":
                                        newSkill = skillsFactory.getBoonSkill();
                                        break;
                                    case "Bane":
                                        newSkill = skillsFactory.getBaneSkill();
                                        break;
                                    case "Staff":
                                        newSkill = skillsFactory.getStaffSkill();
                                        break;
                                    case "Sneak":
                                        newSkill = skillsFactory.getSneakSkill();
                                        break;
                                    case "Detect and Remove Trap":
                                        newSkill = skillsFactory.DisarmTrapSkill();
                                        break;
                                    case "Pickpocket":
                                        newSkill = skillsFactory.getPickpocket();
                                        break;
                                    case "Range":
                                        newSkill = skillsFactory.getRangeSkill();
                                        break;
                                }

                                skills.add(newSkill);

                                skillLevelAmount = Integer.parseInt(skillNode.getAttributes().getNamedItem("level").getTextContent());
                                skillLevel = new SkillLevel(skillLevelAmount);
                                skillLevelMap.put(newSkill, skillLevel);
                                skillRef.put(skillReference, newSkill);
                            }
                        }
                    }
                }
            }
        }
    }

    private InfluenceEffect processInfluenceEffect(NodeList childNodes) {
        Command command;
        int movesRemaining;
        long speed;
        int range;
        Orientation orientation;

        for(int i = 0; i < childNodes.getLength(); i++) {
            Node influenceNode = childNodes.item(i);

            if(influenceNode.getNodeType() == Node.ELEMENT_NODE) {
                command = processCommand(influenceNode.getChildNodes());
                if(command != null) {
                    Point3D point3D;
                    movesRemaining =  Integer.parseInt(influenceNode.getAttributes().getNamedItem("movesRemaining").getTextContent());
                    speed = Long.parseLong(influenceNode.getAttributes().getNamedItem("speed").getTextContent());
                    range = Integer.parseInt(influenceNode.getAttributes().getNamedItem("range").getTextContent());
                    orientation = Orientation.toOrientation(influenceNode.getAttributes().getNamedItem("orientation").getTextContent());
                    point3D = toPoint3D(influenceNode.getAttributes().getNamedItem("point").getTextContent());

                    switch (influenceNode.getNodeName().toLowerCase()) {
                        case "angularinfluenceeffect":
                            return new AngularInfluenceEffect((SettableCommand) command, range, speed, orientation, movesRemaining, 0,point3D);

                        case "linearinfluenceeffect":
                            return new LinearInfluenceEffect((SettableCommand) command, range, speed, orientation, movesRemaining, 0,point3D);

                        case "radialinfluenceeffect":
                            return new RadialInfluenceEffect((SettableCommand) command, range, speed, orientation, movesRemaining, 0,point3D);
                    }
                }
            }
        }

        return null;
    }

    private void processItems(Element element, Level level) {
        List<Point3D> pointsToAdd = getKeyPoints(element);
        List<Item> itemsToAdd = new ArrayList<>();
        String name;
        String reference;

        NodeList itemValues = element.getElementsByTagName("VALUE");
        for (int i = 0; i < itemValues.getLength(); i++) {
            NodeList itemNodes = itemValues.item(i).getChildNodes();

            for(int j = 0; j < itemNodes.getLength(); j++) {
                Node itemNode = itemNodes.item(j);
                if(itemNode.getNodeType() == Node.ELEMENT_NODE) {
                    reference = itemNode.getAttributes().getNamedItem("reference").getTextContent();

                    if(itemRef.containsKey(reference)) {
                        itemsToAdd.add(itemRef.get(reference));
                    }

                    else {
                        Command command = processCommand(itemNode.getChildNodes());
                        Condition condition = processCondition(itemNode.getChildNodes());

                        if (command != null) {
                            name = itemNode.getAttributes().getNamedItem("name").getTextContent();
                            ItemView itemView = new ItemView(new Point3D(0,0,0));
                            switch (itemNode.getNodeName().toLowerCase()) {
                                case "oneshotitem":
                                    OneShotItem oneShotItem = new OneShotItem(name, command);
                                    itemView.setPotion();
                                    oneShotItem.setObserver(itemView);
                                    itemsToAdd.add(oneShotItem);
                                    itemRef.put(reference,oneShotItem);
                                    break;

                                case "interactiveitem":
                                    InteractiveItem interactiveItem = new InteractiveItem(name, command, condition);
                                    itemsToAdd.add(interactiveItem);
                                    itemView.setDoor();
                                    interactiveItem.setObserver(itemView);
                                    itemRef.put(reference, interactiveItem);
                                    break;

                                case "armoritem":
                                    int defense = Integer.parseInt(itemNode.getAttributes().getNamedItem("defense").getTextContent());
                                    ArmorItem armorItem = new ArmorItem(name, (ToggleableCommand) command, defense);
                                    armorItem.setCurrentLevelMessenger(levelMessenger);
                                    armorItem.setObserver(new ItemView(new Point3D(0,0,0)));
                                    System.out.println("LOAD ITEM");
                                    if(defense > 15) {
                                        itemView.setHeavyArmor();
                                    }

                                    else if(defense > 10) {
                                        itemView.setMediumArmor();
                                    }

                                    else
                                        itemView.setLightArmor();

                                    armorItem.setObserver(itemView);
                                    itemsToAdd.add(armorItem);
                                    itemRef.put(reference, armorItem);
                                    break;

                                case "consumableitem":
                                    ConsumableItem consumableItem = new ConsumableItem(name, command);
                                    consumableItem.setCurrentLevelMessenger(levelMessenger);
                                    consumableItem.setObserver(new ItemView(new Point3D(0,0,0)));
                                    itemView.setPotion();
                                    consumableItem.setObserver(itemView);
                                    itemsToAdd.add(consumableItem);
                                    itemRef.put(reference, consumableItem);
                                    break;

                                case "ringitem":
                                    RingItem ringItem = new RingItem(name, (ToggleableCommand) command);
                                    ringItem.setCurrentLevelMessenger(levelMessenger);
                                    ringItem.setObserver(new ItemView(new Point3D(0,0,0)));
                                    itemView.setHealthRing();
                                    ringItem.setObserver(itemView);
                                    itemsToAdd.add(ringItem);
                                    itemRef.put(reference, ringItem);
                                    break;

                                case "weaponitem":
                                    Skill weaponSkill;
                                    InfluenceEffect influenceEffect;
                                    int damage;
                                    int speed;
                                    int accuracy;
                                    int useCost;
                                    int range;

                                    damage = Integer.parseInt(itemNode.getAttributes().getNamedItem("damage").getTextContent());
                                    speed = Integer.parseInt(itemNode.getAttributes().getNamedItem("speed").getTextContent());
                                    accuracy = Integer.parseInt(itemNode.getAttributes().getNamedItem("accuracy").getTextContent());
                                    useCost = Integer.parseInt(itemNode.getAttributes().getNamedItem("useCost").getTextContent());
                                    range = Integer.parseInt(itemNode.getAttributes().getNamedItem("range").getTextContent());

                                    influenceEffect = processWeaponInfluenceEffect(itemNode.getChildNodes());
                                    weaponSkill = processSkill(itemNode.getChildNodes());

                                    WeaponItem weaponItem = new WeaponItem(name, (SettableCommand) command, weaponSkill, influenceEffect, damage, speed, accuracy, useCost, range);
                                    weaponItem.setCurrentLevelMessenger(levelMessenger);
                                    switch (weaponSkill.getName()){
                                        case "One-Handed":
                                            itemView.setOneHandedSword();
                                            break;
                                        case "Two-Handed":
                                            itemView.setTwoHandedWeapon();
                                            break;
                                        case "Brawler":
                                            itemView.setBrawlerWeapon();
                                            break;
                                        case "Staff":
                                            itemView.setStaff();
                                            break;
                                        case "Range":
                                            itemView.setRangedWeapon();
                                            break;
                                    }
                                    weaponItem.setObserver(itemView);
                                    itemsToAdd.add(weaponItem);
                                    itemRef.put(reference, weaponItem);
                                    break;
                            }
                        }
                    }
                }
            }
        }

        for(int i = 0; i < pointsToAdd.size(); i++) {
            itemsToAdd.get(i).notifyObserver(pointsToAdd.get(i));
            level.addItemTo(pointsToAdd.get(i), itemsToAdd.get(i));
        }
    }

    private Condition processCondition(NodeList childNodes) {
        String name;

        for(int i = 0; i < childNodes.getLength(); i++) {
            Node conNode = childNodes.item(i);

            if(conNode.getNodeType() == Node.ELEMENT_NODE && conNode.getNodeName().contains("Condition")) {
                name = conNode.getAttributes().getNamedItem("itemName").getTextContent();

                switch (conNode.getNodeName()) {
                    case "HasItemCondition":
                        return new HasItemCondition(name);
                }
            }
        }

        return null;
    }

    private InfluenceEffect processWeaponInfluenceEffect(NodeList childNodes) {
        Command command;
        int movesRemaining;
        long speed;
        int range;
        Orientation orientation;

        for(int i = 0; i < childNodes.getLength(); i++) {
            Node influenceNode = childNodes.item(i);

            if(influenceNode.getNodeType() == Node.ELEMENT_NODE) {
                if (influenceNode.getNodeName().contains("InfluenceEffect")) {
                    command = processCommand(influenceNode.getChildNodes());
                    if (command != null) {
                        movesRemaining = Integer.parseInt(influenceNode.getAttributes().getNamedItem("movesRemaining").getTextContent());
                        speed = Long.parseLong(influenceNode.getAttributes().getNamedItem("speed").getTextContent());
                        range = Integer.parseInt(influenceNode.getAttributes().getNamedItem("range").getTextContent());
                        orientation = Orientation.toOrientation(influenceNode.getAttributes().getNamedItem("orientation").getTextContent());
                        Point3D point3D = toPoint3D(influenceNode.getAttributes().getNamedItem("point").getTextContent());

                        switch (influenceNode.getNodeName().toLowerCase()) {
                            case "angularinfluenceeffect":
                                return new AngularInfluenceEffect((SettableCommand) command, range, speed, orientation, movesRemaining, 0,point3D);

                            case "linearinfluenceeffect":
                                return new LinearInfluenceEffect((SettableCommand) command, range, speed, orientation, movesRemaining, 0,point3D);

                            case "radialinfluenceeffect":
                                return new RadialInfluenceEffect((SettableCommand) command, range, speed, orientation, movesRemaining, 0,point3D);
                        }
                    }
                }
            }
        }

        return null;
    }

    private Skill processSkill(NodeList childNodes) {
        InfluenceEffect influenceEffect;
        Command command;
        String name;
        int useCost;
        int accuracy;
        String reference;
        Skill skill;

        for(int i = 0; i < childNodes.getLength(); i++) {
            Node influenceNode = childNodes.item(i);

            if(influenceNode.getNodeType() == Node.ELEMENT_NODE) {
                if (influenceNode.getNodeName().contains("Skill")) {
                    name = influenceNode.getAttributes().getNamedItem("name").getTextContent();
                    reference = influenceNode.getAttributes().getNamedItem("reference").getTextContent();
                    if(skillRef.containsKey(reference)) {
                        return skillRef.get(reference);
                    }
                    else switch (name){
                        case "Observe":
                            return skillsFactory.getObserveSkill();
                        case "Bind Wounds":
                            return skillsFactory.getBindWounds();
                        case "Bargain":
                            return skillsFactory.getBargainSkill();
                        case "One-Handed":
                            return skillsFactory.getOneHandedSkill();
                        case "Two-Handed":
                            return skillsFactory.getTwoHandedSkill();
                        case "Brawler":
                            return skillsFactory.getBrawlerSkill();
                        case "Enchant":
                            return skillsFactory.getEnchantSkill();
                        case "Boon":
                            return skillsFactory.getBoonSkill();
                        case "Bane":
                            return skillsFactory.getBaneSkill();
                        case "Staff":
                            return skillsFactory.getStaffSkill();
                        case "Sneak":
                            return skillsFactory.getSneakSkill();
                        case "Detect and Remove Trap":
                            return skillsFactory.DisarmTrapSkill();
                        case "Pickpocket":
                            return skillsFactory.getPickpocket();
                        case "Range":
                            return skillsFactory.getRangeSkill();
                    }


                    command = processCommand(influenceNode.getChildNodes());
                    influenceEffect = processInfluenceEffect(influenceNode.getChildNodes());
                    accuracy = Integer.parseInt(influenceNode.getAttributes().getNamedItem("accuracy").getTextContent());
                    useCost = Integer.parseInt(influenceNode.getAttributes().getNamedItem("useCost").getTextContent());



                     if(command != null) {
                        skill = new Skill(name, influenceEffect, (SettableCommand) command, new SendInfluenceEffectCommand(levelMessenger),accuracy, useCost);
                        skillRef.put(reference, skill);
                        return skill;
                    }
                }
            }
        }

        for(int i = 0; i < childNodes.getLength(); i++) {

            NodeList skillNodes = childNodes.item(i).getChildNodes();
            for(int j = 0; j < skillNodes.getLength(); j++) {
                Node skillNode = skillNodes.item(j);
                if (skillNode.getNodeType() == Node.ELEMENT_NODE) {
                    command = processCommand(skillNode.getChildNodes());
                    influenceEffect = processInfluenceEffect(skillNode.getChildNodes());
                    reference = skillNode.getAttributes().getNamedItem("reference").getTextContent();

                    if(skillRef.containsKey(reference)) {
                        return skillRef.get(reference);
                    }

                    else if(command != null) {
                        name = skillNode.getAttributes().getNamedItem("name").getTextContent();
                        useCost = Integer.parseInt(skillNode.getAttributes().getNamedItem("useCost").getTextContent());
                        accuracy = Integer.parseInt(skillNode.getAttributes().getNamedItem("accuracy").getTextContent());
                        return new Skill(name, influenceEffect, (SettableCommand) command, new SendInfluenceEffectCommand(levelMessenger), accuracy, useCost);
                    }
                }
            }
        }

        return null;
    }

    private void processInfluenceEffects(Element element, Level level) {
        List<Point3D> pointsToAdd = getKeyPoints(element);
        List<InfluenceEffect> influencesToAdd = new ArrayList<>();
        Command command;
        int movesRemaining;
        long speed;
        Orientation orientation;
        int range;

        NodeList influenceValues = element.getElementsByTagName("VALUE");
        for(int i = 0; i < influenceValues.getLength(); i++) {
            NodeList influenceNodes = influenceValues.item(i).getChildNodes();

            for(int j = 0; j < influenceNodes.getLength(); j++) {
                Node influenceNode = influenceNodes.item(j);
                if(influenceNode.getNodeType() == Node.ELEMENT_NODE) {
                    command = processCommand(influenceNode.getChildNodes());

                    if(command != null) {
                        movesRemaining =  Integer.parseInt(influenceNode.getAttributes().getNamedItem("movesRemaining").getTextContent());
                        speed = Long.parseLong(influenceNode.getAttributes().getNamedItem("speed").getTextContent());
                        range = Integer.parseInt(influenceNode.getAttributes().getNamedItem("range").getTextContent());
                        orientation = Orientation.toOrientation(influenceNode.getAttributes().getNamedItem("orientation").getTextContent());
                        Point3D point3D = toPoint3D(influenceNode.getAttributes().getNamedItem("point").getTextContent());

                        switch (influenceNode.getNodeName().toLowerCase()) {
                            case "angularinfluenceeffect":
                               influencesToAdd.add(new AngularInfluenceEffect((SettableCommand) command, range, speed, orientation, movesRemaining, 0,point3D));
                                break;

                            case "linearinfluenceeffect":
                                influencesToAdd.add(new LinearInfluenceEffect((SettableCommand) command, range, speed, orientation, movesRemaining, 0,point3D));
                                break;

                            case "radialinfluenceeffect":
                                influencesToAdd.add(new RadialInfluenceEffect((SettableCommand) command, range, speed, orientation, movesRemaining, 0,point3D));
                                break;
                        }
                    }
                }
            }
        }

        for(int i = 0; i < pointsToAdd.size(); i++) {
            level.addInfluenceEffectTo(pointsToAdd.get(i), influencesToAdd.get(i));
        }
    }

    private void processAreaEffects(Element element, Level level) {
        List<Point3D> pointsToAdd = getKeyPoints(element);
        List<AreaEffect> effectsToAdd = new ArrayList<>();

        NodeList effectValues = element.getElementsByTagName("VALUE");
        for(int i = 0; i < effectValues.getLength(); i++) {
            NodeList effectNodes = effectValues.item(i).getChildNodes();

            for(int j = 0; j < effectNodes.getLength(); j++) {
                Node effectNode = effectNodes.item(j);
                if(effectNode.getNodeType() == Node.ELEMENT_NODE) {
                    Command command = processCommand(effectNode.getChildNodes());
                    AreaEffectView areaEffectView = new AreaEffectView(new Point3D(0,0,0));

                    if(command instanceof AddHealthCommand) {
                        areaEffectView.setHealthPool();
                    }

                    else if(command instanceof RemoveHealthCommand) {
                        areaEffectView.setDamagePool();
                    }

                    else if(command instanceof LevelUpCommand) {
                        areaEffectView.setLevelUp();
                    }

                    else if(command instanceof InstaDeathCommand) {
                        areaEffectView.setInstantDeath();
                    }

                    if(command != null) {
                        switch (effectNode.getNodeName().toLowerCase()) {
                            case "oneshotareaeffect":
                                OneShotAreaEffect oneShotAreaEffect = new OneShotAreaEffect(command);
                                oneShotAreaEffect.setObserver(areaEffectView);

                                effectsToAdd.add(oneShotAreaEffect);
                                break;

                            case "infiniteareaeffect":
                                InfiniteAreaEffect infiniteAreaEffect = new InfiniteAreaEffect(command);
                                infiniteAreaEffect.setObserver(areaEffectView);
                                effectsToAdd.add(infiniteAreaEffect);
                                break;
                        }
                    }
                }
            }
        }

        for(int i = 0; i < pointsToAdd.size(); i++) {
            effectsToAdd.get(i).notifyObserver(pointsToAdd.get(i));
            level.addAreaEffectTo(pointsToAdd.get(i), effectsToAdd.get(i));
        }
    }

    private Command processCommand(NodeList childNodes) {
        int amount;
        boolean hasFired;
        int oldSpeed;
        int oldNoise;
        boolean firstTimeExecuting;
        String commandRef;

        for(int i = 0; i < childNodes.getLength(); i++) {
            Node commandNode = childNodes.item(i);

            if(commandNode.getNodeType() == Node.ELEMENT_NODE) {
                commandRef = commandNode.getAttributes().getNamedItem("reference").getTextContent();

                if (this.commandRef.containsKey(commandRef)) {
                    return this.commandRef.get(commandRef);
                } else {
                    switch (commandNode.getNodeName().toLowerCase()) {
                        case "addhealthcommand":
                            amount = Integer.parseInt(commandNode.getAttributes().getNamedItem("amount").getTextContent());
                            AddHealthCommand addHealthCommand = new AddHealthCommand(amount);
                            this.commandRef.put(commandRef, addHealthCommand);
                            return addHealthCommand;

                        case "removehealthcommand":
                            amount = Integer.parseInt(commandNode.getAttributes().getNamedItem("amount").getTextContent());
                            RemoveHealthCommand removeHealthCommand = new RemoveHealthCommand(amount);
                            this.commandRef.put(commandRef, removeHealthCommand);
                            return removeHealthCommand;

                        case "togglehealthcommand":
                            amount = Integer.parseInt(commandNode.getAttributes().getNamedItem("amount").getTextContent());
                            hasFired = Boolean.parseBoolean(commandNode.getAttributes().getNamedItem("hasFired").getTextContent());
                            ToggleHealthCommand toggleHealthCommand = new ToggleHealthCommand(amount, hasFired);
                            this.commandRef.put(commandRef, toggleHealthCommand);
                            return toggleHealthCommand;

                        case "togglemanacommand":
                            amount = Integer.parseInt(commandNode.getAttributes().getNamedItem("amount").getTextContent());
                            hasFired = Boolean.parseBoolean(commandNode.getAttributes().getNamedItem("hasFired").getTextContent());
                            ToggleManaCommand toggleManaCommand = new ToggleManaCommand(amount, hasFired);
                            this.commandRef.put(commandRef, toggleManaCommand);
                            return toggleManaCommand;

                        case "togglespeedcommand":
                            amount = Integer.parseInt(commandNode.getAttributes().getNamedItem("amount").getTextContent());
                            hasFired = Boolean.parseBoolean(commandNode.getAttributes().getNamedItem("hasFired").getTextContent());
                            ToggleSpeedCommand toggleSpeedCommand = new ToggleSpeedCommand(amount, hasFired);
                            this.commandRef.put(commandRef, toggleSpeedCommand);
                            return toggleSpeedCommand;

                        case "instadeathcommand":
                            InstaDeathCommand instaDeathCommand = new InstaDeathCommand();
                            this.commandRef.put(commandRef, instaDeathCommand);
                            return instaDeathCommand;

                        case "levelupcommand":
                            LevelUpCommand levelUpCommand = new LevelUpCommand();
                            this.commandRef.put(commandRef, levelUpCommand);
                            return levelUpCommand;

                        case "togglesneaking":
                            amount = Integer.parseInt(commandNode.getAttributes().getNamedItem("amount").getTextContent());
                            System.out.println(commandNode.getAttributes().getNamedItem("hasFired").getTextContent());
                            hasFired = Boolean.parseBoolean(commandNode.getAttributes().getNamedItem("hasFired").getTextContent());
                            firstTimeExecuting = Boolean.parseBoolean(commandNode.getAttributes().getNamedItem("firstTimeExecuting").getTextContent());
                            oldSpeed = Integer.parseInt(commandNode.getAttributes().getNamedItem("oldSpeed").getTextContent());
                            oldNoise = Integer.parseInt(commandNode.getAttributes().getNamedItem("oldNoise").getTextContent());
                            ToggleSneaking toggleSneaking = new ToggleSneaking(hasFired, amount, 0, 0, false);
                            this.commandRef.put(commandRef, toggleSneaking);
                            return toggleSneaking;

                     /* Game Loop Commands */
                        case "bartercommand":
                            BarterCommand barterCommand = new BarterCommand(levelMessenger);
                            amount = Integer.parseInt(commandNode.getAttributes().getNamedItem("amount").getTextContent());
                            barterCommand.setAmount(amount);
                            this.commandRef.put(commandRef, barterCommand);
                            return barterCommand;

                        case "dialogcommand": // TODO: implement
                            break;

                        case "observeentitycommand":
                            ObserveEntityCommand observeEntityCommand = new ObserveEntityCommand(levelMessenger);
                            amount = Integer.parseInt(commandNode.getAttributes().getNamedItem("amount").getTextContent());
                            observeEntityCommand.setAmount(amount);
                            this.commandRef.put(commandRef, observeEntityCommand);
                            return observeEntityCommand;

                    /* Game Model Commands */
                        case "confuseentitycommand":
                            ConfuseEntityCommand confuseEntityCommand = new ConfuseEntityCommand(levelMessenger);
                            amount = Integer.parseInt(commandNode.getAttributes().getNamedItem("amount").getTextContent());
                            confuseEntityCommand.setAmount(amount);
                            this.commandRef.put(commandRef, confuseEntityCommand);
                            return confuseEntityCommand;

                        case "freezeentitycommand":
                            FreezeEntityCommand freezeEntityCommand = new FreezeEntityCommand(levelMessenger);
                            amount = Integer.parseInt(commandNode.getAttributes().getNamedItem("amount").getTextContent());
                            freezeEntityCommand.setAmount(amount);
                            this.commandRef.put(commandRef, freezeEntityCommand);
                            return freezeEntityCommand;

                        case "slowentitycommand":
                            SlowEntityCommand slowEntityCommand = new SlowEntityCommand(levelMessenger);
                            amount = Integer.parseInt(commandNode.getAttributes().getNamedItem("amount").getTextContent());
                            slowEntityCommand.setAmount(amount);
                            this.commandRef.put(commandRef, slowEntityCommand);
                            return slowEntityCommand;

                        case "teleportentitycommand":
                            String reference = commandNode.getAttributes().getNamedItem("levelReference").getTextContent();
                            Level level = levelRef.get(reference);

                            if(level == null) {
                                level = new Level();
                                levelRef.put(reference, level);
                            }

                            Point3D point3D = toPoint3D(commandNode.getAttributes().getNamedItem("point").getTextContent());
                            TeleportEntityCommand teleportEntityCommand = new TeleportEntityCommand(levelMessenger, level, point3D);
                            this.commandRef.put(commandRef, teleportEntityCommand);
                            return teleportEntityCommand;

                    /* Level Commands */
                        case "disarmtrapcommand":
                            DisarmTrapCommand disarmTrapCommand = new DisarmTrapCommand(levelMessenger);
                            amount = Integer.parseInt(commandNode.getAttributes().getNamedItem("amount").getTextContent());
                            disarmTrapCommand.setAmount(amount);
                            this.commandRef.put(commandRef, disarmTrapCommand);
                            return disarmTrapCommand;

                        case "dropitemcommand": // TODO: implement
                            break;

                        case "pickpocketcommand":
                            PickPocketCommand pickPocketCommand = new PickPocketCommand(levelMessenger);
                            amount = Integer.parseInt(commandNode.getAttributes().getNamedItem("amount").getTextContent());
                            pickPocketCommand.setAmount(amount);
                            this.commandRef.put(commandRef, pickPocketCommand);
                            return pickPocketCommand;

                        case "sendinfluencecommand": // TODO: implement
                            break;
                    }
                }
            }
        }

        return null;
    }

    private void processTerrains(Element element, Level level) {
        List<Point3D> pointsToAdd = getKeyPoints(element);
        List<Terrain> terrainsToAdd = new ArrayList<>();

        NodeList terrainValues = element.getElementsByTagName("VALUE");
        for(int terrainIter = 0; terrainIter < terrainValues.getLength(); terrainIter++) {
            String value = terrainValues.item(terrainIter).getAttributes().item(0).getTextContent();
            Terrain valueTerrain;

            switch (value.toLowerCase()) {
                case "grass":
                    valueTerrain = Terrain.GRASS;
                    break;

                case "water":
                    valueTerrain = Terrain.WATER;
                    break;

                case "mountains":
                    valueTerrain = Terrain.MOUNTAINS;
                    break;

                default:
                    valueTerrain = Terrain.NONE;
            }

            terrainsToAdd.add(valueTerrain);
        }

        for(int pointIter = 0; pointIter < pointsToAdd.size(); pointIter++) {
            level.addTerrainTo(pointsToAdd.get(pointIter), terrainsToAdd.get(pointIter));
        }
    }

    private void loadGameModel(Document document) {
        NodeList currentLevel = document.getElementsByTagName("CURRENTLEVEL");
        NodeList levelList = document.getElementsByTagName("LEVELLIST");
        NodeList player = document.getElementsByTagName("PLAYER");
        NodeList queues = document.getElementsByTagName("TELEPORTQUEUE");
        NodeList aiMap = document.getElementsByTagName("AICONTROLLERS");
        this.currentLevel = loadLevel(currentLevel);
        this.levelMessenger.setLevel(this.currentLevel);
        this.world = loadWorld(levelList);
        this.entity = loadPlayer(player);
        loadQueue(queues);
        loadAIMap(aiMap);
        for(ReferenceMap referenceMap: needToSetMap) {
            referenceMap.addReference();
        }

        this.gameModel = new GameModel(this.currentLevel, this.levelMessenger, this.world, this.entity, this.aiMap, teleportTuples, failedTuples);
        gameModel.setPetFactory(petAIFactory);
        gameModel.setSkillsFactory(skillsFactory);
        gameModelMessenger.setGameModel(gameModel);
    }

    private void loadAIMap(NodeList aiMap) {
        String entRef;
        String aiState;
        String levelRef;
        Level level = new Level();

        List<AIController> aiList = new ArrayList<>();

        for(int i = 0; i < aiMap.getLength(); i++) {
            NodeList controllerNodes = aiMap.item(i).getChildNodes();

            for(int j = 0; j < controllerNodes.getLength(); j++) {
                Node controllerNode = controllerNodes.item(j);

                if(controllerNode.getNodeType() == Node.ELEMENT_NODE) {
                    aiState = controllerNode.getAttributes().getNamedItem("aiState").getTextContent();
                    entRef = controllerNode.getAttributes().getNamedItem("entityRef").getTextContent();
                    levelRef = controllerNode.getAttributes().getNamedItem("levelRef").getTextContent();

                    Entity entity = entityRef.get(entRef);
                    level = this.levelRef.get(levelRef);
                    AIController controller = new AIController();

                    Skill pickPocketSkill = null;
                    for(Skill pick: skillRef.values()) {
                        if(pick.getName().equalsIgnoreCase("pickpocket")) {
                            pickPocketSkill = pick;
                            break;
                        }
                    }

                    if(pickPocketSkill != null) {
                        petAIFactory = new PetAIFactory(level.getTerrainMap(),
                                level.getObstacleMap(),
                                level.getItemMap(),
                                pickPocketSkill,
                                level.getRiverMap(),
                                level.getEntityMap(),
                                this.entity, entity);

                    }

                    else {
                        SkillsFactory skillsFactory = new SkillsFactory(levelMessenger);
                        petAIFactory = new PetAIFactory(level.getTerrainMap(),
                                level.getObstacleMap(),
                                level.getItemMap(),
                                skillsFactory.getPickpocket(),
                                level.getRiverMap(),
                                level.getEntityMap(),
                                this.entity,
                                entity);
                    }

                    switch (aiState) {
                        case "HostileAI":
                            controller = new AIController();
                            HostileAI hostileAI = new HostileAI(entity, level.getTerrainMap(), level.getEntityMap(), level.getObstacleMap(), level.getRiverMap());
                            PatrolPath path = processPatrolPath(controllerNode.getChildNodes());
                            hostileAI.setPatrolPath(path);
                            controller.setActiveState(hostileAI);
                            aiList.add(controller);
                            break;

                        case "CombatPetState":
                            controller = new AIController();
                            controller.setActiveState(petAIFactory.getCombatPetState());
                            aiList.add(controller);
                            break;

                        case "GeneralPetState":
                            controller = new AIController();
                            controller.setActiveState(petAIFactory.getGeneralPetState());
                            aiList.add(controller);
                            break;

                        case "ItemPetState":
                            controller = new AIController();
                            controller.setActiveState(petAIFactory.getItemPetState());
                            aiList.add(controller);
                            break;

                        case "PassivePetState":
                            controller = new AIController();
                            controller.setActiveState(petAIFactory.getPassivePetState());
                            aiList.add(controller);
                            break;
                    }
                }
            }

            this.aiMap.put(level, aiList);
        }
    }

    private PatrolPath processPatrolPath(NodeList nodeList) {
        ArrayList<Vec3d> points = new ArrayList<>();

        for(int i = 0; i < nodeList.getLength(); i++) {
            NodeList pathNodes = nodeList.item(i).getChildNodes();

            for(int j = 0; j < pathNodes.getLength(); j++) {
                Node path = pathNodes.item(j);

                if(path.getNodeType() == Node.ELEMENT_NODE) {
                    points.add(toVector(path.getAttributes().getNamedItem("vec").getTextContent()));
                }
            }
        }

        return new PatrolPath(points);
    }

    private Entity loadPlayer(NodeList player) {

        for(int i = 0; i < player.getLength(); i++) {
            NodeList playerNodes = player.item(i).getChildNodes();

            for(int j = 0; j < playerNodes.getLength(); j++) {
                Node playerNode = playerNodes.item(j);

                if(playerNode.getNodeType() == Node.ELEMENT_NODE) {
                    return processPlayerEntity((Element) playerNode);
                }
            }
        }

        return new Entity();
    }

    private Entity processPlayerEntity(Element entityNode) {
        boolean moveable;
        int noise;
        int goldAmount;
        int maxGold;
        int attackPoints;
        int attackModifier;
        int defensePoints;
        int defenseModifier;
        long speedAmount;
        int manaPoints;
        int maxMana;
        int sight;
        int currentHealth;
        int maxHealth;
        int currentExperience;
        int levelAmount;
        int experienceToNextLevel;
        int regenRate;
        String reference = entityNode.getAttributes().getNamedItem("reference").getTextContent();

        if(entityRef.containsKey(reference)) {
            return entityRef.get(reference);
        }

        else {
            Entity entity = null;
            ItemHotBar hotBar = null;
            List<Skill> weaponSkills = new ArrayList<>();
            List<Skill> nonWeaponSkills = new ArrayList<>();
            HashMap<Skill, SkillLevel> skillLevelsMap = new HashMap<>();
            Vec3d velocity = null;
            NoiseLevel noiseLevel;
            SightRadius sightRadius;
            XPLevel xpLevel;
            Health health;
            Mana mana;
            Speed speed;
            Gold gold;
            Attack attack;
            Defense defense;
            Equipment equipment = null;
            Inventory inventory = null;
            Orientation orientation;
            List<Terrain> compatableTerrain = new ArrayList<>();
            Mount mount;
            noise = Integer.parseInt(entityNode.getAttributes().getNamedItem("noiseLevel").getTextContent());
            noiseLevel = new NoiseLevel(noise);

            goldAmount = Integer.parseInt(entityNode.getAttributes().getNamedItem("goldAmount").getTextContent());
            maxGold = Integer.parseInt(entityNode.getAttributes().getNamedItem("maxGold").getTextContent());
            gold = new Gold(goldAmount, maxGold);

            speedAmount = Long.parseLong(entityNode.getAttributes().getNamedItem("speed").getTextContent());
            speed = new Speed(speedAmount);

            manaPoints = Integer.parseInt(entityNode.getAttributes().getNamedItem("manaPoints").getTextContent());
            maxMana = Integer.parseInt(entityNode.getAttributes().getNamedItem("maxMana").getTextContent());
            regenRate = Integer.parseInt(entityNode.getAttributes().getNamedItem("regenRate").getTextContent());
            mana = new Mana(manaPoints, maxMana, 0 );

            attackPoints = Integer.parseInt(entityNode.getAttributes().getNamedItem("attackPoints").getTextContent());
            attackModifier = Integer.parseInt(entityNode.getAttributes().getNamedItem("attackModifier").getTextContent());
            attack = new Attack(attackPoints, attackModifier);

            defensePoints = Integer.parseInt(entityNode.getAttributes().getNamedItem("defensePoints").getTextContent());
            defenseModifier = Integer.parseInt(entityNode.getAttributes().getNamedItem("defenseModifier").getTextContent());
            defense = new Defense(defensePoints, defenseModifier);

            sight = Integer.parseInt(entityNode.getAttributes().getNamedItem("sightRadius").getTextContent());
            sightRadius = new SightRadius(sight);

            orientation = Orientation.toOrientation(entityNode.getAttributes().getNamedItem("orientation").getTextContent());

            currentHealth = Integer.parseInt(entityNode.getAttributes().getNamedItem("currentHealth").getTextContent());
            maxHealth = Integer.parseInt(entityNode.getAttributes().getNamedItem("maxHealth").getTextContent());
            health = new Health(currentHealth, maxHealth);

            currentExperience = Integer.parseInt(entityNode.getAttributes().getNamedItem("experience").getTextContent());
            levelAmount = Integer.parseInt(entityNode.getAttributes().getNamedItem("level").getTextContent());
            experienceToNextLevel = Integer.parseInt(entityNode.getAttributes().getNamedItem("experienceToNextLevel").getTextContent());
            xpLevel = new XPLevel(levelAmount, currentExperience, experienceToNextLevel);

            moveable = Boolean.parseBoolean(entityNode.getAttributes().getNamedItem("moveable").getTextContent());

            velocity = toVector(entityNode.getAttributes().getNamedItem("velocity").getTextContent());

            processTerrainList(entityNode, compatableTerrain);
            processWeaponSkillsList(entityNode.getChildNodes(), weaponSkills, skillLevelsMap);
            processNonWeaponSkillsList(entityNode.getChildNodes(), nonWeaponSkills, skillLevelsMap);
            mount = processEntityMount(entityNode);
            equipment = processEquipment(entityNode);
            hotBar = processHotBar(entityNode);
            inventory = processInventory(entityNode);

            entity = new Entity(null, hotBar, weaponSkills, nonWeaponSkills, skillLevelsMap, velocity,
                    noiseLevel, sightRadius, xpLevel, health, mana, speed, gold, attack, defense, equipment,
                    inventory, orientation, compatableTerrain, moveable, mount, new ArrayList<>(), new ArrayList<>());

            for(Skill skill: entity.getWeaponSkills()) {
                if(skill.getName().equalsIgnoreCase("one-handed")) {
                    entity.setObserver(new SmasherView(entity, new Point3D(0,1,-1)));
                    break;
                }

                else if(skill.getName().equalsIgnoreCase("enchant")) {
                    entity.setObserver(new SummonerView(entity, new Point3D(0,1,-1)));
                    break;
                }

                else if(skill.getName().equalsIgnoreCase("range")) {
                    entity.setObserver(new SneakView(entity, new Point3D(0,1,-1)));
                    break;
                }
            }

            if(entity.getObserver() == null) {
                entity.setObserver(new MonsterView(entity, new Point3D(0,1,-1)));
            }

            entityRef.put(reference, entity);
            return entity;
        }
    }

    private ArrayList<Level> loadWorld(NodeList nodeList) {
        ArrayList<Level> world = new ArrayList<>();

        for(int i = 0; i < nodeList.getLength(); i++) {
            Node levelNode = nodeList.item(i);
            world = processLevelList(levelNode.getChildNodes());
        }

        return world;
    }

    private Level loadLevel(NodeList nodeList) {
        Level level = new Level();

        for(int i = 0; i < nodeList.getLength(); i++) {
            Node levelNode = nodeList.item(i);
            processLevel(levelNode.getChildNodes(), level);
        }

        return level;
    }

    private Level processLevel(NodeList nodeList, Level level) {
        String reference;
        for(int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                reference = element.getAttribute("reference");
                levelRef.put(reference, level);
                loadMaps(element.getChildNodes(), level);
            }
        }

        return level;
    }

    private void loadQueue(NodeList childNodes) {
        for(int i = 0; i < childNodes.getLength(); i++) {
            Node queNode = childNodes.item(i);

            if(queNode.getNodeType() == Node.ELEMENT_NODE) {
                System.out.println(queNode.getAttributes().getNamedItem("id").getTextContent());
                switch (queNode.getAttributes().getNamedItem("id").getTextContent()) {
                    case "progress":
                        processProgressTuple(queNode.getChildNodes());
                        break;

                    case "failed":
                        processFailedTuple(queNode.getChildNodes());
                        break;
                }
            }
        }
    }

    private void processProgressTuple(NodeList childNodes) {
        String entRef;
        String levRef;
        Point3D point3D;
        GameModel gameModel = new GameModel(null);
        for(int i = 0; i < childNodes.getLength(); i++) {
            Node tupleNode = childNodes.item(i);

            if(tupleNode.getNodeType() == Node.ELEMENT_NODE) {
                entRef = tupleNode.getAttributes().getNamedItem("entityReference").getTextContent();
                levRef = tupleNode.getAttributes().getNamedItem("levelReference").getTextContent();
                point3D = toPoint3D(tupleNode.getAttributes().getNamedItem("point").getTextContent());
                Entity entity = this.entityRef.get(entRef);
                Level level = this.levelRef.get(levRef);
                teleportTuples.add(gameModel.new TeleportTuple(entity, level, point3D));
            }
        }
    }

    private void processFailedTuple(NodeList childNodes) {
        String entRef;
        String levRef;
        Point3D point3D;
        GameModel gameModel = new GameModel(null);
        for(int i = 0; i < childNodes.getLength(); i++) {
            Node tupleNode = childNodes.item(i);

            if(tupleNode.getNodeType() == Node.ELEMENT_NODE) {
                entRef = tupleNode.getAttributes().getNamedItem("entityReference").getTextContent();
                levRef = tupleNode.getAttributes().getNamedItem("levelReference").getTextContent();
                point3D = toPoint3D(tupleNode.getAttributes().getNamedItem("point").getTextContent());
                Entity entity = this.entityRef.get(entRef);
                Level level = this.levelRef.get(levRef);
                failedTuples.add(gameModel.new TeleportTuple(entity, level, point3D));
            }
        }
    }

    private ArrayList<Level> processLevelList(NodeList nodeList) {
        ArrayList<Level> levelList = new ArrayList<>();
        String reference;

        for(int i = 0; i < nodeList.getLength(); i++) {

            Node node = nodeList.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                reference = element.getAttribute("reference");

                if(levelRef.containsKey(reference) && levelRef.get(reference).equals(currentLevel)) {
                    levelList.add(levelRef.get(reference));
                } else if(levelRef.containsKey(reference) && levelRef.get(reference) != currentLevel) {
                    levelList.add(levelRef.get(reference));
                    loadMaps(element.getChildNodes(), levelRef.get(reference));
                } else {
                    Level level = new Level();

                    loadMaps(element.getChildNodes(), level);
                    levelList.add(level);
                    levelRef.put(reference, level);
                }
            }
        }

        return levelList;
    }

    private ArrayList<Point3D> getKeyPoints(Element element) {
        ArrayList<Point3D> pointsToAdd = new ArrayList<>();

        NodeList terrainKeys = element.getElementsByTagName("KEY");
        for(int terrainIter = 0; terrainIter < terrainKeys.getLength(); terrainIter++) {
            String key = terrainKeys.item(terrainIter).getAttributes().item(0).getTextContent();
            String[] point = key.split(",");
            Point3D keyPoint = new Point3D(Integer.parseInt(point[0]), Integer.parseInt(point[1]), Integer.parseInt(point[2]));
            pointsToAdd.add(keyPoint);
        }

        return pointsToAdd;
    }

    private Vec3d toVector(String flowRate) {
        String[] point = flowRate.split(",");
        return new Vec3d(Integer.parseInt(point[0]), Integer.parseInt(point[1]), Integer.parseInt(point[2]));
    }

    private Point3D toPoint3D(String point) {
        String[] stringPoint = point.split(",");
        return new Point3D(Integer.parseInt(stringPoint[0]), Integer.parseInt(stringPoint[1]), Integer.parseInt(stringPoint[2]));
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public List<Level> getWorld() {
        return world;
    }

    public GameModel getGameModel() {
        return gameModel;
    }

    private class ReferenceMap {
        private String listType;
        private Entity entity;
        private String reference;

        public ReferenceMap(String reference, String listType, Entity entity) {
            this.listType = listType;
            this.reference = reference;
            this.entity = entity;
        }

        public void addReference() {
            switch (listType) {
                case "target":
                    entity.addTarget(entityRef.get(reference));
                    break;

                case "friend":
                    entity.addFriendly(entityRef.get(reference));
                    break;
            }
        }
    }
}
