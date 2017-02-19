package com.example.abhi.videoplayer;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.abhi.videoplayer.recyclercomponents.OnListItemCenteredListener;
import com.example.abhi.videoplayer.recyclercomponents.VideoListAdapter;
import com.example.abhi.videoplayer.recyclercomponents.VideoListDecorator;
import com.example.abhi.videoplayer.recyclercomponents.VideoListLayoutManager;
import com.example.abhi.videoplayer.uicomponents.RatingView;
import com.example.abhi.videoplayer.youtubedataservice.YoutubeDataService;
import com.example.abhi.videoplayer.youtubedataservice.YoutubeDataServiceBinder;
import com.example.abhi.videoplayer.youtubedataservice.YoutubeDataServiceInputProvider;
import com.example.abhi.videoplayer.youtubedataservice.models.YoutubeData;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;

import java.util.List;

public class VideoPlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, OnListItemCenteredListener {
    private @Constants.ListType int listType;
    private int position;

    private String videoId;

    private ServiceConnection serviceConnection;
    private YoutubeDataServiceInputProvider youtubeDataServiceInputProvider;

    private List<YoutubeData> list;

    private TextView titleText;
    private TextView descriptionText;
    private TextView durationText;
    private RatingView ratingView;
    private RecyclerView videoList;

    private VideoListAdapter videoListAdapter;
    private VideoListLayoutManager videoListLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        createServiceConnection();

        bindService(new Intent(this, YoutubeDataService.class), serviceConnection, BIND_AUTO_CREATE);

        listType = getIntent().getIntExtra("LIST_TYPE", Constants.IN_THEATRES);
        position = getIntent().getIntExtra("POSITION" , 0);

        initViews();
        setViews();
    }

    private void setRecyclerView() {
        videoListLayoutManager = new VideoListLayoutManager(this);
        videoListLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        videoListLayoutManager.setOnListItemCenteredListener(this);

        videoListAdapter = new VideoListAdapter(this, list);

        videoList.setAdapter(videoListAdapter);
        videoList.setLayoutManager(videoListLayoutManager);

        videoList.addItemDecoration(new VideoListDecorator());

        videoList.scrollToPosition(position);

        titleText.setText(list.get(position).getItems().get(0).getSnippet().getTitle());
        descriptionText.setText(list.get(position).getItems().get(0).getSnippet().getDescription());
        durationText.setText("Runtime: " + 133 + " mins");
        ratingView.animateArcUsingFraction(0.81f);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //playButton.setVisibility(View.GONE);
    }

    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
        if (videoListLayoutManager != null) {
            videoListLayoutManager.stabilizeList();
        }
    }

    private void createServiceConnection() {
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                YoutubeDataServiceBinder youtubeDataServiceBinder = (YoutubeDataServiceBinder) service;
                youtubeDataServiceInputProvider = youtubeDataServiceBinder.getYoutubeDataServiceInputProvider();

                switch (listType) {
                    case Constants.IN_THEATRES:
                        list = youtubeDataServiceInputProvider.getIntheatresList();
                        break;
                    case Constants.UPCOMING:
                        list = youtubeDataServiceInputProvider.getUpcomingList();
                        break;
                }

                videoId = list.get(position).getItems().get(0).getId();

                setRecyclerView();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                youtubeDataServiceInputProvider = null;
            }
        };
    }

    private void initViews() {
        titleText = (TextView)findViewById(R.id.title_text);
        descriptionText = (TextView)findViewById(R.id.description_text);
        durationText = (TextView)findViewById(R.id.runtime_text);
        ratingView = (RatingView) findViewById(R.id.rating_view);
        videoList = (RecyclerView) findViewById(R.id.player_recycler_view);
    }

    private void setViews() {
        descriptionText.setLineSpacing(0, 1.1f);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        /*youTubePlayer.cueVideo(videoId);
        youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);

        titleText.setText(list.get(position).getItems().get(0).getSnippet().getTitle());
        descriptionText.setText(list.get(position).getItems().get(0).getSnippet().getDescription());
        durationText.setText("Runtime: " + (Math.round(youTubePlayer.getDurationMillis() / 1000f / 60f)) + " mins");
        ratingView.animateArcUsingFraction(0.81f);*/
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    /*@Override
    public void onBackPressed() {
        if ((youTubePlayer != null) && (youTubePlayer.isPlaying())) {
            youTubePlayer.seekToMillis(youTubePlayer.getDurationMillis());
            youTubePlayer.setFullscreen(false);
            playButton.setVisibility(View.VISIBLE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            youTubePlayer.cueVideo(videoId);
        } else {
            super.onBackPressed();
        }
    }*/

    @Override
    public void OnListItemCentered(int position) {
        titleText.setText(list.get(position).getItems().get(0).getSnippet().getTitle());
        descriptionText.setText(list.get(position).getItems().get(0).getSnippet().getDescription());
        durationText.setText("Runtime: " + 133 + " mins");
        ratingView.animateArcUsingFraction(0.81f);
    }

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }
}