package ca.unb.mobiledev.projectcs2063.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "item_table")  // Represents a SQLite table
public class Item {
    //Date of -1 will be the goals

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int date;

    private int steps;
    private int water;
    //private int sGoal;
    //private int wGoal;

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    /*public int getSGoal() {
        return sGoal;
    }

    public void setSGoal(int sGoal) {
        this.sGoal = sGoal;
    }*/

    public int getWater() {
        return water;
    }

    public void setWater(int water) {
        this.water = water;
    }

    /*public int getWGoal() {
        return wGoal;
    }

    public void setWGoal(int wGoal) {
        this.wGoal = wGoal;
    }*/
}