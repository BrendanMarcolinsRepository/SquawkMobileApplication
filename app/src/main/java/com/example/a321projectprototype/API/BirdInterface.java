package com.example.a321projectprototype.API;



import com.example.a321projectprototype.User.BirdModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BirdInterface
{

    @GET("v2/data/obs/AU/recent?key=ictn5ms95tuh")
    Call<List<BirdModel>> listRepos();


    //@GET("species/{input}")
   // Call<> getBird(@Path("input") String url);
}
