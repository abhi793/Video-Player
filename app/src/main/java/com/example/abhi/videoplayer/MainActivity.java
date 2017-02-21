package com.example.abhi.videoplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.abhi.videoplayer.recyclercomponents.GridListDecorator;
import com.example.abhi.videoplayer.recyclercomponents.RecyclerItemClickListener;
import com.example.abhi.videoplayer.recyclercomponents.RecyclerViewAdapter;
import com.example.abhi.videoplayer.uicomponents.IndicatorView;
import com.example.abhi.videoplayer.youtubedataservice.OnYoutubeDataReceivedListener;
import com.example.abhi.videoplayer.youtubedataservice.YoutubeDataService;
import com.example.abhi.videoplayer.youtubedataservice.YoutubeDataServiceBinder;
import com.example.abhi.videoplayer.youtubedataservice.YoutubeDataServiceInputProvider;
import com.example.abhi.videoplayer.youtubedataservice.models.YoutubeData;

import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerItemClickListener.OnItemClickListener, OnYoutubeDataReceivedListener {
    private RecyclerView inTheatresListView;
    private RecyclerView upcomingListView;

    private IndicatorView inTheatresIndicatorView;
    private IndicatorView upcomingIndicatorView;
    private int visibleViewCount;

    private ServiceConnection serviceConnection;
    private YoutubeDataServiceInputProvider youtubeDataServiceInputProvider;

    private RecyclerViewAdapter inTheatreListAdapter;
    private RecyclerViewAdapter upcomingListAdapter;

    private List<YoutubeData> inTheatresList;
    private List<YoutubeData> upcomingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!isInternetConnected(this)){
            buildAlertMessageNoInternet();
        }

            createServiceConnection();

            /*startService(new Intent(this, YoutubeDataService.class));
            bindService(new Intent(this, YoutubeDataService.class), serviceConnection, BIND_AUTO_CREATE);*/
        Constants.setVideoIds();
        Constants.setUpcomingVideoIds();
        visibleViewCount = 4;

        initializeViews();

        inTheatresIndicatorView.setTotalItems(Constants.getVideoIds(Constants.IN_THEATRES).size() / visibleViewCount);
        upcomingIndicatorView.setTotalItems(Constants.getVideoIds(Constants.UPCOMING).size() / visibleViewCount);

        setRecyclerViews();
    }

    @Override
    protected void onStart() {
        if(inTheatresList==null && isInternetConnected(this))
        {
            startService(new Intent(this, YoutubeDataService.class));
            bindService(new Intent(this, YoutubeDataService.class), serviceConnection, BIND_AUTO_CREATE);
        }
        super.onStart();
    }

    private void createServiceConnection() {
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                YoutubeDataServiceBinder youtubeDataServiceBinder = (YoutubeDataServiceBinder) service;
                youtubeDataServiceInputProvider = youtubeDataServiceBinder.getYoutubeDataServiceInputProvider();
                youtubeDataServiceInputProvider.setOnYoutubeDataReceivedListener(MainActivity.this);
                youtubeDataServiceInputProvider.requestYoutubeData();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                youtubeDataServiceInputProvider = null;
            }
        };
    }

    public void initializeViews() {
        inTheatresListView = (RecyclerView) findViewById(R.id.in_theatres);
        upcomingListView = (RecyclerView) findViewById(R.id.upcoming);
        inTheatresIndicatorView = (IndicatorView) findViewById(R.id.in_theatres_indicator);
        upcomingIndicatorView = (IndicatorView) findViewById(R.id.upcoming_indicator);
    }

    public void setRecyclerViews() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.HORIZONTAL, false);
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(this, 2, LinearLayoutManager.HORIZONTAL, false);

        inTheatresListView.setLayoutManager(gridLayoutManager);
        upcomingListView.setLayoutManager(gridLayoutManager1);

        inTheatreListAdapter = new RecyclerViewAdapter(this, inTheatresList);
        upcomingListAdapter = new RecyclerViewAdapter(this, upcomingList);

        inTheatresListView.setAdapter(inTheatreListAdapter);
        upcomingListView.setAdapter(upcomingListAdapter);

        GridListDecorator gridListDecorator = new GridListDecorator();

        inTheatresListView.addItemDecoration(gridListDecorator);
        upcomingListView.addItemDecoration(gridListDecorator);

        inTheatresListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int averageAdapterPosition = recyclerView.getChildAdapterPosition(recyclerView.getChildAt(recyclerView.getChildCount() - 1))
                        + recyclerView.getChildAdapterPosition(recyclerView.getChildAt(0));
                averageAdapterPosition /= 2;
                int indicatorPosition = (averageAdapterPosition / visibleViewCount);
                inTheatresIndicatorView.setIndicatorPosition(indicatorPosition);
            }
        });

        upcomingListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int averageAdapterPosition = recyclerView.getChildAdapterPosition(recyclerView.getChildAt(recyclerView.getChildCount() - 1))
                        + recyclerView.getChildAdapterPosition(recyclerView.getChildAt(0));
                averageAdapterPosition /= 2;
                int indicatorPosition = (averageAdapterPosition / visibleViewCount);
                upcomingIndicatorView.setIndicatorPosition(indicatorPosition);
            }
        });
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View view, int position) {
        Intent intent = new Intent(this, VideoPresenterActivity.class);
        if (recyclerView == inTheatresListView) {
            intent.putExtra("LIST_TYPE", Constants.IN_THEATRES);
        } else if (recyclerView == upcomingListView) {
            intent.putExtra("LIST_TYPE", Constants.UPCOMING);
        }
        intent.putExtra("POSITION", position);
        startActivity(intent);
    }

    @Override
    public void onDataReceived() {
        inTheatresList = youtubeDataServiceInputProvider.getIntheatresList();
        upcomingList = youtubeDataServiceInputProvider.getUpcomingList();

        setRecyclerViews();

        RecyclerItemClickListener recyclerItemClickListener = new RecyclerItemClickListener(this, this);

        inTheatresListView.addOnItemTouchListener(recyclerItemClickListener);
        upcomingListView.addOnItemTouchListener(recyclerItemClickListener);
    }

    @Override
    protected void onDestroy() {
        if(inTheatresList!=null) {
            unbindService(serviceConnection);
            stopService(new Intent(this, YoutubeDataService.class));
        }
        super.onDestroy();
    }
    private void buildAlertMessageNoInternet() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your internet seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();

                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    private static boolean isInternetConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        if (info.getState() != NetworkInfo.State.CONNECTED) {
            return false;
        }
        return true;
    }
}
