package Model.MenuModel;

import Model.Entity.Entity;

public abstract class InGameMenuState extends MenuState {

    protected Entity player;

    public InGameMenuState(MenuModel menuModel, Entity player) {
        super(menuModel);
        this.player = player;
    }

}
