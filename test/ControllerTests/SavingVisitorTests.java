package ControllerTests;

import Controller.Visitor.SavingVisitor;
import Model.AreaEffect.InfiniteAreaEffect;
import Model.AreaEffect.OneShotAreaEffect;
import Model.Command.EntityCommand.SettableCommand.AddHealthCommand;
import Model.Command.EntityCommand.SettableCommand.RemoveHealthCommand;
import Model.Level.Level;
import Model.Level.Terrain;
import javafx.geometry.Point3D;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

public class SavingVisitorTests {

    private SavingVisitor savingVisitor;
    private Level level;

    @Before
    public void init() throws IOException {
        savingVisitor = new SavingVisitor("TESTSAVE.xml");
        level = new Level(new ArrayList<>());
        level.addTerrainTo(new Point3D(0,0,0), Terrain.GRASS);
        level.addTerrainTo(new Point3D(0,0,1), Terrain.MOUNTAINS);
        level.addAreaEffectTo(new Point3D(0,0,0), new InfiniteAreaEffect(new RemoveHealthCommand(5)));
        level.addAreaEffectTo(new Point3D(0,0,1), new OneShotAreaEffect(new AddHealthCommand(10)));
    }

    @Test
    public void testSavingTerrains() {
        savingVisitor.visitLevel(level);
    }
}
