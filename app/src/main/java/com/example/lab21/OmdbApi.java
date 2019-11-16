package com.example.lab21;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OmdbApi {

    @GET("/")
    Single<Record> getByTitle(@Query("t") String title, @Query("apikey") String apiKey);

    @GET("/")
    Single<SearchResponse> searchByTitle(@Query("s") String title, @Query("apikey") String apiKey);
}
