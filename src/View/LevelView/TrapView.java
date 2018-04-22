package View.LevelView;

import Model.Level.Mount;
import Model.Level.Trap;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.File;

public class TrapView extends LevelViewElement{

    private Trap trap;
    private boolean isDetected;
    private boolean isDisarmed;

    public TrapView(Trap trap, Point3D location) {
        super(location, 2);

        this.trap = trap;

        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/scroll.png");

        setSprite(new Image(file.toURI().toString()));
        isDetected = trap.getIsVisible();

    }

    @Override
    public void render(GraphicsContext gc, Point2D playerPos, Point2D scrollOffset) {
        if(isDetected) {
            super.render(gc, playerPos, scrollOffset);
        }
    }

    @Override
    public void notifyViewElement() {
        System.out.println("Notified");
        isDetected = trap.getIsVisible();
        if(trap.getIsDisarmed()) {
            setAsDisarmed();
        }
    }

    @Override
    public void notifyViewElementDeath() {

    }

    private void setAsDisarmed() {

    }

}
