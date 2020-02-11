package com.yulin.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "drama")
public class DramaBean implements Parcelable {

    public DramaBean(String name, String created_at, String thumb, float rating) {
        this.name = name;
        this.created_at = created_at;
        this.thumb = thumb;
        this.rating = rating;
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.total_views);
        dest.writeString(this.created_at);
        dest.writeString(this.thumb);
        dest.writeFloat(this.rating);
    }

    private DramaBean(Parcel in) {
        this.name = in.readString();
        this.total_views = in.readInt();
        this.created_at = in.readString();
        this.thumb = in.readString();
        this.rating = in.readFloat();
    }

    public static final Parcelable.Creator<DramaBean> CREATOR = new Parcelable.Creator<DramaBean>() {
        public DramaBean createFromParcel(Parcel source) {
            return new DramaBean(source);
        }

        public DramaBean[] newArray(int size) {
            return new DramaBean[size];
        }
    };
}
