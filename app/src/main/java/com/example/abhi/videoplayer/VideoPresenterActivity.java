package com.example.abhi.videoplayer;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.abhi.videoplayer.recyclercomponents.OnListItemCenteredListener;
import com.example.abhi.videoplayer.recyclercomponents.OnVideoItemClickListener;
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
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.List;

public class VideoPresenterActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, OnListItemCenteredListener, OnVideoItemClickListener, YouTubePlayer.OnFullscreenListener {
    private @Constants.ListType int listType;
    private int position;

    private ServiceConnection serviceConnection;
    private YoutubeDataServiceInputProvider youtubeDataServiceInputProvider;

    private List<YoutubeData> list;

    private TextView titleText;
    private TextView descriptionText;
    private TextView durationText;
    private RatingView ratingView;
    private RecyclerView videoList;
    private YouTubePlayerView youTubePlayerView;

    private VideoListAdapter videoListAdapter;
    private VideoListLayoutManager videoListLayoutManager;
    private YouTubePlayer youTubePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_presenter);

        createServiceConnection();

        bindService(new Intent(this, YoutubeDataService.class), serviceConnection, BIND_AUTO_CREATE);

        listType = getIntent().getIntExtra("LIST_TYPE", Constants.IN_THEATRES);
        position = getIntent().getIntExtra("POSITION" , 0);

        initViews();
        setViews();

        youTubePlayerView.initialize(Constants.DEVELOPER_KEY, this);
    }

    private void setRecyclerView() {
        videoListLayoutManager = new VideoListLayoutManager(this);
        videoListLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        videoListLayoutManager.setOnListItemCenteredListener(this);

        videoListAdapter = new VideoListAdapter(this, list);

        videoListAdapter.setOnVideoItemClickListener(this);

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
        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player);
    }

    private void setViews() {
        descriptionText.setLineSpacing(0, 1.1f);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        this.youTubePlayer = youTubePlayer;
        youTubePlayer.setOnFullscreenListener(this);

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    @Override
    public void onBackPressed() {
        if ((youTubePlayer != null) && (youTubePlayerView.getVisibility() == View.VISIBLE)) {
            youTubePlayer.pause();
            youTubePlayerView.setVisibility(View.GONE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            videoListLayoutManager.stabilizeList();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void OnListItemCentered(int position) {
        titleText.setText(list.get(position).getItems().get(0).getSnippet().getTitle());
        descriptionText.setText(list.get(position).getItems().get(0).getSnippet().getDescription());
        durationText.setText("Runtime: " + 133 + " mins");
        ratingView.animateArcUsingFraction(0.81f);
    }

    @Override
    public void onVideoItemClick(String videoId) {
        youTubePlayer.loadVideo(videoId);
        youTubePlayer.setFullscreen(true);
        youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
    }

    @Override
    public void onFullscreen(boolean b) {
        if (b) {
            youTubePlayerView.setVisibility(View.VISIBLE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }
}