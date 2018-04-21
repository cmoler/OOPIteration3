package Controller;

import Configs.Commons;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;


public class RunGame extends Application{
    private Stage mainStage;
    private Scene mainScene;

    private Canvas canvas;
    private double mouseX;
    private double mouseY;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){

        //Initialization Shit
        mainStage = primaryStage;
        mainStage.setTitle("HYPED MEAN PENGUINS BACK FOR DAVE SMALL DAVE MEDIUM DAVE LARGE DAVE MORE");

        Group root = new Group();
        mainScene = new Scene(root);
        primaryStage.setScene( mainScene );

        canvas = new Canvas(Commons.SCREEN_WIDTH, Commons.SCREEN_HEIGHT);

        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        canvas.setFocusTraversable(true);

        /*
        List<LevelViewElement> observers = new ArrayList<>();
        Level level = new Level(observers);

        Entity entity = new Entity();
        entity.setOrientation(Orientation.NORTH);
        EntityView ev = new EntityView(entity);

        level.addEntityTo(new Point3D(0, 0, 0), entity);

        ArrayList<LevelViewElement> terrains = new ArrayList<>();
        RadialInfluenceEffect radialInfluenceEffect = new RadialInfluenceEffect(new RemoveHealthCommand(15), 10, 5, Orientation.SOUTHEAST);

        for(int i = 0; i < 8; i++) {
            ArrayList<Point3D> points = radialInfluenceEffect.nextMove(new Point3D(0, 0, 0));
            for(int j = 0; j < points.size(); j++) {
                terrains.add(new TerrainView(points.get(j), 100));
            }
        }

*/
        
        GameLoop gameLoop = new GameLoop();
        gameLoop.init();
        gameLoop.setRunGame(this);


        // TODO: get rid of these when loading from file logic is done vvvv

        // TODO: get rid of these when loading from file logic is done ^^^^


        canvas.setOnKeyPressed(gameLoop.getControls());


        canvas.setOnMousePressed(evt -> {
            mouseX = evt.getScreenX();
            mouseY = evt.getScreenY();
        });

        canvas.setOnMouseDragged(evt -> {
            double deltaX = evt.getScreenX() - mouseX;
            double deltaY = evt.getScreenY() - mouseY;

            Point2D mouseOffSet = new Point2D(deltaX, deltaY);

            gameLoop.setScrollOffSet(mouseOffSet);

        });

        new AnimationTimer() {
            /*
            public void handle(long currentNanoTime) {
                for(int i = 0; i < terrains.size(); i++) {
                    terrains.get(i).render(gc);
                }
                ev.render(gc);
            }
            */
            public void handle(long currentNanoTime){
                gameLoop.tick();
                gameLoop.render(gc);
            }
        }.start();


        mainStage.show();

    }

    public void setInput(EventHandler<KeyEvent> keyEventEventHandler){
        canvas.setOnKeyPressed(keyEventEventHandler);
    }
}
