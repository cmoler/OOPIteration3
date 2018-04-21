package View.LevelView;



import Configs.Commons;
import View.LevelView.LevelViewElement;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ObservationView extends LevelViewElement {
    private String observationText;
    private int duration;
    public ObservationView(Point3D location, String observationText) {
        super(location, 0);

        this.observationText = observationText;
        duration = 100;
    }

    public void render(GraphicsContext gc, Point2D playerPos, Point2D scrollOffset) {

        HexMathHelper hexMathHelper = new HexMathHelper();

        int width = getSize();
        int height = (int)(width * (Math.sqrt(3)/2));

        int xOffset = hexMathHelper.getXCoord(renderLocation)-(int)playerPos.getX();
        int yOffset = hexMathHelper.getYCoord(renderLocation) - (int)playerPos.getY();

        gc.setFill(Color.BLACK);
        gc.setFont(Font.font ("Verdana", FontWeight.BOLD, 30));

        gc.fillText(observationText, (int)((xOffset*width)*.75) + Commons.SCREEN_WIDTH/2 + scrollOffset.getX(), (yOffset*(height/2)) + Commons.SCREEN_HEIGHT/2 + scrollOffset.getY());
        duration--;
    }

    @Override
    public void notifyViewElementDeath() {

    }

    @Override
    public void notifyViewElement() {

    }

    public boolean readyToBeRemoved() {
        return duration <= 0;
    }
}

