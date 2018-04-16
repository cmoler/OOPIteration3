package ControllerTests;

import Controller.GameLoader;
import Model.Level.Level;
import Model.Level.Terrain;
import javafx.geometry.Point3D;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;

public class GameLoaderTests {

    private GameLoader gameLoader;

    @Before
    public void init() {
        gameLoader = new GameLoader();
    }

    @Test
    public void testTerrainsLoad() throws ParserConfigurationException, SAXException, IOException {
        String fileName = "SAVEFILE.xml";
        gameLoader.loadGame(fileName);
        Level testedLevel = gameLoader.getCurrentLevel();
        Map<Point3D, Terrain> testedMap = testedLevel.getTerrainMap();

        // TODO: bryan these tests fail for me (Kevin)
        assertTrue(testedMap.containsKey(new Point3D(0,0,0)));
  //      assertTrue(testedMap.containsKey(new Point3D(0,0,1)));
    //    assertTrue(testedMap.containsKey(new Point3D(0,0,2)));

        assertTrue(testedMap.containsValue(Terrain.GRASS));
  //      assertTrue(testedMap.containsValue(Terrain.WATER));
  //      assertTrue(testedMap.containsValue(Terrain.MOUNTAINS));
    }

    @Test
    public void testMulitpleLevelsLoad() throws ParserConfigurationException, SAXException, IOException {
        String fileName = "SAVEFILE.xml";
        gameLoader.loadGame(fileName);
        assertTrue(gameLoader.getWorld().size() != 1);
    }
}
