package View.LevelView;

import Model.AreaEffect.AreaEffect;
import javafx.geometry.Point3D;
import javafx.scene.image.Image;

import java.io.File;

public class AreaEffectView extends LevelViewElement {

    public AreaEffectView(Point3D location) {
        super(location, 2);

        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/fountain.png");

        setSprite(new Image(file.toURI().toString()));
    }

    @Override
    public void notifyViewElement() {

    }

    @Override
    public void notifyViewElementDeath() {

    }

    public void setInstantDeath() {
        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/skull.png");

        setSprite(new Image(file.toURI().toString()));
    }

    public void setLevelUp() {
        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/star.png");

        setSprite(new Image(file.toURI().toString()));
    }

    public void setHealthPool() {
        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/health2.png");

        setSprite(new Image(file.toURI().toString()));
    }

    public void setDamagePool() {
        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/lava.png");

        setSprite(new Image(file.toURI().toString()));
    }
}
