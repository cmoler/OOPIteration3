package View.LevelView;


import Model.Entity.EntityAttributes.Orientation;
import Model.Level.Terrain;
import View.Sprites;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class TerrainView extends LevelViewElement{
    private Point3D location;
    private Image hex;
    private static Sprites sprites;

    public TerrainView(Terrain terrain, Point3D location) {
        super(location);

        sprites = Sprites.getInstance();





        setSprite(sprites.getTerrainSprite(terrain));
        this.location = location;



    }


    @Override
    public void notifyViewElement() {

    }



    @Override
    public int getRenderPriority() {
        return 4;
    }


}
