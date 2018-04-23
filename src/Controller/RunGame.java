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

        //Initialization
        mainStage = primaryStage;
        mainStage.setTitle("HYPED MEAN PENGUINS BACK FOR DAVE SMALL DAVE MEDIUM DAVE LARGE DAVE MORE");

        Group root = new Group();
        mainScene = new Scene(root);
        primaryStage.setScene( mainScene );

        canvas = new Canvas(Commons.SCREEN_WIDTH, Commons.SCREEN_HEIGHT);

        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        canvas.setFocusTraversable(true);
        
        GameLoop gameLoop = new GameLoop();
        gameLoop.init();
        gameLoop.setRunGame(this);

        canvas.setOnKeyPressed(gameLoop.getControls());


        canvas.setOnMousePressed(evt -> {
            mouseX = evt.getScreenX();
            mouseY = evt.getScreenY();
        });

        canvas.setOnMouseDragged(evt -> {
            double deltaX = evt.getScreenX() - mouseX;
            double deltaY = evt.getScreenY() - mouseY;

            Point2D mouseOffSet = new Point2D(deltaX / 10, deltaY / 10);

            gameLoop.addScrollOffSet(mouseOffSet);

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
