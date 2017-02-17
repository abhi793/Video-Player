package com.example.abhi.videoplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.abhi.videoplayer.recyclercomponents.GridListDecorator;
import com.example.abhi.videoplayer.recyclercomponents.RecyclerViewAdapter;
import com.example.abhi.videoplayer.uicomponents.IndicatorView;

public class MainActivity extends AppCompatActivity {
    private RecyclerView inTheatresListView;
    private RecyclerView upcomingListView;

    private IndicatorView inTheatresIndicatorView;
    private IndicatorView upcomingIndicatorView;

    private int visibleViewCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Constants.setVideoIds();
        visibleViewCount = 4;

        initializeViews();

        inTheatresIndicatorView.setTotalItems(Constants.getVideoIds().size() / visibleViewCount);
        upcomingIndicatorView.setTotalItems(Constants.getVideoIds().size() / visibleViewCount);

        setRecyclerViews();
    }

    public void initializeViews() {
        inTheatresListView = (RecyclerView) findViewById(R.id.in_theatres);
        upcomingListView = (RecyclerView) findViewById(R.id.upcoming);
        inTheatresIndicatorView = (IndicatorView) findViewById(R.id.in_theatres_indicator);
        upcomingIndicatorView = (IndicatorView) findViewById(R.id.upcoming_indicator);
    }

    public void setRecyclerViews() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.HORIZONTAL, false);

        inTheatresListView.setLayoutManager(gridLayoutManager);
        //upcomingListView.setLayoutManager(gridLayoutManager);

        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter();

        inTheatresListView.setAdapter(recyclerViewAdapter);
        //upcomingListView.setAdapter(recyclerViewAdapter);

        GridListDecorator gridListDecorator = new GridListDecorator();

        inTheatresListView.addItemDecoration(gridListDecorator);

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
}
