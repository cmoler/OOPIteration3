package Controller;

import Model.AreaEffect.AreaEffect;
import Model.AreaEffect.InfiniteAreaEffect;
import Model.AreaEffect.OneShotAreaEffect;
import Model.Command.Command;
import Model.Command.EntityCommand.NonSettableCommand.InstaDeathCommand;
import Model.Command.EntityCommand.NonSettableCommand.LevelUpCommand;
import Model.Command.EntityCommand.NonSettableCommand.SendInfluenceEffectCommand;
import Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand.ToggleableCommand;
import Model.Command.EntityCommand.SettableCommand.ToggleableCommand.ToggleSneaking;
import Model.Command.EntityCommand.SettableCommand.AddHealthCommand;
import Model.Command.EntityCommand.SettableCommand.RemoveHealthCommand;
import Model.Command.EntityCommand.SettableCommand.SettableCommand;
import Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand.ToggleHealthCommand;
import Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand.ToggleManaCommand;
import Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand.ToggleSpeedCommand;
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
import View.LevelView.LevelViewElement;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameLoader {

    private Level currentLevel;
    private GameModel gameModel;
    private List<Level> world;
    private Entity entity;
    private HashMap<String, Entity> entityRef = new HashMap<>();
    private HashMap<String, Item> itemRef = new HashMap<>();
    private HashMap<String, Level> levelRef = new HashMap<>();

    public GameLoader() {
        world = new ArrayList<>();
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
        Speed speed;
        Orientation orientation;
        List<Terrain> terrains = new ArrayList<>();

        NodeList mountValues = element.getElementsByTagName("VALUE");
        for(int i = 0; i < mountValues.getLength(); i++) {
            NodeList mountNodes = mountValues.item(i).getChildNodes();

            for(int j = 0; j < mountNodes.getLength(); j++) {
                Node mountNode = mountNodes.item(j);
                if(mountNode.getNodeType() == Node.ELEMENT_NODE) {
                    speed = new Speed(Integer.parseInt(mountNode.getAttributes().getNamedItem("speed").getTextContent()));
                    orientation = Orientation.toOrientation(mountNode.getAttributes().getNamedItem("orientation").getTextContent());
                    processTerrainList(element, terrains);

                    mountsToAdd.add(new Mount(orientation, speed, terrains, new ArrayList<>()));
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
        Vec3d flow;

        NodeList riverValues = element.getElementsByTagName("VALUE");
        for(int i = 0; i < riverValues.getLength(); i++) {
            NodeList riverNodes = riverValues.item(i).getChildNodes();

            for(int j = 0; j < riverNodes.getLength(); j++) {
                Node riverNode = riverNodes.item(j);
                if(riverNode.getNodeType() == Node.ELEMENT_NODE) {
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
        Command command;
        boolean isVisible;
        boolean isDisarmed;
        int trapStrength;

        NodeList trapValues = element.getElementsByTagName("VALUE");
        for(int i = 0; i < trapValues.getLength(); i++) {
            NodeList trapNodes = trapValues.item(i).getChildNodes();

            for(int j = 0; j < trapNodes.getLength(); j++) {
                Node trapNode = trapNodes.item(j);
                if(trapNode.getNodeType() == Node.ELEMENT_NODE) {
                    command = processCommand(trapNode.getChildNodes());

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
        Entity entity;
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
        boolean moveable;
        int noise;
        int goldAmount;
        int maxGold;
        int attackPoints;
        int attackModifier;
        int defensePoints;
        int defenseModifier;
        int speedAmount;
        int manaPoints;
        int maxMana;
        int sight;
        int currentHealth;
        int maxHealth;
        int currentExperience;
        int levelAmount;
        int experienceToNextLevel;
        String reference = "";

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
                        noise = Integer.parseInt(entityNode.getAttributes().getNamedItem("noiseLevel").getTextContent());
                        noiseLevel = new NoiseLevel(noise);

                        goldAmount = Integer.parseInt(entityNode.getAttributes().getNamedItem("goldAmount").getTextContent());
                        maxGold = Integer.parseInt(entityNode.getAttributes().getNamedItem("maxGold").getTextContent());
                        gold = new Gold(goldAmount, maxGold);

                        speedAmount = Integer.parseInt(entityNode.getAttributes().getNamedItem("speed").getTextContent());
                        speed = new Speed(speedAmount);

                        manaPoints = Integer.parseInt(entityNode.getAttributes().getNamedItem("manaPoints").getTextContent());
                        maxMana = Integer.parseInt(entityNode.getAttributes().getNamedItem("maxMana").getTextContent());
                        mana = new Mana(manaPoints, maxMana, 0); // TODO: BRYAN ADD THIS REGENRATE PLS

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

                        processTerrainList(element, compatableTerrain);
                        processWeaponSkillsList(element, weaponSkills, skillLevelsMap);
                        processNonWeaponSkillsList(element, nonWeaponSkills, skillLevelsMap);
                        mount = processEntityMount(element);
                        equipment = processEquipment(element);
                        hotBar = processHotBar(element);
                        inventory = processInventory(element);

                        entity = new Entity(null, hotBar, weaponSkills, nonWeaponSkills, skillLevelsMap, velocity,
                                noiseLevel, sightRadius, xpLevel, health, mana, speed, gold, attack, defense, equipment,
                                inventory, orientation, compatableTerrain, moveable, mount);

                        entitiesToAdd.add(entity);
                        entityRef.put(reference, entity);
                    }
                }
            }
        }

        for(int i = 0; i < pointsToAdd.size(); i++) {
            level.addEntityTo(pointsToAdd.get(i), entitiesToAdd.get(i));
        }
    }

    private Inventory processInventory(Element element) {
        List<TakeableItem> itemsToAdd = new ArrayList<>();
        Command command = null;
        String name;
        int maxSize = 0;

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
                        command = processCommand(itemNode.getChildNodes());
                        name = itemNode.getAttributes().getNamedItem("name").getTextContent();
                        switch (itemNode.getNodeName().toLowerCase()) {
                            case "armoritem":
                                int defense = Integer.parseInt(itemNode.getAttributes().getNamedItem("defense").getTextContent());
                                itemsToAdd.add(new ArmorItem(name, (ToggleableCommand) command, defense));
                                break;

                            case "consumableitem":
                                itemsToAdd.add(new ConsumableItem(name, command));
                                break;

                            case "ringitem":
                                itemsToAdd.add(new RingItem(name, (ToggleableCommand) command));
                                break;

                            case "weaponitem": // TODO: this needs to be changed
                                itemsToAdd.add(new WeaponItem(name, (SettableCommand) command));
                                break;
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
        Command command;
        String name;
        int index = -1;
        String reference;

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
                            command = processCommand(itemNode.getChildNodes());
                            name = itemNode.getAttributes().getNamedItem("name").getTextContent();
                            reference = itemNode.getAttributes().getNamedItem("reference").getTextContent();

                            if(command != null && !name.isEmpty() && index != -1) {
                                switch (itemNode.getNodeName().toLowerCase()) {
                                    case "armoritem":
                                        if(itemRef.containsKey(reference)) {
                                            itemHotBar.addItem((ArmorItem)itemRef.get(reference), index);
                                        }

                                        else {
                                            int defense = Integer.parseInt(itemNode.getAttributes().getNamedItem("defense").getTextContent());
                                            ArmorItem armorItem = new ArmorItem(name, (ToggleableCommand) command, defense);
                                            itemHotBar.addItem(armorItem, index);
                                            itemRef.put(armorItem.toString(), armorItem);
                                        }
                                        break;

                                    case "consumableitem":
                                        if(itemRef.containsKey(reference)) {
                                            itemHotBar.addItem((ConsumableItem)itemRef.get(reference), index);
                                        }

                                        else {
                                            ConsumableItem consumableItem = new ConsumableItem(name, command);
                                            itemHotBar.addItem(consumableItem, index);
                                            itemRef.put(consumableItem.toString(), consumableItem);
                                        }
                                        break;

                                    case "ringitem":
                                        if(itemRef.containsKey(reference)) {
                                            itemHotBar.addItem((RingItem)itemRef.get(reference), index);
                                        }

                                        else {
                                            RingItem ringItem = new RingItem(name, (ToggleableCommand) command);
                                            itemHotBar.addItem(ringItem, index);
                                            itemRef.put(ringItem.toString(), ringItem);
                                        }
                                        break;

                                    case "weaponitem": //TODO: this needs to be changed
                                        if(itemRef.containsKey(reference)) {
                                            itemHotBar.addItem((WeaponItem)itemRef.get(reference), index);
                                        }

                                        else {
                                            WeaponItem weaponItem = new WeaponItem(name, (SettableCommand) command);
                                            itemHotBar.addItem(weaponItem, index);
                                            itemRef.put(weaponItem.toString(), weaponItem);
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

        NodeList equipValues = element.getElementsByTagName("Equipment");
        for(int i = 0; i < equipValues.getLength(); i++) {
            NodeList equipNodes = equipValues.item(i).getChildNodes();

            for(int j = 0; j < equipNodes.getLength(); j++) {
                Node equipNode = equipNodes.item(j);

                if(equipNode.getNodeType() == Node.ELEMENT_NODE) {
                    command = processCommand(equipNode.getChildNodes());
                    name = equipNode.getAttributes().getNamedItem("name").getTextContent();
                    reference = equipNode.getAttributes().getNamedItem("reference").getTextContent();

                    switch (equipNode.getNodeName().toLowerCase()) {
                        case "armoritem":
                            if(itemRef.containsKey(reference)) {
                                armorItem = (ArmorItem)itemRef.get(reference);
                            }

                            else {
                                int defense = Integer.parseInt(equipNode.getAttributes().getNamedItem("defense").getTextContent());
                                armorItem = new ArmorItem(name, (ToggleableCommand) command, defense);
                                itemRef.put(armorItem.toString(), armorItem);
                            }
                            break;

                        case "ringitem":
                            if(itemRef.containsKey(reference)) {
                                ringItem = (RingItem)itemRef.get(reference);
                            }

                            else {
                                ringItem = new RingItem(name, (ToggleableCommand) command);
                                itemRef.put(ringItem.toString(), ringItem);
                            }
                            break;

                        case "weaponitem": //TODO: this needs to be changed
                            if(itemRef.containsKey(reference)) {
                                weaponItem = (WeaponItem)itemRef.get(reference);
                            }

                            else {
                                weaponItem = new WeaponItem(name, (SettableCommand) command);
                                itemRef.put(weaponItem.toString(), weaponItem);
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
                return new Mount(orientation, speed, terrains, new ArrayList<>());
            }
        }

        return null;
    }

    private void processNonWeaponSkillsList(Element element, List<Skill> skills, HashMap<Skill, SkillLevel> skillLevelMap) {
        Command command;
        InfluenceEffect influenceEffect = null;
        SendInfluenceEffectCommand sendInfluenceEffectCommand = null;
        Skill newSkill;
        SkillLevel skillLevel;

        String name;
        int useCost;
        int accuracy;
        int skillLevelAmount;

        NodeList skillValues = element.getElementsByTagName("NONWEAPONSKILLS");
        for(int i = 0; i < skillValues.getLength(); i++) {

            NodeList skillNodes = skillValues.item(i).getChildNodes();
            for(int j = 0; j < skillNodes.getLength(); j++) {
                Node skillNode = skillNodes.item(j);
                if (skillNode.getNodeType() == Node.ELEMENT_NODE) {
                    command = processCommand(skillNode.getChildNodes());

                    if(command != null) {
                        name = skillNode.getAttributes().getNamedItem("name").getTextContent();
                        useCost = Integer.parseInt(skillNode.getAttributes().getNamedItem("useCost").getTextContent());
                        accuracy = Integer.parseInt(skillNode.getAttributes().getNamedItem("accuracy").getTextContent());
                        newSkill = new Skill(name, influenceEffect, (SettableCommand) command, sendInfluenceEffectCommand, accuracy, useCost);
                        skills.add(newSkill);

                        skillLevelAmount = Integer.parseInt(skillNode.getAttributes().getNamedItem("level").getTextContent());
                        skillLevel = new SkillLevel(skillLevelAmount);
                        skillLevelMap.put(newSkill, skillLevel);
                    }
                }
            }
        }
    }

    private void processWeaponSkillsList(Element element, List<Skill> skills, HashMap<Skill, SkillLevel> skillLevelMap) {
        Command command;
        InfluenceEffect influenceEffect = null;
        SendInfluenceEffectCommand sendInfluenceEffectCommand = null;
        Skill newSkill;
        SkillLevel skillLevel;

        String name;
        int useCost;
        int accuracy;
        int skillLevelAmount;

        NodeList skillValues = element.getElementsByTagName("WEAPONSKILLS");
        for(int i = 0; i < skillValues.getLength(); i++) {

            NodeList skillNodes = skillValues.item(i).getChildNodes();
            for(int j = 0; j < skillNodes.getLength(); j++) {
                Node skillNode = skillNodes.item(j);
                if (skillNode.getNodeType() == Node.ELEMENT_NODE) {
                    command = processCommand(skillNode.getChildNodes());

                    if(command != null) { // TODO: change order of skill level?
                        name = skillNode.getAttributes().getNamedItem("name").getTextContent();
                        useCost = Integer.parseInt(skillNode.getAttributes().getNamedItem("useCost").getTextContent());
                        accuracy = Integer.parseInt(skillNode.getAttributes().getNamedItem("accuracy").getTextContent());
                        newSkill = new Skill(name, influenceEffect, (SettableCommand) command, sendInfluenceEffectCommand, accuracy, useCost);
                        skills.add(newSkill);

                        skillLevelAmount = Integer.parseInt(skillNode.getAttributes().getNamedItem("level").getTextContent());
                        skillLevel = new SkillLevel(skillLevelAmount);
                        skillLevelMap.put(newSkill, skillLevel);
                    }
                }
            }
        }
    }

    private void processItems(Element element, Level level) {
        List<Point3D> pointsToAdd = getKeyPoints(element);
        List<Item> itemsToAdd = new ArrayList<>();
        Command command;
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
                        command = processCommand(itemNode.getChildNodes());
                        if (command != null) {
                            name = itemNode.getAttributes().getNamedItem("name").getTextContent();
                            switch (itemNode.getNodeName().toLowerCase()) {
                                case "oneshotitem":
                                    OneShotItem oneShotItem = new OneShotItem(name, command);
                                    itemsToAdd.add(oneShotItem);
                                    itemRef.put(oneShotItem.toString(),oneShotItem);
                                    break;

                                case "interactiveitem":
                                    InteractiveItem interactiveItem = new InteractiveItem(name, command);
                                    itemsToAdd.add(interactiveItem);
                                    itemRef.put(interactiveItem.toString(), interactiveItem);
                                    break;

                                case "armoritem":
                                    int defense = Integer.parseInt(itemNode.getAttributes().getNamedItem("defense").getTextContent());
                                    ArmorItem armorItem = new ArmorItem(name, (ToggleableCommand) command, defense);
                                    itemsToAdd.add(armorItem);
                                    itemRef.put(armorItem.toString(), armorItem);
                                    break;

                                case "consumableitem":
                                    ConsumableItem consumableItem = new ConsumableItem(name, command);
                                    itemsToAdd.add(consumableItem);
                                    itemRef.put(consumableItem.toString(), consumableItem);
                                    break;

                                case "ringitem":
                                    RingItem ringItem = new RingItem(name, (ToggleableCommand) command);
                                    itemsToAdd.add(ringItem);
                                    itemRef.put(ringItem.toString(), ringItem);
                                    break;

                                case "weaponitem": //TODO: this needs to be changed
                                    WeaponItem weaponItem = new WeaponItem(name, (SettableCommand) command);
                                    itemsToAdd.add(weaponItem);
                                    itemRef.put(weaponItem.toString(), weaponItem);
                                    break;
                            }
                        }
                    }
                }
            }
        }

        for(int i = 0; i < pointsToAdd.size(); i++) {
            level.addItemnTo(pointsToAdd.get(i), itemsToAdd.get(i));
        }
    }

    private void processInfluenceEffects(Element element, Level level) {
        List<Point3D> pointsToAdd = getKeyPoints(element);
        List<InfluenceEffect> influencesToAdd = new ArrayList<>();
        Command command;
        int nextMoveTime;
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
                        nextMoveTime =  Integer.parseInt(influenceNode.getAttributes().getNamedItem("movesRemaining").getTextContent());
                        speed = Long.parseLong(influenceNode.getAttributes().getNamedItem("speed").getTextContent());
                        range = Integer.parseInt(influenceNode.getAttributes().getNamedItem("range").getTextContent());
                        orientation = Orientation.toOrientation(influenceNode.getAttributes().getNamedItem("orientation").getTextContent());

                        switch (influenceNode.getNodeName().toLowerCase()) {
                            case "angularinfluenceeffect":
                                influencesToAdd.add(new AngularInfluenceEffect((SettableCommand) command, range, speed, orientation, nextMoveTime)); // TODO: is this POOP?
                                break;

                            case "linearinfluenceeffect":
                                influencesToAdd.add(new LinearInfluenceEffect((SettableCommand) command, range, speed, orientation, nextMoveTime)); // TODO: is this POOP?
                                break;

                            case "radialinfluenceeffect":
                                influencesToAdd.add(new RadialInfluenceEffect((SettableCommand) command, range, speed, orientation, nextMoveTime)); // TODO: is this POOP?
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
        Command command;

        NodeList effectValues = element.getElementsByTagName("VALUE");
        for(int i = 0; i < effectValues.getLength(); i++) {
            NodeList effectNodes = effectValues.item(i).getChildNodes();

            for(int j = 0; j < effectNodes.getLength(); j++) {
                Node effectNode = effectNodes.item(j);
                if(effectNode.getNodeType() == Node.ELEMENT_NODE) {
                    command = processCommand(effectNode.getChildNodes());

                    if(command != null) {
                        switch (effectNode.getNodeName().toLowerCase()) {
                            case "oneshotareaeffect":
                                effectsToAdd.add(new OneShotAreaEffect(command));
                                break;

                            case "infiniteareaeffect":
                                effectsToAdd.add(new InfiniteAreaEffect(command));
                                break;
                        }
                    }
                }
            }
        }

        for(int i = 0; i < pointsToAdd.size(); i++) {
            level.addAreaEffectTo(pointsToAdd.get(i), effectsToAdd.get(i));
        }
    }

    private Command processCommand(NodeList childNodes) {
        int amount;
        boolean hasFired;
        for(int i = 0; i < childNodes.getLength(); i++) {
            Node commandNode = childNodes.item(i);

            if(commandNode.getNodeType() == Node.ELEMENT_NODE) {
                switch (commandNode.getNodeName().toLowerCase()) {
                    case "addhealthcommand":
                        amount = Integer.parseInt(commandNode.getAttributes().getNamedItem("amount").getTextContent());
                        return new AddHealthCommand(amount);
                    case "removehealthcommand":
                        amount = Integer.parseInt(commandNode.getAttributes().getNamedItem("amount").getTextContent());
                        return new RemoveHealthCommand(amount);

                    case "togglehealthcommand":
                        amount = Integer.parseInt(commandNode.getAttributes().getNamedItem("amount").getTextContent());
                        hasFired = Boolean.parseBoolean(commandNode.getAttributes().getNamedItem("hasFired").getTextContent());
                        return new ToggleHealthCommand(amount, hasFired);

                    case "togglemanacommand":
                        amount = Integer.parseInt(commandNode.getAttributes().getNamedItem("amount").getTextContent());
                        hasFired = Boolean.parseBoolean(commandNode.getAttributes().getNamedItem("hasFired").getTextContent());
                        return new ToggleManaCommand(amount, hasFired);

                    case "togglespeedcommand":
                        amount = Integer.parseInt(commandNode.getAttributes().getNamedItem("amount").getTextContent());
                        hasFired = Boolean.parseBoolean(commandNode.getAttributes().getNamedItem("hasFired").getTextContent());
                        return new ToggleSpeedCommand(amount, hasFired);

                    case "instadeathcommand":
                        return new InstaDeathCommand();

                    case "levelupcommand":
                        return new LevelUpCommand();

                    case "setassneakingcommand": // TODO: save stealthAmount var
                        amount = Integer.parseInt(commandNode.getAttributes().getNamedItem("amount").getTextContent());
                        return new ToggleSneaking(amount);

                     /* Game Loop Commands */
                    case "bartercommand": // TODO: implement
                        break;

                    case "dialogcommand": // TODO: implement
                        break;

                    case "observeentitycommand": // TODO: implement
                        break;

                    /* Game Model Commands */
                    case "confuseentitycommand": // TODO: implement
                        break;

                    case "freezeentitycommand": // TODO: implement
                        break;

                    case "slowentitycommand": // TODO: implement
                        break;

                    case "teleportentitycommand": // TODO: implement
                        break;

                    /* Level Commands */
                    case "disarmtrapcommand": // TODO: implement
                        break;

                    case "dropitemcommand": // TODO: implement
                        break;

                    case "pickpocketcommand": // TODO: implement
                        break;

                    case "sendinfluencecommand": // TODO: implement
                        break;
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
        this.currentLevel = loadLevel(currentLevel);
        this.world = loadWorld(levelList);
        this.entity = loadPlayer(player);
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
        boolean moveable;
        int noise;
        int goldAmount;
        int maxGold;
        int attackPoints;
        int attackModifier;
        int defensePoints;
        int defenseModifier;
        int speedAmount;
        int manaPoints;
        int maxMana;
        int sight;
        int currentHealth;
        int maxHealth;
        int currentExperience;
        int levelAmount;
        int experienceToNextLevel;
        String reference = entityNode.getAttributes().getNamedItem("reference").getTextContent();

        if(entityRef.containsKey(reference)) {
            return entityRef.get(reference);
        }

        else {
            noise = Integer.parseInt(entityNode.getAttributes().getNamedItem("noiseLevel").getTextContent());
            noiseLevel = new NoiseLevel(noise);

            goldAmount = Integer.parseInt(entityNode.getAttributes().getNamedItem("goldAmount").getTextContent());
            maxGold = Integer.parseInt(entityNode.getAttributes().getNamedItem("maxGold").getTextContent());
            gold = new Gold(goldAmount, maxGold);

            speedAmount = Integer.parseInt(entityNode.getAttributes().getNamedItem("speed").getTextContent());
            speed = new Speed(speedAmount);

            manaPoints = Integer.parseInt(entityNode.getAttributes().getNamedItem("manaPoints").getTextContent());
            maxMana = Integer.parseInt(entityNode.getAttributes().getNamedItem("maxMana").getTextContent());
            mana = new Mana(manaPoints, maxMana, 0 );  // TODO: BRYAN ADD THIS REGENRATE PLS

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
            processWeaponSkillsList(entityNode, weaponSkills, skillLevelsMap);
            processNonWeaponSkillsList(entityNode, nonWeaponSkills, skillLevelsMap);
            mount = processEntityMount(entityNode);
            equipment = processEquipment(entityNode);
            hotBar = processHotBar(entityNode);
            inventory = processInventory(entityNode);

            entity = new Entity(null, hotBar, weaponSkills, nonWeaponSkills, skillLevelsMap, velocity,
                    noiseLevel, sightRadius, xpLevel, health, mana, speed, gold, attack, defense, equipment,
                    inventory, orientation, compatableTerrain, moveable, mount);

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
        for(int i = 0; i < nodeList.getLength(); i++) {

            Node node = nodeList.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                loadMaps(element.getChildNodes(), level);
            }
        }

        return level;
    }

    private ArrayList<Level> processLevelList(NodeList nodeList) {
        ArrayList<Level> levelList = new ArrayList<>();

        for(int i = 0; i < nodeList.getLength(); i++) {

            Node node = nodeList.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE) {
                Level level = new Level();
                Element element = (Element) node;
                loadMaps(element.getChildNodes(), level);
                levelList.add(level);
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

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public List<Level> getWorld() {
        return world;
    }
}
