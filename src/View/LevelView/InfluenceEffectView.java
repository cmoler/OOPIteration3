package View.LevelView;

import Model.InfluenceEffect.InfluenceEffect;
import javafx.geometry.Point3D;
import javafx.scene.image.Image;

import java.io.File;

public class InfluenceEffectView extends LevelViewElement {

    private InfluenceEffect effect;

    public InfluenceEffectView(InfluenceEffect effect, Point3D location) {
        super(location, 2);
        this.effect = effect;

        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/bluefireball.png");

        setSprite(new Image(file.toURI().toString()));

        effect.setObserver(this);
    }

    @Override
    public void notifyViewElement() {
        setOrientation(effect.getOrientation());
    }

    @Override
    public void notifyViewElementDeath() {

    }
    public void setArrow() {
        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/arrow.png");

        setSprite(new Image(file.toURI().toString()));
    }
}