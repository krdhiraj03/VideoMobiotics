package com.mobiotic.videoplay.demo.my_interface;

import com.mobiotic.videoplay.demo.model.VideosListModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VideoDataService {


    @GET("media.json")
    Call<List<VideosListModel>> getVideoData(@Query("print") String pretty);

}