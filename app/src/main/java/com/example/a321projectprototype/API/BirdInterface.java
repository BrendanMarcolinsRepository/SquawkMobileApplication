package com.example.a321projectprototype.API;



import com.example.a321projectprototype.User.BirdModel;
import com.example.a321projectprototype.ui.Discover.ItemDataModel;
import com.google.firebase.database.core.Repo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BirdInterface
{

    @GET("v2/data/obs/AU/recent?key=ictn5ms95tuh")
    Call<List<BirdModel>> listRepos();
}
