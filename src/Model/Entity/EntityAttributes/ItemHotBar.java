package Model.Entity.EntityAttributes;

import Controller.Visitor.SavingVisitor;
import Controller.Visitor.Visitable;
import Model.Item.Item;
import Model.Item.TakeableItem.TakeableItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    @Override
    public void accept(SavingVisitor visitor) {
        for(TakeableItem item: items) {
            visitor.visitItem(item);
        }
    }

    public TakeableItem getItem(int i) {
        if(i < 5 && i >= 0)
            return items[i];
        else
            return null;
    }
}
