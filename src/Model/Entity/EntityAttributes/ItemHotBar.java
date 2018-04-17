package Model.Entity.EntityAttributes;

import Model.Entity.Entity;
import Model.Item.TakeableItem.TakeableItem;

import java.util.ArrayList;

public class ItemHotBar {

    private Entity entity;
    private TakeableItem[] items = new TakeableItem[5];

    public ItemHotBar(Entity entity){
        this.entity = entity;
    }

    public void addItem(TakeableItem takeableItem, int index){
        if(0 < index && index < 5)
            items[index] = takeableItem;
    }

    public void use(int index) {
        if(items[index] != null) items[index].select();
    }
}
