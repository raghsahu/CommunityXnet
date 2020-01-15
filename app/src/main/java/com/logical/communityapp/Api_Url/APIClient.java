package com.logical.communityapp.Api_Url;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

//        OkHttpClient client = new OkHttpClient.Builder()
//                //.connectionSpecs(tlsSpecs)
//                .connectTimeout(100, TimeUnit.SECONDS)
//                .readTimeout(100,TimeUnit.SECONDS)
//                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(Base_Url.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();



        return retrofit;
    }
}
