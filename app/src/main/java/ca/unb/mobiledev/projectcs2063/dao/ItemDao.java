package ca.unb.mobiledev.projectcs2063.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ca.unb.mobiledev.projectcs2063.entity.Item;

/**
 * This DAO object validates the SQL at compile-time and associates it with a method
 */
@Dao
public interface ItemDao {

    @Query("SELECT * from item_table WHERE name = :itemName")
    LiveData<List<Item>> listAllRecords(String itemName);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Item item);

    @Delete
    void deleteItem(Item item);
}
