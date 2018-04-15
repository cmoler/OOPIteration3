package Controller;

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
    private Level level;

    public GameLoader() {
        parser = new XMLDocumentParser();
        level = new Level(new ArrayList<>());
    }

    public void loadGame(String fileName) throws IOException, SAXException, ParserConfigurationException {
        File file = new File(fileName);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(file);

        loadLevelMaps(document);
    }

    private void loadTerrains(NodeList nodeList) {
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

    private void loadLevelMaps(Document document) {
        NodeList nodeList = document.getElementsByTagName("LEVELMAP");
        loadTerrains(nodeList);
    }

    public Level getLevel() {
        return level;
    }
}
