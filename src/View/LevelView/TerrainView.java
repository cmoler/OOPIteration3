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
    private static Sprites sprites;
    private Image fogSprite;

    public TerrainView(Terrain terrain, Point3D location) {
        super(location, 4);

        sprites = Sprites.getInstance();





        setSprite(sprites.getTerrainSprite(terrain));
        fogSprite = sprites.getFogSprite();
        this.location = location;



    }


    @Override
    public void notifyViewElement() {

    }





    @Override
    public void render(GraphicsContext gc, Point2D playerPos, Point2D scrollOffset) {
        super.render(gc, playerPos, scrollOffset);

        if(isShrouded()) {
            renderFog(gc, playerPos, scrollOffset);
        }
    }

    private void renderFog(GraphicsContext gc, Point2D playerPos, Point2D scrollOffset) {
        int width = getSize();
        int height = (int)(width * (Math.sqrt(3)/2));

        HexMathHelper hexMathHelper = new HexMathHelper();
        int xOffset = hexMathHelper.getXCoord(location)-(int)playerPos.getX();
        int yOffset = hexMathHelper.getYCoord(location) - (int)playerPos.getY();

        rotate(gc, getOrientation().getDegreeOfOrientation(getOrientation()), ((xOffset*width)*.75)+(width/2) + Commons.SCREEN_WIDTH/2 + scrollOffset.getX(), (yOffset*(height/2))+(height/2) + Commons.SCREEN_HEIGHT/2 + scrollOffset.getY());
        gc.drawImage(fogSprite, (int)((xOffset*width)*.75) + Commons.SCREEN_WIDTH/2 + scrollOffset.getX(), (yOffset*(height/2)) + Commons.SCREEN_HEIGHT/2 + scrollOffset.getY(), width, height);
    }
    public boolean isShrouded() {
        return isShrouded;
    }

    public void setShrouded(boolean shrouded) {
        isShrouded = shrouded;
    }

    @Override
    public int getRenderPriority() {
        if(isShrouded()) {
            return 0;
        }
        return super.getRenderPriority();
    }
}
