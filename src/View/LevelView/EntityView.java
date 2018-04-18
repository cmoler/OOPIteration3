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
    }
    @Override
    public void notifyViewElement() {
        entityOrientation = entity.getOrientation();
    }

    public void render(GraphicsContext gc, Point2D offset) {
        int width = entitySize;
        int height = (int)(width * (Math.sqrt(3)/2));

        int xOffset = hexMathHelper.getXCoord(entityLocation) + (int)offset.getX();
        int yOffset = hexMathHelper.getYCoord(entityLocation) + (int)offset.getY();
        
        rotate(gc, (double)entityOrientation.getDegreeOfOrientation(entityOrientation), 100+(int)((xOffset*width)*.75)+(width/2), 100 + (yOffset*(height/2))+(height/2));
        gc.drawImage(entitySprite, 100+(int)((xOffset*width)*.75), 100 + (yOffset*(height/2)), width, height);
    }

    private void rotate(GraphicsContext gc, double angle, double px, double py) {
        Rotate r = new Rotate(angle, px, py);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }
}
