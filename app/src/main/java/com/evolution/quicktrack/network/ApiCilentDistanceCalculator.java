package com.evolution.quicktrack.network;

import com.evolution.quicktrack.constants.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiCilentDistanceCalculator {
    private static Retrofit retrofit = null;

    static class C15101 implements Interceptor {
        C15101() {
        }

        public Response intercept(Chain chain) throws IOException {
            return chain.proceed(chain.request().newBuilder().addHeader("test", "test").build());
        }
    }

    public static Retrofit getClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        //interceptor.setLevel(Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        if (retrofit == null) {
            retrofit = new Builder()
                    .baseUrl(Constants.DistanceCalculatorUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
