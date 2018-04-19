package Model.MenuModel;

import Controller.GameLoop;
import Model.Entity.Entity;
import Model.Item.TakeableItem.TakeableItem;
import View.MenuView.InventoryView;

public class AssignItemMenu extends InGameMenuState {

    private TakeableItem takeableItem;

    public AssignItemMenu(MenuModel menuModel, Entity player, GameLoop gameLoop, TakeableItem takeableItem) {
        super(menuModel, player, gameLoop);
        this.takeableItem = takeableItem;
    }

    @Override
    public void correctParameters() {
        if(selectedUpDown != 0) selectedUpDown = 0;
        if(selectedLeftRight < 0) selectedLeftRight = 4;
        if(selectedLeftRight > 4) selectedLeftRight = 0;
    }

    @Override
    public void select() {
        player.addItemToHotBar(takeableItem, selectedLeftRight);
        gameLoop.setMenuState(new InventoryMenu(menuModel, player, gameLoop), new InventoryView(menuModel));
    }
}
