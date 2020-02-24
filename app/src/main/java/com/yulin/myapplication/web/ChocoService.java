package com.yulin.myapplication.web;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface ChocoService {

    @GET("interview/dramas-sample.json")
    Single<DramaResponse> getDramas();
}
