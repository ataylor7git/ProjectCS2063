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

    @Query("SELECT * from item_table WHERE date = :itemDate")
    LiveData<Item> getRecordByDate(int itemDate);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Item item);

    @Delete
    void deleteItem(Item item);

    @Query("UPDATE item_table SET steps = :itemStep, water = :itemWater WHERE date = :itemDate")
        //int updateItem(int itemStep, int itemWater, int itemDate);
    int updateItem(int itemStep, int itemWater, int itemDate);

    @Query("UPDATE item_table SET steps = :itemStep, water = :itemWater," +
            "name = :itemName, weight = :itemWeight, height = :itemHeight, age = :itemAge, " +
            "sex = :itemSex  WHERE date = :itemDate")
    int updateItemAll(int itemStep, int itemWater, String itemName, String itemWeight,
                      String itemHeight, String itemAge, boolean itemSex, int itemDate);

    @Query("UPDATE item_table SET steps = :itemStep WHERE date = :itemDate")
    int updateStepItem(int itemStep, int itemDate);

    @Query("UPDATE item_table SET water = :itemWater WHERE date = :itemDate")
    int updateWaterItem(int itemWater, int itemDate);
}
