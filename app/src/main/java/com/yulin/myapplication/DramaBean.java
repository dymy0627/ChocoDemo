package com.yulin.myapplication;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "drama")
public class DramaBean {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "drama_id")
    private int drama_id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "total_views")
    private int total_views;

    @ColumnInfo(name = "created_at")
    private String created_at;

    @ColumnInfo(name = "thumb")
    private String thumb;

    @ColumnInfo(name = "rating")
    private float rating;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDrama_id() {
        return drama_id;
    }

    public void setDrama_id(int drama_id) {
        this.drama_id = drama_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotal_views() {
        return total_views;
    }

    public void setTotal_views(int total_views) {
        this.total_views = total_views;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
