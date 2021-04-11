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


    private String name;
    private String weight;
    private String height;
    private String age;
    private boolean sex;

    //private int sGoal;
    //private int wGoal;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

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