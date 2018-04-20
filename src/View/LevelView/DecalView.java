package View.LevelView;

import Model.Level.Decal;
import javafx.geometry.Point3D;

public class DecalView extends LevelViewElement { // TODO: implement

    // private Decal decal; TODO: needed?

    public DecalView(/*Decal decal,*/ Point3D location) {
        super(location, 2);
        //  this.decal = decal; TODO: needed?


        //   String workingDir = System.getProperty("user.dir");

        // File file = new File(workingDir + "/src/View/Assets/warrior.png");

        //   setSprite(new Image(file.toURI().toString()));
    }

    @Override
    public void notifyViewElement() {

    }


}