package Model.MenuModel;

import Model.Entity.Entity;
import Model.Item.TakeableItem.TakeableItem;

public class AssignItemMenu extends InGameMenuState {

    private TakeableItem takeableItem;

    public AssignItemMenu(MenuModel menuModel, Entity player, TakeableItem takeableItem) {
        super(menuModel, player);
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
        menuModel.setActiveState(new InventoryMenu(menuModel, player));
    }
}
