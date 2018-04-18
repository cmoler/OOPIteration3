package View.LevelView;

import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;

import java.io.File;

public class EntityView implements LevelViewElement{

    private Point3D entityLocation;
    private Entity entity;
    private Orientation entityOrientation;
    private Image entitySprite;
    private int entitySize;
    private HexMathHelper hexMathHelper;

    public EntityView(Entity entity) {
        entity.addObserver(this);
        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/warrior.png");
        entitySprite = new Image(file.toURI().toString());

        entityOrientation = entity.getOrientation();
        entitySize = 75;

        hexMathHelper = new HexMathHelper();

        entityLocation = new Point3D(0, 0, 0);
        entityOrientation = Orientation.NORTH;
    }
    @Override
    public void notifyViewElement() {
        entityOrientation = entity.getOrientation();
    }

    public void render(GraphicsContext gc, Point2D offset) {
        int width = entitySize;
        int height = (int)(width * (Math.sqrt(3)/2));

        int xOffset = hexMathHelper.getXCoord(entityLocation);
        int yOffset = hexMathHelper.getYCoord(entityLocation);


        //rotate(gc, (double)entityOrientation.getDegreeOfOrientation(entityOrientation), ((xOffset*width)*.75)+offset.getX(), (yOffset*(height/2))+offset.getY());
        gc.drawImage(entitySprite, (int)((xOffset*width)*.75)+offset.getX(), (yOffset*(height/2))+offset.getY(), width, height);
    }

    @Override
    public int getRenderPriority() {
        return 0;
    }

    private void rotate(GraphicsContext gc, double angle, double px, double py) {
        Rotate r = new Rotate(angle, px, py);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }
}
