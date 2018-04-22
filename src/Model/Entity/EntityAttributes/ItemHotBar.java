package Model.Entity.EntityAttributes;

import Controller.Visitor.Visitable;
import Controller.Visitor.Visitor;
import Model.Entity.Entity;
import Model.Item.TakeableItem.TakeableItem;

import java.util.HashMap;

public class ItemHotBar implements Visitable {

    private TakeableItem[] items = new TakeableItem[5];

    public ItemHotBar(){

    }

    public void addItem(TakeableItem takeableItem, int index){
        if(0 <= index && index < 5)
            items[index] = takeableItem;
    }

    public void use(int index) {
        if(items[index] != null) items[index].select();
    }

    public HashMap<Integer, TakeableItem> getItemBarMap() {
        HashMap<Integer, TakeableItem> itemMap = new HashMap<>();

        for(int i = 0; i < items.length; i++) {
            if(items[i] != null) {
                itemMap.put(i, items[i]);
            }
        }

        return itemMap;
    }

    public TakeableItem getItem(int i) {
        if(i < 5 && i >= 0)
            return items[i];
        else
            return null;
    }

    public void accept(Visitor visitor) { // TODO: change? if so how prevent LoD?
        for(TakeableItem item: items) {
            item.accept(visitor);
        }
    }

    public void setStrategies(Entity entity) {
        for(TakeableItem item: items) {
            if(item != null) {
                item.setDropStrategyEntity(entity);
                item.setItemStrategyEntity(entity);
            }
        }
    }
}
