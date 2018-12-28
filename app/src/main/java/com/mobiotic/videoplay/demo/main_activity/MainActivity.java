package com.mobiotic.videoplay.demo.main_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mobiotic.videoplay.demo.R;
import com.mobiotic.videoplay.demo.adapter.VideoListAdapter;
import com.mobiotic.videoplay.demo.model.VideosListModel;
import com.mobiotic.videoplay.demo.video_play_activity.VideoPlayActivity;

import java.io.Serializable;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainContract.MainView {

    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    private MainContract.presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeToolbarAndRecyclerView();
        initProgressBar();

        presenter = new MainPresenterImpl(this, new VideoIntractorImpl());
        presenter.requestDataFromServer();

    }

    private void initializeToolbarAndRecyclerView() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recycler_view_video_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);


    }

    private void initProgressBar() {
        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        progressBar.setIndeterminate(true);

        RelativeLayout relativeLayout = new RelativeLayout(this);
        relativeLayout.setGravity(Gravity.CENTER);
        relativeLayout.addView(progressBar);

        RelativeLayout.LayoutParams params = new
                RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        progressBar.setVisibility(View.INVISIBLE);

        this.addContentView(relativeLayout, params);
    }

    private RecyclerItemClickListener recyclerItemClickListener = new RecyclerItemClickListener() {
        @Override
        public void onItemClick(VideosListModel videos, List<VideosListModel> dataList) {

            Toast.makeText(MainActivity.this,
                    "List title:  " + videos.getTitle(),
                    Toast.LENGTH_LONG).show();
            Intent videoIntent = new Intent(MainActivity.this,VideoPlayActivity.class);
            videoIntent.putExtra("VIDEO_TITLE",videos.getTitle());
            videoIntent.putExtra("VIDEO_DESC",videos.getDescription());
            videoIntent.putExtra("VIDEO_ID",videos.getId());
            videoIntent.putExtra("VIDEO_URL",videos.getUrl());
            videoIntent.putExtra("RELATED_VIDEOS", (Serializable) dataList);
            startActivity(videoIntent);
        }
    };


    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }


    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
    }


    @Override
    public void setDataToRecyclerView(List<VideosListModel> videosArrayList) {

        VideoListAdapter adapter = new VideoListAdapter(this, videosArrayList, recyclerItemClickListener);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        Toast.makeText(MainActivity.this,
                "Something went wrong...Error message: " + throwable.getMessage(),
                Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            presenter.onRefreshButtonClick();
        }

        return super.onOptionsItemSelected(item);
    }


}

