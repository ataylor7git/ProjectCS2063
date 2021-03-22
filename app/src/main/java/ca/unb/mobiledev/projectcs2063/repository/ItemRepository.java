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

    public LiveData<List<Item>> listAllRecords(String name) {
        return itemDao.listAllRecords(name);
    }

    public void insertRecord(String name, int num) {
        Item newItem = new Item();
        newItem.setName(name);
        newItem.setNum(num);

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
}
