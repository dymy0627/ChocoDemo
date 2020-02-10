package com.yulin.myapplication.database;

import com.yulin.myapplication.DramaBean;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import io.reactivex.Single;

@Dao
public interface DramaDao {

    @Query("select * from drama")
    Single<List<DramaBean>> getDramaList();

    @Insert
    void insertDramaList(List<DramaBean> dramaBeans);

    @Query("delete from drama")
    void deleteAllDrama();
}
