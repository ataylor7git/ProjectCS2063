package ca.unb.mobiledev.projectcs2063.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "item_table")  // Represents a SQLite table
public class Item {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    private String name;
    private int num;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
