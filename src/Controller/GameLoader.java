package Controller;

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

    private XMLDocumentParser parser;
    private DOMParser domParser;
    private Level currentLevel;
    private GameModel gameModel;
    private List<Level> world;

    public GameLoader() {
        parser = new XMLDocumentParser();
        world = new ArrayList<>();
    }

    public void loadGame(String fileName) throws IOException, SAXException, ParserConfigurationException {
        File file = new File(fileName);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(file);

        loadGameModel(document);
    }

    private void loadTerrains(NodeList nodeList, Level level) {
        for (int temp = 0; temp < nodeList.getLength(); temp++) {
            Node node = nodeList.item(temp);

            if(node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element)node;
                switch(element.getAttribute("id")) {
                    case "TERRAIN":
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
                        break;

                    default:
                }
            }
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
                loadTerrains(element.getChildNodes(), level);
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
                loadTerrains(element.getChildNodes(), level);
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
