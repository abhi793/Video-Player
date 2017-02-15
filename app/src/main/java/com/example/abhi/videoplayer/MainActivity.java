package com.example.abhi.videoplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.example.abhi.videoplayer.uicomponents.IndicatorView;

public class MainActivity extends AppCompatActivity {
    private RecyclerView inTheatresListView;
    private RecyclerView upcomingListView;

    private IndicatorView indicatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();

        indicatorView.setTotalItems(5);
        indicatorView.setIndicatorPosition(1);
    }

    public void initializeViews() {
        inTheatresListView = (RecyclerView) findViewById(R.id.in_theatres);
        upcomingListView = (RecyclerView) findViewById(R.id.upcoming);

        indicatorView = (IndicatorView) findViewById(R.id.in_theatres_indicator);
    }
}
