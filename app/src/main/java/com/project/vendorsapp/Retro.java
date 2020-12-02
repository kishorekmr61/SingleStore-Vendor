package com.project.vendorsapp;

import android.content.Context;
import android.net.ConnectivityManager;

import com.project.vendorsapp.Utilities.CommonUtilities;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retro {

    private static Retrofit retrofit = null;

    public Retrofit call()
    {

               /*  Retrofit retrofit = new Retrofit.Builder().baseUrl("http://livesale.asrpos.com/").
                         addConverterFactory(GsonConverterFactory.create()).build();

                return retrofit;*/

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES).build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(CommonUtilities.url)
                    .addConverterFactory(GsonConverterFactory.create()).client(client)
                    .build();
        }
        return retrofit;
    }

    public Retrofit call(String ServiceURL)
    {

               /*  Retrofit retrofit = new Retrofit.Builder().baseUrl("http://livesale.asrpos.com/").
                         addConverterFactory(GsonConverterFactory.create()).build();

                return retrofit;*/

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES).build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(ServiceURL)
                    .addConverterFactory(GsonConverterFactory.create()).client(client)
                    .build();
        }
        return retrofit;
    }


    public boolean isNetworkConnected(Context context)
    {
        ConnectivityManager cm =(ConnectivityManager)context. getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

}
