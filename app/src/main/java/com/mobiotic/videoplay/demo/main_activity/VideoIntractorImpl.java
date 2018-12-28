package com.mobiotic.videoplay.demo.main_activity;

import android.util.Log;

import com.mobiotic.videoplay.demo.model.VideosListModel;
import com.mobiotic.videoplay.demo.my_interface.VideoDataService;
import com.mobiotic.videoplay.demo.network.RetrofitInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by bpn on 12/7/17.
 */

public class VideoIntractorImpl implements MainContract.videoIntractor {

    @Override
    public void getVideoArrayList(final OnFinishedListener onFinishedListener) {


        /** Create handle for the RetrofitInstance interface*/
        VideoDataService service = RetrofitInstance.getRetrofitInstance().create(VideoDataService.class);

        /** Call the method with parameter in the interface to get the notice data*/
        Call<List<VideosListModel>> call = service.getVideoData("pretty");

        /**Log the URL called*/
        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<List<VideosListModel>>() {
            @Override
            public void onResponse(Call<List<VideosListModel>> call, Response<List<VideosListModel>> response) {
                onFinishedListener.onFinished(response.body());

            }

            @Override
            public void onFailure(Call<List<VideosListModel>> call, Throwable t) {
                onFinishedListener.onFailure(t);
            }
        });

    }

}
