package com.mobiotic.videoplay.demo.main_activity;

import com.mobiotic.videoplay.demo.model.VideosListModel;

import java.util.List;

public interface RecyclerItemClickListener {
    void onItemClick(VideosListModel notice, List<VideosListModel> dataList);
}