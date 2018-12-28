package com.mobiotic.videoplay.demo.video_play_activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import com.mobiotic.videoplay.demo.R;
import com.mobiotic.videoplay.demo.SQLiteHandler;
import com.mobiotic.videoplay.demo.model.VideosListModel;
import com.mobiotic.videoplay.demo.model.RelatedVideoModel;
import com.mobiotic.videoplay.demo.model.VideoSQLiteData;
import com.poliveira.parallaxrecyclerview.ParallaxRecyclerAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;



public class VideoPlayActivity extends AppCompatActivity{

    private TextView title, description;
    private String videoUrl,videoID,videoTitle,videoDesc;
    private static SimpleExoPlayer exoPlayer;
    private PlayerView playerView;
    private Uri fetched_uri;
    private static AudioManager audioManager;
    private SeekBar volumeSeekBar;
    private ImageButton exoplayer_pause,exoplayer_play,exoplayer_prev,exoplayer_next;
    private View header;
    Activity activity;
    private ProgressBar videoProgress;
    private Switch autoPlaySwitch;
    private Boolean autoPlayFlag = false;
    private int relatedVideoPosition = -1;
    private RecyclerView recyclerViewRelatedVideos;
    private List<VideosListModel> relatedVideosList = new ArrayList<>();
    private Boolean alert =false;
    private LayoutInflater inflater;
    SQLiteHandler handler;
    List<RelatedVideoModel> relatedVideoModelsList = new ArrayList<RelatedVideoModel>();
    ParallaxRecyclerAdapter<RelatedVideoModel> adapter;
    java.util.List<VideoSQLiteData> videodata = new ArrayList<VideoSQLiteData>();
    private MediaSource medias;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.fragment_video_playing);
        activity = VideoPlayActivity.this;
        handler = new SQLiteHandler(getApplicationContext());

        Intent i = getIntent();
        videoTitle = getIntent().getExtras().getString("VIDEO_TITLE");
        videoDesc = getIntent().getExtras().getString("VIDEO_DESC");
        videoID = getIntent().getExtras().getString("VIDEO_ID");
        videoUrl = getIntent().getExtras().getString("VIDEO_URL");
        relatedVideosList = (List<VideosListModel>) i.getSerializableExtra("RELATED_VIDEOS");
        playerView = findViewById(R.id.playing_video);
        exoplayer_pause=findViewById(R.id.exo_pause);
        exoplayer_play=findViewById(R.id.exo_play);
        exoplayer_prev=findViewById(R.id.exo_prev);
        exoplayer_next=findViewById(R.id.exo_next);
        videoProgress = findViewById(R.id.video_progressbar);
        videoProgress.setVisibility(View.VISIBLE);
        volumeSeekBar = findViewById(R.id.volume_seekbar);
        fetched_uri = Uri.parse(videoUrl);
        recyclerViewRelatedVideos = findViewById(R.id.rv_related_videos);
        recyclerViewRelatedVideos.setLayoutManager(new LinearLayoutManager(this));
        header = getLayoutInflater().inflate(R.layout.current_video_details_view, recyclerViewRelatedVideos, false);

        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        title.setText(videoTitle);
        description.setText(videoDesc);
        autoPlaySwitch = findViewById(R.id.switch_autoplay);
        autoPlaySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                autoPlayFlag = isChecked;
            }
        });
        inflater = (LayoutInflater) VideoPlayActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(handler.check()!= null && handler.check().getCount()>0){

            Log.d("TAG", "onCreat111e: "+ handler.getData(Integer.parseInt(videoID)));
            loadrelatedvideos(videoID);
            playVideo(fetched_uri);
            if(handler.isExist(videoID))
                exoPlayer.seekTo(Long.parseLong(handler.getData(Integer.parseInt(videoID))));
            else
                exoPlayer.seekTo(0);

        }
        else {
            Toast.makeText(getBaseContext(), "No records yet!", Toast.LENGTH_SHORT).show();
            loadrelatedvideos(videoID);
            playVideo(fetched_uri);

        }
    }
    private MediaSource buildMediaSource (Uri uri){
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplaer-codelab")).createMediaSource(uri);
    }
    private void releasePlayer ()
    {
        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(false);
        }
    }
    @Override
    public void onResume () {
        super.onResume();
    }

    @Override
    public void onPause () {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onStop () {
        super.onStop();

    }

    @Override
    public void onBackPressed() {

        Toast.makeText(VideoPlayActivity.this, exoPlayer.getCurrentPosition()+"", Toast.LENGTH_LONG).show();
        if (handler.isExist(videoID)) {
            handler.updatePlayedVideoInfo(videoID,String.valueOf(exoPlayer.getCurrentPosition()));
        }else {
            handler.savePlayedVideoInfo(videoID,String.valueOf(exoPlayer.getCurrentPosition()));

        }

        super.onBackPressed();
    }
    private void loadrelatedvideos(final String videoID) {


        for (int i = 0;i<relatedVideosList.size();i++){
            RelatedVideoModel relatedVideoModel = new RelatedVideoModel();
            String videoId = relatedVideosList.get(i).getId();
            String videoTitle = relatedVideosList.get(i).getTitle();
            String videoDescription = relatedVideosList.get(i).getDescription();
            String videoThumb = relatedVideosList.get(i).getThumb();
            String videoUrl = relatedVideosList.get(i).getUrl();
            relatedVideoModel.setId(videoId);
            relatedVideoModel.setTitle(videoTitle);
            relatedVideoModel.setDescription(videoDescription);
            relatedVideoModel.setThumb(videoThumb);
            relatedVideoModel.setUrl(videoUrl);
            if(relatedVideoModel.getId().equals(videoID)){

                continue;
            }
            relatedVideoModelsList.add(relatedVideoModel);
            createAdapter(recyclerViewRelatedVideos);
        }



    }
    private void createAdapter(final RecyclerView recyclerView) {
        adapter = new ParallaxRecyclerAdapter<RelatedVideoModel>(relatedVideoModelsList) {
            @Override
            public void onBindViewHolderImpl(RecyclerView.ViewHolder viewHolder, ParallaxRecyclerAdapter<RelatedVideoModel> adapter, int i) {

                ((VideoPlayActivity.ViewHolder) viewHolder).relatedVideoTitle.setText(String.valueOf(adapter.getData().get(i).getTitle()));
                ((VideoPlayActivity.ViewHolder) viewHolder).relatedVideoDescription.setText(adapter.getData().get(i).getDescription());

                Picasso.get().load(adapter.getData().get(i).getThumb()).into(((VideoPlayActivity.ViewHolder) viewHolder).thumbnail);
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolderImpl(ViewGroup viewGroup, final ParallaxRecyclerAdapter<RelatedVideoModel> adapter, int i) {
                return new VideoPlayActivity.ViewHolder(getLayoutInflater().inflate(R.layout.video_playing_activity_related_videos_cardview, viewGroup, false));
            }

            @Override
            public int getItemCountImpl(ParallaxRecyclerAdapter<RelatedVideoModel> adapter) {
                return relatedVideoModelsList.size();
            }
        };

        adapter.setOnClickEvent(new ParallaxRecyclerAdapter.OnClickEvent() {
            @Override
            public void onClick(View v, int position) {

                relatedVideoPosition = position;
                videoID = relatedVideoModelsList.get(position).getId();
                title.setText(relatedVideoModelsList.get(position).getTitle());
                description.setText(relatedVideoModelsList.get(position).getDescription());
                exoPlayer.stop();
                exoPlayer.seekTo(0);
                releasePlayer();
                exoPlayer = null;
                Uri uri = Uri.parse(relatedVideoModelsList.get(position).getUrl());
                playVideo(uri);
            }
        });

        ((ViewGroup)header).removeView(header);
        adapter.setData(relatedVideoModelsList);
        recyclerView.setAdapter(adapter);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView relatedVideoTitle, relatedVideoDescription;

        ViewHolder(View itemView) {
            super(itemView);
            thumbnail= itemView.findViewById(R.id.video_thumb);
            relatedVideoTitle = itemView.findViewById(R.id.related_video_title);
            relatedVideoDescription = itemView.findViewById(R.id.video_desc);


        }
    }

    private void playVideo(Uri fetched_uri) {

        exoPlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(), new DefaultLoadControl());
        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
        playerView.setPlayer(exoPlayer);
        if (exoPlayer!=null) {
            exoPlayer.setPlayWhenReady(true);
        }
        medias = buildMediaSource(fetched_uri);

//        MediaSource[] mediaSources = new MediaSource[relatedVideosList.size()];
//        for (int i = 0; i < relatedVideosList.size(); i++) {
//            mediaSources[i] = buildMediaSource(Uri.parse(relatedVideosList.get(i).getUrl()));
//        }
//        medias = mediaSources.length == 1 ? mediaSources[5]
//                : new ConcatenatingMediaSource(mediaSources);
        exoPlayer.prepare(medias, true, true);
                videoProgress.setVisibility(View.INVISIBLE);
        audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            public void onProgressChanged(SeekBar bar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(exoPlayer.getAudioStreamType(), progress, 0);
            }

            public void onStartTrackingTouch(SeekBar bar) {
                // no-op
            }

            public void onStopTrackingTouch(SeekBar bar) {
                // no-op
            }
        });


        exoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

                if ( playbackState == Player.STATE_READY) {
                    videoProgress.setVisibility(View.INVISIBLE);
                    playerView.setKeepScreenOn(true);
                }else if(playbackState == Player.STATE_ENDED){
                    videoProgress.setVisibility(View.INVISIBLE);

                    if((relatedVideoPosition+1) < relatedVideoModelsList.size()){
                        relatedVideoPosition++;
                        if(autoPlaySwitch.isChecked()){
                            Uri uri = Uri.parse(relatedVideoModelsList.get(relatedVideoPosition).getUrl());

                            exoPlayer.setPlayWhenReady(true);
                            exoPlayer.stop();
                            exoPlayer.seekTo(0);

                            exoPlayer = ExoPlayerFactory.newSimpleInstance(
                                    new DefaultRenderersFactory(VideoPlayActivity.this),
                                    new DefaultTrackSelector(), new DefaultLoadControl());
                            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);

                            playerView.setPlayer(exoPlayer);
                            if (exoPlayer!=null) {
                                exoPlayer.setPlayWhenReady(true);
                            }
//                            MediaSource mediaSource = buildMediaSource(Uri.parse(relatedVideoModelsList.get(relatedVideoPosition).getUrl()));
                            MediaSource[] mediaSources = new MediaSource[relatedVideosList.size()];
                            for (int i = 0; i < relatedVideosList.size(); i++) {
                                mediaSources[i] = buildMediaSource(Uri.parse(relatedVideosList.get(i).getUrl()));
                            }
                            medias = mediaSources.length == 1 ? mediaSources[0]
                                    : new ConcatenatingMediaSource(mediaSources);
                            exoPlayer.prepare(medias, true, true);
                            playVideo(uri);
                            title.setText(relatedVideoModelsList.get(relatedVideoPosition).getTitle());
                            description.setText(relatedVideoModelsList.get(relatedVideoPosition).getDescription());
                            loadrelatedvideos(videoID);
                        }

                    }
                }

                else  {
                    playerView.setKeepScreenOn(false);
                }
                if(playbackState == Player.STATE_BUFFERING) {
                    exoplayer_pause.setVisibility(View.GONE);
                    videoProgress.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
//                preparePlayer();
            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });
        Log.d("TAG", "fetched_uri in video playing fragment->" + fetched_uri);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            exoPlayer.setPlayWhenReady(true);
        }else{
            exoPlayer.setPlayWhenReady(false);
        }
    }



}
