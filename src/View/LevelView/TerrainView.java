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

        if(isShrouded()) {//Render fog if its shrouded
            renderHex(gc, playerPos, scrollOffset, fogSprite);
        }
    }

    private void renderHex(GraphicsContext gc, Point2D playerPos, Point2D scrollOffset, Image renderSprite) {
        int width = getSize();
        int height = (int)(width * (Math.sqrt(3)/2));

        HexMathHelper hexMathHelper = new HexMathHelper();
        int xOffset = hexMathHelper.getXCoord(location)-(int)playerPos.getX();
        int yOffset = hexMathHelper.getYCoord(location) - (int)playerPos.getY();

        rotate(gc, getOrientation().getDegreeOfOrientation(getOrientation()), ((xOffset*width)*.75)+(width/2) + Commons.SCREEN_WIDTH/2 + scrollOffset.getX(), (yOffset*(height/2))+(height/2) + Commons.SCREEN_HEIGHT/2 + scrollOffset.getY());
        gc.drawImage(renderSprite, (int)((xOffset*width)*.75) + Commons.SCREEN_WIDTH/2 + scrollOffset.getX(), (yOffset*(height/2)) + Commons.SCREEN_HEIGHT/2 + scrollOffset.getY(), width, height);
    }


    public boolean isShrouded() {
        return isShrouded;
    }

    public void setShrouded(boolean shrouded) {
        if(!shrouded) { setSeen(true); }//Tile is unshrouded at least once, therefor seen
        isShrouded = shrouded;
    }

    private void setSeen(boolean seen) {
        this.isSeen = seen;
    }
    private boolean isSeen() {
        return isSeen;
    }

    @Override
    public int getRenderPriority() {
        if(isShrouded() || !isSeen()) {
            return 0;
        }
        return super.getRenderPriority();
    }

    @Override
    public void notifyViewElementDeath() {

    }
}
