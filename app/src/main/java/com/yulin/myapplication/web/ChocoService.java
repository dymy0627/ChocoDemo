package com.yulin.myapplication.web;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ChocoService {

    @GET
    Single<DramaResponse> getDramas(@Url String url);
}
