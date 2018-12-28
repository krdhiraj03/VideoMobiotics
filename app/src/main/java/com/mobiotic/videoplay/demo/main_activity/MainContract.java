package com.mobiotic.videoplay.demo.main_activity;

import com.mobiotic.videoplay.demo.model.VideosListModel;

import java.util.List;


public interface MainContract {

    interface presenter{

        void onDestroy();

        void onRefreshButtonClick();

        void requestDataFromServer();

    }

    interface MainView {

        void showProgress();

        void hideProgress();

        void setDataToRecyclerView(List<VideosListModel> videosArrayList);

        void onResponseFailure(Throwable throwable);

    }

    interface videoIntractor {

        interface OnFinishedListener {
            void onFinished(List<VideosListModel> noticeArrayList);
            void onFailure(Throwable t);
        }

        void getVideoArrayList(OnFinishedListener onFinishedListener);
    }
}
