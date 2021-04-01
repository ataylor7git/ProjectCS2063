package ca.unb.mobiledev.projectcs2063.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import ca.unb.mobiledev.projectcs2063.dao.ItemDao;
import ca.unb.mobiledev.projectcs2063.db.AppDatabase;
import ca.unb.mobiledev.projectcs2063.entity.Item;

public class ItemRepository {
    private ItemDao itemDao;

    public ItemRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        itemDao = db.itemDao();
    }

    public LiveData<Item> getRecordByDate(int date) {
        return itemDao.getRecordByDate(date);
    }

    public LiveData<Item> getGoals() {
        return itemDao.getRecordByDate(-1);
    }

    public void insertRecord(int date, int steps, int water) {
        Item newItem = new Item();
        newItem.setDate(date);
        newItem.setSteps(steps);
        newItem.setWater(water);

        insertRecord(newItem);
    }

    private void insertRecord(final Item item) {
        AppDatabase.databaseWriterExecutor.execute(() -> {
            itemDao.insert(item);
        });
    }

    public void deleteRecord(final Item item) {
        AppDatabase.databaseWriterExecutor.execute(() -> {
            itemDao.deleteItem(item);
        });
    }

    public void updateItem(int itemStep, int itemWater, int itemDate)
    {
        AppDatabase.databaseWriterExecutor.execute(() -> {
            itemDao.updateItem(itemStep, itemWater, itemDate);
        });
    }
}
