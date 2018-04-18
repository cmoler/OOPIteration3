package Controller;

import Model.AreaEffect.AreaEffect;
import Model.AreaEffect.InfiniteAreaEffect;
import Model.AreaEffect.OneShotAreaEffect;
import Model.Command.Command;
import Model.Command.EntityCommand.NonSettableCommand.InstaDeathCommand;
import Model.Command.EntityCommand.NonSettableCommand.LevelUpCommand;
import Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand.ToggleableCommand;
import Model.Command.EntityCommand.SettableCommand.ToggleableCommand.ToggleSneaking;
import Model.Command.EntityCommand.SettableCommand.AddHealthCommand;
import Model.Command.EntityCommand.SettableCommand.RemoveHealthCommand;
import Model.Command.EntityCommand.SettableCommand.SettableCommand;
import Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand.ToggleHealthCommand;
import Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand.ToggleManaCommand;
import Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand.ToggleSpeedCommand;
import Model.Entity.EntityAttributes.Orientation;
import Model.Entity.EntityAttributes.Speed;
import Model.InfluenceEffect.AngularInfluenceEffect;
import Model.InfluenceEffect.InfluenceEffect;
import Model.InfluenceEffect.LinearInfluenceEffect;
import Model.InfluenceEffect.RadialInfluenceEffect;
import Model.Item.InteractiveItem;
import Model.Item.Item;
import Model.Item.OneShotItem;
import Model.Item.TakeableItem.ArmorItem;
import Model.Item.TakeableItem.ConsumableItem;
import Model.Item.TakeableItem.RingItem;
import Model.Item.TakeableItem.WeaponItem;
import Model.Level.*;
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
import java.util.List;

public class GameLoader {

    private Level currentLevel;
    private GameModel gameModel;
    private List<Level> world;

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

                    mountsToAdd.add(new Mount(orientation, speed, terrains, null));
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

                        traps.add(new Trap(null, command, isVisible, isDisarmed, trapStrength));
                    }
                }
            }
        }

        for(int i = 0; i < pointsToAdd.size(); i++) {
            level.addTrapTo(pointsToAdd.get(i), traps.get(i));
        }
    }

    private void processEntities(Element element, Level level) {
    }

    private void processItems(Element element, Level level) {
        List<Point3D> pointsToAdd = getKeyPoints(element);
        List<Item> itemsToAdd = new ArrayList<>();
        Command command;
        String name;

        NodeList itemValues = element.getElementsByTagName("VALUE");
        for (int i = 0; i < itemValues.getLength(); i++) {
            NodeList itemNodes = itemValues.item(i).getChildNodes();

            for(int j = 0; j < itemNodes.getLength(); j++) {
                Node itemNode = itemNodes.item(j);
                if(itemNode.getNodeType() == Node.ELEMENT_NODE) {
                    command = processCommand(itemNode.getChildNodes());

                    if(command != null) {
                        name = itemNode.getAttributes().getNamedItem("name").getTextContent();
//                        System.out.println(itemNode.getNodeName().toLowerCase());
                        switch (itemNode.getNodeName().toLowerCase()) {
                            case "oneshotitem":
                                itemsToAdd.add(new OneShotItem(name, command));
                                break;

                            case "interactiveitem":
                                itemsToAdd.add(new InteractiveItem(name, command));
                                break;

                            case "armoritem":
                                int defense = Integer.parseInt(itemNode.getAttributes().getNamedItem("defense").getTextContent());
                                itemsToAdd.add(new ArmorItem(name, command, defense));
                                break;

                            case "consumableitem":
                                itemsToAdd.add(new ConsumableItem(name, command));
                                break;

                            case "ringitem":
                                itemsToAdd.add(new RingItem(name, (ToggleableCommand) command));
                                break;

                            case "weaponitem": //TODO: this needs to be changed
                                itemsToAdd.add(new WeaponItem(name, (SettableCommand) command));
                                break;
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
                        amount = Integer.parseInt(commandNode.getAttributes().getNamedItem("healAmount").getTextContent());
                        return new AddHealthCommand(amount);
                    case "removehealthcommand":
                        amount = Integer.parseInt(commandNode.getAttributes().getNamedItem("damageAmount").getTextContent());
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
                    case "bartercommand":
                        break;

                    case "dialogcommand":
                        break;

                    case "observeentitycommand":
                        break;

                    /* Game Model Commands */
                    case "confuseentitycommand":
                        break;

                    case "freezeentitycommand":
                        break;

                    case "slowentitycommand":
                        break;

                    case "teleportentitycommand":
                        break;

                    /* Level Commands */
                    case "disarmtrapcommand":
                        break;

                    case "dropitemcommand":
                        break;

                    case "pickpocketcommand":
                        break;

                    case "sendinfluencecommand":
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
        this.currentLevel = loadLevel(currentLevel);
        this.world = loadWorld(levelList);
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
        Level level = new Level(new ArrayList<>());

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
                Level level = new Level(new ArrayList<>());
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
