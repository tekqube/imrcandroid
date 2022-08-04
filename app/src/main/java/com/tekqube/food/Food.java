package com.tekqube.food;

import java.io.Serializable;

/**
 * Created by abhisheksoni on 6/13/16.
 */
public class Food implements Serializable {
    String cuisine;
    String mealName;
    String category;
    String time;
    int day;

    public Food(String category, String time) {
        this.category = category;
        this.time = time;
    }

    public Food() {

    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
