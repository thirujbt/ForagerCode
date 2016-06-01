package com.forager.meal.bean;

import android.graphics.Bitmap;

/**
 * Created by pickzy01 on 9/25/2015.
 */
public class ShopMealPlan {

    private int id;

    private String desc;

    private Bitmap bitmap;

    private int active_status;

    public int getActive_status() {
        return active_status;
    }

    public void setActive_status(int active_status) {
        this.active_status = active_status;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



}
