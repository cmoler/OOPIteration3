package View.LevelView;

import Model.Level.Mount;
import Model.Level.Trap;
import javafx.geometry.Point3D;
import javafx.scene.image.Image;

import java.io.File;

public class TrapView extends LevelViewElement{

    private Trap trap;

    public TrapView(Trap trap, Point3D location) {
        super(location);

        this.trap = trap;

        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/trap.png");

        setSprite(new Image(file.toURI().toString()));
    }
    @Override
    public void notifyViewElement() {

    }

    @Override
    public int getRenderPriority() {
        return 1;
    }
}
