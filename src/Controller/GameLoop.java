package Controller;

import Controller.Factories.ControllerSetFactory;
import Controller.Factories.EntityFactories.EntityFactory;
import Controller.Factories.PetAIFactory;
import Controller.Factories.SkillsFactory;
import Controller.Visitor.SavingVisitor;
import Model.AI.AIState;
import Model.AI.PetAI.PetPriority;
import Model.Entity.Entity;
import Model.Level.GameLoopMessenger;
import Model.Level.GameModel;
import Model.MenuModel.*;
import Model.Level.Level;
import Model.MenuModel.MainMenuState;
import Model.MenuModel.MenuModel;
import Model.MenuModel.MenuState;
import View.LevelView.HUDStatsView;
import View.LevelView.HotbarView;

import View.MenuView.*;

import View.LevelView.ObservationView;
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
    private boolean playerFresh = true;

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

        gameModel.init();

        renderer = new Renderer();

        ((KeyEventImplementor) controls).createMainMenuSet(menuModel);
        setMenuState(new MainMenuState(menuModel, this), new TitleScreenView(menuModel));
        renderer.updateCurrentLevel(gameModel.getCurrentLevel());
        renderer.setPlayerHUD(new HUDStatsView(gameModel.getPlayer()));
        renderer.setHotBarView(new HotbarView(gameModel.getPlayer()));
        renderer.closeMenu();
        ((KeyEventImplementor) controls).createPlayerControlsSet(gameModel.getPlayer(), menuModel);

        renderer.updateCurrentLevel(gameModel.getCurrentLevel());

        for (Level level : gameModel.getLevels()) {
            renderer.loadModelSprites(level);
        }
    }

    public void openBarterWindow(Entity playerEntity, Entity receivingEntity) {
        if(playerEntity == null || receivingEntity == null) {
            // do nothing if either entity is null
        }else{
            SkillsFactory skillsFactory = gameModel.getSkillsFactory();
            int barterLevel = playerEntity.getSkillLevel(skillsFactory.getBargainSkill());
            setMenuState(new BarterMenu(menuModel, this, barterLevel, playerEntity, receivingEntity), new BarterView(menuModel));
            setInGameMenuKeySet();
        }
    }

    public void openDialogWindow(Entity playerEntity, Entity receivingEntity) {
        boolean wantToTalk = gameModel.getAIForEntity(receivingEntity).wantToTalk();
        setMenuState(new DialogMenu(menuModel, this, wantToTalk, playerEntity, receivingEntity), new DialogView(menuModel));
        setInGameMenuKeySet();
    }

    public void createObservationWindow(Entity entity, String randomEntityFacts) {
        renderer.addObservationView(new ObservationView(entity, randomEntityFacts));
    }

    public void startTimer() {
        loopTimer.start();
    }

    public void pauseTimer() {
        loopTimer.stop();
    }

    public void loadGame(int i) {
        playerFresh = true;
    }

    public void saveGame(int i) {

    }

    public void newGame(int i) {
        playerFresh = true;
    }

    public void setMenuState(MenuState menuState, MenuViewState menuViewState){
        menuModel.setActiveState(menuState);
        renderer.setActiveMenuState(menuViewState);
    }

    private void setGameOver() {
        setMenuState(new GameOverMenu(menuModel, this), new GameOverView(menuModel));
        setInGameMenuKeySet();
    }

    public void tick() {
        if(gameModel.playerIsDead() && playerFresh) {
            //gameModel.resetPlayer();
            setGameOver();
            playerFresh = false;
        }
        else{
            gameModel.advance();
        }
    }

    public void render(GraphicsContext gc){
        renderer.updateTerrainFog(gameModel.getPlayerPosition(), gameModel.getPlayer().getSight());
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

    public void resetScrollOffSet() {
        this.scrollOffSet = Point2D.ZERO;
    }

    public void addScrollOffSet(Point2D mouseOffSet) {
        this.scrollOffSet = new Point2D(scrollOffSet.getX() + mouseOffSet.getX(), scrollOffSet.getY() + mouseOffSet.getY());
    }

    public PetAIFactory getPetAIFactoryFromCurrentLevel() {
        return gameModel.getPetAIFactory();
    }

    public void setAIOnCurrentLevel(Entity entity, AIState aiState) {
        gameModel.setAIOnCurrentLevel(entity, aiState);
    }

    public void setAIPriorityOnCurrentLevel(Entity entity, PetPriority petPriority) {
        gameModel.setAIPriorityOnCurrentLevel(entity, petPriority);
    }
}
