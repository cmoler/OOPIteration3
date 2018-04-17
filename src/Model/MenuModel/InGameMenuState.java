package Model.MenuModel;

import Controller.GameLoop;
import Model.Entity.Entity;

public abstract class InGameMenuState extends MenuState {

    protected Entity player;
    protected InGameMenuBar inGameMenuBar;

    public InGameMenuState(MenuModel menuModel, Entity player, GameLoop gameLoop) {
        super(menuModel, gameLoop);
        this.player = player;
        this.inGameMenuBar = new InGameMenuBar(menuModel, player, gameLoop);
    }

}
