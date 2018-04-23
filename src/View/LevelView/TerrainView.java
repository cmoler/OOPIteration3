package View.LevelView;


import Configs.Commons;
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
    private boolean isShrouded;
    private boolean isSeen;
    private static Sprites sprites;
    private Image fogSprite;
    private Image blackHex;

    public TerrainView(Terrain terrain, Point3D location) {
        super(location, 4);

        sprites = Sprites.getInstance();





        setSprite(sprites.getTerrainSprite(terrain));
        fogSprite = sprites.getFogSprite();
        blackHex = sprites.getBlackHex();
        this.location = location;

        setShrouded(false);
        setSeen(false);


    }


    @Override
    public void notifyViewElement() {

    }





    @Override
    public void render(GraphicsContext gc, Point2D playerPos, Point2D scrollOffset) {

        if(!isSeen()) {//Tile hasn't been seen yet, render black hex
            renderHex(gc, playerPos, scrollOffset, blackHex);
            return;
        }

        //Render terrain sprite
        super.render(gc, playerPos, scrollOffset);


    }






    @Override
    public int getRenderPriority() {
        if(!isSeen()) {
            return 0;
        }
        return super.getRenderPriority();
    }

    @Override
    public void notifyViewElementDeath() {

    }
}
