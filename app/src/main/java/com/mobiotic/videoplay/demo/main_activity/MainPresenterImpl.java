package com.mobiotic.videoplay.demo.main_activity;

import com.mobiotic.videoplay.demo.model.VideosListModel;

import java.util.List;


public class MainPresenterImpl implements MainContract.presenter, MainContract.videoIntractor.OnFinishedListener {

    private MainContract.MainView mainView;
    private MainContract.videoIntractor getVideoIntractor;

    public MainPresenterImpl(MainContract.MainView mainView, MainContract.videoIntractor getNoticeIntractor) {
        this.mainView = mainView;
        this.getVideoIntractor = getNoticeIntractor;
    }

    @Override
    public void onDestroy() {

        mainView = null;

    }

    @Override
    public void onRefreshButtonClick() {

        if(mainView != null){
            mainView.showProgress();
        }
        getVideoIntractor.getVideoArrayList(this);

    }

    @Override
    public void requestDataFromServer() {
        getVideoIntractor.getVideoArrayList(this);
    }


    @Override
    public void onFinished(List<VideosListModel> noticeArrayList) {
        if(mainView != null){
            mainView.setDataToRecyclerView(noticeArrayList);
            mainView.hideProgress();
        }
    }

    @Override
    public void onFailure(Throwable t) {
        if(mainView != null){
            mainView.onResponseFailure(t);
            mainView.hideProgress();
        }
    }
}
