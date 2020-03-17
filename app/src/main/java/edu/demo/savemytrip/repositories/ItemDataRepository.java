package edu.demo.savemytrip.repositories;

import androidx.lifecycle.LiveData;

import java.util.List;

import edu.demo.savemytrip.database.dao.ItemDao;
import edu.demo.savemytrip.models.Item;

public class ItemDataRepository {

    private final ItemDao itemDao;

    public ItemDataRepository(ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    // -- GET --
    public LiveData<List<Item>> getItems(long userId){
        return itemDao.getItems(userId);
    }

    // - CREATE --
    public void createItem(Item item){
        itemDao.insertItem(item);
    }

    // -- DELETE  --
    public void deleteItem(long itemId){
        itemDao.deleteItem(itemId);
    }

    // -- UPDATE --
    public void updateItem(Item item){
        itemDao.updateItem(item);
    }
}
