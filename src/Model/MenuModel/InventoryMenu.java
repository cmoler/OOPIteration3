package Model.MenuModel;

import Controller.GameLoop;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Inventory;
import Model.Item.TakeableItem.TakeableItem;

public class InventoryMenu extends InGameMenuState {

    private Inventory inventory;
    private int selectedItem = 0;

    public InventoryMenu(MenuModel menuModel, Entity player, GameLoop gameLoop) {
        super(menuModel, player, gameLoop);
        this.inventory = player.getInventory();
    }

    @Override
    public void correctParameters() {
        if(selectedLeftRight < 0) selectedLeftRight = 3;
        if(selectedLeftRight > 3)selectedLeftRight = 0;
        if(selectedLeftRight == 0){
            if (selectedUpDown < 0) selectedUpDown = 3;
            if (selectedUpDown > 3) selectedUpDown = 0;
        }
        if(selectedLeftRight == 1) {
            if (selectedUpDown < 0) selectedUpDown = inventory.size() - 1;
            if (selectedUpDown > inventory.size() - 1) selectedUpDown = 0;
        }
        if(selectedLeftRight == 2){
            if (selectedUpDown < 0) selectedUpDown = 2;
            if (selectedUpDown > 2) selectedUpDown = 0;
        }
    }

    @Override
    public void select() {
        switch (selectedLeftRight){
            case 0:
                menuPart();
                break;
            case 1:
                inventoryPart();
                break;
            case 2:
                itemPart();
                break;
        }
    }

    private void menuPart(){
        switch (selectedUpDown){
            case 0:
                menuModel.setActiveState(new InventoryMenu(menuModel, player, gameLoop));
                break;
            case 1:
                menuModel.setActiveState(new StatsMenu(menuModel, player, gameLoop));
                break;
            case 2:
                menuModel.setActiveState(new LevelUpMenu(menuModel, player, gameLoop));
                break;
            case 3:
                menuModel.setActiveState(new ExitGameMenu(menuModel, player, gameLoop));
                break;
        }
    }

    private void inventoryPart(){
        selectedItem = selectedUpDown;
    }

    private void itemPart(){
        TakeableItem itemManipulating = inventory.getItem(selectedItem);
        if(itemManipulating == null) return;
        switch (selectedUpDown){
            case 0:
                itemManipulating.select();
                break;
            case 1:
                menuModel.setActiveState(new AssignItemMenu(menuModel, player, gameLoop, itemManipulating));
                break;
            case 2:
                itemManipulating.dropItem();
                break;
        }
    }
}
