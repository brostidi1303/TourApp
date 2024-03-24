package com.example.tourapp.Utility;

import com.example.tourapp.Interface.Api;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static String BASE_URL="http://192.168.1.27:4000/";
    //private static String BASE_URL="http://localhost:4000";

    public static Retrofit getInstanceAccess(final String token) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        if (token != null && !token.isEmpty()) {
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .header("Authorization", "Bearer " + token)
                            .build();
                    return chain.proceed(request);
                }
            });
        }
        Gson gson = new GsonBuilder()
                .setDateFormat("HH:mm:ss dd/MM/yyyy") // Đặt định dạng ngày ở đây
                .create();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson)) // Sử dụng Gson với định dạng đã chỉ định
                .client(httpClient.build())
                .build();
    }




    public static Retrofit getInstance(){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }

}
