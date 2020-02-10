package com.yulin.myapplication.web;

import com.yulin.myapplication.DramaBean;

import java.util.List;

public class DramaResponse {

    private List<DramaBean> data;

    public List<DramaBean> getData() {
        return data;
    }

    public void setData(List<DramaBean> data) {
        this.data = data;
    }
}
