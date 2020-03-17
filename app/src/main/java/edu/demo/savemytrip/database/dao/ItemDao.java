package edu.demo.savemytrip.database.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import edu.demo.savemytrip.models.Item;

@Dao
public interface ItemDao {

    @Query("SELECT * FROM Item WHERE userId = :userId")
    Cursor getItemsWithCursor(long userId);

    @Query("SELECT * FROM ITEM WHERE userId = :userId")
    LiveData<List<Item>> getItems(long userId);

    @Insert
    long insertItem(Item item);

    @Update
    int updateItem(Item item);

    @Query("DELETE FROM Item WHERE id = :itemId")
    int deleteItem(long itemId);

}
