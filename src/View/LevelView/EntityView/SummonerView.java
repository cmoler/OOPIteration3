package View.LevelView.EntityView;

import Model.Entity.Entity;
import javafx.geometry.Point3D;
import javafx.scene.image.Image;

import java.io.File;

public class SummonerView extends EntityView {

    public SummonerView(Entity entity, Point3D location) {
        super(entity, location);

        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/mage.png");

        setSprite(new Image(file.toURI().toString()));
    }

    @Override
    public void notifyViewElementDeath() {

    }
}
