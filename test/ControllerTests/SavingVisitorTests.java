package ControllerTests;

import Controller.GameLoader;
import Controller.Visitor.SavingVisitor;
import Model.AreaEffect.AreaEffect;
import Model.AreaEffect.InfiniteAreaEffect;
import Model.AreaEffect.OneShotAreaEffect;
import Model.Command.EntityCommand.NonSettableCommand.TeleportEntityCommand;
import Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand.ToggleHealthCommand;
import Model.Command.EntityCommand.SettableCommand.AddHealthCommand;
import Model.Command.EntityCommand.SettableCommand.RemoveHealthCommand;
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
import Model.Level.*;
import View.LevelView.LevelViewElement;
import com.sun.javafx.geom.Vec3d;
import javafx.geometry.Point3D;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;

public class SavingVisitorTests {

    private SavingVisitor savingVisitor;
    private GameLoader gameLoader;
    private Level level;
    private GameModel gameModel;

    @Before
    public void init() throws IOException, ParserConfigurationException, SAXException {
        ArrayList<Level> levels = new ArrayList<>();
        ArrayList<Terrain> mountTerrain = new ArrayList<Terrain>(){{ add(Terrain.GRASS); add(Terrain.WATER); }};
        savingVisitor = new SavingVisitor("TESTSAVE.xml");
        gameLoader = new GameLoader();
        level = new Level(new ArrayList<>());
        level.addTerrainTo(new Point3D(0,0,0), Terrain.GRASS);
        level.addTerrainTo(new Point3D(0,0,1), Terrain.MOUNTAINS);

        level.addAreaEffectTo(new Point3D(0,0,0), new InfiniteAreaEffect(new RemoveHealthCommand(5)));
        level.addAreaEffectTo(new Point3D(0,0,1), new OneShotAreaEffect(new AddHealthCommand(10)));

        level.addInfluenceEffectTo(new Point3D(0,0,0), new LinearInfluenceEffect(new AddHealthCommand(5), 0, 0, Orientation.NORTH));
        level.addInfluenceEffectTo(new Point3D(0,0,1), new RadialInfluenceEffect(new AddHealthCommand(5), 0, 0, Orientation.NORTH));
        level.addInfluenceEffectTo(new Point3D(0,0,2), new AngularInfluenceEffect(new AddHealthCommand(5), 0, 0, Orientation.NORTH));

        level.addItemnTo(new Point3D(0,0,0), new InteractiveItem("door", new RemoveHealthCommand(10)));
        level.addItemnTo(new Point3D(0,0,1), new OneShotItem("bomb", new RemoveHealthCommand(10)));
        level.addItemnTo(new Point3D(0,0,2), new ArmorItem("helemet", new AddHealthCommand(10), 10));
        level.addItemnTo(new Point3D(0,0,3), new RingItem("ring", new ToggleHealthCommand(10)));
        level.addItemnTo(new Point3D(0,0,4), new ConsumableItem("potion", new AddHealthCommand(10)));

        level.addTrapTo(new Point3D(0,0,0), new Trap(null, new RemoveHealthCommand(0), false, false, 10));

        level.addObstacleTo(new Point3D(0,0,5), new Obstacle());

        level.addRiverTo(new Point3D(0,0,0), new River(new Vec3d(0,0,0)));

        level.addMountTo(new Point3D(0,0,0), new Mount(Orientation.NORTH, new Speed(10), mountTerrain, null));

        levels.add(level);
        levels.add(new Level(new ArrayList<LevelViewElement>()));
        gameModel = new GameModel(level, null, levels, null, null);
        savingVisitor.visitGameModel(gameModel);
        gameLoader.loadGame("TESTSAVE.xml");
    }

    @Test
    public void testSavingTerrains() {
        Level levelToTest = gameLoader.getCurrentLevel();
        Map<Point3D, Terrain> terrainsToTest = levelToTest.getTerrainLocations();
        assertTrue(terrainsToTest.get(new Point3D(0,0,0)) == Terrain.GRASS);
        assertTrue(terrainsToTest.get(new Point3D(0,0,1)) == Terrain.MOUNTAINS);
    }

    @Test
    public void testSavingAreaEffectsAndLoad() {
        Level levelToTest = gameLoader.getCurrentLevel();
        Map<Point3D, AreaEffect> areasToTest = levelToTest.getAreaEffectLocations();
        assertTrue(areasToTest.get(new Point3D(0,0,0)) instanceof InfiniteAreaEffect);
        assertTrue(areasToTest.get(new Point3D(0,0,1)) instanceof OneShotAreaEffect);
    }

    @Test
    public void testSavingInfluencesAndLoad() {
        Level levelToTest = gameLoader.getCurrentLevel();
        Map<Point3D, InfluenceEffect> influencesToTest = levelToTest.getInfluencesMap();
        assertTrue(influencesToTest.get(new Point3D(0,0,0)) instanceof LinearInfluenceEffect);
        assertTrue(influencesToTest.get(new Point3D(0,0,1)) instanceof RadialInfluenceEffect);
        assertTrue(influencesToTest.get(new Point3D(0,0,2)) instanceof AngularInfluenceEffect);
    }

    @Test
    public void testSavingItemsAndLoad() {
        Level levelToTest = gameLoader.getCurrentLevel();
        Map<Point3D, Item> itemsToTest = levelToTest.getItemLocations();
        assertTrue(itemsToTest.get(new Point3D(0,0,0)) instanceof InteractiveItem);
        assertTrue(itemsToTest.get(new Point3D(0,0,1)) instanceof OneShotItem);
        assertTrue(itemsToTest.get(new Point3D(0,0,2)) instanceof ArmorItem);
        assertTrue(itemsToTest.get(new Point3D(0,0,3)) instanceof RingItem);
        assertTrue(itemsToTest.get(new Point3D(0,0,4)) instanceof ConsumableItem);
    }

    @Test
    public void testTrapsSaveAndLoad() {
        Level levelToTest = gameLoader.getCurrentLevel();
        Map<Point3D, Trap> trapsToTest = levelToTest.getTrapLocations();
        assertTrue(trapsToTest.get(new Point3D(0,0,0)) instanceof Trap);
    }

    @Test
    public void testObstaclesSaveAndLoad() {
        Level levelToTest = gameLoader.getCurrentLevel();
        Map<Point3D, Obstacle> testedObstacles = levelToTest.getObstacleLocations();
        assertTrue(!testedObstacles.isEmpty());
    }

    @Test
    public void testRiversSaveAndLoad() {
        Level levelToTest = gameLoader.getCurrentLevel();
        Map<Point3D, River> riverMap = levelToTest.getRiverLocations();
        assertTrue(!riverMap.isEmpty());
    }

    @Test
    public void testMountsSaveAndLoad() {
        Level levelToTest = gameLoader.getCurrentLevel();
        Map<Point3D, Mount> mountMap = levelToTest.getMountLocations();
        assertTrue(!mountMap.isEmpty());
        assertTrue(mountMap.get(new Point3D(0,0,0)).speedToString().equals("10"));
        assertTrue(mountMap.get(new Point3D(0,0,0)).getPassableTerrain().get(0) == Terrain.GRASS);
        assertTrue(mountMap.get(new Point3D(0,0,0)).getPassableTerrain().get(1) == Terrain.WATER);
        assertTrue(mountMap.get(new Point3D(0,0,0)).getOrientation() == Orientation.NORTH);
    }
}
