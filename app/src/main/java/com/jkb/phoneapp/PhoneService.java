package com.jkb.phoneapp;

import com.jkb.phoneapp.Phone;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PhoneService {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.0.102:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET("phone")
    Call<CMRespDto<List<Phone>>> findAll();

    @POST("phone")
    Call<Phone> insertPost(@Body Phone phone);

    @DELETE("phone/{id}")
    Call<Void> deletePost(@Path("id") int id);

    @PUT("phone/{id}")
    Call<Phone> updatePost(@Path("id") int id, @Body Phone phone);


}