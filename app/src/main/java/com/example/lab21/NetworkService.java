package com.example.lab21;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {
    private static NetworkService instance;
    private static  final String BASE_URL="http://www.omdbapi.com";
    private Retrofit mRetrofit;

    private NetworkService(){
        mRetrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

    }

    public static NetworkService getInstance(){
        if (instance==null){
            instance=new NetworkService();
        }
        return instance;
    }

    public OmdbApi getJSONapi(){
        return mRetrofit.create(OmdbApi.class);
    }
}
