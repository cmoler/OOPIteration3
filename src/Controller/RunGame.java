package Controller;

import Configs.Commons;
import Model.Entity.Entity;
import Model.InfluenceEffect.AngularInfluenceEffect;
import Model.InfluenceEffect.LinearInfluenceEffect;
import Model.InfluenceEffect.RadialInfluenceEffect;
import Model.Level.Level;
import Model.MenuModel.MainMenuState;
import Model.MenuModel.MenuModel;
import View.LevelView.EntityView;
import View.LevelView.LevelViewElement;
import View.LevelView.TerrainView;
import View.MenuView.MenuView;
import View.MenuView.TitleScreenView;
import javafx.animation.AnimationTimer;
import javafx.application.Application;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import Model.Entity.EntityAttributes.Orientation;

import java.util.ArrayList;
import java.util.List;


public class RunGame extends Application{
    private Stage mainStage;
    private Scene mainScene;

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

        Canvas canvas = new Canvas(Commons.SCREEN_WIDTH, Commons.SCREEN_HEIGHT);
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

        canvas.setOnKeyPressed(gameLoop.getControls());



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
                gameLoop.render(gc);
            }
        }.start();


        mainStage.show();

    }
}
