package Model.MenuModel;

import Controller.GameLoop;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Inventory;
import Model.Item.Item;
import Model.Item.TakeableItem.TakeableItem;

import java.util.Random;

public class BarterMenu extends MenuState {

    private Entity player;
    private Entity npc;
    private Inventory playerInventory;
    private Inventory npcInventory;
    private int barterStrength;

    public BarterMenu(MenuModel menuModel, GameLoop gameLoop, int barterStrength, Entity player, Entity npc) {
        super(menuModel, gameLoop);
        this.barterStrength = barterStrength;
        this.player = player;
        this.playerInventory = player.getInventory();
        this.npc = npc;
        this.npcInventory = npc.getInventory();
    }

    @Override
    public void correctParameters() {
        if(selectedLeftRight < 0) selectedLeftRight = 1;
        if(selectedLeftRight > 1) selectedLeftRight = 0;
        if(selectedLeftRight == 0){
            if (selectedUpDown < 0) selectedUpDown = playerInventory.size() - 1;
            if (selectedUpDown > playerInventory.size() - 1) selectedUpDown = 0;
        }
        if(selectedLeftRight == 1){
            if (selectedUpDown < 0) selectedUpDown = npcInventory.size() - 1;
            if (selectedUpDown > npcInventory.size() - 1) selectedUpDown = 0;
        }
    }

    @Override
    public void select() {
        switch (selectedLeftRight){
            case 0:
                sell();
                break;
            case 1:
                buy();
                break;
        }
    }

    private void sell() {
        TakeableItem itemSelling = playerInventory.getItem(selectedUpDown);
        if(itemSelling == null) return;
        Random random = new Random();
        int price = itemSelling.getPrice() / (random.nextInt(99 - barterStrength) / 50);
        if(npc.getGold() >= price) {
            player.removeItemFromInventory(itemSelling);
            player.addGold(price);
            npc.removeGold(price);
            npc.addItemToInventory(itemSelling);
        }
    }

    private void buy() {
        TakeableItem itemBuying = npcInventory.getItem(selectedUpDown);
        if(itemBuying == null) return;
        Random random = new Random();
        int price = itemBuying.getPrice() * (random.nextInt(99 - barterStrength) / 50);
        if(player.getGold() >= price) {
            player.removeGold(price);
            npc.removeItemFromInventory(itemBuying);
            npc.addGold(price);
            player.addItemToInventory(itemBuying);
        }
    }

    public Inventory getPlayerInventory(){
        return playerInventory;
    }

    public Inventory getNpcInventory() {
        return npcInventory;
    }

    public int getPlayerGold(){
        return player.getGold();
    }

    public int getNPCGold(){
        return npc.getGold();
    }
}
