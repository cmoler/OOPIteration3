package Controller;

import Controller.Factories.ControllerSetFactory;
import Controller.Factories.EntityFactories.EntityFactory;
import Controller.Visitor.SavingVisitor;
import Model.Entity.Entity;
import Model.Level.GameLoopMessenger;
import Model.Level.GameModel;
import Model.MenuModel.MainMenuState;
import Model.MenuModel.MenuModel;
import Model.MenuModel.MenuState;
import View.MenuView.MenuView;
import View.MenuView.MenuViewState;
import View.MenuView.TitleScreenView;
import View.Renderer;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyEvent;
import javafx.scene.canvas.GraphicsContext;


public class GameLoop {

    private AnimationTimer loopTimer;
    private GameLoopMessenger gameLoopMessenger;
    private GameModel gameModel;
    private MenuView menuView;
    private MenuModel menuModel;
    private SavingVisitor gameSaver;
    private EntityFactory entityFactory;
    private ControllerSetFactory controllerSetFactory;
    private Renderer renderer;
    private EventHandler<KeyEvent> controls;
    private RunGame runGame;
    private Point2D scrollOffSet;

    public GameLoop() {
        gameLoopMessenger = new GameLoopMessenger(this);
    }

    public void init() {
        //TODO: Add loading from file logic
        scrollOffSet = new Point2D(0, 0);
        controls = new KeyEventImplementor(this);
        loopTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gameModel.advance();
            }
        };

        menuModel = new MenuModel(this);

        gameModel = new GameModel(gameLoopMessenger);

        renderer = new Renderer();

        ((KeyEventImplementor)controls).createMainMenuSet(menuModel);
        setMenuState(new MainMenuState(menuModel, this), new TitleScreenView(menuModel));
        renderer.updateCurrentLevel(gameModel.getCurrentLevel());
        renderer.closeMenu();
        ((KeyEventImplementor)controls).createPlayerControlsSet(gameModel.getPlayer(), menuModel);

        renderer.updateCurrentLevel(gameModel.getCurrentLevel());

        gameModel.registerAllLevelObservers();
    }

    public void openBarterWindow(Entity playerEntity, int playerBarterStrength, Entity receivingEntity) {
        // TODO: implement
        if(playerEntity == null || receivingEntity == null) {
            // do nothing if either entity is null
        }
    }

    public void openDialogWindow(Entity playerEntity, Entity receivingEntity) {
        // TODO: implement
        System.out.println("I (player) am talking to you!");
    }

    public void createObservationWindow(String randomEntityFacts) {
        // TODO: implement
    }

    public void startTimer() {
        loopTimer.start();
    }

    public void pauseTimer() {
        loopTimer.stop();
    }

    public void loadGame(int i) {

    }

    public void saveGame(int i) {

    }

    public void newGame(int i) {

    }

    public void setMenuState(MenuState menuState, MenuViewState menuViewState){
        menuModel.setActiveState(menuState);
        renderer.setActiveMenuState(menuViewState);
    }

    public void tick() {
        gameModel.advance();
    }

    public void render(GraphicsContext gc){
        renderer.render(gc, gameModel.getPlayerPosition(), scrollOffSet);
    }

    public KeyEventImplementor getControls() {
        return ((KeyEventImplementor)controls);
    }

    public void setControls(){
        controls = new KeyEventImplementor(this);
        ((KeyEventImplementor)controls).createMainMenuSet(menuModel);
        runGame.setInput(controls);
    }

    public void setKeyBinding(int selectedLeftRight, int selectedUpDown) {
        controls = new KeyBindingSetter(this, selectedLeftRight, selectedUpDown);
        runGame.setInput(controls);
    }

    public void setRunGame(RunGame runGame) {
        this.runGame = runGame;
    }

    public void closeMenu() {
        renderer.closeMenu();
        ((KeyEventImplementor)controls).createPlayerControlsSet(gameModel.getPlayer(), menuModel);
    }

    public void setInGameMenuKeySet() {
        ((KeyEventImplementor)controls).createInGameMenuSet(menuModel);
    }

    public void setMainMenuKeySet() {
        ((KeyEventImplementor)controls).createMainMenuSet(menuModel);
    }

    public void setScrollOffSet(Point2D scrollOffSet) {
        this.scrollOffSet = scrollOffSet;
    }
}
