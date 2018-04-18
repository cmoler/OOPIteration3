package View.LevelView;


import Model.Entity.EntityAttributes.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;

import java.io.File;

public class TerrainView implements LevelViewElement{
    private Point3D location;
    private Image hex;
    private HexMathHelper hexMathHelper;
    private int size;

    public TerrainView(Point3D location, int size) {
        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/hexTilesGrass1.png");
        hex = new Image(file.toURI().toString());

        this.location = location;


        hexMathHelper = new HexMathHelper();

        this.size = size;
    }


    @Override
    public void notifyViewElement() {

    }

    public void render(GraphicsContext gc, Point2D offset) {
        int width = size;
        int height = (int)(width * (Math.sqrt(3)/2));

        int xOffset = hexMathHelper.getXCoord(location);
        int yOffset = hexMathHelper.getYCoord(location);

        rotate(gc, -90, ((xOffset*width)*.75)+offset.getX(), (yOffset*(height/2))+offset.getY());
        gc.drawImage(hex, (int)((xOffset*width)*.75)+offset.getX(), (yOffset*(height/2))+offset.getY(), width, height);


    }

    @Override
    public int getRenderPriority() {
        return 4;
    }

    private void rotate(GraphicsContext gc, double angle, double px, double py) {
        Rotate r = new Rotate(angle, px, py);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }
}
