package Model.MenuModel;

import Controller.GameLoop;
import Model.Entity.Entity;

public abstract class InGameMenuState extends MenuState {

    protected Entity player;

    public InGameMenuState(MenuModel menuModel, Entity player, GameLoop gameLoop) {
        super(menuModel, gameLoop);
        this.player = player;
    }

}
