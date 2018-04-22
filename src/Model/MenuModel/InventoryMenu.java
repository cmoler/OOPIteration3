package Model.MenuModel;

import Controller.GameLoop;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Inventory;
import Model.Item.TakeableItem.TakeableItem;
import View.MenuView.AssignItemView;
import View.MenuView.SaveGameView;

public class InventoryMenu extends InGameMenuState {

    private Inventory inventory;
    private int selectedItem = 0;

    public InventoryMenu(MenuModel menuModel, Entity player, GameLoop gameLoop) {
        super(menuModel, player, gameLoop);
        this.inventory = player.getInventory();
    }

    @Override
    public void correctParameters() {
        if(selectedLeftRight < 0) selectedLeftRight = 2;
        if(selectedLeftRight > 2)selectedLeftRight = 0;
        if(selectedLeftRight == 0){
            if (selectedUpDown < 0) selectedUpDown = inGameMenuBar.getMaxUp();
            if (selectedUpDown > inGameMenuBar.getMaxUp()) selectedUpDown = 0;
        }
        if(selectedLeftRight == 1) {
            if (selectedUpDown < 0) selectedUpDown = inventory.size() - 1;
            if (selectedUpDown > inventory.size() - 1) selectedUpDown = 0;
        }
        if(selectedLeftRight == 2){
            TakeableItem itemManipulating = inventory.getItem(selectedItem);
            if(itemManipulating.usableByEntity(player)){
                if (selectedUpDown < 0) selectedUpDown = 2;
                if (selectedUpDown > 2) selectedUpDown = 0;
            }
            else if(selectedUpDown != 0) selectedUpDown = 0;
        }
    }

    @Override
    public void select() {
        switch (selectedLeftRight){
            case 0:
                inGameMenuBar.select(selectedUpDown);
                break;
            case 1:
                inventoryPart();
                break;
            case 2:
                itemPart();
                break;
        }
    }

    private void inventoryPart(){
        selectedItem = selectedUpDown;
    }

    private void itemPart(){
        TakeableItem itemManipulating = inventory.getItem(selectedItem);
        if(itemManipulating == null) return;
        if(itemManipulating.usableByEntity(player)) {
            switch (selectedUpDown) {
                case 0:
                    itemManipulating.select();
                    break;
                case 1:
                    gameLoop.setMenuState(new AssignItemMenu(menuModel, player, gameLoop, itemManipulating), new AssignItemView(menuModel));
                    break;
                case 2:
                    itemManipulating.drop();
                    break;
            }
        }else{
            itemManipulating.drop();
        }
    }

    public TakeableItem getSelectedItem(){
        return inventory.getItem(selectedItem);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public boolean getItemUsableByPlayer(){
        TakeableItem itemManipulating = inventory.getItem(selectedItem);
        return itemManipulating.usableByEntity(player);
    }
}
