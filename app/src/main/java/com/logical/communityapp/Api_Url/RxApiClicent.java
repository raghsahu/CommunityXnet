package com.logical.communityapp.Api_Url;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Raghvendra Sahu on 23/12/2019.
 */
public class RxApiClicent {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl) {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()

                    .baseUrl(baseUrl)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
