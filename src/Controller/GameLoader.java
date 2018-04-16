package Controller;

import Model.AreaEffect.AreaEffect;
import Model.AreaEffect.InfiniteAreaEffect;
import Model.AreaEffect.OneShotAreaEffect;
import Model.Command.Command;
import Model.Command.EntityCommand.InstaDeathCommand;
import Model.Command.EntityCommand.LevelUpCommand;
import Model.Command.EntityCommand.SetAsSneakingCommand;
import Model.Command.EntityCommand.SettableEntityCommand.AddHealthCommand;
import Model.Command.EntityCommand.SettableEntityCommand.RemoveHealthCommand;
import Model.Command.EntityCommand.ToggleableCommand.ToggleHealthCommand;
import Model.Command.EntityCommand.ToggleableCommand.ToggleManaCommand;
import Model.Command.EntityCommand.ToggleableCommand.ToggleSpeedCommand;
import Model.Entity.EntityAttributes.Orientation;
import Model.InfluenceEffect.AngularInfluenceEffect;
import Model.InfluenceEffect.InfluenceEffect;
import Model.InfluenceEffect.LinearInfluenceEffect;
import Model.InfluenceEffect.RadialInfluenceEffect;
import Model.Level.GameModel;
import Model.Level.Level;
import Model.Level.Terrain;
import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import com.sun.org.apache.xerces.internal.parsers.XMLDocumentParser;
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
                }
            }
        }
    }

    private void processInfluenceEffects(Element element, Level level) {
        List<Point3D> pointsToAdd = new ArrayList<>();
        List<InfluenceEffect> influencesToAdd = new ArrayList<>();
        Command command;
        int nextMoveTime;
        long speed;
        Orientation orientation;
        int range;

        NodeList influenceKeys = element.getElementsByTagName("KEY");
        for(int i = 0; i < influenceKeys.getLength(); i++) {
            String key = influenceKeys.item(i).getAttributes().item(0).getTextContent();
            String[] point = key.split(",");
            Point3D keyPoint = new Point3D(Integer.parseInt(point[0]), Integer.parseInt(point[1]), Integer.parseInt(point[2]));
            pointsToAdd.add(keyPoint);
        }

        NodeList influenceValues = element.getElementsByTagName("VALUE");
        for(int i = 0; i < influenceValues.getLength(); i++) {
            NodeList influenceNodes = influenceValues.item(i).getChildNodes();

            for(int j = 0; j < influenceNodes.getLength(); j++) {
                Node influenceNode = influenceNodes.item(j);
                if(influenceNode.getNodeType() == Node.ELEMENT_NODE) {
                    command = processCommand(influenceNode.getChildNodes());

                    if(command != null) {
                        nextMoveTime =  Integer.parseInt(influenceNode.getAttributes().getNamedItem("nextMoveTime").getTextContent());
                        speed = Long.parseLong(influenceNode.getAttributes().getNamedItem("speed").getTextContent());
                        range = Integer.parseInt(influenceNode.getAttributes().getNamedItem("range").getTextContent());
                        orientation = Orientation.toOrientation(influenceNode.getAttributes().getNamedItem("orientation").getTextContent());

                        switch (influenceNode.getNodeName().toLowerCase()) {
                            case "angularinfluenceeffect":
                                influencesToAdd.add(new AngularInfluenceEffect(command, range, speed, orientation, nextMoveTime));
                                break;

                            case "linearinfluenceeffect":
                                influencesToAdd.add(new LinearInfluenceEffect(command, range, speed, orientation, nextMoveTime));
                                break;

                            case "radialinfluenceeffect":
                                influencesToAdd.add(new RadialInfluenceEffect(command, range, speed, orientation, nextMoveTime));
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
        List<Point3D> pointsToAdd = new ArrayList<>();
        List<AreaEffect> effectsToAdd = new ArrayList<>();
        Command command;

        NodeList effectKeys = element.getElementsByTagName("KEY");
        for(int i = 0; i < effectKeys.getLength(); i++) {
            String key = effectKeys.item(i).getAttributes().item(0).getTextContent();
            String[] point = key.split(",");
            Point3D keyPoint = new Point3D(Integer.parseInt(point[0]), Integer.parseInt(point[1]), Integer.parseInt(point[2]));
            pointsToAdd.add(keyPoint);
        }

        NodeList effectValues = element.getElementsByTagName("VALUE");
        for(int i = 0; i < effectValues.getLength(); i++) {
            NodeList effectNodes = effectValues.item(i).getChildNodes();

            for(int j = 0; j < effectNodes.getLength(); j++) {
                Node effectNode = effectNodes.item(j);
                if(effectNode.getNodeType() == Node.ELEMENT_NODE) {
                    command = processCommand(effectNode.getChildNodes());

                    if(command != null) {
                        switch (effectNode.getNodeName().toLowerCase()) {
                            case "oneshotarea":
                                effectsToAdd.add(new OneShotAreaEffect(command));
                                break;

                            case "infinitearea":
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

                    case "setassneakingcommand":
                        return new SetAsSneakingCommand();

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
        List<Point3D> pointsToAdd = new ArrayList<>();
        List<Terrain> terrainsToAdd = new ArrayList<>();

        NodeList terrainKeys = element.getElementsByTagName("KEY");
        for(int terrainIter = 0; terrainIter < terrainKeys.getLength(); terrainIter++) {
            String key = terrainKeys.item(terrainIter).getAttributes().item(0).getTextContent();
            String[] point = key.split(",");
            Point3D keyPoint = new Point3D(Integer.parseInt(point[0]), Integer.parseInt(point[1]), Integer.parseInt(point[2]));
            pointsToAdd.add(keyPoint);
        }

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

                case "mountain":
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

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public List<Level> getWorld() {
        return world;
    }
}
