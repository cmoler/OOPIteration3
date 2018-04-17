package ControllerTests;

import Controller.GameLoader;
import Controller.Visitor.SavingVisitor;
import Model.AreaEffect.InfiniteAreaEffect;
import Model.AreaEffect.OneShotAreaEffect;
import Model.Command.EntityCommand.SettableCommand.AddHealthCommand;
import Model.Command.EntityCommand.SettableCommand.RemoveHealthCommand;
import Model.Entity.EntityAttributes.Orientation;
import Model.InfluenceEffect.AngularInfluenceEffect;
import Model.InfluenceEffect.LinearInfluenceEffect;
import Model.InfluenceEffect.RadialInfluenceEffect;
import Model.Level.GameModel;
import Model.Level.Level;
import Model.Level.LevelMessenger;
import Model.Level.Terrain;
import View.LevelView.LevelViewElement;
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
    public void init() throws IOException {
        ArrayList<Level> levels = new ArrayList<>();
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

        levels.add(level);
        levels.add(new Level(new ArrayList<LevelViewElement>()));
        gameModel = new GameModel(level, null, levels, null, null);
    }

    @Test
    public void testSavingTerrains() throws ParserConfigurationException, SAXException, IOException {
        savingVisitor.visitGameModel(gameModel);
        gameLoader.loadGame("TESTSAVE.xml");
        Level levelToTest = gameLoader.getCurrentLevel();
        Map<Point3D, Terrain> terrainsToTest = levelToTest.getTerrainLocations();
        assertTrue(terrainsToTest.size() == 2);
        assertTrue(terrainsToTest.get(new Point3D(0,0,0)) == Terrain.GRASS);
        assertTrue(terrainsToTest.get(new Point3D(0,0,1)) == Terrain.MOUNTAINS);
    }
}
